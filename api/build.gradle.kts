plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.yaml:snakeyaml:2.3")
    implementation("org.zeroturnaround:zt-zip:1.17")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect"))

            include(dependency("com.google.code.gson:gson"))
            include(dependency("org.yaml:snakeyaml"))
            include(dependency("org.zeroturnaround:zt-zip"))
        }
    }
}
