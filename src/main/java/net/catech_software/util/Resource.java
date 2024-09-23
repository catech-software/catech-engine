package net.catech_software.util;

import java.io.*;

public class Resource {
  public static BufferedReader getResourceFromJar(String path) throws IOException {
    InputStream in = Resource.class.getResourceAsStream(path);
    InputStreamReader inputReader = new InputStreamReader(in);
    return new BufferedReader(inputReader);
  }
}
