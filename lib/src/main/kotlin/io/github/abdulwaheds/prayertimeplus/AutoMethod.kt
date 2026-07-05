package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.data.AutoMethodResolution

/**
 * Resolves a [CalculationMethod] from a country, mirroring the app's "Auto"
 * mode at the country level.
 *
 * This uses the bundled country-to-method table, which agrees with the app for
 * the overwhelming majority of cities — the ones whose method matches their
 * country default. It does not model the rarer per-city overrides.
 *
 * ```
 * val method = AutoMethod.forCountry("OM") // CalculationMethod.OMAN
 * ```
 */
public object AutoMethod {
    /**
     * The method for the ISO-3166 alpha-2 [countryCode] (case-insensitive),
     * falling back to [CalculationMethod.MUSLIM_WORLD_LEAGUE] for unknown codes.
     */
    public fun forCountry(countryCode: String): CalculationMethod {
        val key = AutoMethodResolution.forCountry(countryCode)
        return CalculationMethod.fromKey(key) ?: CalculationMethod.MUSLIM_WORLD_LEAGUE
    }
}
