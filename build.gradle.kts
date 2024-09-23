plugins {
  java
  application
}

group = "net.catech_software.engine"

val lwjglVersion = "3.3.2"
val jomlVersion = "1.10.5"

val lwjglNatives = Pair(
        System.getProperty("os.name")!!,
        System.getProperty("os.arch")!!
).let { (name, arch) ->
  when {
    arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
      if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
        "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
      else
        "natives-linux"
    arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }                ->
      "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
    arrayOf("Windows").any { name.startsWith(it) }                           ->
      if (arch.contains("64"))
        "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
      else
        "natives-windows-x86"
    else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
  }
}

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
  runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
  runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
  runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
  runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
  runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
  runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
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
