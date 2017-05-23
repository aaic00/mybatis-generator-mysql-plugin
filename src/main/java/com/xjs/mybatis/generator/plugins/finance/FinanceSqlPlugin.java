package com.xjs.mybatis.generator.plugins.finance;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.xjs.mybatis.generator.plugins.PropertiesUtils;
import com.xjs.mybatis.generator.plugins.SqlPlugin;

public class FinanceSqlPlugin extends SqlPlugin {

  private static final String TABLE_ENABLE_SELECT_CODE = "selectCode";
  private static final String TABLE_ENABLE_SELECT_RECENT_REAL = "selectRecentReal";
  private static final String TABLE_ENABLE_DELETE_ESTIMATE = "deleteEstimate";

  @Override
  public boolean validate(final List<String> warnings) {
    return true;
  }

  @Override
  public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    final FullyQualifiedJavaType entityType =
        new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

    if (PropertiesUtils.isTrue(introspectedTable, FinanceSqlPlugin.TABLE_ENABLE_SELECT_CODE)) {
      // org.apache.ibatis.annotations.Param
      interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
      this.addMethodSelectList(interfaze, introspectedTable, entityType,
          FinanceSqlPlugin.TABLE_ENABLE_SELECT_CODE,
          new Parameter(FullyQualifiedJavaType.getStringInstance(), "code", "@Param(\"code\")"));
    }

    if (PropertiesUtils.isTrue(introspectedTable,
        FinanceSqlPlugin.TABLE_ENABLE_SELECT_RECENT_REAL)) {
      this.addMethodSelect(interfaze, introspectedTable, entityType,
          FinanceSqlPlugin.TABLE_ENABLE_SELECT_RECENT_REAL,
          new Parameter(FullyQualifiedJavaType.getStringInstance(), "code"));
    }

    if (PropertiesUtils.isTrue(introspectedTable, FinanceSqlPlugin.TABLE_ENABLE_DELETE_ESTIMATE)) {
      this.addMethodDelete(interfaze, introspectedTable, entityType,
          FinanceSqlPlugin.TABLE_ENABLE_DELETE_ESTIMATE,
          new Parameter(FullyQualifiedJavaType.getStringInstance(), "code"));
    }

    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(final Document document,
      final IntrospectedTable introspectedTable) {
    if (PropertiesUtils.isTrue(introspectedTable, FinanceSqlPlugin.TABLE_ENABLE_SELECT_CODE)) {
      this.addElementSelectCode(document, introspectedTable,
          FinanceSqlPlugin.TABLE_ENABLE_SELECT_CODE);
    }

    if (PropertiesUtils.isTrue(introspectedTable,
        FinanceSqlPlugin.TABLE_ENABLE_SELECT_RECENT_REAL)) {
      this.addElementSelectRecentReal(document, introspectedTable,
          FinanceSqlPlugin.TABLE_ENABLE_SELECT_RECENT_REAL);
    }

    if (PropertiesUtils.isTrue(introspectedTable, FinanceSqlPlugin.TABLE_ENABLE_DELETE_ESTIMATE)) {
      this.addElementDeleteEstimate(document, introspectedTable,
          FinanceSqlPlugin.TABLE_ENABLE_DELETE_ESTIMATE);
    }


    return true;
  }

  private void addElementSelectCode(final Document document,
      final IntrospectedTable introspectedTable, final String sqlId) {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlId)); //$NON-NLS-1$
    answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
        introspectedTable.getBaseResultMapId()));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("select ")); //$NON-NLS-1$
    answer.addElement(this.getBaseColumnListElement(introspectedTable));
    answer.addElement(new TextElement(
        "from ".concat(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
    answer.addElement(new TextElement("where code = #{code,jdbcType=VARCHAR}"));
    answer.addElement(new TextElement("order by trade_date asc"));
    document.getRootElement().addElement(answer);
  }

  private void addElementDeleteEstimate(final Document document,
      final IntrospectedTable introspectedTable, final String sqlId) {
    final XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlId)); //$NON-NLS-1$

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("delete ")); //$NON-NLS-1$
    answer.addElement(new TextElement(
        "from ".concat(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()))); // NOSONAR
    answer.addElement(new TextElement("where code = #{code,jdbcType=VARCHAR}")); // NOSONAR
    answer.addElement(new TextElement("and stable = 2"));
    document.getRootElement().addElement(answer);
  }

  private void addElementSelectRecentReal(final Document document,
      final IntrospectedTable introspectedTable, final String sqlId) {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", sqlId)); //$NON-NLS-1$
    answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
        introspectedTable.getBaseResultMapId()));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("select ")); //$NON-NLS-1$
    answer.addElement(this.getBaseColumnListElement(introspectedTable));
    answer.addElement(new TextElement(
        "from ".concat(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
    answer.addElement(new TextElement("where code = #{code,jdbcType=VARCHAR}"));
    answer.addElement(new TextElement("and stable = 0"));
    answer.addElement(new TextElement("order by trade_date desc"));
    answer.addElement(new TextElement("limit 1"));
    document.getRootElement().addElement(answer);
  }



}
