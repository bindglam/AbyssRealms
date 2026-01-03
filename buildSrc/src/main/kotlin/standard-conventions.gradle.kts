plugins {
    id("java-library")
}

group = "com.bindglam.abyssrealms"
version = property("game_version").toString()

repositories {
    mavenCentral()
}

dependencies {
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
