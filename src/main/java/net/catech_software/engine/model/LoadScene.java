package net.catech_software.engine.model;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;

import net.catech_software.util.Resource;

public class LoadScene {
  /*
   * Based on LWJGL Assimp demo: https://github.com/LWJGL/lwjgl3-demos/blob/main/src/org/lwjgl/demo/opengl/assimp/WavefrontObjDemo.java
   */
  public static AIScene loadScene(String path) {
    AIFileIO fileIO;
    AIScene scene;

    fileIO = AIFileIO.create()
                     .OpenProc((pFileIO, fileName, openMode) -> {
                       String filePath = MemoryUtil.memUTF8(fileName);
                       ByteBuffer data;

                       try  {
                         data = Resource.getResourceAsByteBuffer(filePath);
                       } catch (IOException e) {
                         throw new RuntimeException(e);
                       }

                       return AIFile.create()
                                    .ReadProc((pFile, pBuffer, size, count) -> {
                                      long items = Math.min(data.remaining() / size, count);

                                      MemoryUtil.memCopy(MemoryUtil.memAddress(data), pBuffer, items * size);
                                      data.position(data.position() + (int) (items * size));
                                      return items;
                                    })
                                    .TellProc(pFile -> data.position())
                                    .FileSizeProc(pFile -> data.limit())
                                    .SeekProc((pFile, offset, origin) -> {
                                      if (origin == Assimp.aiOrigin_SET) {
                                        data.position((int) offset);
                                      } else if (origin == Assimp.aiOrigin_CUR) {
                                        data.position(data.position() + (int) offset);
                                      } else if (origin == Assimp.aiOrigin_END) {
                                        data.position(data.limit() + (int) offset);
                                      }
                                      return 0;
                                    })
                                    .address();
                     })
                     .CloseProc((pFileIO, pFile) -> {
                       AIFile file = AIFile.create(pFile);

                       file.ReadProc().free();
                       file.TellProc().free();
                       file.FileSizeProc().free();
                       file.SeekProc().free();
                     });

    scene = Assimp.aiImportFileEx(path, Assimp.aiProcess_SplitLargeMeshes |
                                        Assimp.aiProcess_JoinIdenticalVertices |
                                        Assimp.aiProcess_Triangulate |
                                        Assimp.aiProcess_GenUVCoords |
                                        Assimp.aiProcess_GenNormals |
                                        Assimp.aiProcess_CalcTangentSpace |
                                        Assimp.aiProcess_SortByPType, fileIO);
    fileIO.OpenProc().free();
    fileIO.CloseProc().free();
    return scene;
  }
}
