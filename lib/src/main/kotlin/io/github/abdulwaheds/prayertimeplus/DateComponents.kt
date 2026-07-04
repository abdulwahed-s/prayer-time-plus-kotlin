package io.github.abdulwaheds.prayertimeplus

import java.time.LocalDate

/**
 * A calendar date reduced to its year, month and day.
 *
 * Prayer-time calculation is anchored to the civil calendar date only — never a
 * time of day or a time zone — so this deliberately carries just the three
 * fields the astronomy needs.
 *
 * @property year proleptic Gregorian year.
 * @property month month of year, `1` (January) through `12` (December).
 * @property day day of month, `1`-based.
 */
public data class DateComponents(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    public companion object {
        /** Extracts the year/month/day of [date], discarding any zone context. */
        public fun from(date: LocalDate): DateComponents =
            DateComponents(date.year, date.monthValue, date.dayOfMonth)
    }
}
