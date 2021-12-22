import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class CompileSoLibraryTask : Exec() {
    @get:OutputFile
    abstract val soFilePath: RegularFileProperty

    @TaskAction
    override fun exec() {
        val generatorPath = soFilePath.get().asFile
        val sourcePath = project.projectDir.resolve("src")
            .resolve("main")
            .resolve("c")
            .resolve("pt_davidafsilva_apple_OSXKeychain.c")

        setCommandLine(
            "gcc",
            "-arch",
            "arm64",
            "-arch",
            "x86_64",
            "-mmacosx-version-min=10.11",
            "-dynamiclib",
            "-framework",
            "CoreFoundation",
            "-framework",
            "Security",
            "-I",
            "/Library/Java/JavaVirtualMachines/openjdk.jdk/Contents/Home/include",
            "-I",
            "/Library/Java/JavaVirtualMachines/openjdk.jdk/Contents/Home/include/darwin",
            "-std=c99",
            "-pedantic",
            "-Wall",
            "-o",
            generatorPath.absolutePath,
            sourcePath.absolutePath
        )

        super.exec()
    }
}
