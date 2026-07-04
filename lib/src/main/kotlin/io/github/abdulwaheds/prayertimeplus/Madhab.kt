package io.github.abdulwaheds.prayertimeplus

/**
 * The juristic school that fixes the Asr shadow rule.
 *
 * Asr begins when an object's shadow equals its noon shadow plus
 * [shadowFactor] times the object's height. [SHAFI] (the standard opinion,
 * shared by Maliki and Hanbali) uses a factor of one; [HANAFI] uses two, giving
 * a later Asr.
 */
public enum class Madhab {
    /** Shafiʿi/Maliki/Hanbali (standard): shadow factor `1`. */
    SHAFI,

    /** Hanafi: shadow factor `2`, a later Asr. */
    HANAFI,

    ;

    /**
     * The shadow-length multiplier used in the Asr formula: `1` for [SHAFI],
     * `2` for [HANAFI] (the enum ordinal plus one).
     */
    public val shadowFactor: Int get() = ordinal + 1
}
