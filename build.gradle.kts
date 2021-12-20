plugins {
    java
    id("org.kordamp.gradle.jacoco") version "0.47.0"
    id("org.sonarqube") version "3.3"
}

config {
    coverage {
        jacoco {
            enabled
            aggregateExecFile
            aggregateReportHtmlFile
            aggregateReportXmlFile
            additionalSourceDirs
            additionalClassDirs
        }
    }
}

group = "io.jonathanlee"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

subprojects {
    apply(plugin = "org.kordamp.gradle.jacoco")
}

sonarqube {
    properties {
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.projectKey", "split")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.login", "cc1e1c3c359b62d3c9c186ad6b54e3533151eb97")
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }
}
