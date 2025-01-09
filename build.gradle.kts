import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    id("io.freefair.lombok") version "8.6"
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.papermc.paperweight.userdev") version "1.7.5"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
}

group = "dev.arubiku.crystal"
version = "1.1.0"
description = "A Minecraft plugin for redstone enthusiasts"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
    implementation("commons-io:commons-io:2.11.0")
    // Database libraries
    implementation("com.h2database:h2:1.4.200")
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("com.zaxxer:HikariCP:4.0.3")
    // Logging (optional, but recommended)
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")


}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    reobfJar {
        dependsOn("build")
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    dependencies {
        include(dependency("commons-io:commons-io:2.11.0"))

    }
    relocate("dev.jorel.commandapi", "dev.arubiku.libs.commandapi")
    relocate("org.apache.commons", "dev.arubiku.libs.commons")
    relocate("com.fasterxml.jackson", "dev.arubiku.libs.jackson")
    relocate("com.zaxxer.hikari", "dev.arubiku.libs.hikari")
    minimize()
}

bukkitPluginYaml {
    main = "dev.arubiku.crystal.crystal"
    apiVersion = "1.21"
    authors.add("ArubikU")
    commands {
        register("crystal") {
            description = "crystal main command"
            permission = "crystal.use"
        }
    }
    permissions {
        register("crystal.use") {
            description = "Allows use of crystal commands"
        }
    }
}

