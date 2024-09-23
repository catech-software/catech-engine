import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.joml.Matrix4f;
import net.catech_software.engine.render.shader.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static net.catech_software.util.Resource.getResourceFromJar;
import static net.catech_software.util.BufferedReaderToString.bufferedReaderToString;

public class Main {
  private static long window;
  private static int vao, vbo;
  private static ShaderList shaders;
  private static IntBuffer width, height;
  private static float prevRotation, rotation = 0f;
  private static int fpsCount, upsCount;

  private static final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

  private static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) glfwSetWindowShouldClose(window, true);
    }
  };

  private static void init() {
    GLFWVidMode vidMode;

    glfwSetErrorCallback(errorCallback);

    if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    window = glfwCreateWindow(640, 480, "Foobar", NULL, NULL);
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

    vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    if (vidMode == null) throw new RuntimeException("Failed to get video mode");
    glfwSetWindowPos(window, (vidMode.width() - 640) / 2, (vidMode.height() - 480) / 2);

    glfwSetKeyCallback(window, keyCallback);

    glfwMakeContextCurrent(window);
    GL.createCapabilities();

    glfwSwapInterval(1);

    vao = glGenVertexArrays();
    glBindVertexArray(vao);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer vertices = stack.mallocFloat(36 * 10);
      // Position: 3 floats (xyz), Color: 4 floats (rgba), Normal: 3 floats (xyz)
      vertices.put(-0.5f).put(0.5f).put(0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(0f).put(1f)
              .put(-0.5f).put(-0.5f).put(0.5f).put(0f).put(0f).put(1f).put(1f).put(0f).put(0f).put(1f)
              .put(0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(0f).put(1f)
              .put(0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(0f).put(1f)
              .put(0.5f).put(0.5f).put(0.5f).put(0f).put(1f).put(0f).put(1f).put(0f).put(0f).put(1f)
              .put(-0.5f).put(0.5f).put(0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(0f).put(1f)
              .put(0.5f).put(0.5f).put(0.5f).put(0f).put(0f).put(0f).put(1f).put(1f).put(0f).put(0f)
              .put(0.5f).put(-0.5f).put(0.5f).put(0f).put(0f).put(1f).put(1f).put(1f).put(0f).put(0f)
              .put(0.5f).put(-0.5f).put(-0.5f).put(0f).put(1f).put(1f).put(1f).put(1f).put(0f).put(0f)
              .put(0.5f).put(-0.5f).put(-0.5f).put(0f).put(1f).put(1f).put(1f).put(1f).put(0f).put(0f)
              .put(0.5f).put(0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(1f).put(1f).put(0f).put(0f)
              .put(0.5f).put(0.5f).put(0.5f).put(0f).put(0f).put(0f).put(1f).put(1f).put(0f).put(0f)
              .put(-0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(1f).put(0f)
              .put(-0.5f).put(0.5f).put(0.5f).put(0f).put(0f).put(1f).put(1f).put(0f).put(1f).put(0f)
              .put(0.5f).put(0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(1f).put(0f)
              .put(0.5f).put(0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(1f).put(0f)
              .put(0.5f).put(0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(1f).put(0f).put(1f).put(0f)
              .put(-0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(1f).put(0f)
              .put(-0.5f).put(-0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(-1f).put(0f)
              .put(-0.5f).put(-0.5f).put(0.5f).put(0f).put(0f).put(1f).put(1f).put(0f).put(-1f).put(0f)
              .put(0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(-1f).put(0f)
              .put(0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(-1f).put(0f)
              .put(0.5f).put(-0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(1f).put(0f).put(-1f).put(0f)
              .put(-0.5f).put(-0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(-1f).put(0f)
              .put(-0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(-1f).put(0f).put(0f)
              .put(-0.5f).put(-0.5f).put(-0.5f).put(0f).put(0f).put(1f).put(1f).put(-1f).put(0f).put(0f)
              .put(-0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(-1f).put(0f).put(0f)
              .put(-0.5f).put(-0.5f).put(0.5f).put(0f).put(1f).put(1f).put(1f).put(-1f).put(0f).put(0f)
              .put(-0.5f).put(0.5f).put(0.5f).put(0f).put(1f).put(0f).put(1f).put(-1f).put(0f).put(0f)
              .put(-0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(-1f).put(0f).put(0f)
              .put(0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(0f).put(-1f)
              .put(0.5f).put(-0.5f).put(-0.5f).put(0f).put(0f).put(1f).put(1f).put(0f).put(0f).put(-1f)
              .put(-0.5f).put(-0.5f).put(-0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(0f).put(-1f)
              .put(-0.5f).put(-0.5f).put(-0.5f).put(0f).put(1f).put(1f).put(1f).put(0f).put(0f).put(-1f)
              .put(-0.5f).put(0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(1f).put(0f).put(0f).put(-1f)
              .put(0.5f).put(0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(1f).put(0f).put(0f).put(-1f)
              .flip();

      vbo = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vbo);
      glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }

    shaders = new ShaderList();
    shaders.setProgram("default", new ShaderProgram());
    shaders.setVertexShader("default", new VertexShader());
    shaders.setFragmentShader("default", new FragmentShader());
    try (BufferedReader vertReader = getResourceFromJar("/assets/shaders/default.vert");
         BufferedReader fragReader = getResourceFromJar("/assets/shaders/default.frag")) {
      shaders.getVertexShader("default").setSource(bufferedReaderToString(vertReader));
      shaders.getFragmentShader("default").setSource(bufferedReaderToString(fragReader));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    shaders.getVertexShader("default").setShader(glCreateShader(GL_VERTEX_SHADER));
    glShaderSource(shaders.getVertexShader("default").getShader(), shaders.getVertexShader("default").getSource());
    glCompileShader(shaders.getVertexShader("default").getShader());
    if (glGetShaderi(shaders.getVertexShader("default").getShader(), GL_COMPILE_STATUS) != GL_TRUE)
      throw new RuntimeException(glGetShaderInfoLog(shaders.getVertexShader("default").getShader()));

    shaders.getFragmentShader("default").setShader(glCreateShader(GL_FRAGMENT_SHADER));
    glShaderSource(shaders.getFragmentShader("default").getShader(), shaders.getFragmentShader("default").getSource());
    glCompileShader(shaders.getFragmentShader("default").getShader());
    if (glGetShaderi(shaders.getFragmentShader("default").getShader(), GL_COMPILE_STATUS) != GL_TRUE)
      throw new RuntimeException(glGetShaderInfoLog(shaders.getFragmentShader("default").getShader()));

    shaders.getProgram("default").setProgram(glCreateProgram());
    glAttachShader(shaders.getProgram("default").getProgram(), shaders.getVertexShader("default").getShader());
    glAttachShader(shaders.getProgram("default").getProgram(), shaders.getFragmentShader("default").getShader());
    glLinkProgram(shaders.getProgram("default").getProgram());
    if (glGetProgrami(shaders.getProgram("default").getProgram(), GL_LINK_STATUS) != GL_TRUE)
      throw new RuntimeException(glGetProgramInfoLog(shaders.getProgram("default").getProgram()));
    glUseProgram(shaders.getProgram("default").getProgram());

    shaders.getProgram("default").setAttribute("position", glGetAttribLocation(shaders.getProgram("default").getProgram(), "position"));
    shaders.getProgram("default").setAttribute("color", glGetAttribLocation(shaders.getProgram("default").getProgram(), "color"));
    shaders.getProgram("default").setAttribute("normal", glGetAttribLocation(shaders.getProgram("default").getProgram(), "normal"));
    shaders.getProgram("default").setUniform("model", glGetUniformLocation(shaders.getProgram("default").getProgram(), "model"));
    shaders.getProgram("default").setUniform("view", glGetUniformLocation(shaders.getProgram("default").getProgram(), "view"));
    shaders.getProgram("default").setUniform("projection", glGetUniformLocation(shaders.getProgram("default").getProgram(), "projection"));
    shaders.getProgram("default").setUniform("lightColor", glGetUniformLocation(shaders.getProgram("default").getProgram(), "lightColor"));
    shaders.getProgram("default").setUniform("ambientLight", glGetUniformLocation(shaders.getProgram("default").getProgram(), "ambientLight"));
    shaders.getProgram("default").setUniform("directionalLight", glGetUniformLocation(shaders.getProgram("default").getProgram(), "directionalLight"));
    shaders.getProgram("default").setDataLocation("fragColor", glGetFragDataLocation(shaders.getProgram("default").getProgram(), "fragColor"));

    glEnableVertexAttribArray(shaders.getProgram("default").getAttribute("position"));
    glVertexAttribPointer(shaders.getProgram("default").getAttribute("position"), 3, GL_FLOAT, false, 10 * 4, 0);

    glEnableVertexAttribArray(shaders.getProgram("default").getAttribute("color"));
    glVertexAttribPointer(shaders.getProgram("default").getAttribute("color"), 4, GL_FLOAT, false, 10 * 4, 3 * 4);

    glEnableVertexAttribArray(shaders.getProgram("default").getAttribute("normal"));
    glVertexAttribPointer(shaders.getProgram("default").getAttribute("normal"), 3, GL_FLOAT, false, 10 * 4, 7 *4);

    width = MemoryUtil.memAllocInt(1);
    height = MemoryUtil.memAllocInt(1);
  }

  private static void input() {}

  private static void update(double delta) {
    prevRotation = rotation;
    rotation += (float) (delta * Math.toRadians(50f));

    upsCount++;
  }

  private static void render(double alpha) {
    float ratio;

    glfwGetFramebufferSize(window, width, height);
    ratio = width.get() / (float) height.get();

    try (MemoryStack stack = MemoryStack.stackPush()) {
      Matrix4f prevModel, model;
      FloatBuffer fb = stack.mallocFloat(16);

      prevModel = new Matrix4f().rotate(prevRotation, 0f, 1f, 0f);
      model = new Matrix4f().rotate(rotation, 0f, 1f, 0f);
      glUniformMatrix4fv(shaders.getProgram("default").getUniform("model"), false, prevModel.lerp(model, (float) alpha).get(fb));
      glUniformMatrix4fv(shaders.getProgram("default").getUniform("view"), false, new Matrix4f().translate(0f, 0f, -2f).rotate((float) Math.toRadians(25f), 1f, 0f, 0f).get(fb));
      glUniformMatrix4fv(shaders.getProgram("default").getUniform("projection"), false, new Matrix4f().perspective((float) Math.toRadians(70f), ratio, 0.1f, 100f).get(fb));
    }

    glUniform3fv(shaders.getProgram("default").getUniform("lightColor"), new float[]{1f, 1f, 1f});
    glUniform1f(shaders.getProgram("default").getUniform("ambientLight"), 0.1f);
    glUniform3fv(shaders.getProgram("default").getUniform("directionalLight"), new float[]{0f, 0.5f, 1f});

    width.rewind();
    height.rewind();

    glViewport(0, 0, width.get(), height.get());
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    glClearColor(0f, 0f, 0f, 0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glDrawArrays(GL_TRIANGLES, 0, 36);

    glfwSwapBuffers(window);
    glfwPollEvents();

    width.flip();
    height.flip();

    fpsCount++;
  }

  private static void exit() {
    MemoryUtil.memFree(width);
    MemoryUtil.memFree(height);
    glDeleteProgram(shaders.getProgram("default").getProgram());
    glDeleteShader(shaders.getVertexShader("default").getShader());
    glDeleteShader(shaders.getFragmentShader("default").getShader());
    glDeleteVertexArrays(vao);
    glDeleteBuffers(vbo);
    keyCallback.free();
    glfwDestroyWindow(window);
    glfwTerminate();
    errorCallback.free();
  }

  public static void main(String[] args) {
    long lastLoopTime, time;
    double delta, accumulator = 0d;
    float timeCount = 0f;

    init();

    lastLoopTime = System.nanoTime();
    while (!glfwWindowShouldClose(window)) {
      time = System.nanoTime();
      delta = (time - lastLoopTime) / 1000000000d;
      lastLoopTime = time;
      timeCount += (float) delta;
      accumulator += delta;

      input();

      if (accumulator >= 1d / 30d) {
        update(accumulator);
        accumulator %= 1d / 30d;
      }

      render(accumulator * 30d);

      if (timeCount >= 1f) {
        System.out.printf("FPS: %d, UPS: %d\n", fpsCount, upsCount);

        fpsCount = 0;
        upsCount = 0;

        timeCount %= 1f;
      }
    }

    exit();
  }
}
