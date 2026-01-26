plugins {
	kotlin("jvm") version "2.2.10"
	kotlin("plugin.spring") version "2.2.10"
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.sentry.jvm.gradle") version "5.12.2"
	id("org.owasp.dependencycheck") version "12.2.0"
	kotlin("plugin.jpa") version "2.2.10"
}

group = "server.web"
version = "0.0.1-SNAPSHOT"
description = "Projet backend de Casa avec Spring Boot Kotlin"
sentry {
	// Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
	// This enables source context, allowing you to see your source
	// code as part of your stack traces in Sentry.
	includeSourceContext = true
	org = "casanayo"
	projectName = "casanayo"
	authToken =  System.getenv("SENTRY_AUTH_TOKEN")
}
java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}
extra["springCloudGcpVersion"] = "7.3.1"
extra["springCloudVersion"] = "2025.0.0"
extra["sentryVersion"] = "8.27.0"
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //vonage
//    implementation("com.vonage:server-sdk-kotlin:2.1.1")
    //twilio
    implementation("com.twilio.sdk:twilio:9.2.1")
    //redis
//    implementation("org.springframework.boot:spring-boot-starter-session-data-redis")
    //websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    //reactive
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    //schedule
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    //gcs
    implementation("com.google.cloud:spring-cloud-gcp-starter-storage")
    //generator page
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // security
	implementation("org.springframework.boot:spring-boot-starter-security")
    //
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.38")
    // crypto
    implementation("org.springframework.security:spring-security-crypto")
    // validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
//    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
    //rate limiting
    implementation("com.bucket4j:bucket4j-core:8.7.0")
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	compileOnly("org.projectlombok:lombok")
	// @sentry
	implementation("io.sentry:sentry-opentelemetry-agent:8.22.0")
	implementation("io.r2dbc:r2dbc-postgresql")
	implementation("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")
	implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:11.19.0")
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-session-data-redis-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.boot:spring-boot-starter-r2dbc-test")
	testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${property("springCloudGcpVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//		mavenBom("io.sentry:sentry-bom:${property("sentryVersion")}")
    }
}



kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
