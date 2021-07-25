plugins {
    java
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.att.training.k8s.reverse"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<JavaCompile>().configureEach {
        with(options) {
            release.set(11)
            compilerArgs.add("-Xlint:all,-processing,-auxiliaryclass")
        }
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
