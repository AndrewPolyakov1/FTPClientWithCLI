import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.company.contest"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.json:json:20211205")
    testImplementation("org.testng:testng:6.14.3")
    testImplementation("org.mockftpserver:MockFtpServer:3.2.0")
    implementation("org.glassfish:javax.json:1.1.4")
    implementation("javax.json:javax.json-api:1.1.4")
}

tasks.test {
    useTestNG ()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.infotec.ftp.client.Main"
    }
}

// Create a shadow JAR task
tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("") // Set classifier to empty to overwrite the default JAR
}
