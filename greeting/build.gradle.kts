plugins {
    java
    id("org.springframework.boot") version "2.7.0"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.freefair.lombok") version "6.4.3"
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
            release.set(17)
            compilerArgs.add("-Xlint:all,-processing,-auxiliaryclass")
        }
        // For additional-spring-configuration-metadata.json
        inputs.files(processResources)
    }

    jar {
        enabled = false
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }
}
