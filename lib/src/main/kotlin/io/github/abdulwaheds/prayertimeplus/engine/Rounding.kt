package io.github.abdulwaheds.prayertimeplus.engine

import io.github.abdulwaheds.prayertimeplus.math.fixHour
import kotlin.math.floor

private const val HALF_MINUTE_IN_HOURS = 0.0083333333
private const val MINUTES_PER_HOUR = 60

/**
 * Rounds a fractional-hour time to the nearest minute of the day (`0..1439`),
 * or returns `null` when the time is undefined (`NaN`).
 *
 * Adds 30 seconds and truncates, matching the reference engine's rounding.
 */
internal fun roundedMinuteOfDay(hours: Double): Int? {
    if (hours.isNaN()) return null
    val bumped = fixHour(hours + HALF_MINUTE_IN_HOURS)
    val hour = floor(bumped).toInt()
    val minute = floor((bumped - hour) * MINUTES_PER_HOUR).toInt()
    return hour * MINUTES_PER_HOUR + minute
}
