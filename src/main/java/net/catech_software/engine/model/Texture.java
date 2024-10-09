package net.catech_software.engine.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL41;
import org.lwjgl.opengl.GL41C;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import net.catech_software.util.Resource;

public class Texture {
  private final int texture;

  public Texture(String path) {
    this.texture = GL41C.glGenTextures();
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, this.texture);
    GL41C.glTexParameteri(GL41C.GL_TEXTURE_2D, GL41C.GL_TEXTURE_WRAP_S, GL41C.GL_REPEAT);
    GL41C.glTexParameteri(GL41C.GL_TEXTURE_2D, GL41C.GL_TEXTURE_WRAP_T, GL41C.GL_REPEAT);
    GL41C.glTexParameteri(GL41C.GL_TEXTURE_2D, GL41.GL_TEXTURE_MIN_FILTER, GL41C.GL_LINEAR);
    GL41C.glTexParameteri(GL41C.GL_TEXTURE_2D, GL41.GL_TEXTURE_MAG_FILTER, GL41C.GL_LINEAR);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer width = stack.mallocInt(1);
      IntBuffer height = stack.mallocInt(1);
      IntBuffer components = stack.mallocInt(1);
      ByteBuffer image;

      STBImage.stbi_set_flip_vertically_on_load(true);
      image = STBImage.stbi_load_from_memory(Resource.getResourceAsByteBuffer(path), width, height, components, 4);
      if (image == null) throw new RuntimeException(STBImage.stbi_failure_reason());

      GL41C.glTexImage2D(GL41C.GL_TEXTURE_2D, 0, GL41C.GL_RGBA8, width.get(), height.get(), 0, GL41C.GL_RGBA, GL41C.GL_UNSIGNED_BYTE, image);
      GL41C.glGenerateMipmap(GL41C.GL_TEXTURE_2D);

      STBImage.stbi_image_free(image);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, 0);
  }

  public void free() {
    GL41C.glDeleteTextures(this.texture);
  }

  public int getTexture() {
    return this.texture;
  }
}
