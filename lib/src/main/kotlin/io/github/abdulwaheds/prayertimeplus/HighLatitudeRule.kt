package io.github.abdulwaheds.prayertimeplus

/**
 * Strategy for estimating Fajr and Isha when the sun never reaches the required
 * depression angle, so the pure angle-based time is undefined (typical of high
 * latitudes in summer).
 *
 * When a fixed rule is selected, the missing time is replaced with a fraction
 * of the night, where the night runs from sunset to the following sunrise.
 */
public enum class HighLatitudeRule {
    /**
     * No fixed rule: use the natural angle-based times, but if that leaves Fajr
     * or Isha undefined, recompute once using [SEVENTH_OF_THE_NIGHT].
     */
    AUTOMATIC,

    /**
     * No adjustment. The angle-based time is used as-is and may be undefined
     * (returned as `null`) when the sun stays above the depression angle.
     */
    NONE,

    /** Fajr and Isha are pinned to one half of the night from their anchor. */
    MIDDLE_OF_THE_NIGHT,

    /**
     * Fajr is placed one seventh of the night before sunrise and Isha one
     * seventh of the night after sunset.
     */
    SEVENTH_OF_THE_NIGHT,

    /**
     * The night fraction is proportional to the prayer's twilight angle
     * (`angle / 60` of the night), giving a seasonally varying portion.
     */
    TWILIGHT_ANGLE,
}
