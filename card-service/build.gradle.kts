plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.aston"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"
extra["testcontainersVersion"] = "1.19.7"
extra["lombokVersion"] = "1.18.32"
extra["mapstructVersion"] = "1.5.5.Final"

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.4")
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.3")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave:3.3.0")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.2.4")
    implementation("com.github.loki4j:loki-logback-appender:1.5.0-m1")
    implementation("org.testcontainers:minio:${property("testcontainersVersion")}")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:postgresql:${property("testcontainersVersion")}")
    implementation("io.minio:minio:8.5.9")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.liquibase:liquibase-core:4.26.0")
    runtimeOnly("org.postgresql:postgresql:42.7.3")
    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
