plugins {
    java
    id("org.springframework.boot") version "2.5.3"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.freefair.lombok") version "5.3.3.3"
}

group = "com.att.training.k8s.greeting"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<JavaCompile>().configureEach {
        with(options) {
            release.set(11)
            compilerArgs.add("-Xlint:all,-processing,-auxiliaryclass")
        }
        // For additional-spring-configuration-metadata.json
        inputs.files(processResources)
    }

    jar {
        enabled = false
    }

    generateLombokConfig {
        isEnabled = true
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }
}
