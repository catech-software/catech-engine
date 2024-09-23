package net.catech_software.util;

import java.io.IOException;
import java.io.BufferedReader;

public class BufferedReaderToString {
  public static String bufferedReaderToString(BufferedReader reader) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line);
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }
}
