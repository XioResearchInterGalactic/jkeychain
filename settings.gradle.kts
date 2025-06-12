pluginManagement {
    val axionVersion: String by settings
    val jreleaseVersion: String by settings
    plugins {
        id("pl.allegro.tech.build.axion-release") version axionVersion
        id("org.jreleaser") version jreleaseVersion
    }
}

rootProject.name = "jkeychain"
