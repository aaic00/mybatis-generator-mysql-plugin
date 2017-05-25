package com.xjs.mybatis.generator.plugins.finance;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.PropertyRegistry;

import com.xjs.mybatis.generator.plugins.utils.PropertiesUtils;

public class FinanceStableEntityPlugin extends PluginAdapter {

  private static final String ENTITY_HAS_STABLE = "hasStable";

  private static final FullyQualifiedJavaType LOCAL_DATE_TYPE =
      new FullyQualifiedJavaType("java.time.LocalDate");
  private List<String> warnings;

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    if (PropertiesUtils.isTrue(introspectedTable, FinanceStableEntityPlugin.ENTITY_HAS_STABLE)) {
      this.generateMethodStables(topLevelClass, introspectedTable, "LAST", "ESTIMATE"); // NOSONAR //$NON-NLS-1$
    }
    return true;
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    if (PropertiesUtils.isTrue(introspectedTable, FinanceStableEntityPlugin.ENTITY_HAS_STABLE)) {
      this.generateMethodStables(topLevelClass, introspectedTable, "LAST", "ESTIMATE"); //$NON-NLS-1$
    }
    return true;
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    if (PropertiesUtils.isTrue(introspectedTable, FinanceStableEntityPlugin.ENTITY_HAS_STABLE)) {
      this.generateMethodStables(topLevelClass, introspectedTable, "LAST", "ESTIMATE"); //$NON-NLS-1$
    }
    return true;
  }

  private void generateMethodStables(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable, final String... stables) {
    for (final String stable : stables) {
      this.generateMethodStable(topLevelClass, introspectedTable, stable);
    }
  }

  private void generateMethodStable(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable, final String stable) {
    final RootClassInfo rootClassInfo =
        RootClassInfo.getInstance(this.getRootClass(introspectedTable), this.warnings);

    final Method method = new Method();
    method.addAnnotation("@Override");
    method.setReturnType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(stable.toLowerCase());
    method.addParameter(new Parameter(FinanceStableEntityPlugin.LOCAL_DATE_TYPE, "tradeDate")); // NOSONAR //$NON-NLS-1$

    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    final Iterator<IntrospectedColumn> columnIterator =
        introspectedTable.getAllColumns().iterator();

    final StringBuilder sb = new StringBuilder();
    sb.append("return new "); //$NON-NLS-1$
    sb.append(topLevelClass.getType().getShortName());
    sb.append('(');

    boolean first = true;

    while (columnIterator.hasNext()) {
      final IntrospectedColumn introspectedColumn = columnIterator.next();
      if (PropertiesUtils.check(introspectedColumn, PropertiesUtils.COLUMN_IGNORE_CONSTRUCTOR,
          "true") || introspectedColumn.isIdentity() || introspectedColumn.isGeneratedAlways()
          || rootClassInfo.containsProperty(introspectedColumn)) {
        continue;
      }

      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }

      final String javaProperty = introspectedColumn.getJavaProperty();
      // body
      if (Objects.equals("tradeDate", javaProperty)) { //$NON-NLS-1$
        sb.append("tradeDate"); //$NON-NLS-1$
      } else if (Objects.equals("stable", javaProperty)) { //$NON-NLS-1$
        sb.append("Stable."); //$NON-NLS-1$
        sb.append(stable);
      } else {
        sb.append("this."); //$NON-NLS-1$
        sb.append(javaProperty);
      }

      if (sb.length() >= 80) {
        method.addBodyLine(sb.toString());
        sb.setLength(0);
      }
    }
    sb.append(");");
    method.addBodyLine(sb.toString());
    topLevelClass.addMethod(method);
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
