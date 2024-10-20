package net.catech_software.engine.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL41C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
  private AIMesh mesh;
  private Material material;
  private final int vao = GL41C.glGenVertexArrays();
  private final int vbo = GL41C.glGenBuffers();
  private final int ebo = GL41C.glGenBuffers();

  public Mesh(AIMesh mesh, ArrayList<Material> materials) {
    int count;
    FloatBuffer vertices;
    IntBuffer indices;

    this.mesh = mesh;
    this.material = materials.get(this.mesh.mMaterialIndex());

    GL41C.glBindVertexArray(this.vao);

    GL41C.glBindBuffer(GL41C.GL_ARRAY_BUFFER, this.vbo);
    count = this.mesh.mNumVertices();
    vertices = MemoryUtil.memAllocFloat(count * 18);
    for (int i = 0; i < count; i++) {
      AIVector3D vertex = this.mesh.mVertices().get(i);
      AIVector3D texCoord = this.mesh.mTextureCoords(0).get(i);
      AIVector3D tangent = this.mesh.mTangents().get(i);
      AIVector3D bitangent = this.mesh.mBitangents().get(i);
      AIVector3D normal = this.mesh.mNormals().get(i);

      // Position: 3 floats (xyz), Color: 4 floats (rgba), Texture: 2 floats (uv),
      // Tangent: 3 floats (xyz), Bitangent: 3 floats (xyz), Normal: 3 floats (xyz)
      vertices.put(vertex.x()).put(vertex.y()).put(vertex.z())
              .put(1f).put(1f).put(1f).put(1f)
              .put(texCoord.x()).put(texCoord.y())
              .put(tangent.x()).put(tangent.y()).put(tangent.z())
              .put(bitangent.x()).put(bitangent.y()).put(bitangent.z())
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
    GL41C.glVertexAttribPointer(0, 3, GL41C.GL_FLOAT, false, 18 * 4, 0);
    GL41C.glEnableVertexAttribArray(1);
    GL41C.glVertexAttribPointer(1, 4, GL41C.GL_FLOAT, false, 18 * 4, 3 * 4);
    GL41C.glEnableVertexAttribArray(2);
    GL41C.glVertexAttribPointer(2, 2, GL41C.GL_FLOAT, false, 18 * 4, 7 * 4);
    GL41C.glEnableVertexAttribArray(3);
    GL41C.glVertexAttribPointer(3, 3, GL41C.GL_FLOAT, false, 18 * 4, 9 * 4);
    GL41C.glEnableVertexAttribArray(4);
    GL41C.glVertexAttribPointer(4, 3, GL41C.GL_FLOAT, false, 18 * 4, 12 * 4);
    GL41C.glEnableVertexAttribArray(5);
    GL41C.glVertexAttribPointer(5, 3, GL41C.GL_FLOAT, false, 18 * 4, 15 * 4);

    GL41C.glBindVertexArray(0);
    GL41C.glBindBuffer(GL41C.GL_ARRAY_BUFFER, 0);
    GL41C.glBindBuffer(GL41C.GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  public void draw(int model, Matrix4f transformation) {
    GL41C.glActiveTexture(GL41C.GL_TEXTURE0);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, this.material.getBaseColorTex().getTexture());
    GL41C.glActiveTexture(GL41C.GL_TEXTURE1);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, this.material.getEmissiveTex().getTexture());
    GL41C.glActiveTexture(GL41C.GL_TEXTURE2);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, this.material.getNormalTex().getTexture());
    GL41C.glActiveTexture(GL41C.GL_TEXTURE3);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, this.material.getOcclusionRoughnessMetallicTex().getTexture());

    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer mat4 = stack.mallocFloat(16);

      GL41C.glUniformMatrix4fv(model, false, transformation.get(mat4));
    }

    GL41C.glBindVertexArray(this.vao);
    GL41C.glDrawElements(GL41C.GL_TRIANGLES, this.mesh.mNumFaces() * 3, GL41C.GL_UNSIGNED_INT, 0);
    GL41C.glBindVertexArray(0);

    GL41C.glActiveTexture(GL41C.GL_TEXTURE0);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, 0);
    GL41C.glActiveTexture(GL41C.GL_TEXTURE1);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, 0);
    GL41C.glActiveTexture(GL41C.GL_TEXTURE2);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, 0);
    GL41C.glActiveTexture(GL41C.GL_TEXTURE3);
    GL41C.glBindTexture(GL41C.GL_TEXTURE_2D, 0);
  }

  public void free() {
    GL41C.glDeleteVertexArrays(this.vao);
    GL41C.glDeleteBuffers(this.vbo);
    GL41C.glDeleteBuffers(this.ebo);
    this.material = null;
    this.mesh = null;
  }
}
