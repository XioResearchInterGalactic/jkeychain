pluginManagement {
    val axionVersion: String by settings
    plugins {
        id("pl.allegro.tech.build.axion-release") version axionVersion
    }
}

rootProject.name = "jkeychain"
