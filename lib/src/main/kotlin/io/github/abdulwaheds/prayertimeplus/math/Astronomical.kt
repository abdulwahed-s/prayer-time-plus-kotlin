package io.github.abdulwaheds.prayertimeplus.math

import kotlin.math.floor

/**
 * Sun position at a given instant: [declination] in degrees and
 * [equationOfTime] in hours (apparent minus mean solar time).
 */
internal data class SolarCoordinates(
    val declination: Double,
    val equationOfTime: Double,
)

/**
 * Julian day number for a Gregorian calendar date, with **no time of day**.
 *
 * The time of day is folded in later as a day fraction, so this takes plain
 * integer [year], [month] (1..12) and [day]. All arithmetic is in [Double] with
 * floor toward negative infinity to match the reference engine bit-for-bit.
 */
internal fun julianDay(year: Int, month: Int, day: Int): Double {
    var y = year
    var m = month
    if (m <= 2) {
        y -= 1
        m += 12
    }
    val a = floor(y / 100.0)
    return floor((y + 4716) * 365.25) +
        floor((m + 1) * 30.6001) +
        day +
        (2 - a + floor(a / 4.0)) -
        1524.5
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
    val daysSinceEpoch = julianDate - 2451545.0
    val meanAnomaly = fixAngle(0.98560028 * daysSinceEpoch + 357.529)
    val meanLongitude = fixAngle(0.98564736 * daysSinceEpoch + 280.459)
    val eclipticLongitude = fixAngle(
        meanLongitude + 1.915 * sinDeg(meanAnomaly) + 0.02 * sinDeg(2.0 * meanAnomaly),
    )
    val obliquity = 23.439 - 0.00000036 * daysSinceEpoch

    val declination = arcsinDeg(sinDeg(obliquity) * sinDeg(eclipticLongitude))
    val rightAscensionHours =
        arctan2Deg(cosDeg(obliquity) * sinDeg(eclipticLongitude), cosDeg(eclipticLongitude)) / 15.0
    val equationOfTime = meanLongitude / 15.0 - fixHour(rightAscensionHours)

    return SolarCoordinates(declination, equationOfTime)
}
