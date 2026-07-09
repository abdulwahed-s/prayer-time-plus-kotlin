import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import groovy.json.JsonSlurper
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.publish)
    `java-library`
    signing
}

group = "io.github.abdulwahed-s"
version = "0.2.0"
description = "Dependency-free Kotlin library for computing Islamic prayer times."

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    explicitApi()
    jvmToolchain(17)
    compilerOptions {
        allWarningsAsErrors.set(true)
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt.yml"))
}

mavenPublishing {
    // Publishes the main jar, a sources jar, and a Dokka-generated javadoc jar.
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
            sourcesJar = true,
        ),
    )
    coordinates(group.toString(), "prayer-time-plus", version.toString())
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    pom {
        name.set("prayer-time-plus")
        description.set(project.description)
        inceptionYear.set("2026")
        url.set("https://github.com/abdulwahed-s/prayer-time-plus-kotlin")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("abdulwahed-s")
                name.set("abdulwahed-s")
            }
        }
        scm {
            url.set("https://github.com/abdulwahed-s/prayer-time-plus-kotlin")
            connection.set("scm:git:https://github.com/abdulwahed-s/prayer-time-plus-kotlin.git")
            developerConnection.set("scm:git:ssh://git@github.com/abdulwahed-s/prayer-time-plus-kotlin.git")
        }
    }
}

// Sign releases with the local gpg command line (compatible with ed25519 keys);
// CI can instead supply an in-memory key via the `signingInMemoryKey` property.
if (!providers.gradleProperty("signingInMemoryKey").isPresent) {
    signing {
        useGpgCmd()
    }
}

// ---------------------------------------------------------------------------
// Bundled-data generator.
//
// Emits the method-parameter and Auto-resolution tables as Kotlin source from
// the committed JSON in tools/data/, so the numbers are reviewable and exact.
// Standalone (not wired into compilation): run it whenever the JSON changes and
// commit the regenerated output.
// ---------------------------------------------------------------------------
tasks.register("generatePrayerData") {
    group = "build setup"
    description = "Generates the bundled method-parameter and Auto-resolution Kotlin tables."

    val methodsFile = rootProject.file("tools/data/method_parameters.json")
    val autoFile = rootProject.file("tools/data/auto_method_resolution.json")
    val outputDir = file("src/main/kotlin/io/github/abdulwaheds/prayertimeplus/data")

    inputs.files(methodsFile, autoFile)
    outputs.dir(outputDir)

    // Fully self-contained action (captures only File values + JsonSlurper) so
    // it stays compatible with Gradle's configuration cache.
    doLast {
        outputDir.mkdirs()

        @Suppress("UNCHECKED_CAST")
        val methodsRoot = JsonSlurper().parse(methodsFile) as Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        val methods = methodsRoot["methods"] as Map<String, List<Number>>
        val methodRows =
            methods.entries.joinToString(separator = ",\n", postfix = ",") { (key, values) ->
                val literals = values.joinToString(", ") { it.toDouble().toString() }
                "            \"$key\" to doubleArrayOf($literals)"
            }
        File(outputDir, "MethodParameters.kt").writeText(
            buildString {
                appendLine("// GENERATED — do not edit by hand.")
                appendLine("// Regenerate with: ./gradlew :lib:generatePrayerData")
                appendLine("package io.github.abdulwaheds.prayertimeplus.data")
                appendLine()
                appendLine("/** Bundled 11-element parameter arrays, keyed by calculation-method name. */")
                appendLine("internal object MethodParameters {")
                appendLine("    private val TABLE: Map<String, DoubleArray> =")
                appendLine("        mapOf(")
                appendLine(methodRows)
                appendLine("        )")
                appendLine()
                appendLine("    /** Parameter array for [key], falling back to Muslim World League when unknown. */")
                appendLine(
                    "    fun forKey(key: String): DoubleArray = (TABLE[key] ?: TABLE.getValue(\"mwl\")).copyOf()",
                )
                appendLine()
                appendLine("    /** Every method key present in the bundled table. */")
                appendLine("    val keys: Set<String> get() = TABLE.keys")
                appendLine("}")
            },
        )

        @Suppress("UNCHECKED_CAST")
        val autoRoot = JsonSlurper().parse(autoFile) as Map<String, Any>
        val mwlDefault = autoRoot["mwl_default"] as String

        @Suppress("UNCHECKED_CAST")
        val country = autoRoot["country"] as Map<String, String>
        val autoRows =
            country.entries.joinToString(separator = ",\n", postfix = ",") { (code, method) ->
                "            \"$code\" to \"$method\""
            }
        File(outputDir, "AutoMethodResolution.kt").writeText(
            buildString {
                appendLine("// GENERATED — do not edit by hand.")
                appendLine("// Regenerate with: ./gradlew :lib:generatePrayerData")
                appendLine("package io.github.abdulwaheds.prayertimeplus.data")
                appendLine()
                appendLine("/** Bundled country-code to calculation-method map for Auto resolution. */")
                appendLine("internal object AutoMethodResolution {")
                appendLine("    /** Method used when a country has no explicit mapping. */")
                appendLine("    const val MWL_DEFAULT: String = \"$mwlDefault\"")
                appendLine()
                appendLine("    private val COUNTRY_TO_METHOD: Map<String, String> =")
                appendLine("        mapOf(")
                appendLine(autoRows)
                appendLine("        )")
                appendLine()
                appendLine("    /** Resolves the method key for an ISO-3166 alpha-2 [iso2] country code. */")
                append("    fun forCountry(iso2: String): String = ")
                appendLine("COUNTRY_TO_METHOD[iso2.uppercase()] ?: MWL_DEFAULT")
                appendLine("}")
            },
        )
    }
}
