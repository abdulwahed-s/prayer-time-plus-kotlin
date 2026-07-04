package io.github.abdulwaheds.prayertimeplus

/**
 * The daily prayers (plus Sunrise), in chronological order, with a [NONE]
 * sentinel for "before the first prayer of the day".
 */
public enum class Prayer {
    /** Dawn prayer. */
    FAJR,

    /** Sunrise; not a prayer, but the end of the Fajr window. */
    SUNRISE,

    /** Midday prayer, just after solar noon. */
    DHUHR,

    /** Afternoon prayer. */
    ASR,

    /** Sunset prayer. */
    MAGHRIB,

    /** Night prayer. */
    ISHA,

    /** No prayer — e.g. the period before Fajr, or an undefined result. */
    NONE,
}
