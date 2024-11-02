plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
    id("java")
    id("maven-publish")
}

project.version = "0.0.1-alpha-1"
project.group = "io.github.teambutterpl"

dependencies {
    implementation(project(":paper", "shadow"))
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            groupId = project.group.toString()
            artifactId = project.name.toString()
            version = project.version.toString()

            from(components.getByName("java"))

            // Remove all dependencies from the generated POM
            pom {
                withXml {
                    val root = asNode()
                    val dependencies = root.children().find { (it as groovy.util.Node).name() == "dependencies" }
                    if (dependencies != null) {
                        root.remove(dependencies as groovy.util.Node)
                    }
                }
            }

            // Replace the original jar with shadow jar
            artifact(tasks.getByName("shadowJar")) {
                classifier = ""
            }
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }
}

allprojects {
    group = project.group
    version = project.version

    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "java")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        disableAutoTargetJvm()
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        testImplementation(kotlin("test"))
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        compileJava {
            options.release.set(17)
        }
    }
}