import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GenerateEnumsTask : Exec() {
    @get:InputFile
    abstract val generatorFilePath: RegularFileProperty

    @get:OutputDirectory
    abstract val generatorSourcesPath: RegularFileProperty

    @TaskAction
    override fun exec() {
        val generatorPath = generatorFilePath.get().asFile
        val sourcesPath = generatorSourcesPath.get().asFile
            .resolve("pt")
            .resolve("davidafsilva")
            .resolve("apple")
            .apply { mkdirs() }
        val authTypeJavaFile = sourcesPath.resolve("OSXKeychainAuthenticationType.java")
        val protocolTypeJavaFile = sourcesPath.resolve("OSXKeychainProtocolType.java")

        setCommandLine(
            generatorPath.absolutePath,
            authTypeJavaFile.absolutePath,
            protocolTypeJavaFile.absolutePath
        )

        super.exec()
    }
}
