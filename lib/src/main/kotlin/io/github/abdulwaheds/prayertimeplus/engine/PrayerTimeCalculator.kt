package io.github.abdulwaheds.prayertimeplus.engine

import io.github.abdulwaheds.prayertimeplus.CalculationParameters
import io.github.abdulwaheds.prayertimeplus.Coordinates
import io.github.abdulwaheds.prayertimeplus.DateComponents
import io.github.abdulwaheds.prayertimeplus.HighLatitudeRule
import io.github.abdulwaheds.prayertimeplus.math.arccosDeg
import io.github.abdulwaheds.prayertimeplus.math.arccotDeg
import io.github.abdulwaheds.prayertimeplus.math.cosDeg
import io.github.abdulwaheds.prayertimeplus.math.fixHour
import io.github.abdulwaheds.prayertimeplus.math.julianDay
import io.github.abdulwaheds.prayertimeplus.math.sinDeg
import io.github.abdulwaheds.prayertimeplus.math.sunPosition
import io.github.abdulwaheds.prayertimeplus.math.tanDeg
import kotlin.math.abs
import kotlin.math.sqrt

/** The seven daily solar times as fractional local hours (any may be `NaN`). */
internal data class SolarTimes(
    val fajr: Double,
    val sunrise: Double,
    val dhuhr: Double,
    val asr: Double,
    val sunset: Double,
    val maghrib: Double,
    val isha: Double,
)

/**
 * The prayer-time engine for a single location, date and parameter set.
 *
 * A single evaluation pass mirrors the reference algorithm exactly: each prayer
 * is seeded with a fixed time-of-day guess, the sun position is evaluated once
 * at that guess, and the result is localised, offset, Maghrib/Isha reconciled,
 * and (optionally) corrected for high latitude. Nothing here rounds or formats.
 */
internal class PrayerTimeCalculator(
    coordinates: Coordinates,
    date: DateComponents,
    private val parameters: CalculationParameters,
    private val countryCode: String,
    private val timeZoneHours: Double,
) {
    private val latitude = coordinates.latitude
    private val longitude = coordinates.longitude
    private val altitude = coordinates.altitude

    /** Julian day shifted west by the longitude so the sun can be sampled by day fraction. */
    private val baseDate = julianDay(date.year, date.month, date.day) - longitude / 360.0

    /** Combined method + caller minute offsets, applied together. */
    private val offsets = parameters.methodAdjustments + parameters.adjustments

    /** Computes the seven solar times, applying the given high-latitude [rule]. */
    fun compute(rule: HighLatitudeRule): SolarTimes {
        val dip = if (usesElevation()) REFRACTION_DIP + ELEVATION_COEFFICIENT * sqrt(altitude) else REFRACTION_DIP

        // Raw astronomical times in the longitude-shifted frame (single pass).
        val rawFajr = sunAngleTime(HALF_CIRCLE - parameters.fajrAngle, FAJR_SEED)
        val rawSunrise = sunAngleTime(HALF_CIRCLE - dip, SUNRISE_SEED)
        val rawDhuhr = midDay(DHUHR_SEED)
        val rawAsr = asrTime(parameters.madhab.shadowFactor.toDouble(), ASR_SEED)
        val rawSunset = sunAngleTime(dip, SUNSET_SEED)
        // Provisional angle-based Isha; used only when Isha is an angle, not an interval.
        val rawIsha = sunAngleTime(parameters.ishaValue, ISHA_SEED)

        // Localise every time from the longitude frame to the wall clock.
        val localisation = timeZoneHours - longitude / DEGREES_PER_HOUR
        var fajr = rawFajr + localisation
        var sunrise = rawSunrise + localisation
        var dhuhr = rawDhuhr + localisation
        var asr = rawAsr + localisation
        val sunset = rawSunset + localisation
        var isha = rawIsha + localisation

        // Per-prayer minute offsets.
        fajr += offsets.fajr / MINUTES_PER_HOUR
        sunrise += offsets.sunrise / MINUTES_PER_HOUR
        dhuhr += offsets.dhuhr / MINUTES_PER_HOUR
        asr += offsets.asr / MINUTES_PER_HOUR

        // Maghrib is always derived from sunset.
        var maghrib = sunset + offsets.maghrib / MINUTES_PER_HOUR
        if (parameters.maghribIsInterval) {
            maghrib += parameters.maghribValue / MINUTES_PER_HOUR
        }

        // Isha is either a fixed interval after Maghrib or the angle value above.
        if (parameters.ishaIsInterval) {
            isha = maghrib + parameters.ishaValue / MINUTES_PER_HOUR
        }
        isha += offsets.isha / MINUTES_PER_HOUR

        // Umm al-Qura adds 30 minutes to Isha during Ramadan in Saudi Arabia.
        if (parameters.method == MAKKAH_KEY &&
            countryCode.equals(SAUDI_ARABIA_CODE, ignoreCase = true) &&
            parameters.isRamadan
        ) {
            isha += RAMADAN_ISHA_BUMP_HOURS
        }

        if (rule != HighLatitudeRule.NONE) {
            val night = fixHour(sunrise - sunset)

            val fajrPortion = nightPortion(rule, parameters.fajrAngle) * night
            if (fajr.isNaN() || fixHour(sunrise - fajr) > fajrPortion) {
                fajr = sunrise - fajrPortion + offsets.fajr / MINUTES_PER_HOUR
            }

            val ishaTwilightAngle = if (parameters.ishaIsInterval) DEFAULT_ISHA_ANGLE else parameters.ishaValue
            val ishaPortion = nightPortion(rule, ishaTwilightAngle) * night
            if (isha.isNaN() || fixHour(isha - maghrib) > ishaPortion) {
                isha = maghrib + ishaPortion + offsets.isha / MINUTES_PER_HOUR
            }
        }

        return SolarTimes(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha)
    }

    /** Solar noon (Dhuhr) at the day-fraction [t]. */
    private fun midDay(t: Double): Double {
        val equationOfTime = sunPosition(baseDate + t).equationOfTime
        return fixHour(SOLAR_NOON - equationOfTime)
    }

    /**
     * The clock time at which the sun sits [angle] degrees below the horizon,
     * seeded at day fraction [t]. A morning request passes `180 − angle`, which
     * exceeds 90° and flips the result to before noon.
     */
    private fun sunAngleTime(
        angle: Double,
        t: Double,
    ): Double {
        val declination = sunPosition(baseDate + t).declination
        val noon = midDay(t)
        val numerator = -sinDeg(angle) - sinDeg(declination) * sinDeg(latitude)
        val denominator = cosDeg(declination) * cosDeg(latitude)
        var hourAngle = arccosDeg(numerator / denominator) / DEGREES_PER_HOUR
        if (angle > RIGHT_ANGLE) {
            hourAngle = -hourAngle
        }
        return noon + hourAngle
    }

    /** Asr time for the given shadow [factor], seeded at day fraction [t]. */
    private fun asrTime(
        factor: Double,
        t: Double,
    ): Double {
        val declination = sunPosition(baseDate + t).declination
        val sunAltitude = arccotDeg(factor + tanDeg(abs(latitude - declination)))
        return sunAngleTime(-sunAltitude, t)
    }

    private fun usesElevation(): Boolean =
        parameters.method in ELEVATION_METHODS || countryCode.uppercase() in ELEVATION_COUNTRIES

    private fun nightPortion(
        rule: HighLatitudeRule,
        angle: Double,
    ): Double =
        when (rule) {
            HighLatitudeRule.AUTOMATIC -> SEVENTH_PORTION
            HighLatitudeRule.NONE -> 0.0
            HighLatitudeRule.MIDDLE_OF_THE_NIGHT -> MIDDLE_PORTION
            HighLatitudeRule.SEVENTH_OF_THE_NIGHT -> SEVENTH_PORTION
            HighLatitudeRule.TWILIGHT_ANGLE -> angle / TWILIGHT_ANGLE_DIVISOR
        }

    private companion object {
        const val REFRACTION_DIP = 0.833
        const val ELEVATION_COEFFICIENT = 0.0347
        const val SOLAR_NOON = 12.0
        const val HALF_CIRCLE = 180.0
        const val RIGHT_ANGLE = 90.0
        const val DEGREES_PER_HOUR = 15.0
        const val MINUTES_PER_HOUR = 60.0
        const val TWILIGHT_ANGLE_DIVISOR = 60.0
        const val DEFAULT_ISHA_ANGLE = 18.0
        const val RAMADAN_ISHA_BUMP_HOURS = 0.5
        const val MAKKAH_KEY = "makkah"
        const val SAUDI_ARABIA_CODE = "SA"

        // Literal 0.14286 (not 1/7) for numeric parity with the reference algorithm.
        const val SEVENTH_PORTION = 0.14286
        const val MIDDLE_PORTION = 0.5

        const val FAJR_SEED = 5.0 / 24.0
        const val SUNRISE_SEED = 6.0 / 24.0
        const val DHUHR_SEED = 12.0 / 24.0
        const val ASR_SEED = 13.0 / 24.0
        const val SUNSET_SEED = 18.0 / 24.0
        const val ISHA_SEED = 18.0 / 24.0

        val ELEVATION_METHODS =
            setOf("iraq", "morocco", "tunisia", "jordan", "orleans", "sudan", "belgium", "kazakhstan")
        val ELEVATION_COUNTRIES = setOf("PS", "IL", "CZ", "CH")
    }
}
