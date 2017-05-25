package com.xjs.mybatis.generator.plugins.base;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
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
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.GeneratedKey;

import com.xjs.mybatis.generator.plugins.utils.PropertiesUtils;

public abstract class AbstractSqlPlugin extends PluginAdapter {

  private Attribute baseResultMapAttribute;
  private XmlElement baseColumnListElement;
  private String tableName;
  private TextElement fromTableElement;
  private FullyQualifiedJavaType entityType;
  private FullyQualifiedJavaType listType;

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public void initialized(final IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);
    this.baseResultMapAttribute = new Attribute("resultMap", //$NON-NLS-1$
        introspectedTable.getBaseResultMapId());
    this.baseColumnListElement = new XmlElement("include"); // $NON-NLS-1$
    this.baseColumnListElement.addAttribute(new Attribute("refid", // $NON-NLS-1$
        introspectedTable.getBaseColumnListId()));
    this.tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
    this.fromTableElement = new TextElement("from ".concat(this.tableName));

    this.entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    this.listType = new FullyQualifiedJavaType("List");
    this.listType.addTypeArgument(this.entityType);
  }


  @Override
  public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.addJavaMethods(interfaze, topLevelClass, introspectedTable);
    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(final Document document,
      final IntrospectedTable introspectedTable) {
    this.addXmlElements(document, introspectedTable);
    return true;
  }

  protected abstract void addJavaMethods(final Interface interfaze,
      final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable);

  protected abstract void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable);

  private boolean checkMethodEnable(final IntrospectedTable introspectedTable,
      final String methodName) {
    return PropertiesUtils.isTrue(introspectedTable, methodName);
  }

  protected void addMethodInsert(final Interface interfaze,
      final IntrospectedTable introspectedTable, final String methodName) {
    if (!this.checkMethodEnable(introspectedTable, methodName)) {
      return;
    }
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    method.addParameter(new Parameter(this.entityType, "record")); // $NON-NLS-1$
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(this.entityType);
    interfaze.addMethod(method);
  }

  protected void addMethodInsertList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final String methodName) {
    if (!this.checkMethodEnable(introspectedTable, methodName)) {
      return;
    }
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
      final IntrospectedTable introspectedTable, final String methodName,
      final Parameter... parameters) {
    if (!this.checkMethodEnable(introspectedTable, methodName)) {
      return;
    }
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addMethod(method);
  }

  protected void addMethodUpdate(final Interface interfaze,
      final IntrospectedTable introspectedTable, final String methodName,
      final Parameter... parameters) {
    this.addMethodDelete(interfaze, introspectedTable, methodName, parameters);
  }

  protected void addMethodSelect(final Interface interfaze,
      final IntrospectedTable introspectedTable, final String methodName,
      final Parameter... parameters) {
    if (!this.checkMethodEnable(introspectedTable, methodName)) {
      return;
    }
    final Method method = new Method();
    method.setReturnType(this.entityType);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(this.entityType);
    interfaze.addMethod(method);
  }

  protected void addMethodSelectList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final String methodName,
      final Parameter... parameters) {
    if (!this.checkMethodEnable(introspectedTable, methodName)) {
      return;
    }
    final Method method = new Method();
    method.setReturnType(this.listType);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
    interfaze.addImportedType(this.entityType);
    interfaze.addMethod(method);
  }

  protected XmlElement createInsertElement(final String sqlName,
      final IntrospectedTable introspectedTable) {
    final XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);
    final GeneratedKey gk = introspectedTable.getGeneratedKey();
    if (gk != null) {
      final IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn != null || gk.isJdbcStandard()) {
        answer.addAttribute(new Attribute("keyColumn", "true")); // $NON-NLS-1$ // $NON-NLS-2$
        answer.addAttribute(new Attribute("keyProperty", // $NON-NLS-1$
            introspectedColumn.getJavaProperty())); // NOSONAR
        answer.addAttribute(
            new Attribute("useGeneratedKeys", introspectedColumn.getActualColumnName())); // $NON-NLS-1$
      }
    }
    return answer;
  }

  protected XmlElement createDeleteElement(final String sqlName) {
    final XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);
    return answer;
  }

  protected XmlElement createUpdateElement(final String sqlName) {
    final XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);
    return answer;
  }

  protected XmlElement createSelectElement(final String sqlName) {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlName)); //$NON-NLS-1$
    answer.addAttribute(this.getBaseResultMapAttribute());
    this.context.getCommentGenerator().addComment(answer);
    return answer;
  }

  protected String getTableName() {
    return this.tableName;
  }

  protected Attribute getBaseResultMapAttribute() {
    return this.baseResultMapAttribute;
  }

  protected XmlElement getBaseColumnListElement() {
    return this.baseColumnListElement;
  }

  protected FullyQualifiedJavaType getEntityType() {
    return this.entityType;
  }

  protected FullyQualifiedJavaType getListType() {
    return this.listType;
  }

  protected TextElement getFromTableElement() {
    return this.fromTableElement;
  }


}
