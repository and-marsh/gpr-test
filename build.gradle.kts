repositories {
    google()
    jcenter()
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.71"))
        classpath("com.android.tools.build:gradle:3.5.3")
    }
}

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.71"
    id("maven-publish")
    id("com.android.library") version "3.5.3"
}

dependencies {
    implementation(kotlin("stdlib"))
}

group="com.example"
version="0.0.1"

kotlin {
    val android = android("android")

    val macos = macosX64("macos")

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

android {
    compileSdkVersion(28)
    testBuildType = "debug"

    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isJniDebuggable = true
        }
    }
    sourceSets {
        val main by getting {
            setRoot("$projectDir/src/androidMain")
        }
        val androidTest by getting {
            java.srcDir("$projectDir/src/androidTest/kotlin")
        }
    }
}

project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.example"
                artifactId = "github_test"
                version = "${android.defaultConfig.versionName}"

                tasks.getByName("bundleReleaseAar").outputs.files.forEach {
                    artifact(it)
                }

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    fun addDependency(dep: Dependency, scope: String) {
                        if (dep.group == null || dep.version == null || dep.name == "unspecified") {
                            return
                        } // Ignore invalid dependencies.

                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dep.group)
                        dependencyNode.appendNode("artifactId", dep.name)
                        dependencyNode.appendNode("version", dep.version)
                        dependencyNode.appendNode("scope", scope)
                    }

                    // List all "compile" dependencies (for old Gradle).
                    configurations.getByName("compile").dependencies.forEach { addDependency(it, "compile") }
                    // List all "api" dependencies (for new Gradle) as "compile" dependencies.
                    configurations.getByName("api").dependencies.forEach { addDependency(it, "compile") }
                    // List all "implementation" dependencies (for new Gradle) as "runtime" dependencies.
                    configurations.getByName("implementation").dependencies.forEach { addDependency(it, "runtime") }
                }
            }
        }

        repositories {
//            mavenLocal()
            maven {
                name = "GithubPackageRegistry"
                url = uri("https://maven.pkg.github.com/and-marsh/gpr-test")
                credentials {
                    username = property("TEST_GITHUB_USERNAME").toString()
                    password = property("TEST_GITHUB_TOKEN").toString()
                }
            }
        }
    }
}

