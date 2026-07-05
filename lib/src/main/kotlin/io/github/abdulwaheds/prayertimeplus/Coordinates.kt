package io.github.abdulwaheds.prayertimeplus

/**
 * A geographic location on Earth.
 *
 * Longitude is **east-positive** and latitude is **north-positive**. [altitude]
 * is metres above sea level and is only consulted by the handful of calculation
 * methods that apply a horizon-dip correction; every other method ignores it.
 *
 * The primary constructor performs no validation (it is permissive by design).
 * Use [validated] when you want out-of-range coordinates rejected.
 *
 * ```
 * val sohar = Coordinates(latitude = 24.3486, longitude = 56.6953, altitude = 5.0)
 * ```
 *
 * @property latitude degrees north of the equator, nominally `[-90, 90]`.
 * @property longitude degrees east of the prime meridian, nominally `[-180, 180]`.
 * @property altitude metres above sea level; defaults to `0.0`.
 */
public data class Coordinates(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
) {
    public companion object {
        /**
         * Builds a [Coordinates] after range-checking [latitude] and [longitude].
         *
         * @throws IllegalArgumentException if [latitude] is outside `[-90, 90]`
         *   or [longitude] is outside `[-180, 180]`.
         */
        public fun validated(
            latitude: Double,
            longitude: Double,
            altitude: Double = 0.0,
        ): Coordinates {
            require(latitude in MIN_LATITUDE..MAX_LATITUDE) {
                "latitude must be in [$MIN_LATITUDE, $MAX_LATITUDE] degrees, but was $latitude"
            }
            require(longitude in MIN_LONGITUDE..MAX_LONGITUDE) {
                "longitude must be in [$MIN_LONGITUDE, $MAX_LONGITUDE] degrees, but was $longitude"
            }
            return Coordinates(latitude, longitude, altitude)
        }

        private const val MIN_LATITUDE = -90.0
        private const val MAX_LATITUDE = 90.0
        private const val MIN_LONGITUDE = -180.0
        private const val MAX_LONGITUDE = 180.0
    }
}
