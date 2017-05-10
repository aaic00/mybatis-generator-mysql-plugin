package org.mybatis.generator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mybatis.generator.internal.DefaultShellCallback;

public class TestShellCallback extends DefaultShellCallback {

  public TestShellCallback(final boolean overwrite) {
    super(overwrite);
  }

  @Override
  public File getDirectory(final String targetProject, final String targetPackage) {
    try {
      final Path path = Paths.get(this.getClass().getResource(".").toURI()).getParent().getParent()
          .getParent().resolve("generater");

      final File file = path.toFile();

      if (!file.exists()) {
        Files.createDirectories(path);
      }
      return path.toFile();
    } catch (final Exception e) {
      System.err.println(e);
      return null;
    }
  }



}
