plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.intellij") version "1.4.0"
}

group = "com.tobiasgrunwald"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")
}

tasks.test {
    useJUnitPlatform()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.3.3")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php:213.7172.28"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks {

    patchPluginXml {
        changeNotes.set("""
            Add change notes here.<br>
            <em>most HTML tags may be used</em>        """.trimIndent())
    }

    buildSearchableOptions{
        enabled = false
    }
}