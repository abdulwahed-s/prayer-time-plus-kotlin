package io.github.abdulwaheds.prayertimeplus.math

/**
 * Sun position at a given instant: [declination] in degrees and
 * [equationOfTime] in hours (apparent minus mean solar time).
 */
internal data class SolarCoordinates(
    val declination: Double,
    val equationOfTime: Double,
)
