import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
    kotlin("jvm") version "2.0.0"
}

vkidManifestPlaceholders {
    fun error() = logger.error(
        "Warning! Build will not work!\n" +
                "Create the 'secrets.properties' file in the 'app' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it."
    )

    val properties = Properties()
    properties.load(file("app/secrets.properties").inputStream())
    val clientId = properties["VKIDClientID"] ?: error()
    val clientSecret = properties["VKIDClientSecret"] ?: error()

    init(
        clientId = clientId.toString(),
        clientSecret = clientSecret.toString(),
    )
}