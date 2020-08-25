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
version = "1.0.2"

docker {
    name = "mongouploader:$version"
    this.tag("DockerHub","jtejedor/ninety-nine:$version-mongouploader")
    this.files(tasks.bootJar.get().outputs)
}

repositories {
    mavenCentral()
}

dependencies {

    annotationProcessor(
        group = "org.springframework.boot",
        name = "spring-boot-configuration-processor",
        version = "2.3.3.RELEASE"
    )
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(group = "org.springframework.boot", name = "spring-boot-starter")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-mongodb")
    implementation(group = "org.iban4j", name = "iban4j", version = "3.2.1")
    implementation(group = "commons-net", name = "commons-net", version = "3.6")
    implementation(group = "org.javamoney", name = "moneta", version = "1.4.2")
    testImplementation(
        group = "org.springframework.boot",
        name = "spring-boot-starter-test"
    ) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(group = "io.kotest", name = "kotest-runner-junit5-jvm", version = "4.1.3")
    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = "4.1.3")
    testImplementation(group = "io.kotest", name = "kotest-property-jvm", version = "4.1.3")
    testImplementation(group = "io.kotest", name = "kotest-extensions-spring", version = "4.1.3")
    testImplementation(group ="io.mockk", name = "mockk", version="1.10.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.allWarningsAsErrors = true
}

tasks.named<BootJar>("bootJar") {
    this.archiveFileName.set("MongoTransferUploader.jar")
    this.archiveBaseName.set(rootProject.name)
    this.archiveVersion.set(project.version.toString())
}

tasks.withType<Test> {
    useJUnitPlatform()
}