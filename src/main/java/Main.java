import java.io.IOException;
import java.lang.Math;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL41C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.catech_software.engine.input.Controls;
import net.catech_software.engine.model.LoadScene;
import net.catech_software.engine.model.Model;
import net.catech_software.engine.object.Player;
import net.catech_software.engine.render.shader.*;
import net.catech_software.util.Resource;

public class Main {
  private static long window;
  private static ShaderList shaders;
  private static Model model;
  private static Model level;
  private static IntBuffer width, height;
  private static int fpsCount, upsCount;
  private static final Controls controls = new Controls();
  private static final Player player = new Player();

  private static final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

  private static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
      if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) GLFW.glfwSetWindowShouldClose(window, true);

      if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS) controls.moveForward = true;
      if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_RELEASE) controls.moveForward = false;

      if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS) controls.moveBack = true;
      if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_RELEASE) controls.moveBack = false;

      if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) controls.moveLeft = true;
      if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_RELEASE) controls.moveLeft = false;

      if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS) controls.moveRight = true;
      if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_RELEASE) controls.moveRight = false;

      if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) controls.moveUp = true;
      if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_RELEASE) controls.moveUp = false;

      if (key == GLFW.GLFW_KEY_LEFT_CONTROL && action == GLFW.GLFW_PRESS) controls.moveDown = true;
      if (key == GLFW.GLFW_KEY_LEFT_CONTROL && action == GLFW.GLFW_RELEASE) controls.moveDown = false;
    }
  };

  private static void init() {
    GLFWVidMode vidMode;

    GLFW.glfwSetErrorCallback(errorCallback);

    if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
    GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
    window = GLFW.glfwCreateWindow(640, 480, "Foobar", MemoryUtil.NULL, MemoryUtil.NULL);
    if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");

    width = MemoryUtil.memAllocInt(1);
    height = MemoryUtil.memAllocInt(1);
    GLFW.glfwGetFramebufferSize(window, width, height);

    vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    if (vidMode == null) throw new RuntimeException("Failed to get video mode");
    GLFW.glfwSetWindowPos(window, (vidMode.width() - 640) / 2, (vidMode.height() - 480) / 2);

    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    GLFW.glfwSetInputMode(window, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
    controls.mouseX = width.get() / 2d;
    controls.mouseY = height.get() / 2d;
    width.rewind();
    height.rewind();
    GLFW.glfwSetCursorPos(window, controls.mouseX, controls.mouseY);
    GLFW.glfwSetKeyCallback(window, keyCallback);

    GLFW.glfwMakeContextCurrent(window);
    GL.createCapabilities();

    shaders = new ShaderList();
    shaders.setProgram("default", new ShaderProgram());
    shaders.setVertexShader("default", new VertexShader());
    shaders.setFragmentShader("default", new FragmentShader());
    try {
      shaders.getVertexShader("default").setSource(Resource.getResourceAsString("assets/shaders/default.vert"));
      shaders.getFragmentShader("default").setSource(Resource.getResourceAsString("assets/shaders/default.frag"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    shaders.getVertexShader("default").setShader(GL41C.glCreateShader(GL41C.GL_VERTEX_SHADER));
    GL41C.glShaderSource(shaders.getVertexShader("default").getShader(), shaders.getVertexShader("default").getSource());
    GL41C.glCompileShader(shaders.getVertexShader("default").getShader());
    if (GL41C.glGetShaderi(shaders.getVertexShader("default").getShader(), GL41C.GL_COMPILE_STATUS) != GL41C.GL_TRUE)
      throw new RuntimeException(GL41C.glGetShaderInfoLog(shaders.getVertexShader("default").getShader()));

    shaders.getFragmentShader("default").setShader(GL41C.glCreateShader(GL41C.GL_FRAGMENT_SHADER));
    GL41C.glShaderSource(shaders.getFragmentShader("default").getShader(), shaders.getFragmentShader("default").getSource());
    GL41C.glCompileShader(shaders.getFragmentShader("default").getShader());
    if (GL41C.glGetShaderi(shaders.getFragmentShader("default").getShader(), GL41C.GL_COMPILE_STATUS) != GL41C.GL_TRUE)
      throw new RuntimeException(GL41C.glGetShaderInfoLog(shaders.getFragmentShader("default").getShader()));

    shaders.getProgram("default").setProgram(GL41C.glCreateProgram());
    GL41C.glAttachShader(shaders.getProgram("default").getProgram(), shaders.getVertexShader("default").getShader());
    GL41C.glAttachShader(shaders.getProgram("default").getProgram(), shaders.getFragmentShader("default").getShader());
    GL41C.glLinkProgram(shaders.getProgram("default").getProgram());
    if (GL41C.glGetProgrami(shaders.getProgram("default").getProgram(), GL41C.GL_LINK_STATUS) != GL41C.GL_TRUE)
      throw new RuntimeException(GL41C.glGetProgramInfoLog(shaders.getProgram("default").getProgram()));
    GL41C.glUseProgram(shaders.getProgram("default").getProgram());

    shaders.getProgram("default").setAttribute("position", GL41C.glGetAttribLocation(shaders.getProgram("default").getProgram(), "position"));
    shaders.getProgram("default").setAttribute("color", GL41C.glGetAttribLocation(shaders.getProgram("default").getProgram(), "color"));
    shaders.getProgram("default").setAttribute("texCoord", GL41C.glGetAttribLocation(shaders.getProgram("default").getProgram(), "texCoord"));
    shaders.getProgram("default").setAttribute("normal", GL41C.glGetAttribLocation(shaders.getProgram("default").getProgram(), "normal"));
    shaders.getProgram("default").setUniform("model", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "model"));
    shaders.getProgram("default").setUniform("view", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "view"));
    shaders.getProgram("default").setUniform("projection", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "projection"));
    shaders.getProgram("default").setUniform("viewPosition", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "viewPosition"));
    shaders.getProgram("default").setUniform("lightColor", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "lightColor"));
    shaders.getProgram("default").setUniform("ambientLight", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "ambientLight"));
    shaders.getProgram("default").setUniform("directionalLight", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "directionalLight"));
    shaders.getProgram("default").setUniform("baseColorTex", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "baseColorTex"));
    shaders.getProgram("default").setUniform("emissiveTex", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "emissiveTex"));
    shaders.getProgram("default").setUniform("normalTex", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "normalTex"));
    shaders.getProgram("default").setUniform("occlusionRoughnessMetallicTex", GL41C.glGetUniformLocation(shaders.getProgram("default").getProgram(), "occlusionRoughnessMetallicTex"));
    shaders.getProgram("default").setDataLocation("fragColor", GL41C.glGetFragDataLocation(shaders.getProgram("default").getProgram(), "fragColor"));

    model = new Model(LoadScene.loadScene("assets/models/WaterBottle/WaterBottle.gltf"));
    level = new Model(LoadScene.loadScene("assets/models/map0/map0.gltf"));
  }

  private static void input() {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      DoubleBuffer mouseX = stack.mallocDouble(1);
      DoubleBuffer mouseY = stack.mallocDouble(1);
      double newMouseX, newMouseY;

      GLFW.glfwGetCursorPos(window, mouseX, mouseY);
      newMouseX = mouseX.get();
      newMouseY = mouseY.get();
      controls.deltaX += newMouseX - controls.mouseX;
      controls.deltaY += newMouseY - controls.mouseY;
      controls.mouseX = newMouseX;
      controls.mouseY = newMouseY;
    }
  }

  private static void update(double delta) {
    Vector3f camera = new Vector3f();

    player.prevRotation = new Quaternionf(player.rotation);
    player.rotation.rotateY((float) (-controls.deltaX * delta) * 0.2f);

    player.prevCamera = new Quaternionf(player.camera);
    player.camera.rotateX((float) (-controls.deltaY * delta) * 0.2f);
    player.camera.getEulerAnglesXYZ(camera);
    if (camera.x() < -Math.PI / 2) player.camera.rotationX((float) -Math.PI / 2);
    if (camera.x() > Math.PI / 2) player.camera.rotationX((float) Math.PI / 2);

    controls.deltaX = 0d;
    controls.deltaY = 0d;

    player.prevPosition = new Vector3f(player.position);
    if (controls.moveForward) player.position.add(new Vector3f(0f, 0f, -1.3f * (float) delta).rotate(player.rotation));
    if (controls.moveBack) player.position.add(new Vector3f(0f, 0f, 1.3f * (float) delta).rotate(player.rotation));
    if (controls.moveLeft) player.position.add(new Vector3f(-1.3f * (float) delta, 0f, 0f).rotate(player.rotation));
    if (controls.moveRight) player.position.add(new Vector3f(1.3f * (float) delta, 0f, 0f).rotate(player.rotation));
    if (controls.moveDown) player.position.add(0f, -1.3f * (float) delta, 0f);
    if (controls.moveUp) player.position.add(0f, 1.3f * (float) delta, 0f);

    upsCount++;
  }

  private static void render(double alpha) {
    float ratio;

    GLFW.glfwGetFramebufferSize(window, width, height);
    ratio = width.get() / (float) height.get();

    try (MemoryStack stack = MemoryStack.stackPush()) {
      Matrix4f prevView, currView;
      FloatBuffer mat4 = stack.mallocFloat(16);
      FloatBuffer vec3 = stack.mallocFloat(3);

      prevView = new Matrix4f().rotate(new Quaternionf(player.prevCamera).invert()).rotate(new Quaternionf(player.prevRotation).invert()).translate(new Vector3f(player.prevPosition).negate());
      currView = new Matrix4f().rotate(new Quaternionf(player.camera).invert()).rotate(new Quaternionf(player.rotation).invert()).translate(new Vector3f(player.position).negate());
      GL41C.glUniformMatrix4fv(shaders.getProgram("default").getUniform("view"), false, prevView.lerp(currView, (float) alpha).translate(0f, -1.59f, 0f).get(mat4));
      GL41C.glUniformMatrix4fv(shaders.getProgram("default").getUniform("projection"), false, new Matrix4f().perspective((float) Math.toRadians(70f), ratio, 0.01f, 100f).get(mat4));

      GL41C.glUniform3fv(shaders.getProgram("default").getUniform("viewPosition"), player.position.get(vec3));
      GL41C.glUniform3fv(shaders.getProgram("default").getUniform("lightColor"), new float[]{1f, 1f, 1f});
      GL41C.glUniform1f(shaders.getProgram("default").getUniform("ambientLight"), 0.1f);
      GL41C.glUniform3fv(shaders.getProgram("default").getUniform("directionalLight"), new float[]{0f, -0.5f, -1f});

      GL41C.glUniform1i(shaders.getProgram("default").getUniform("baseColorTex"), 0);
      GL41C.glUniform1i(shaders.getProgram("default").getUniform("emissiveTex"), 1);
      GL41C.glUniform1i(shaders.getProgram("default").getUniform("normalTex"), 2);
      GL41C.glUniform1i(shaders.getProgram("default").getUniform("occlusionRoughnessMetallicTex"), 3);
    }

    width.rewind();
    height.rewind();

    GL41C.glViewport(0, 0, width.get(), height.get());
    GL41C.glEnable(GL41C.GL_DEPTH_TEST);
    GL41C.glEnable(GL41C.GL_CULL_FACE);
    GL41C.glClearColor(0f, 0f, 0f, 0f);
    GL41C.glClear(GL41C.GL_COLOR_BUFFER_BIT | GL41C.GL_DEPTH_BUFFER_BIT);
    model.draw(shaders.getProgram("default").getUniform("model"), new Matrix4f().translate(0f, 0f, -3f));
    level.draw(shaders.getProgram("default").getUniform("model"), new Matrix4f());

    GLFW.glfwSwapBuffers(window);
    GLFW.glfwPollEvents();

    width.flip();
    height.flip();

    fpsCount++;
  }

  private static void exit() {
    MemoryUtil.memFree(width);
    MemoryUtil.memFree(height);
    level.free();
    GL41C.glDeleteProgram(shaders.getProgram("default").getProgram());
    GL41C.glDeleteShader(shaders.getVertexShader("default").getShader());
    GL41C.glDeleteShader(shaders.getFragmentShader("default").getShader());
    keyCallback.free();
    GLFW.glfwDestroyWindow(window);
    GLFW.glfwTerminate();
    errorCallback.free();
  }

  public static void main(String[] args) {
    long lastLoopTime, time;
    double delta, accumulator = 0d;
    float timeCount = 0f;

    init();

    lastLoopTime = System.nanoTime();
    while (!GLFW.glfwWindowShouldClose(window)) {
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
