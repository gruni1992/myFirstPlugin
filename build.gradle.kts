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
    version.set("2021.2.3")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php:212.5457.49"))
}
tasks {
    patchPluginXml {
        changeNotes.set("""
            Add change notes here.<br>
            <em>most HTML tags may be used</em>        """.trimIndent())
    }
}