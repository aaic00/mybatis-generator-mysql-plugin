package com.xjs.mybatis.generator.plugins.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import com.xjs.mybatis.generator.plugins.base.JDBCType;

public class ColumnContext {

  private final static Set<FullyQualifiedJavaType> importedTypes =
      Collections.unmodifiableSet(new HashSet<FullyQualifiedJavaType>(
          Arrays.asList(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"))));

  private final String columnName;
  private final String javaProperty;
  private final Parameter parameter;
  private final Parameter annotatedParameter;
  private final TextElement whereElement;

  public ColumnContext(final String columnName, final JDBCType jdbcType,
      final FullyQualifiedJavaType javaType) {
    this.columnName = columnName;
    this.javaProperty = JavaBeansUtil.getCamelCaseString(this.columnName, false);
    this.parameter = new Parameter(javaType, this.javaProperty);
    this.annotatedParameter =
        new Parameter(javaType, this.javaProperty, "@Param(\"" + this.javaProperty + "\")");
    this.whereElement = new TextElement(StringUtils.join("where ", columnName, " = #{",
        this.javaProperty, ",jdbcType=", jdbcType.name(), "}"));
  }

  public Set<FullyQualifiedJavaType> getImportedTypes() {
    return ColumnContext.importedTypes;
  }

  public String getColumnName() {
    return this.columnName;
  }

  public String getJavaProperty() {
    return this.javaProperty;
  }

  public Parameter getParameter() {
    return this.parameter;
  }

  public Parameter getAnnotatedParameter() {
    return this.annotatedParameter;
  }

  public TextElement getWhereElement() {
    return this.whereElement;
  }

}
