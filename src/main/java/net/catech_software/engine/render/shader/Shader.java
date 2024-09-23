package net.catech_software.engine.render.shader;

abstract class Shader {
  private int shader;
  private String source;

  public int getShader() {
    return this.shader;
  }

  public void setShader(int value) {
    this.shader = value;
  }

  public String getSource() {
    return this.source;
  }

  public void setSource(String value) {
    this.source = value;
  }
}
