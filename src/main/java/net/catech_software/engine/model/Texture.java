package net.catech_software.engine.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import net.catech_software.util.Resource;

public class Texture {
  private int texture;
  private ByteBuffer image;
  private int width, height, components;
  private String path;

  public Texture(String path) {
    this.path = path;

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer width = stack.mallocInt(1);
      IntBuffer height = stack.mallocInt(1);
      IntBuffer components = stack.mallocInt(1);

      this.image = STBImage.stbi_load_from_memory(Resource.getResourceAsByteBuffer(this.path), width, height, components, 4);
      if (this.image == null) throw new RuntimeException(STBImage.stbi_failure_reason());
      this.width = width.get();
      this.height = height.get();
      this.components = components.get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
