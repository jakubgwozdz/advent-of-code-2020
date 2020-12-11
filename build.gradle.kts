plugins {
    kotlin("multiplatform") version "1.4.20"
//    kotlin("plugin.serialization") version "1.4.20"
}

repositories {
    jcenter()
}

kotlin {

    jvm()

    js {
        browser {
            @Suppress("EXPERIMENTAL_API_USAGE")
            dceTask {
                keep(
                    "advent-of-code-2020.advent2020.day01.createUI",
                    "advent-of-code-2020.advent2020.day02.createUI",
                    "advent-of-code-2020.advent2020.day03.createUI",
                    "advent-of-code-2020.advent2020.day04.createUI",
                    "advent-of-code-2020.advent2020.day05.createUI",
                    "advent-of-code-2020.advent2020.day06.createUI",
                    "advent-of-code-2020.advent2020.day07.createUI",
                    "advent-of-code-2020.advent2020.day08.createUI",
                    "advent-of-code-2020.advent2020.day09.createUI",
                    "advent-of-code-2020.advent2020.day10.createUI",
                    "advent-of-code-2020.advent2020.day11.createUI",
                    "advent-of-code-2020.advent2020.day12.createUI",
                    "advent-of-code-2020.advent2020.day13.createUI",
                    "advent-of-code-2020.advent2020.day14.createUI",
                    "advent-of-code-2020.advent2020.day15.createUI",
                    "advent-of-code-2020.advent2020.day16.createUI",
                    "advent-of-code-2020.advent2020.day17.createUI",
                    "advent-of-code-2020.advent2020.day18.createUI",
                    "advent-of-code-2020.advent2020.day19.createUI",
                    "advent-of-code-2020.advent2020.day20.createUI",
                    "advent-of-code-2020.advent2020.day21.createUI",
                    "advent-of-code-2020.advent2020.day22.createUI",
                    "advent-of-code-2020.advent2020.day23.createUI",
                    "advent-of-code-2020.advent2020.day24.createUI",
                    "advent-of-code-2020.advent2020.day25.createUI",
                    "advent-of-code-2020.advent2020.createIndex"
                )
            }

            webpackTask {
                cssSupport.enabled = true
            }

            runTask {
                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.0")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {
            dependencies {
//                implementation("org.slf4j:slf4j-simple:1.7.30")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                implementation("org.junit.platform:junit-platform-console:1.5.2")
                implementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
            }
        }

        all {
            languageSettings.apply {
                languageVersion = "1.4"
                apiVersion = "1.4"
                useExperimentalAnnotation("kotlin.js.ExperimentalJsExport")
                useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            }
        }
    }

    tasks.getByName("jvmTest", Test::class) {
        useJUnitPlatform()
    }

    // copied from https://github.com/DaanVandenBosch/kotlin-js-karma-resources-test/blob/fix/build.gradle.kts
    val generateKarmaConfig = tasks.register("generateKarmaConfig") {
        val outputFile = file("karma.config.d/karma.config.generated.js")

        outputs.file(outputFile)

        outputFile.printWriter().use { writer ->
            writer.println("var PROJECT_PATH = '${projectDir.absolutePath.replace("\\", "\\\\")}';")
        }
    }

    tasks.getByName("jsBrowserTest") {
        dependsOn(generateKarmaConfig)
    }
}

