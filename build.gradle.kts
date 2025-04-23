plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "yandex.prakticum"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sourceSets {
	create("intTest") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
		resources {
			srcDir(sourceSets.test.get().resources)
		}
	}
}

configurations["intTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["intTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

tasks.register<Test>("intTest") {
	description = "Runs integration tests."
	group = "verification"

	testClassesDirs = sourceSets["intTest"].output.classesDirs
	classpath = sourceSets["intTest"].runtimeClasspath
	shouldRunAfter("test")
}
repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.apache.commons:commons-collections4:4.4")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

springBoot {
	buildInfo()  // исполнение задачи по добавлению информации о сборке
}


tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed")
	}
}
tasks.check {
	dependsOn("intTest")
}
