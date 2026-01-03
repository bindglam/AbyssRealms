plugins {
    id("java-library")
}

group = "com.bindglam.abyssrealms"
version = property("game_version").toString()

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    compileOnly("org.projectlombok:lombok:1.18.38")
    implementation("org.tinylog:tinylog-api:2.8.0-M1")
    implementation("org.tinylog:tinylog-impl:2.8.0-M1")
    compileOnly("org.jetbrains:annotations:26.0.2-1")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
