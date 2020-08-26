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
group = "com.ninety.nine"
version = "1.0.1"

repositories {
    mavenCentral()
}

docker {
    name = "transfer-backend:$version"
    this.tag("DockerHub","jtejedor/ninety-nine:$version-transfer-backend")
    this.files(tasks.bootJar.get().outputs)
}

dependencies {
    annotationProcessor(
        group = "org.springframework.boot",
        name = "spring-boot-configuration-processor",
        version = "2.3.3.RELEASE"
    )
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(group = "org.springframework.boot", name= "spring-boot-starter-data-mongodb-reactive")
    implementation(group = "org.springframework.boot", name= "spring-boot-starter-webflux")
    implementation(group = "org.iban4j", name = "iban4j", version = "3.2.1")
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
    testImplementation(group = "io.mockk", name = "mockk", version = "1.10.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.allWarningsAsErrors = true
}

tasks.named<BootJar>("bootJar") {
    this.archiveFileName.set("TransferBackend.jar")
    this.archiveBaseName.set("transfer-backend")
    this.archiveVersion.set(project.version.toString())
}

tasks.withType<Test> {
    useJUnitPlatform()
}
