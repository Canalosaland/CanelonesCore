plugins {
    id("java")
}

group = "me.pk2.canalosaland"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("fr.xephi:authme:5.6.0-SNAPSHOT")

    implementation(files("libs/Telecom-Release.jar"))
}

tasks.test {
    useJUnitPlatform()
}