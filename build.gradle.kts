import dev.kordex.gradle.plugins.docker.file.*
import dev.kordex.gradle.plugins.kordex.DataCollection

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.johnrengelman.shadow")
    id("io.gitlab.arturbosch.detekt")

    id("dev.kordex.gradle.docker")
    id("dev.kordex.gradle.kordex")
}
val projectVersion: String = (System.getenv("VERSION") ?: "").ifEmpty { "1.0" }

group = "qbt-bot"
version = projectVersion

dependencies {
    detektPlugins(libs.detekt)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kx.ser)

    // Logging dependencies
    implementation(libs.groovy)
    implementation(libs.jansi)
    implementation(libs.logback)
    implementation(libs.logback.groovy)
    implementation(libs.logging)

    // TODO: use it once the latest version is available on sonatype again
//    implementation(libs.doc.generator)
}

kordEx {
    ignoreIncompatibleKotlinVersion = true
    bot {
        // See https://docs.kordex.dev/data-collection.html
        dataCollection(DataCollection.None)


        mainClass = "qbtbot.AppKt"
    }
}

detekt {
    buildUponDefaultConfig = true

    config.from(rootProject.files("detekt.yml"))
}

// Automatically generate a Dockerfile. Set `generateOnBuild` to `false` if you'd prefer to manually run the
// `createDockerfile` task instead of having it run whenever you build.
docker {
    // Create the Dockerfile in the root folder.
    file(rootProject.file("Dockerfile"))
    commands {
        // Each function (aside from comment/emptyLine) corresponds to a Dockerfile instruction.
        // See: https://docs.docker.com/reference/dockerfile/

        from("openjdk:21-jdk-slim")

        emptyLine()

        runShell("mkdir -p /bot/plugins")
        runShell("mkdir -p /bot/data")

        emptyLine()

        copy("build/libs/$name-*-all.jar", "/bot/bot.jar")

        emptyLine()

        workdir("/bot")

        emptyLine()

        entryPointExec(
            "java", "-Xms2G", "-Xmx2G",
            "-jar", "/bot/bot.jar"
        )
    }
}
