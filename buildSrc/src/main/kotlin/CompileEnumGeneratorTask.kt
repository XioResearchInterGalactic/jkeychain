import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class CompileEnumGeneratorTask : Exec() {
    @get:OutputFile
    abstract val generatorFilePath: RegularFileProperty

    @TaskAction
    override fun exec() {
        val generatorPath = generatorFilePath.get().asFile
        val enumGeneratorSourcePath = project.projectDir.resolve("src")
            .resolve("main")
            .resolve("c")
            .resolve("generate_enums.c")

        setCommandLine(
            "gcc",
            "-arch",
            "arm64",
            "-arch",
            "x86_64",
            "-mmacosx-version-min=10.11",
            "-framework",
            "Security",
            "-std=c99",
            "-pedantic",
            "-Wall",
            "-o",
            generatorPath.absolutePath,
            enumGeneratorSourcePath.absolutePath
        )

        super.exec()
    }
}
