// GENERATED — do not edit by hand.
// Regenerate with: ./gradlew :lib:generatePrayerData
package io.github.abdulwaheds.prayertimeplus.data

/** Bundled 11-element parameter arrays, keyed by calculation-method name. */
internal object MethodParameters {
    private val TABLE: Map<String, DoubleArray> =
        mapOf(
            "mwl" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "custom" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "none" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "morocco" to doubleArrayOf(19.09, 0.0, 0.0, 0.0, 17.0, 0.0, -2.0, 5.0, 0.0, 3.0, 0.0),
            "azrou" to doubleArrayOf(19.1, 0.0, 0.0, 0.0, 17.0, 0.0, -1.0, 5.0, 0.0, 1.0, 0.0),
            "algeria" to doubleArrayOf(18.0, 1.0, 3.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "sudan" to doubleArrayOf(18.12, 0.0, 0.0, 0.0, 17.88, 0.0, 3.0, 3.0, 0.0, -4.0, 0.0),
            "makkah" to doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "egypt" to doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "karachi" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "tunisia" to doubleArrayOf(18.0, 1.0, 1.0, 0.0, 18.0, -1.0, 0.0, 7.0, 0.0, 0.0, 1.0),
            "turkey" to doubleArrayOf(18.0, 0.0, 0.0, 0.0, 16.93, 0.0, -6.0, 6.0, 4.0, 5.0, 0.0),
            "malaysia" to doubleArrayOf(20.0, 1.0, 0.0, 0.0, 18.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0),
            "malaysia2" to doubleArrayOf(20.0, 1.0, 0.0, 0.0, 18.46, 0.0, 0.0, 3.0, 2.0, 1.0, 0.0),
            "indonesia" to doubleArrayOf(20.0, 1.0, 0.0, 0.0, 18.0, 2.0, -2.0, 2.0, 2.0, 2.0, 2.0),
            "palestine" to doubleArrayOf(20.11, 0.0, 0.0, 0.0, 17.9, 0.0, -5.0, 0.0, 0.0, 4.0, 0.0),
            "oman" to doubleArrayOf(18.0, 1.0, 5.0, 0.0, 18.0, 0.0, 0.0, 5.0, 5.0, 0.0, 1.0),
            "omanMuscat" to doubleArrayOf(17.74, 0.0, 0.0, 0.0, 18.229, 0.0, 1.0, 6.0, 6.0, 6.0, 0.0),
            "kazakhstan" to doubleArrayOf(14.97, 0.0, 0.0, 0.0, 14.96, 0.0, 0.0, 5.0, 5.0, 0.0, 0.0),
            "tajikistan" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "emirates" to doubleArrayOf(18.5, 1.0, 2.0, 0.0, 18.5, 1.0, -4.0, 2.0, 0.0, 0.0, -3.0),
            "jordan" to doubleArrayOf(18.12, 0.0, 0.0, 0.0, 17.975, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0),
            "kuwait" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "qatar" to doubleArrayOf(18.0, 1.0, 0.0, 1.0, 90.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0),
            "libya" to doubleArrayOf(18.3, 0.0, 0.0, 0.0, 18.35, 0.0, 0.0, 4.0, 0.0, 4.0, 0.0),
            "syria" to doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "iraq" to doubleArrayOf(18.0, 0.0, 0.0, 0.0, 17.0, 0.0, 0.0, 5.0, 3.0, 2.0, 0.0),
            "maldives" to doubleArrayOf(19.0, 1.0, 0.0, 0.0, 19.0, 0.0, -1.0, 4.0, 1.0, 1.0, 1.0),
            "moscow" to doubleArrayOf(16.0, 0.0, 0.0, 0.0, 15.1, 0.0, 0.0, 1.0, 1.0, 1.0, 2.0),
            "blackburn" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "london" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "birmingham" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "aachen" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "munchen" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "potsdam" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "nurnberg" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "austria" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "belgium" to doubleArrayOf(18.0, 0.0, 0.0, 0.0, 18.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "luxembourg" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "czech" to doubleArrayOf(12.04, 0.0, 0.0, 0.0, 12.04, 0.0, 0.0, 5.0, 0.0, -2.0, 0.0),
            "switzerland" to doubleArrayOf(17.99, 0.0, 0.0, 1.0, 100.0, 1.0, 4.0, 0.0, 0.0, -4.0, 0.0),
            "fribourg" to doubleArrayOf(18.01, 0.0, 0.0, 1.0, 100.0, 1.0, 5.0, 0.0, 0.0, -5.0, 0.0),
            "uoif" to doubleArrayOf(12.0, 1.0, 0.0, 0.0, 12.0, -5.0, 0.0, 5.0, 0.0, 4.0, 5.0),
            "paris" to doubleArrayOf(12.0, 1.0, 0.0, 0.0, 12.0, -5.0, 0.0, 5.0, 0.0, 4.0, 5.0),
            "toulouse" to doubleArrayOf(12.0, 1.0, 0.0, 0.0, 12.0, -5.0, 0.0, 5.0, 0.0, 4.0, 5.0),
            "lyon" to doubleArrayOf(12.0, 1.0, 0.0, 0.0, 12.0, -5.0, 0.0, 5.0, 0.0, 4.0, 5.0),
            "orleans" to doubleArrayOf(15.0, 0.0, 0.0, 0.0, 12.34, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0),
            "isna" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "montreal" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "windsor" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "calgary" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "mississauga" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "southkorea" to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0, 1.0, -1.0, 0.0, 0.0, 0.0, -6.0),
            "rotterdam" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "dordrecht" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            "eindhoven" to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        )

    /** Parameter array for [key], falling back to Muslim World League when unknown. */
    fun forKey(key: String): DoubleArray = (TABLE[key] ?: TABLE.getValue("mwl")).copyOf()

    /** Every method key present in the bundled table. */
    val keys: Set<String> get() = TABLE.keys
}
