package com.xjs.mybatis.generator.plugins;



import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

public abstract class AbstractSqlGeneratorPlugin extends PluginAdapter {
  private static final int pluginLength =
      AbstractSqlGeneratorPlugin.class.getSimpleName().length() - "Abstract".length();

  private boolean enable;

  protected abstract void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable);

  protected abstract void addJavaMethods(final Interface interfaze,
      final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable);

  private FullyQualifiedJavaType entityType;
  private FullyQualifiedJavaType listType;


  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public void initialized(final IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);
    this.enable = this.checkEnable(introspectedTable);
    this.entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    this.listType = new FullyQualifiedJavaType("List");
    this.listType.addTypeArgument(this.entityType);
  }

  protected boolean checkEnable(final IntrospectedTable introspectedTable) {
    final String simpleName = this.getClass().getSimpleName();
    final String propertyName = simpleName.substring(0, 1).toLowerCase().concat(
        simpleName.substring(1, simpleName.length() - AbstractSqlGeneratorPlugin.pluginLength));
    return PropertiesUtils.isTrue(introspectedTable, propertyName);
  }

  @Override
  public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    if (this.enable) {
      this.addJavaMethods(interfaze, topLevelClass, introspectedTable);
    }
    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(final Document document,
      final IntrospectedTable introspectedTable) {
    if (this.enable) {
      this.addXmlElements(document, introspectedTable);
    }
    return true;
  }

  protected XmlElement getBaseColumnListElement(final IntrospectedTable introspectedTable) {
    final XmlElement answer = new XmlElement("include"); // $NON-NLS-1$
    answer.addAttribute(new Attribute("refid", // $NON-NLS-1$
        introspectedTable.getBaseColumnListId()));
    return answer;
  }

  protected void addMethodInsert(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName) {
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    method.addParameter(new Parameter(entityType, "record")); // $NON-NLS-1$
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  protected void addMethodInsertList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName) {
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    method.addParameter(new Parameter(this.listType, "recordList")); // $NON-NLS-1$
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
    interfaze.addMethod(method);
  }

  protected void addMethodDelete(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName, final Parameter... parameters) {
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  protected void addMethodUpdate(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName, final Parameter... parameters) {
    this.addMethodDelete(interfaze, introspectedTable, entityType, methodName, parameters);
  }

  protected void addMethodSelect(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName, final Parameter... parameters) {
    final Method method = new Method();
    method.setReturnType(entityType);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  protected void addMethodSelectList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName, final Parameter... parameters) {
    final Method method = new Method();
    method.setReturnType(this.listType);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  protected FullyQualifiedJavaType getEntityType() {
    return this.entityType;
  }

  protected FullyQualifiedJavaType getListType() {
    return this.listType;
  }

}
