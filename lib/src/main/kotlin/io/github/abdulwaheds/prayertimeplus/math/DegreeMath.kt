package io.github.abdulwaheds.prayertimeplus.math

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

/*
 * Degree-based trigonometry.
 *
 * The whole solar model is expressed in degrees; every helper converts to and
 * from radians internally so that call sites read exactly like the classic
 * PrayTimes/USNO formulas. Keeping these wrappers (rather than inlining radian
 * math) is a parity requirement: the reference engine rounds intermediate
 * angles the same way, and `arccot` in particular must branch like `atan2`.
 */

private const val DEGREES_PER_RADIAN = 180.0 / PI
private const val RADIANS_PER_DEGREE = PI / 180.0
private const val DEGREES_IN_CIRCLE = 360.0
private const val HOURS_IN_DAY = 24.0

/** Sine of an angle given in [degrees]. */
internal fun sinDeg(degrees: Double): Double = sin(degrees * RADIANS_PER_DEGREE)

/** Cosine of an angle given in [degrees]. */
internal fun cosDeg(degrees: Double): Double = cos(degrees * RADIANS_PER_DEGREE)

/** Tangent of an angle given in [degrees]. */
internal fun tanDeg(degrees: Double): Double = tan(degrees * RADIANS_PER_DEGREE)

/** Arcsine of [x], returned in degrees. */
internal fun arcsinDeg(x: Double): Double = asin(x) * DEGREES_PER_RADIAN

/** Arccosine of [x], returned in degrees. */
internal fun arccosDeg(x: Double): Double = acos(x) * DEGREES_PER_RADIAN

/** Two-argument arctangent of ([y], [x]), returned in degrees. */
internal fun arctan2Deg(
    y: Double,
    x: Double,
): Double = atan2(y, x) * DEGREES_PER_RADIAN

/**
 * Arccotangent of [x], returned in degrees.
 *
 * Implemented as `atan2(1, x)`, not `arctan(1/x)`: the two agree for positive
 * arguments but differ in branch/sign for negative ones, and the Asr altitude
 * relies on the `atan2` branch.
 */
internal fun arccotDeg(x: Double): Double = atan2(1.0, x) * DEGREES_PER_RADIAN

/** Wraps [angle] into the half-open range `[0, 360)` degrees. */
internal fun fixAngle(angle: Double): Double = wrap(angle, DEGREES_IN_CIRCLE)

/** Wraps [hour] into the half-open range `[0, 24)` hours. */
internal fun fixHour(hour: Double): Double = wrap(hour, HOURS_IN_DAY)

/**
 * Wraps [value] into `[0, modulus)` using floor toward negative infinity, so
 * negative inputs wrap the same way the reference engine's `Math.floor` does.
 */
private fun wrap(
    value: Double,
    modulus: Double,
): Double {
    val remainder = value - modulus * floor(value / modulus)
    return if (remainder < 0.0) remainder + modulus else remainder
}
