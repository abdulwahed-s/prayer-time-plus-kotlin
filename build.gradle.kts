// Root build: declares the shared plugins once (applied in the subprojects) so
// the Kotlin plugin is not loaded independently per module.
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
}
