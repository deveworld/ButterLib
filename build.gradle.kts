plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
    id("java")
}

project.version = "0.0.1-test-1"

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