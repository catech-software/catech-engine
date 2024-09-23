package net.catech_software.engine.render.shader;

import java.util.HashMap;

public class ShaderProgram {
  private int program;
  private final HashMap<String, Integer> attributes = new HashMap<>();
  private final HashMap<String, Integer> uniforms = new HashMap<>();
  private final HashMap<String, Integer> dataLocations = new HashMap<>();

  public int getProgram() {
    return this.program;
  }

  public void setProgram(int value) {
    this.program = value;
  }

  public int getAttribute(String key) {
    return this.attributes.get(key);
  }

  public void setAttribute(String key, int value) {
    this.attributes.put(key, value);
  }

  public HashMap<String, Integer> getAttributes() {
    return this.attributes;
  }

  public int getUniform(String key) {
    return this.uniforms.get(key);
  }

  public void setUniform(String key, int value) {
    this.uniforms.put(key, value);
  }

  public HashMap<String, Integer> getUniforms() {
    return this.uniforms;
  }

  public int getDataLocation(String key) {
    return this.dataLocations.get(key);
  }

  public void setDataLocation(String key, int value) {
    this.dataLocations.put(key, value);
  }

  public HashMap<String, Integer> getDataLocations() {
    return this.dataLocations;
  }
}
