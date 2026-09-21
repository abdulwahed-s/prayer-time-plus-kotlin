package io.github.abdulwaheds.prayertimeplus

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Cross-package conformance tests shared visibly with the Dart and Swift suites. */
class CustomMaghribAngleTest {
    private val sohar = Coordinates(latitude = 24.3486, longitude = 56.6953, altitude = 5.0)
    private val date = DateComponents(2026, 6, 28)
    private val offset = ZoneOffset.ofHours(4)

    private fun custom(
        maghribValue: Double = 0.0,
        maghribIsInterval: Boolean = false,
        ishaValue: Double = 17.0,
        ishaIsInterval: Boolean = false,
    ): CalculationParameters =
        CalculationParameters(
            method = "custom",
            fajrAngle = 18.0,
            maghribIsInterval = maghribIsInterval,
            maghribValue = maghribValue,
            ishaIsInterval = ishaIsInterval,
            ishaValue = ishaValue,
            highLatitudeRule = HighLatitudeRule.NONE,
        )

    private fun times(parameters: CalculationParameters): PrayerTimes =
        PrayerTimes(
            coordinates = sohar,
            dateComponents = date,
            calculationParameters = parameters,
            utcOffset = offset,
            countryCode = "OM",
            cityName = "sohar",
        )

    @Test
    fun positiveMaghribAngleProducesAPostSunsetTime() {
        val result = times(custom(maghribValue = 4.0))

        val maghrib = assertNotNull(result.maghrib)
        assertTrue(maghrib.isAfter(assertNotNull(result.sunset)))
    }

    @Test
    fun zeroAndNegativeMaghribAnglesRetainSunset() {
        for (angle in listOf(0.0, -4.0)) {
            val result = times(custom(maghribValue = angle))
            assertEquals(result.sunset, result.maghrib, "$angle degrees")
        }
    }

    @Test
    fun maghribIntervalRemainsMinutesAfterSunset() {
        val result = times(custom(maghribIsInterval = true, maghribValue = 5.0))

        assertEquals(5, minutesBetween(result.sunset, result.maghrib))
    }

    @Test
    fun intervalIshaIsBasedOnFinalAngleBasedMaghrib() {
        val result = times(custom(maghribValue = 4.0, ishaIsInterval = true, ishaValue = 90.0))

        assertTrue(assertNotNull(result.maghrib).isAfter(assertNotNull(result.sunset)))
        assertEquals(90, minutesBetween(result.maghrib, result.isha))
    }

    @Test
    fun unavailableMaghribAngleFallsBackToSunset() {
        val london =
            PrayerTimes(
                coordinates = Coordinates(latitude = 51.5080, longitude = -0.1281),
                dateComponents = DateComponents(2026, 7, 9),
                calculationParameters = custom(maghribValue = 18.0, ishaIsInterval = true, ishaValue = 90.0),
                utcOffset = ZoneOffset.ofHours(1),
                countryCode = "GB",
                cityName = "London",
            )

        assertEquals(london.sunset, london.maghrib)
        assertEquals(90, minutesBetween(london.maghrib, london.isha))
    }

    @Test
    fun maghribAngleAtOrAfterAngleBasedIshaFallsBackToSunset() {
        val result = times(custom(maghribValue = 20.0, ishaValue = 17.0))

        assertEquals(result.sunset, result.maghrib)
        assertTrue(assertNotNull(result.maghrib).isBefore(assertNotNull(result.isha)))
    }

    @Test
    fun customAnglePrayersRemainChronological() {
        val result = times(custom(maghribValue = 4.0, ishaValue = 17.0))

        val maghrib = assertNotNull(result.maghrib)
        assertTrue(assertNotNull(result.fajr).isBefore(maghrib))
        assertTrue(assertNotNull(result.sunset).isBefore(maghrib))
        assertTrue(maghrib.isBefore(assertNotNull(result.isha)))
    }

    @Test
    fun existingPresetsWithoutAMaghribAngleRetainGoldenResults() {
        val mwl = times(CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters())
        val oman = times(CalculationMethod.OMAN.parameters())

        assertEquals("19:05", mwl.sunset.hhmm())
        assertEquals("19:05", mwl.maghrib.hhmm())
        assertEquals("20:28", mwl.isha.hhmm())
        assertEquals("19:10", oman.maghrib.hhmm())
        assertEquals("20:35", oman.isha.hhmm())
    }

    @Test
    fun customPresetHasSharedDefaultsAndStableKey() {
        val parameters = CalculationMethod.OTHER.parameters()
        val result = times(parameters)

        assertEquals("custom", CalculationMethod.OTHER.key)
        assertEquals(CalculationMethod.OTHER, CalculationMethod.fromKey("custom"))
        assertEquals("custom", parameters.method)
        assertEquals(18.0, parameters.fajrAngle)
        assertEquals(true, parameters.maghribIsInterval)
        assertEquals(0.0, parameters.maghribValue)
        assertEquals(false, parameters.ishaIsInterval)
        assertEquals(17.0, parameters.ishaValue)
        assertEquals(result.sunset, result.maghrib)
    }

    @Test
    fun methodAndUserAdjustmentsAreAppliedExactlyOnce() {
        val base = times(custom(maghribValue = 4.0, ishaIsInterval = true, ishaValue = 90.0))
        val tuned =
            times(
                custom(maghribValue = 4.0, ishaIsInterval = true, ishaValue = 90.0).copy(
                    methodAdjustments = PrayerAdjustments(maghrib = 2, isha = 3),
                    adjustments = PrayerAdjustments(maghrib = 3, isha = 4),
                ),
            )

        assertEquals(5, minutesBetween(base.maghrib, tuned.maghrib))
        assertEquals(12, minutesBetween(base.isha, tuned.isha))
    }

    private fun minutesBetween(
        earlier: OffsetDateTime?,
        later: OffsetDateTime?,
    ): Long = Duration.between(assertNotNull(earlier), assertNotNull(later)).toMinutes()
}
