package com.xjs.mybatis.generator.plugins;

import java.util.Objects;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;

public final class PropertiesUtils {

  public final static String TABLE_ENABLE_INSERT_IGNORE = "insertIgnore";
  public static final String TABLE_ENABLE_INSERT_BATCH = "insertBatch";
  public final static String COLUMN_IGNORE_CONSTRUCTOR = "ignoreConstructor";

  private PropertiesUtils() {

  }

  public static boolean isTrue(final IntrospectedTable introspectedTable, final String property) {
    return PropertiesUtils.check(introspectedTable, property, "true");
  }

  public static boolean check(final IntrospectedTable introspectedTable, final String property,
      final String value) {
    return Objects.equals(value, introspectedTable.getTableConfigurationProperty(property));
  }

  public static boolean isTrue(final IntrospectedColumn introspectedColumn, final String property) {
    return PropertiesUtils.check(introspectedColumn, property, "true");
  }

  public static boolean check(final IntrospectedColumn introspectedColumn, final String property,
      final String value) {
    return Objects.equals(value, introspectedColumn.getProperties().getProperty(property));
  }

}
