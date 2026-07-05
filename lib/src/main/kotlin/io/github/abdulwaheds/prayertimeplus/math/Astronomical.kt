package io.github.abdulwaheds.prayertimeplus.math

import kotlin.math.floor

/** J2000.0 epoch, expressed as a Julian day. */
private const val J2000_EPOCH_JULIAN_DAY = 2451545.0

/** Mean anomaly of the sun: rate per day and value at epoch, in degrees. */
private const val MEAN_ANOMALY_DAILY_RATE = 0.98560028
private const val MEAN_ANOMALY_AT_EPOCH = 357.529

/** Mean ecliptic longitude of the sun: rate per day and value at epoch, in degrees. */
private const val MEAN_LONGITUDE_DAILY_RATE = 0.98564736
private const val MEAN_LONGITUDE_AT_EPOCH = 280.459

/** Equation-of-centre coefficients for the first and second harmonics, in degrees. */
private const val EQUATION_OF_CENTER_FIRST = 1.915
private const val EQUATION_OF_CENTER_SECOND = 0.02
private const val SECOND_HARMONIC = 2.0

/** Mean obliquity of the ecliptic at epoch, and its decrease per day, in degrees. */
private const val MEAN_OBLIQUITY_AT_EPOCH = 23.439
private const val OBLIQUITY_DAILY_DECREASE = 0.00000036

/** Degrees of hour angle per hour of time. */
private const val DEGREES_PER_HOUR = 15.0

/** Julian-day calendar constants. */
private const val MEAN_JULIAN_YEAR_DAYS = 365.25
private const val MONTH_LENGTH_TERM = 30.6001
private const val JULIAN_DAY_YEAR_OFFSET = 4716
private const val JULIAN_DAY_BASE_OFFSET = 1524.5
private const val DAYS_PER_GREGORIAN_CENTURY = 100.0
private const val LEAP_YEAR_DIVISOR = 4.0
private const val JAN_FEB_MONTH_SHIFT = 12
private const val GREGORIAN_LEAP_CORRECTION = 2

/**
 * Julian day number for a Gregorian calendar date, with **no time of day**.
 *
 * The time of day is folded in later as a day fraction, so this takes plain
 * integer [year], [month] (1..12) and [day]. All arithmetic is in [Double] with
 * floor toward negative infinity to match the reference engine bit-for-bit.
 */
internal fun julianDay(
    year: Int,
    month: Int,
    day: Int,
): Double {
    var y = year
    var m = month
    if (m <= GREGORIAN_LEAP_CORRECTION) {
        y -= 1
        m += JAN_FEB_MONTH_SHIFT
    }
    val century = floor(y / DAYS_PER_GREGORIAN_CENTURY)
    return floor((y + JULIAN_DAY_YEAR_OFFSET) * MEAN_JULIAN_YEAR_DAYS) +
        floor((m + 1) * MONTH_LENGTH_TERM) +
        day +
        (GREGORIAN_LEAP_CORRECTION - century + floor(century / LEAP_YEAR_DIVISOR)) -
        JULIAN_DAY_BASE_OFFSET
}

/**
 * Low-precision USNO sun position for [julianDate] (which already includes the
 * longitude shift and the day-fraction time of day).
 *
 * Returns the [declination] (degrees) and the [equationOfTime] (hours). The
 * literal coefficients are the well-known USNO almanac values and must be kept
 * exactly for numeric parity.
 */
internal fun sunPosition(julianDate: Double): SolarCoordinates {
    val daysSinceEpoch = julianDate - J2000_EPOCH_JULIAN_DAY
    val meanAnomaly = fixAngle(MEAN_ANOMALY_DAILY_RATE * daysSinceEpoch + MEAN_ANOMALY_AT_EPOCH)
    val meanLongitude = fixAngle(MEAN_LONGITUDE_DAILY_RATE * daysSinceEpoch + MEAN_LONGITUDE_AT_EPOCH)
    val eclipticLongitude =
        fixAngle(
            meanLongitude +
                EQUATION_OF_CENTER_FIRST * sinDeg(meanAnomaly) +
                EQUATION_OF_CENTER_SECOND * sinDeg(SECOND_HARMONIC * meanAnomaly),
        )
    val obliquity = MEAN_OBLIQUITY_AT_EPOCH - OBLIQUITY_DAILY_DECREASE * daysSinceEpoch

    val declination = arcsinDeg(sinDeg(obliquity) * sinDeg(eclipticLongitude))
    val rightAscensionHours =
        arctan2Deg(cosDeg(obliquity) * sinDeg(eclipticLongitude), cosDeg(eclipticLongitude)) / DEGREES_PER_HOUR
    val equationOfTime = meanLongitude / DEGREES_PER_HOUR - fixHour(rightAscensionHours)

    return SolarCoordinates(declination, equationOfTime)
}
