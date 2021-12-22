import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar

class CodeGenerationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val generateEnumsTask = project.registerEnumGenerationTasks()
        project.registerSoGenerationTasks(generateEnumsTask)
    }

    private fun Project.registerEnumGenerationTasks(): TaskProvider<out Task> {
        val generatorFile = buildDir.resolve("generated").resolve("bin").resolve("enum-gen")
        val generatedSourcesPath = buildDir.resolve("generated")
            .resolve("sources")
            .resolve("enums")
            .resolve("java")

        // add the generated sources to the existent main source set
        val sourceSets = extensions.findByType(JavaPluginExtension::class.java)!!.sourceSets
        sourceSets.getByName("main").java {
            srcDir(generatedSourcesPath)
        }

        // compile the enum generator
        val compileEnumGeneratorTask = tasks.register(
            "compileEnumGenerator",
            CompileEnumGeneratorTask::class.java
        ) {
            generatorFilePath.set(generatorFile)
        }

        // run the generate enums
        val generateEnumsTask = tasks.register("generateEnums", GenerateEnumsTask::class.java) {
            dependsOn(compileEnumGeneratorTask)
            generatorFilePath.set(generatorFile)
            generatorSourcesPath.set(generatedSourcesPath)
        }
        tasks.getByName("compileJava").dependsOn(generateEnumsTask)
        // if there is a source jar task, add the dependency to it as it generates sources as well
        afterEvaluate {
            tasks.findByName("sourcesJar")?.dependsOn(generateEnumsTask)
        }

        return generateEnumsTask
    }

    private fun Project.registerSoGenerationTasks(generateEnumsTask: TaskProvider<out Task>) {
        val soFile = buildDir.resolve("generated")
            .resolve("libs")
            .resolve("osxkeychain.so")
        val sourceSets = extensions.findByType(JavaPluginExtension::class.java)!!.sourceSets

        // generate the .h header
        val generateJniHeadersTask = tasks.register("generateJndiHeaders", GenerateJndiHeadersTask::class.java) {
            dependsOn(generateEnumsTask)
        }

        // compile the C code into an .so library
        val compileSoLibraryTask = tasks.register("compileSoLibrary", CompileSoLibraryTask::class.java) {
            dependsOn(generateJniHeadersTask)
            soFilePath.set(soFile)
        }
        tasks.getByName("compileJava").finalizedBy(compileSoLibraryTask)
        // include the .so library within the main jar file
        tasks.named("jar", Jar::class.java) {
            dependsOn(compileSoLibraryTask)
            from(soFile)
        }

        // copy the .so library to tests classpath so it can be loaded
        val copySoLibForTestsTask = tasks.register("copySoLibForTests", Copy::class.java) {
            dependsOn(compileSoLibraryTask)
            from(soFile)

            into(sourceSets.findByName("test")!!.output.classesDirs.singleFile)
        }
        tasks.getByName("test").dependsOn(copySoLibForTestsTask)
    }
}
