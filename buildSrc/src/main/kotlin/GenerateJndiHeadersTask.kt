import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile

abstract class GenerateJndiHeadersTask : JavaCompile() {

    init {
        val mainSourceSet = project.extensions.findByType(JavaPluginExtension::class.java)!!
            .sourceSets.getByName("main")
        classpath = mainSourceSet.compileClasspath
        source = mainSourceSet.java
        destinationDirectory.set(project.buildDir.resolve("generated").resolve("jndi-compilation"))
        options.compilerArgs.addAll(
            listOf(
                "-h",
                project.projectDir.resolve("src").resolve("main").resolve("c").absolutePath,
            )
        )
    }
}
