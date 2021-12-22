import pl.allegro.tech.build.axion.release.domain.ChecksConfig
import pl.allegro.tech.build.axion.release.domain.RepositoryConfig
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

// register repositories for both buildscript and application
buildscript.repositories.registerRepositories()
repositories.registerRepositories()

plugins {
    idea
    java
    `maven-publish`
    signing
    id("pl.allegro.tech.build.axion-release")
    id("code-generation")
}

group = "pt.davidafsilva.apple"
scmVersion {
    tag(closureOf<TagNameSerializationConfig> {
        prefix = "v"
        versionSeparator = ""
    })
    checks(closureOf<ChecksConfig> {
        isUncommittedChanges = false
    })
    repository(closureOf<RepositoryConfig> {
        pushTagsOnly = true
    })
}
version = scmVersion.version

dependencies {
    val junitJuniperVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJuniperVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJuniperVersion")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "Sonatype"
            val repository = when {
                version.toString().endsWith("-SNAPSHOT") -> "/content/repositories/snapshots/"
                else -> "/service/local/staging/deploy/maven2/"
            }
            setUrl("https://s01.oss.sonatype.org/$repository")
            credentials {
                username = System.getenv("OSSRH_USER")
                password = System.getenv("OSSRH_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("artifacts") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                val githubRepoUrl = "https://github.com/davidafsilva/jkeychain"

                name.set(project.name)
                description.set("https://github.com/davidafsilva/jkeychain")
                url.set("https://github.com/davidafsilva/jkeychain")
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

configure<SigningExtension> {
    val signingGpgKey: String? by project
    val signingGpgKeyId: String? by project
    val signingGpgKeyPassword: String? by project
    if (signingGpgKey != null && signingGpgKeyId != null && signingGpgKeyPassword != null) {
        useInMemoryPgpKeys(signingGpgKeyId, signingGpgKey, signingGpgKeyPassword)
    }

    sign(publishing.publications.getByName("artifacts"))
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
