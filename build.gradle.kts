plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
    id("java")
    id("maven-publish")
}

project.version = "0.0.1-alpha-1"

dependencies {
    implementation(project(":paper", "shadow"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

allprojects {
    group = "io.github.teambutterpl"
    version = project.version

    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "java")

    publishing {
        publications {
            create("maven-public", MavenPublication::class) {
                groupId = rootProject.group.toString()
                artifactId = rootProject.name
                version = rootProject.version.toString()
                from(components.getByName("java"))
            }
        }
    }

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