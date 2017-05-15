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

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class ToStringPlugin extends PluginAdapter {

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

    this.generateToString(topLevelClass, columns, introspectedTable);
    return true;
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.generateToString(topLevelClass, introspectedTable.getPrimaryKeyColumns(),
        introspectedTable);
    return true;
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.generateToString(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
    return true;
  }

  private void generateToString(final TopLevelClass topLevelClass,
      final List<IntrospectedColumn> introspectedColumns,
      final IntrospectedTable introspectedTable) {
    final Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getStringInstance());
    method.setName("toString"); //$NON-NLS-1$
    if (introspectedTable.isJava5Targeted()) {
      method.addAnnotation("@Override"); //$NON-NLS-1$
    }

    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    method.addBodyLine("StringBuilder sb = new StringBuilder();"); //$NON-NLS-1$
    method.addBodyLine("sb.append(getClass().getSimpleName());"); //$NON-NLS-1$
    method.addBodyLine("sb.append(\" [\");"); //$NON-NLS-1$
    method.addBodyLine("sb.append(\"Hash = \").append(hashCode());"); //$NON-NLS-1$
    final StringBuilder sb = new StringBuilder();
    for (final IntrospectedColumn introspectedColumn : introspectedColumns) {
      final String property = introspectedColumn.getJavaProperty();
      final String getterMethod = JavaBeansUtil.getGetterMethodName(property,
          introspectedColumn.getFullyQualifiedJavaType());
      sb.setLength(0);
      sb.append("sb.append(\"").append(", ").append(property) //$NON-NLS-1$ //$NON-NLS-2$
          .append("=\")").append(".append(this.").append(getterMethod) //$NON-NLS-1$ //$NON-NLS-2$
          .append("());"); //$NON-NLS-1$
      method.addBodyLine(sb.toString());
    }

    method.addBodyLine("sb.append(\"]\");"); //$NON-NLS-1$

    method.addBodyLine("return sb.toString();"); //$NON-NLS-1$

    topLevelClass.addMethod(method);
  }
}
