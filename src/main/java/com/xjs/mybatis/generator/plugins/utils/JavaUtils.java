package com.xjs.mybatis.generator.plugins.utils;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Parameter;

public final class JavaUtils {

  private JavaUtils() {}

  public static Parameter annotationParameter(final FullyQualifiedJavaType type,
      final String name) {
    return new Parameter(type, name, "@Param(\"" + name + "\")");
  }

  public static void addImport(final Interface interfaze, final String... imports) {
    for (final String type : imports) {
      interfaze.addImportedType(new FullyQualifiedJavaType(type));
    }
  }
}
