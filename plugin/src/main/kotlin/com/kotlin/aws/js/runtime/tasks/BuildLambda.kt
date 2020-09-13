package com.kotlin.aws.js.runtime.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class BuildLambda : DefaultTask() {

    init {
        group = "aws"
    }

    @TaskAction
    fun generate() {
        val dir = File(project.buildDir, "distributions")
        dir.mkdirs()
        with(File(dir, "bootstrap")) {
            writeText("""
                # just a placeholder, not tested yet
                node sample.js
            """.trimIndent())
        }
    }

}