import java.util.Properties

plugins {
    id("java")
//    id("application")
//    id("war")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.2"
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

//val springVersion: String by project
//val jakartaVersion: String by project
//val hibernateVersion: String by project
val postgresVersion: String by project
//val freemarkerVersion: String by project
//val hikariVersion: String by project
//val springDataVersion: String by project
val springSecurityVersion: String by project

repositories {
    mavenCentral()
}

val jacocoExcludes = listOf(
    "**/com/bagautdinov/dto/**",
    "**/com/bagautdinov/model/**",
    "**/com/bagautdinov/config/**"
)

dependencies {

//    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")
//    implementation("org.springframework:spring-jdbc:$springVersion")
//    implementation("org.springframework:spring-orm:$springVersion")
//    implementation("org.springframework.data:spring-data-jpa:$springDataVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework:spring-context-support:$springVersion")

//    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
//    implementation("org.freemarker:freemarker:$freemarkerVersion")
//    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
//    implementation("org.springframework.security:spring-security-core:$springSecurityVersion")
//    implementation("org.springframework.security:spring-security-config:${springSecurityVersion}")
//    implementation("org.springframework.security:spring-security-web:${springSecurityVersion}")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("org.springframework.security:spring-security-taglibs:${springSecurityVersion}")

    implementation("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.postgresql:postgresql:$postgresVersion")
    liquibaseRuntime("info.picocli:picocli:4.6.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

liquibase {
    activities.register("main") {
        arguments =  mapOf (
            "changelogFile" to props.get("change-log-file").toString() + ".xml",
            "url" to props.get("url"),
            "username" to props.get("username"),
            "password" to props.get("password"),
            "driver" to props.get("driver-class-name"),
        )
    }
}

tasks.named<org.gradle.api.tasks.testing.Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExcludes)
        }
    }))
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory.set(layout.buildDirectory.dir("jacoco"))
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = BigDecimal.valueOf(0.5)
            }
        }
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExcludes)
        }
    }))
}
tasks.named("check") {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
val props = Properties()
props.load(file("src/main/resources/db/liquibase.properties").inputStream())
