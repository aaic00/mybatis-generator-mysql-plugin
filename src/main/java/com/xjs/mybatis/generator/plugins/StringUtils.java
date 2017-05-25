package com.xjs.mybatis.generator.plugins;

public final class StringUtils {
  private StringUtils() {}

  public static String join(final Object... elements) {
    final StringBuilder sb = new StringBuilder();
    for (final Object element : elements) {
      if (element != null) {
        sb.append(element);
      }
    }
    return sb.toString();
  }
}
