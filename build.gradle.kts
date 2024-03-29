/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
    id ("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.0.1"
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        name = "trustgamesRepoPrivate"
        url = uri("http://81.201.49.240:8080/private")
        isAllowInsecureProtocol = true
        credentials(PasswordCredentials::class)
        authentication {
            create<BasicAuthentication>("basic")
        }
    }

    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("net.trustgames:core:0.2-SNAPSHOT")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.1.0")
}

group = "net.trustgames"
version = "0.1-SNAPSHOT"
description = "Lobby"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    repositories {
        maven {
            name = "trustgamesRepoPrivate"
            url = uri("http://81.201.49.240:8080/private")
            isAllowInsecureProtocol = true
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = rootProject.name
            version = version
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    assemble {
        dependsOn(shadowJar)
    }
}
