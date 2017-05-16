package com.xjs.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
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
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

public class SqlPlugin extends PluginAdapter {

  public static final String TABLE_ENABLE_INSERT_BATCH = "insertBatch"; // $NON-NLS-1$
  public static final String TABLE_ENABLE_INSERT_IGNORE = "insertIgnore"; // $NON-NLS-1$
  public static final String TABLE_ENABLE_INSERT_OR_UPDATE = "insertOrUpdate"; // $NON-NLS-1$

  private static final String XML_ATTRIBUTE_KEY_COLUMN = "keyColumn"; // $NON-NLS-1$
  private static final String XML_ATTRIBUTE_KEY_PROPERTY = "keyProperty"; // $NON-NLS-1$
  private static final String XML_ATTRIBUTE_USE_GENERATED_KEYS = "useGeneratedKeys"; // $NON-NLS-1$

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    final FullyQualifiedJavaType entityType =
        new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_IGNORE)) {
      this.addMethodInsert(interfaze, introspectedTable, entityType,
          SqlPlugin.TABLE_ENABLE_INSERT_IGNORE);
    }

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_BATCH)) {
      this.addMethodInsertList(interfaze, introspectedTable, entityType,
          SqlPlugin.TABLE_ENABLE_INSERT_BATCH);
    }

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_OR_UPDATE)) {
      this.addMethodInsert(interfaze, introspectedTable, entityType,
          SqlPlugin.TABLE_ENABLE_INSERT_OR_UPDATE);
    }

    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(final Document document,
      final IntrospectedTable introspectedTable) {
    final FullyQualifiedJavaType entityType =
        new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_IGNORE)) {
      this.addElementInsertIgnore(document, introspectedTable, entityType);
    }

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_BATCH)) {
      this.addElementInsertBatch(document, introspectedTable, entityType);
    }

    if (PropertiesUtils.isTrue(introspectedTable, SqlPlugin.TABLE_ENABLE_INSERT_OR_UPDATE)) {
      this.addElementInsertOrUpdate(document, introspectedTable, entityType);
    }

    return true;
  }

  public void addMethodInsert(final Interface interfaze, final IntrospectedTable introspectedTable,
      final FullyQualifiedJavaType entityType, final String methodName) {
    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    method.addParameter(new Parameter(entityType, "record")); // $NON-NLS-1$
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  public void addMethodInsertList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName) {
    final FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
    listType.addTypeArgument(entityType);

    final Method method = new Method();
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    method.addParameter(new Parameter(listType, "recordList")); // $NON-NLS-1$
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(listType);
    interfaze.addMethod(method);
  }

  public void addMethodDelete(final Interface interfaze, final IntrospectedTable introspectedTable,
      final FullyQualifiedJavaType entityType, final String methodName,
      final Parameter... parameters) {
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

  public void addMethodUpdate(final Interface interfaze, final IntrospectedTable introspectedTable,
      final FullyQualifiedJavaType entityType, final String methodName,
      final Parameter... parameters) {
    this.addMethodDelete(interfaze, introspectedTable, entityType, methodName, parameters);
  }

  public void addMethodSelect(final Interface interfaze, final IntrospectedTable introspectedTable,
      final FullyQualifiedJavaType entityType, final String methodName,
      final Parameter... parameters) {
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

  public void addMethodSelectList(final Interface interfaze,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType,
      final String methodName, final Parameter... parameters) {
    final Method method = new Method();
    final FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
    listType.addTypeArgument(entityType);
    method.setReturnType(listType);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(methodName);
    for (final Parameter parameter : parameters) {
      method.addParameter(parameter);
    }
    this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    interfaze.addImportedType(listType);
    interfaze.addImportedType(entityType);
    interfaze.addMethod(method);
  }

  public void addElementInsertIgnore(final Document document,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType) {
    final XmlElement answer = new XmlElement("insert"); // $NON-NLS-1$ //NOSONAR
    answer.addAttribute(new Attribute("id", SqlPlugin.TABLE_ENABLE_INSERT_IGNORE)); // $NON-NLS-1$
    answer.addAttribute(new Attribute("parameterType", // $NON-NLS-1$ //NOSONAR
        entityType.getFullyQualifiedName()));
    this.context.getCommentGenerator().addComment(answer);
    final GeneratedKey gk = introspectedTable.getGeneratedKey();
    if (gk != null) {
      final IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn != null || gk.isJdbcStandard()) {
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_USE_GENERATED_KEYS, "true")); // $NON-NLS-1$
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_PROPERTY,
            introspectedColumn.getJavaProperty())); // NOSONAR
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_COLUMN,
            introspectedColumn.getActualColumnName()));
      }
    }

    final StringBuilder insertClause = new StringBuilder();
    final StringBuilder valuesClause = new StringBuilder();

    insertClause.append("insert ignore into "); // $NON-NLS-1$
    insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    insertClause.append(" ("); // $NON-NLS-1$

    valuesClause.append("values ("); // $NON-NLS-1$

    final List<String> valuesClauses = new ArrayList<String>();
    final List<IntrospectedColumn> columns =
        ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
    for (int i = 0; i < columns.size(); i++) {
      final IntrospectedColumn introspectedColumn = columns.get(i);

      insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      if (i + 1 < columns.size()) {
        insertClause.append(", "); // $NON-NLS-1$
        valuesClause.append(", "); // $NON-NLS-1$
      }

      if (valuesClause.length() > 80) {
        answer.addElement(new TextElement(insertClause.toString()));
        insertClause.setLength(0);
        OutputUtilities.xmlIndent(insertClause, 1);

        valuesClauses.add(valuesClause.toString());
        valuesClause.setLength(0);
        OutputUtilities.xmlIndent(valuesClause, 1);
      }
    }

    insertClause.append(')'); // $NON-NLS-1$
    answer.addElement(new TextElement(insertClause.toString()));

    valuesClause.append(')'); // $NON-NLS-1$
    valuesClauses.add(valuesClause.toString());

    for (final String clause : valuesClauses) {
      answer.addElement(new TextElement(clause));
    }

    document.getRootElement().addElement(answer);
  }

  public void addElementInsertBatch(final Document document,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType) {
    final XmlElement answer = new XmlElement("insert"); // $NON-NLS-1$ // NOSONAR
    answer.addAttribute(new Attribute("id", SqlPlugin.TABLE_ENABLE_INSERT_BATCH)); // $NON-NLS-1$
    answer.addAttribute(new Attribute("parameterType", // $NON-NLS-1$
        entityType.getFullyQualifiedName()));
    this.context.getCommentGenerator().addComment(answer);
    final GeneratedKey gk = introspectedTable.getGeneratedKey();
    if (gk != null) {
      final IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_USE_GENERATED_KEYS, "true")); // $NON-NLS-1$
      answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_PROPERTY,
          introspectedColumn.getJavaProperty())); // $NON-NLS-1$
      answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_COLUMN,
          introspectedColumn.getActualColumnName())); // $NON-NLS-1$
    }

    final StringBuilder insertClause = new StringBuilder();
    final StringBuilder valuesClause = new StringBuilder();

    final XmlElement valuesForeachElement = new XmlElement("foreach"); // $NON-NLS-1$
    valuesForeachElement.addAttribute(new Attribute("item", "item")); // $NON-NLS-1$ //$NON-NLS-2$
    valuesForeachElement.addAttribute(new Attribute("index", "index")); // $NON-NLS-1$ //$NON-NLS-2$
    valuesForeachElement.addAttribute(new Attribute("collection", "list")); // $NON-NLS-1$ //$NON-NLS-2$
    valuesForeachElement.addAttribute(new Attribute("separator", ",")); // $NON-NLS-1$ //$NON-NLS-2$

    insertClause.append("insert into "); // $NON-NLS-1$
    insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    insertClause.append(" ("); // $NON-NLS-1$

    valuesClause.append("("); // $NON-NLS-1$

    final List<IntrospectedColumn> columns =
        ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
    for (int i = 0; i < columns.size(); i++) {
      final IntrospectedColumn introspectedColumn = columns.get(i);

      insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      valuesClause
          .append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "item.")); // $NON-NLS-1$
      if (i + 1 < columns.size()) {
        insertClause.append(", "); // $NON-NLS-1$
        valuesClause.append(", "); // $NON-NLS-1$
      }

      if (valuesClause.length() > 80) {
        answer.addElement(new TextElement(insertClause.toString()));
        insertClause.setLength(0);
        OutputUtilities.xmlIndent(insertClause, 1);

        valuesForeachElement.addElement(new TextElement(valuesClause.toString()));
        valuesClause.setLength(0);
      }
    }

    insertClause.append(')'); // $NON-NLS-1$
    answer.addElement(new TextElement(insertClause.toString()));

    valuesClause.append(')'); // $NON-NLS-1$
    valuesForeachElement.addElement(new TextElement(valuesClause.toString()));

    answer.addElement(new TextElement("values")); // $NON-NLS-1$
    answer.addElement(valuesForeachElement);

    document.getRootElement().addElement(answer);
  }

  public void addElementInsertOrUpdate(final Document document,
      final IntrospectedTable introspectedTable, final FullyQualifiedJavaType entityType) {
    final XmlElement answer = new XmlElement("insert"); // $NON-NLS-1$
    answer.addAttribute(new Attribute("id", SqlPlugin.TABLE_ENABLE_INSERT_OR_UPDATE)); // $NON-NLS-1$
    answer.addAttribute(new Attribute("parameterType", // $NON-NLS-1$
        entityType.getFullyQualifiedName()));
    this.context.getCommentGenerator().addComment(answer);
    final GeneratedKey gk = introspectedTable.getGeneratedKey();
    if (gk != null) {
      final IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn != null || gk.isJdbcStandard()) {
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_USE_GENERATED_KEYS, "true")); // $NON-NLS-1$
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_PROPERTY,
            introspectedColumn.getJavaProperty())); // $NON-NLS-1$
        answer.addAttribute(new Attribute(SqlPlugin.XML_ATTRIBUTE_KEY_COLUMN,
            introspectedColumn.getActualColumnName())); // $NON-NLS-1$
      }
    }

    final StringBuilder insertClause = new StringBuilder();
    final StringBuilder valuesClause = new StringBuilder();
    final StringBuilder updateClause = new StringBuilder();

    insertClause.append("insert into "); // $NON-NLS-1$
    insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    insertClause.append(" ("); // $NON-NLS-1$

    valuesClause.append("values ("); // $NON-NLS-1$

    final List<String> valuesClauses = new ArrayList<String>();
    final List<String> updateClauses = new ArrayList<String>();
    final List<IntrospectedColumn> columns =
        ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
    for (int i = 0; i < columns.size(); i++) {
      final IntrospectedColumn introspectedColumn = columns.get(i);

      insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      updateClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      updateClause.append(" = "); // $NON-NLS-1$
      updateClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

      if (i + 1 < columns.size()) {
        insertClause.append(", "); // $NON-NLS-1$
        valuesClause.append(", "); // $NON-NLS-1$
        updateClause.append(", "); // $NON-NLS-1$
      }

      if (valuesClause.length() > 80) {
        answer.addElement(new TextElement(insertClause.toString()));
        insertClause.setLength(0);
        OutputUtilities.xmlIndent(insertClause, 1);

        valuesClauses.add(valuesClause.toString());
        valuesClause.setLength(0);
        OutputUtilities.xmlIndent(valuesClause, 1);
      }

      if (updateClause.length() > 80) {
        updateClauses.add(updateClause.toString());
        updateClause.setLength(0);
        OutputUtilities.xmlIndent(updateClause, 1);
      }

    }

    insertClause.append(')'); // $NON-NLS-1$
    answer.addElement(new TextElement(insertClause.toString()));

    valuesClause.append(')'); // $NON-NLS-1$
    valuesClauses.add(valuesClause.toString());

    for (final String clause : valuesClauses) {
      answer.addElement(new TextElement(clause));
    }

    answer.addElement(new TextElement("on duplicate key update")); // $NON-NLS-1$

    for (final String clause : updateClauses) {
      answer.addElement(new TextElement(clause));
    }

    document.getRootElement().addElement(answer);
  }

  protected XmlElement getBaseColumnListElement(final IntrospectedTable introspectedTable) {
    final XmlElement answer = new XmlElement("include"); // $NON-NLS-1$
    answer.addAttribute(new Attribute("refid", // $NON-NLS-1$
        introspectedTable.getBaseColumnListId()));
    return answer;
  }

}
