package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.data.MethodParameters

/**
 * A named calculation-method preset.
 *
 * Each constant carries the authority's [key] and produces a fresh, fully
 * populated [CalculationParameters] via [parameters]; customise the result with
 * [CalculationParameters.copy].
 *
 * ```
 * val params = CalculationMethod.EGYPTIAN.parameters()
 * ```
 *
 * The standard international methods ([MUSLIM_WORLD_LEAGUE], [EGYPTIAN],
 * [KARACHI], [UMM_AL_QURA], [NORTH_AMERICA]) match widely published angles; the
 * regional and city methods carry authority-specific angles and per-prayer
 * minute offsets and will not match any generic table.
 *
 * @property key the stable string identifier used by [fromKey] and Auto
 *   resolution.
 */
public enum class CalculationMethod(public val key: String) {
    /** Muslim World League: Fajr 18°, Isha 17°. */
    MUSLIM_WORLD_LEAGUE("mwl"),

    /** Egyptian General Authority of Survey: Fajr 19.5°, Isha 17.5°. */
    EGYPTIAN("egypt"),

    /** University of Islamic Sciences, Karachi: Fajr 18°, Isha 18°. */
    KARACHI("karachi"),

    /** Umm al-Qura, Makkah: Fajr 18.5°, Isha 90 min after Maghrib (120 in Ramadan). */
    UMM_AL_QURA("makkah"),

    /** Islamic Society of North America: Fajr 15°, Isha 15°. */
    NORTH_AMERICA("isna"),

    /** Algeria (Ministry of Religious Affairs): Fajr 18°, Maghrib +3 min, Isha 17°. */
    ALGERIA("algeria"),

    /** Morocco (Habous): Fajr 19.09°, Maghrib +3 min, Isha 17°. */
    MOROCCO("morocco"),

    /** Azrou (Morocco) variant: Fajr 19.1°, Maghrib +1 min, Isha 17°. */
    AZROU("azrou"),

    /** Sudan: Fajr 18.12°, Maghrib −4 min, Isha 17.88°. */
    SUDAN("sudan"),

    /** Tunisia: Fajr 18°, Maghrib +1 min, Isha 18° + 1 min. */
    TUNISIA("tunisia"),

    /** Libya: Fajr 18.3°, Maghrib +4 min, Isha 18.35°. */
    LIBYA("libya"),

    /** Diyanet, Turkey: Fajr 18°, Maghrib +5 min, Isha 16.93°. */
    TURKEY("turkey"),

    /** JAKIM, Malaysia / Southeast Asia: Fajr 20°, Isha 18°. */
    MALAYSIA("malaysia"),

    /** JAKIM, Malaysia (offset variant): Fajr 20°, Maghrib +1 min, Isha 18.46°. */
    MALAYSIA2("malaysia2"),

    /** Kemenag, Indonesia: Fajr 20°, Maghrib +2 min, Isha 18°. */
    INDONESIA("indonesia"),

    /** Palestine: Fajr 20.11°, Maghrib +4 min, Isha 17.9°. */
    PALESTINE("palestine"),

    /** Oman (Ministry of Awqaf): Fajr 18°, Maghrib +5 min, Isha 18° + 1 min. */
    OMAN("oman"),

    /** Muscat (Oman) variant: Fajr 17.74°, Maghrib +6 min, Isha 18.229°. */
    OMAN_MUSCAT("omanMuscat"),

    /** GAIAE, United Arab Emirates: Fajr 18.5°, Maghrib +2 min, Isha 18.5° − 3 min. */
    EMIRATES("emirates"),

    /**
     * Dubai (UAE): uses the Muslim World League base parameters as a fallback,
     * since Dubai has no dedicated angle table.
     */
    DUBAI("dubai"),

    /** Qatar Calendar House: Fajr 18°, Maghrib +2 min, Isha 90 min after Maghrib. */
    QATAR("qatar"),

    /** Kuwait: Fajr 18°, Isha 17.5°. */
    KUWAIT("kuwait"),

    /** Jordan: Fajr 18.12°, Maghrib +1 min, Isha 17.975°. */
    JORDAN("jordan"),

    /** Syria (Hashemi): Fajr 19.5°, Isha 17.5°. */
    SYRIA("syria"),

    /** Iraq: Fajr 18°, Maghrib +2 min, Isha 17°. */
    IRAQ("iraq"),

    /** Maldives: Fajr 19°, Maghrib +1 min, Isha 19° + 1 min. */
    MALDIVES("maldives"),

    /** Kazakhstan (Kostanay): Fajr 14.97°, Isha 14.96°. */
    KAZAKHSTAN("kazakhstan"),

    /** Tajikistan: Fajr 18°, Isha 18°. */
    TAJIKISTAN("tajikistan"),

    /** Moscow: Fajr 16°, Maghrib +1 min, Isha 15.1° + 2 min; defaults to a night-fraction rule. */
    MOSCOW("moscow"),

    /** Republic of Korea: Fajr 18°, Isha 18° − 6 min. */
    SOUTH_KOREA("southkorea"),

    /** Union des Organisations Islamiques de France: Fajr 12°, Maghrib +4 min, Isha 12° + 5 min. */
    UOIF("uoif"),

    /** Paris (France) variant of [UOIF]. */
    PARIS("paris"),

    /** Toulouse (France) variant of [UOIF]. */
    TOULOUSE("toulouse"),

    /** Lyon (France) variant of [UOIF]. */
    LYON("lyon"),

    /** Orléans (France): Fajr 15°, Isha 12.34°. */
    ORLEANS("orleans"),

    /** Czech Republic: Fajr 12.04°, Maghrib −2 min, Isha 12.04°. */
    CZECH("czech"),

    /** Geneva (Switzerland): Fajr 17.99°, Maghrib −4 min, Isha 100 min after Maghrib. */
    SWITZERLAND("switzerland"),

    /** Fribourg (Switzerland) variant: Fajr 18.01°, Maghrib −5 min, Isha 100 min after Maghrib. */
    FRIBOURG("fribourg"),

    /** Belgium: Fajr 18°, Isha 18°; applies the horizon-dip elevation correction. */
    BELGIUM("belgium"),

    /** Luxembourg: Fajr 18°, Isha 17°. */
    LUXEMBOURG("luxembourg"),

    /** Austria: Fajr 18°, Isha 17°. */
    AUSTRIA("austria"),

    /** Aachen (Germany): Fajr 18°, Isha 17°. */
    AACHEN("aachen"),

    /** München (Germany): Fajr 18°, Isha 17°. */
    MUNCHEN("munchen"),

    /** Potsdam (Germany): Fajr 18°, Isha 17°. */
    POTSDAM("potsdam"),

    /** Nürnberg (Germany): Fajr 18°, Isha 17°. */
    NURNBERG("nurnberg"),

    /** London (UK): Fajr 18°, Isha 17°. */
    LONDON("london"),

    /** Birmingham (UK): Fajr 18°, Isha 17°. */
    BIRMINGHAM("birmingham"),

    /** Blackburn (UK): Fajr 18°, Isha 17°. */
    BLACKBURN("blackburn"),

    /** Montréal (Canada): Fajr 15°, Isha 15°. */
    MONTREAL("montreal"),

    /** Windsor (Canada): Fajr 15°, Isha 15°. */
    WINDSOR("windsor"),

    /** Calgary (Canada): Fajr 15°, Isha 15°. */
    CALGARY("calgary"),

    /** Mississauga (Canada): Fajr 15°, Isha 15°. */
    MISSISSAUGA("mississauga"),

    /** Rotterdam (Netherlands): Fajr 15°, Isha 15°. */
    ROTTERDAM("rotterdam"),

    /** Dordrecht (Netherlands): Fajr 15°, Isha 15°. */
    DORDRECHT("dordrecht"),

    /** Eindhoven (Netherlands): Fajr 15°, Isha 15°. */
    EINDHOVEN("eindhoven"),

    /** Placeholder carrying Muslim World League parameters; a starting point for a fully custom set. */
    OTHER("custom"),

    /** Placeholder carrying Muslim World League parameters. */
    NONE("none"),

    ;

    /** Builds a fresh [CalculationParameters] from this method's bundled parameter array. */
    public fun parameters(): CalculationParameters {
        val values = MethodParameters.forKey(key)
        return CalculationParameters(
            method = key,
            fajrAngle = values[FAJR_ANGLE],
            maghribIsInterval = values[MAGHRIB_FLAG] == 1.0,
            maghribValue = values[MAGHRIB_VALUE],
            ishaIsInterval = values[ISHA_FLAG] == 1.0,
            ishaValue = values[ISHA_VALUE],
            methodAdjustments =
                PrayerAdjustments(
                    fajr = values[FAJR_ADJ].toInt(),
                    sunrise = values[SUNRISE_ADJ].toInt(),
                    dhuhr = values[DHUHR_ADJ].toInt(),
                    asr = values[ASR_ADJ].toInt(),
                    maghrib = values[MAGHRIB_ADJ].toInt(),
                    isha = values[ISHA_ADJ].toInt(),
                ),
        )
    }

    public companion object {
        private val BY_KEY: Map<String, CalculationMethod> = entries.associateBy(CalculationMethod::key)

        /** The method whose [key] equals [key], or `null` if none matches. */
        public fun fromKey(key: String): CalculationMethod? = BY_KEY[key]

        private const val FAJR_ANGLE = 0
        private const val MAGHRIB_FLAG = 1
        private const val MAGHRIB_VALUE = 2
        private const val ISHA_FLAG = 3
        private const val ISHA_VALUE = 4
        private const val FAJR_ADJ = 5
        private const val SUNRISE_ADJ = 6
        private const val DHUHR_ADJ = 7
        private const val ASR_ADJ = 8
        private const val MAGHRIB_ADJ = 9
        private const val ISHA_ADJ = 10
    }
}
