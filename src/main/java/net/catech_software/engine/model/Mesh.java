package net.catech_software.engine.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL41C;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
  private AIMesh mesh;
  private final int vao, vbo, ebo;

  public Mesh(AIMesh mesh, ArrayList<Material> materials) {
    int count;
    FloatBuffer vertices;
    IntBuffer indices;

    this.mesh = mesh;

    this.vao = GL41C.glGenVertexArrays();
    this.vbo = GL41C.glGenBuffers();
    this.ebo = GL41C.glGenBuffers();

    GL41C.glBindVertexArray(this.vao);

    GL41C.glBindBuffer(GL41C.GL_ARRAY_BUFFER, this.vbo);
    count = mesh.mNumVertices();
    vertices = MemoryUtil.memAllocFloat(count * 12);
    for (int i = 0; i < count; i++) {
      AIVector3D vertex = mesh.mVertices().get(i);
      AIVector3D texCoord = mesh.mTextureCoords(0).get(i);
      AIVector3D normal = mesh.mNormals().get(i);

      // Position: 3 floats (xyz), Color: 4 floats (rgba), Texture: 2 floats (uv), Normal: 3 floats (xyz)
      vertices.put(vertex.x()).put(vertex.y()).put(vertex.z())
              .put(1f).put(1f).put(1f).put(1f)
              .put(texCoord.x()).put(texCoord.y())
              .put(normal.x()).put(normal.y()).put(normal.z());
    }
    vertices.flip();
    GL41C.glBufferData(GL41C.GL_ARRAY_BUFFER, vertices, GL41C.GL_STATIC_DRAW);
    MemoryUtil.memFree(vertices);

    GL41C.glBindBuffer(GL41C.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
    count = mesh.mNumFaces();
    indices = MemoryUtil.memAllocInt(count * 3);
    for (int i = 0; i < count; i++) {
      AIFace face = mesh.mFaces().get(i);
      if (face.mNumIndices() != 3) throw new RuntimeException("Faces must be triangles");
      indices.put(face.mIndices());
    }
    indices.flip();
    GL41C.glBufferData(GL41C.GL_ELEMENT_ARRAY_BUFFER, indices, GL41C.GL_STATIC_DRAW);
    MemoryUtil.memFree(indices);

    GL41C.glEnableVertexAttribArray(0);
    GL41C.glVertexAttribPointer(0, 3, GL41C.GL_FLOAT, false, 12 * 4, 0);

    GL41C.glEnableVertexAttribArray(1);
    GL41C.glVertexAttribPointer(1, 4, GL41C.GL_FLOAT, false, 12 * 4, 3 * 4);

    GL41C.glEnableVertexAttribArray(2);
    GL41C.glVertexAttribPointer(2, 2, GL41C.GL_FLOAT, false, 12 * 4, 7 * 4);

    GL41C.glEnableVertexAttribArray(3);
    GL41C.glVertexAttribPointer(3, 3, GL41C.GL_FLOAT, false, 12 * 4, 9 * 4);

    GL41C.glBindVertexArray(0);
  }

  public void draw() {
    GL41C.glBindVertexArray(this.vao);
    GL41C.glDrawElements(GL41C.GL_TRIANGLES, this.mesh.mNumFaces() * 3, GL41C.GL_UNSIGNED_INT, 0);
    GL41C.glBindVertexArray(0);
  }

  public void free() {
    GL41C.glDeleteVertexArrays(this.vao);
    GL41C.glDeleteBuffers(this.vbo);
    GL41C.glDeleteBuffers(this.ebo);
    this.mesh = null;
  }
}
