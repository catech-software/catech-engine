package net.catech_software.engine.render.shader;

import java.util.HashMap;

public class ShaderList {
  private final HashMap<String, ShaderProgram> programs = new HashMap<>();
  private final HashMap<String, VertexShader> vertexShaders = new HashMap<>();
  private final HashMap<String, TessControlShader> tessControlShaders = new HashMap<>();
  private final HashMap<String, TessEvaluationShader> tessEvaluationShaders = new HashMap<>();
  private final HashMap<String, GeometryShader> geometryShaders = new HashMap<>();
  private final HashMap<String, FragmentShader> fragmentShaders = new HashMap<>();
  private final HashMap<String, ComputeShader> computeShaders = new HashMap<>();

  public ShaderProgram getProgram(String key) {
    return this.programs.get(key);
  }

  public void setProgram(String key, ShaderProgram value) {
    this.programs.put(key, value);
  }

  public HashMap<String, ShaderProgram> getPrograms() {
    return this.programs;
  }

  public VertexShader getVertexShader(String key) {
    return this.vertexShaders.get(key);
  }

  public void setVertexShader(String key, VertexShader value) {
    this.vertexShaders.put(key, value);
  }

  public HashMap<String, VertexShader> getVertexShaders() {
    return this.vertexShaders;
  }

  public TessControlShader getTessControlShader(String key) {
    return this.tessControlShaders.get(key);
  }

  public void setTessControlShader(String key, TessControlShader value) {
    this.tessControlShaders.put(key, value);
  }

  public HashMap<String, TessControlShader> getTessControlShaders() {
    return this.tessControlShaders;
  }

  public TessEvaluationShader getTessEvaluationShader(String key) {
    return this.tessEvaluationShaders.get(key);
  }

  public void setTessEvaluationShader(String key, TessEvaluationShader value) {
    this.tessEvaluationShaders.put(key, value);
  }

  public HashMap<String, TessEvaluationShader> getTessEvaluationShaders() {
    return this.tessEvaluationShaders;
  }

  public GeometryShader getGeometryShader(String key) {
    return this.geometryShaders.get(key);
  }

  public void setGeometryShader(String key, GeometryShader value) {
    this.geometryShaders.put(key, value);
  }

  public HashMap<String, GeometryShader> getGeometryShaders() {
    return this.geometryShaders;
  }

  public FragmentShader getFragmentShader(String key) {
    return this.fragmentShaders.get(key);
  }

  public void setFragmentShader(String key, FragmentShader value) {
    this.fragmentShaders.put(key, value);
  }

  public HashMap<String, FragmentShader> getFragmentShaders() {
    return this.fragmentShaders;
  }

  public ComputeShader getComputeShader(String key) {
    return this.computeShaders.get(key);
  }

  public void setComputeShader(String key, ComputeShader value) {
    this.computeShaders.put(key, value);
  }

  public HashMap<String, ComputeShader> getComputeShaders() {
    return this.computeShaders;
  }
}
