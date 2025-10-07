import org.jreleaser.model.Active

plugins {
    id("java")
    id("maven-publish")
    id("org.jreleaser") version "1.20.0"
}

group = "dev.neovoxel.nbapi"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("org.json:json:20250517")
    implementation("org.slf4j:slf4j-api:2.0.17")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    compileOnly("org.jetbrains:annotations:24.0.1")
    annotationProcessor("org.jetbrains:annotations:24.0.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name = project.name
                description = "A library used to link to some bot client"
                url = "https://github.com/NeoVoxelDev/NeoBotAPI"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "LGPL-3.0-or-later"
                        url = "https://spdx.org/licenses/LGPL-3.0-or-later.html"
                    }
                }
                developers {
                    developer {
                        id = "aurelian2842"
                        name = "Aurelian2842"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/NeoVoxelDev/NeoBotAPI.git"
                    developerConnection = "scm:git:ssh://github.com/NeoVoxelDev/NeoBotAPI.git"
                    url = "http://github.com/NeoVoxelDev/NeoBotAPI"
                }
            }
        }
    }

    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

tasks.publish {
    dependsOn(tasks.named("publishMavenPublicationToMavenLocal"))
}

jreleaser {
    signing {
        active = Active.ALWAYS
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}