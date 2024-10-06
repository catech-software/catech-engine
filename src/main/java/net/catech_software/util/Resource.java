package net.catech_software.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Resource {
  public static ByteBuffer getResourceAsByteBuffer(String path) throws IOException {
    try (InputStream stream = Resource.class.getClassLoader().getResourceAsStream(path)) {
      if (stream != null) {
        ReadableByteChannel rbc = Channels.newChannel(stream);
        ByteBuffer buffer = ByteBuffer.allocateDirect(8196).order(ByteOrder.nativeOrder());

        while (true) {
          int bytes = rbc.read(buffer);

          if (bytes == -1) break;

          if (buffer.remaining() == 0) {
            buffer.flip();
            buffer = ByteBuffer.allocateDirect(buffer.capacity() + 8196).order(ByteOrder.nativeOrder()).put(buffer);
          }
        }

        return buffer.flip();
      } else {
        throw new IOException("Could not open " + path);
      }
    }
  }

  public static String getResourceAsString(String path) throws IOException {
    try (InputStream stream = Resource.class.getClassLoader().getResourceAsStream(path)) {
      if (stream != null) {
        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
      } else {
        throw new IOException("Could not open " + path);
      }
    }
  }
}
