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

            artifact(tasks.shadowJar)

            // Completely suppress all dependency declarations
            suppressAllPomMetadataWarnings()
            pom {
                withXml {
                    asNode().appendNode("dependencies")  // Creates empty dependencies node
                }
            }
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        exclude("${project.group}/**")
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