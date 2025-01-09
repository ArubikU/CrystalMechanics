
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
        maven("https://plugins.gradle.org/m2/")
        maven("https://repo.auxilor.io/repository/maven-public/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

}


rootProject.name = "CrystalMechanics"
