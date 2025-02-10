plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.rider"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Dependency สำหรับ Annotation Processor
	implementation ("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
	// Export Excel
	implementation ("org.apache.poi:poi-ooxml:5.2.3")
	//JWT
	implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5")

	//Dotenv ช่วยให้คุณสามารถเก็บค่าการตั้งค่าต่าง ๆ เช่น รหัสผ่าน, คีย์ API, หรือ URL ของฐานข้อมูล ไว้ในไฟล์ .env แยกจากโค้ดของคุณ
	implementation("io.github.cdimascio:dotenv-java:3.0.0")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	//json
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")






}

tasks.withType<Test> {
	useJUnitPlatform()
}
