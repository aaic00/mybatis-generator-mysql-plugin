/**
 * Copyright 2006-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.xjs.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.PropertyRegistry;

public class ParameterizedConstructorPlugin extends PluginAdapter {

  private List<String> warnings;

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    List<IntrospectedColumn> columns;
    if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
      columns = introspectedTable.getNonBLOBColumns();
    } else {
      columns = introspectedTable.getAllColumns();
    }

    this.generateConstructor(topLevelClass, columns, introspectedTable);

    return true;
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.generateConstructor(topLevelClass, introspectedTable.getPrimaryKeyColumns(),
        introspectedTable);

    return true;
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.generateConstructor(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);

    return true;
  }


  protected void generateConstructor(final TopLevelClass topLevelClass,
      final List<IntrospectedColumn> allColumns, final IntrospectedTable introspectedTable) {
    final List<Method> methods = new ArrayList<Method>();
    methods.add(this.generateDefaultConstructor(topLevelClass, introspectedTable));
    methods.add(this.generateOptionalConstructor(topLevelClass, allColumns, introspectedTable));

    for (final Method method : topLevelClass.getMethods()) {
      if (!method.isConstructor()) {
        methods.add(method);
      }
    }

    final List<Method> classMethods = topLevelClass.getMethods();
    classMethods.clear();
    classMethods.addAll(methods);

  }

  protected Method generateOptionalConstructor(final TopLevelClass topLevelClass,
      final List<IntrospectedColumn> allColumns, final IntrospectedTable introspectedTable) {
    final String rootClass = this.getRootClass(introspectedTable);
    final RootClassInfo rootClassInfo = RootClassInfo.getInstance(rootClass, this.warnings);

    final List<String> inheritConstructorParameters = new ArrayList<String>();

    final Method method = this.generateConstructorFramework(topLevelClass, introspectedTable);
    for (final IntrospectedColumn introspectedColumn : allColumns) {
      if (PropertiesUtils.check(introspectedColumn,
          PropertiesUtils.COLUMN_IGNORE_CONSTRUCTOR, "true") || introspectedColumn.isIdentity()
          || introspectedColumn.isGeneratedAlways()) {
        continue;
      }
      topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());

      final String javaProperty = introspectedColumn.getJavaProperty();

      // parameters
      method.addParameter(
          new Parameter(introspectedColumn.getFullyQualifiedJavaType(), javaProperty));

      if (rootClassInfo.containsProperty(introspectedColumn)) {
        inheritConstructorParameters.add(javaProperty);
      }
    }

    final StringBuilder sb = new StringBuilder();
    sb.append("super(");
    final Iterator<String> inheritConstructorParametersIterator =
        inheritConstructorParameters.iterator();
    while (inheritConstructorParametersIterator.hasNext()) {
      sb.append(inheritConstructorParametersIterator.next());
      if (inheritConstructorParametersIterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append(");");
    method.addBodyLine(sb.toString());

    for (final IntrospectedColumn introspectedColumn : allColumns) {
      if (PropertiesUtils.check(introspectedColumn, PropertiesUtils.COLUMN_IGNORE_CONSTRUCTOR,
          "true") || introspectedColumn.isIdentity() || introspectedColumn.isGeneratedAlways()
          || rootClassInfo.containsProperty(introspectedColumn)) {
        continue;
      }
      final String javaProperty = introspectedColumn.getJavaProperty();

      // body
      sb.setLength(0);
      sb.append("this."); //$NON-NLS-1$
      sb.append(javaProperty);
      sb.append(" = "); //$NON-NLS-1$
      sb.append(javaProperty);
      sb.append(";");
      method.addBodyLine(sb.toString());
    }

    return method;
  }

  protected Method generateDefaultConstructor(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    final Method method = this.generateConstructorFramework(topLevelClass, introspectedTable);
    method.addBodyLine("super();");
    return method;
  }

  private Method generateConstructorFramework(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    final Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setConstructor(true);
    method.setName(topLevelClass.getType().getShortName());
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    return method;
  }

  private String getRootClass(final IntrospectedTable introspectedTable) {
    String rootClass =
        introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_CLASS);
    if (rootClass == null) {
      final Properties properties =
          this.context.getJavaModelGeneratorConfiguration().getProperties();
      rootClass = properties.getProperty(PropertyRegistry.ANY_ROOT_CLASS);
    }
    return rootClass;
  }
}
