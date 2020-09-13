package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.tasks.BuildLambda
import com.kotlin.aws.js.runtime.tasks.GenerateMain
import com.kotlin.aws.js.runtime.tasks.GenerateWebpackConfig
import com.kotlin.aws.js.runtime.utils.getTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

class RuntimeKotlinGradlePlugin: Plugin<Project> {
    override fun apply(target: Project) {

        val generateMain = target.tasks.create("generateMain", GenerateMain::class.java)
        val generateJsConf = target.tasks.create("generateWebpackConfig", GenerateWebpackConfig::class.java)
        val buildLambda = target.tasks.create("buildLambda", BuildLambda::class.java)
        buildLambda.dependsOn("assemble")


        target.afterEvaluate {
            // set task dependency
            with(target.getTask("compileKotlinJs")) {
                this.dependsOn("generateMain")
                this.dependsOn("generateWebpackConfig")
            }

            // tweak sourcesets-dir
            val sourceSetContainer = target.extensions.getByName("kotlin") as KotlinSourceSetContainer
            with (sourceSetContainer.sourceSets.getByName("main").kotlin) {
                this.setSrcDirs(this.srcDirs.plus(it.buildDir.absolutePath + "/kotlin-gen"))
            }

            // set browser as a target
            val kotlinJsProjectExtension = target.extensions.getByName("kotlin") as KotlinJsProjectExtension
            kotlinJsProjectExtension.js {
                browser {
                    testTask {
                        enabled = false
                    }
                }
                binaries.executable()
            }
        }

    }
}
