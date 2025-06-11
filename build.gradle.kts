// register repositories for both buildscript and application
buildscript.repositories.registerRepositories()
repositories.registerRepositories()

plugins {
    idea
    java
    `maven-publish`
    id("pl.allegro.tech.build.axion-release")
    id("org.jreleaser")
    id("code-generation")
}

group = "org.merlyn.oss"
scmVersion {
    tag {
        prefix = "v"
        versionSeparator = ""
    }
    checks {
        uncommittedChanges = false
    }
    repository {
        pushTagsOnly = true
    }
}
version = scmVersion.version

dependencies {
    val junitJuniperVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJuniperVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJuniperVersion")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
    withJavadocJar()
}

configure<PublishingExtension> {
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }

    publications {
        create<MavenPublication>("artifacts") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                val githubRepoUrl = "https://github.com/XioResearchInterGalactic/jkeychain"

                name.set(project.name)
                description.set("https://github.com/XioResearchInterGalactic/jkeychain")
                url.set("https://github.com/XioResearchInterGalactic/jkeychain")
                inceptionYear.set("2017")
                licenses {
                    license {
                        name.set("BSD 2-Clause License")
                        url.set("https://opensource.org/licenses/BSD-2-Clause")
                    }
                }
                developers {
                    developer {
                        id.set("davidafsilva")
                        name.set("David Silva")
                        url.set("https://github.com/davidafsilva")
                    }
                    developer {
                        id.set("xrubioj-merlyn")
                        name.set("Xavier Rubio Jansana")
                        url.set("https://merlyn.org")
                    }
                }
                scm {
                    val githubRepoCheckoutUrl = "$githubRepoUrl.git"

                    connection.set(githubRepoCheckoutUrl)
                    developerConnection.set(githubRepoCheckoutUrl)
                    url.set(githubRepoUrl)
                }
            }
        }
    }
}

jreleaser {
    signing {
        setActive("ALWAYS")
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                    setAuthorization("BEARER")
                    username = System.getenv("JRELEASER_MAVENCENTRAL_USERNAME")
                    // snapshotSupported = true
                    applyMavenCentralRules = true
                }
            }
        }
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<Jar> {
        from("${projectDir}/LICENSE") {
            rename("LICENSE", "META-INF/LICENSE.txt")
        }
    }
}

fun RepositoryHandler.registerRepositories() {
    mavenLocal()
    mavenCentral()
}
