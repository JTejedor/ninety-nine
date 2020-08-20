import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.palantir.docker") version "0.25.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

group = "com.ninety.nine"
version = "1.0.1"

docker {
    name = "transferuploader:$version"
    this.tag("DockerHub","jtejedor/ninety-nine:$version-transferuploader")
    this.files(tasks.bootJar.get().outputs)
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(group = "commons-net", name = "commons-net", version = "3.6")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter")
    implementation(group = "org.iban4j", name = "iban4j", version = "3.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    // Latest Kotest Version
    testImplementation ("io.kotest:kotest-runner-junit5-jvm:4.1.3")
    testImplementation ("io.kotest:kotest-assertions-core-jvm:4.1.3")
    testImplementation ("io.kotest:kotest-property-jvm:4.1.3")
    testImplementation ("io.kotest:kotest-extensions-spring:4.1.3")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.allWarningsAsErrors = true
}

tasks.named<BootJar>("bootJar") {
    this.archiveFileName.set("TransferFTPUploader.jar")
    this.archiveBaseName.set("transfer-ftp-uploader")
    this.archiveVersion.set("0.1.0")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
