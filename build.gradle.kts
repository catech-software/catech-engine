plugins {
  java
  application
}

group = "net.catech_software.engine"

val lwjglVersion = "3.3.4"
val jomlVersion = "1.10.7"
val lwjglNatives = listOf("natives-freebsd",
                          "natives-linux",
                          "natives-linux-arm64",
                          "natives-linux-arm32",
                          "natives-linux-ppc64le",
                          "natives-linux-riscv64",
                          "natives-macos",
                          "natives-macos-arm64",
                          "natives-windows",
                          "natives-windows-x86",
                          "natives-windows-arm64")

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")

  implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

  implementation("org.lwjgl", "lwjgl")
  implementation("org.lwjgl", "lwjgl-assimp")
  implementation("org.lwjgl", "lwjgl-glfw")
  implementation("org.lwjgl", "lwjgl-openal")
  implementation("org.lwjgl", "lwjgl-opengl")
  implementation("org.lwjgl", "lwjgl-stb")

  for (native in lwjglNatives) {
    runtimeOnly("org.lwjgl", "lwjgl", classifier = native)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = native)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = native)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = native)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = native)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = native)
  }

  implementation("org.joml", "joml", jomlVersion)
}

tasks.test {
  useJUnitPlatform()
}

application {
  mainClass.set("Main")
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to application.mainClass))
  }
}
