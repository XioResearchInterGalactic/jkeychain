repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        register("code-generation-plugin") {
            id = "code-generation"
            implementationClass = "CodeGenerationPlugin"
            description = "Configures the necessary tasks for the code generation, including SO library, " +
                "which bridges the JNDI calls to the OS X keychain."
        }
    }
}
