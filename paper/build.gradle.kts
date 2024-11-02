plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
    id("io.papermc.paperweight.userdev") version "1.7.3"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    implementation(project(":api", "shadow"))

    // api-version is 1.20.5, but we will use 1.20.6 paper dev bundle
    // Because 1.20.6 is the latest version of 1.20.x
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
    }
}