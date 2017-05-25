package com.xjs.mybatis.generator.plugins.finance;

import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.xjs.mybatis.generator.plugins.base.AbstractSqlGenerator;
import com.xjs.mybatis.generator.plugins.base.AbstractSqlPlugin;
import com.xjs.mybatis.generator.plugins.utils.ColumnContext;

public class FinanceSqlPlugin extends AbstractSqlPlugin {

  protected ColumnContext columnCode;

  @Override
  public void setProperties(final Properties properties) {
    super.setProperties(properties);
    this.columnCode = new ColumnContext(properties.getProperty("codeColumnName"));
  }

  @Override
  protected void addJavaMethods(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.addMethodSelectList(interfaze, introspectedTable, "selectCode",
        this.columnCode.getParameter());
    this.addMethodSelect(interfaze, introspectedTable, "selectRecent",
        this.columnCode.getParameter());
    this.addMethodSelect(interfaze, introspectedTable, "selectRecentReal",
        this.columnCode.getParameter());
    this.addMethodDelete(interfaze, introspectedTable, "deleteEstimate",
        this.columnCode.getParameter());
  }

  @Override
  protected void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable) {
    new SelectCode().setTable(introspectedTable).addElements(document);
    new SelectRecent().setTable(introspectedTable).addElements(document);
    new SelectRecentReal().setTable(introspectedTable).addElements(document);
    new DeleteEstimate().setTable(introspectedTable).addElements(document);
  }

  private class SelectCode extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();

      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement());
      answer.addElement(new TextElement("order by trade_date asc"));

      document.getRootElement().addElement(answer);
    }
  }

  private class SelectRecent extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();

      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement());
      answer.addElement(new TextElement("order by trade_date desc"));
      answer.addElement(new TextElement("limit 1"));

      document.getRootElement().addElement(answer);
    }
  }

  private class SelectRecentReal extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement(); // $NON-NLS-1$

      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement());
      answer.addElement(new TextElement("and stable = 0"));
      answer.addElement(new TextElement("order by trade_date desc"));
      answer.addElement(new TextElement("limit 1"));
      document.getRootElement().addElement(answer);
    }
  }

  private class DeleteEstimate extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createDeleteElement(); // $NON-NLS-1$

      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement()); // NOSONAR
      answer.addElement(new TextElement("and stable = 2"));
      document.getRootElement().addElement(answer);
    }
  }

}


