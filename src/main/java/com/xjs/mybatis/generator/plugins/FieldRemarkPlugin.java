package com.xjs.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class FieldRemarkPlugin extends PluginAdapter {

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelFieldGenerated(final Field field, final TopLevelClass topLevelClass,
      final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable,
      final ModelClassType modelClassType) {
    final List<String> javaDocLines = field.getJavaDocLines();
    javaDocLines.set(2, " * ".concat(introspectedColumn.getRemarks()));
    javaDocLines.remove(3);
    return true;
  }



}
