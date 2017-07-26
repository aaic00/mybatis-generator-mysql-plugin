package com.xjs.mybatis.generator.plugins.finance;

import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.xjs.mybatis.generator.plugins.base.AbstractSqlGenerator;
import com.xjs.mybatis.generator.plugins.base.AbstractSqlPlugin;
import com.xjs.mybatis.generator.plugins.base.JDBCType;
import com.xjs.mybatis.generator.plugins.utils.ColumnContext;

public class FinanceSqlPlugin extends AbstractSqlPlugin {

  protected ColumnContext columnCode;
  protected ColumnContext columnId;

  @Override
  public void setProperties(final Properties properties) {
    super.setProperties(properties);
    this.columnCode = new ColumnContext(properties.getProperty("codeColumnName", "fund_code"),
        JDBCType.VARCHAR, FullyQualifiedJavaType.getStringInstance());
    this.columnId = new ColumnContext(properties.getProperty("idColumnName", "fof_id"),
        JDBCType.INTEGER, new FullyQualifiedJavaType("Integer"));
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

    this.addMethodSelectList(interfaze, introspectedTable, "selectId",
        this.columnId.getParameter());
    this.addMethodSelect(interfaze, introspectedTable, "selectIdRecent",
        this.columnId.getParameter());
    this.addMethodSelect(interfaze, introspectedTable, "selectIdRecentReal",
        this.columnId.getParameter());
    this.addMethodDelete(interfaze, introspectedTable, "deleteIdEstimate",
        this.columnId.getParameter());


    this.addMethodSelect(interfaze, introspectedTable, "selectCodeDate",
        this.columnCode.getAnnotatedParameter(),
        new Parameter(localDateType, "tradeDate", "@Param(\"tradeDate\")"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectCodeTerm",
        this.columnCode.getAnnotatedParameter(),
        new Parameter(localDateType, "begin", "@Param(\"begin\")"),
        new Parameter(localDateType, "end", "@Param(\"end\")"));
    this.addMethodSelect(interfaze, introspectedTable, "selectIdDate",
        this.columnId.getAnnotatedParameter(),
        new Parameter(localDateType, "tradeDate", "@Param(\"tradeDate\")"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectIdTerm",
        this.columnId.getAnnotatedParameter(),
        new Parameter(localDateType, "begin", "@Param(\"begin\")"),
        new Parameter(localDateType, "end", "@Param(\"end\")"));
  }

  @Override
  protected void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable) {
    new SelectCode().setTable(introspectedTable).addElements(document);
    new SelectRecent().setTable(introspectedTable).addElements(document);
    new SelectRecentReal().setTable(introspectedTable).addElements(document);
    new DeleteEstimate().setTable(introspectedTable).addElements(document);
    new SelectId().setTable(introspectedTable).addElements(document);
    new SelectIdRecent().setTable(introspectedTable).addElements(document);
    new SelectIdRecentReal().setTable(introspectedTable).addElements(document);
    new DeleteIdEstimate().setTable(introspectedTable).addElements(document);

    new SelectCodeDate().setTable(introspectedTable).addElements(document);
    new SelectCodeTerm().setTable(introspectedTable).addElements(document);
    new SelectIdDate().setTable(introspectedTable).addElements(document);
    new SelectIdTerm().setTable(introspectedTable).addElements(document);
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

  private class SelectId extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();

      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement());
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

  private class SelectIdRecent extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();

      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement());
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

  private class SelectIdRecentReal extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement(); // $NON-NLS-1$

      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement());
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

  private class DeleteIdEstimate extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createDeleteElement(); // $NON-NLS-1$

      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement()); // NOSONAR
      answer.addElement(new TextElement("and stable = 2"));
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectCodeDate extends AbstractSqlGenerator {
    private final TextElement dateElement = new TextElement(
        "and trade_date = #{tradeDate,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}");

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement());
      answer.addElement(this.dateElement);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectCodeTerm extends AbstractSqlGenerator {
    private final XmlElement begin = new XmlElement("if");
    private final XmlElement end = new XmlElement("if");

    SelectCodeTerm() {
      super();
      this.begin.addAttribute(new Attribute("test", "begin != null"));
      this.begin.addElement(new TextElement(
          "AND trade_date &gt;= #{begin,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}"));
      this.end.addAttribute(new Attribute("test", "end != null"));
      this.end.addElement(new TextElement(
          "AND trade_date &lt;= #{end,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}"));
    }

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(FinanceSqlPlugin.this.columnCode.getWhereElement());
      answer.addElement(this.begin);
      answer.addElement(this.end);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectIdDate extends AbstractSqlGenerator {
    private final TextElement dateElement = new TextElement(
        "and trade_date = #{tradeDate,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}");

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement());
      answer.addElement(this.dateElement);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectIdTerm extends AbstractSqlGenerator {
    private final XmlElement begin = new XmlElement("if");
    private final XmlElement end = new XmlElement("if");

    SelectIdTerm() {
      super();
      this.begin.addAttribute(new Attribute("test", "begin != null"));
      this.begin.addElement(new TextElement(
          "AND trade_date &gt;= #{begin,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}"));
      this.end.addAttribute(new Attribute("test", "end != null"));
      this.end.addElement(new TextElement(
          "AND trade_date &lt;= #{end,jdbcType=DATE,typeHandler=org.apache.ibatis.type.LocalDateTypeHandler}"));
    }

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(FinanceSqlPlugin.this.columnId.getWhereElement());
      answer.addElement(this.begin);
      answer.addElement(this.end);
      document.getRootElement().addElement(answer);
    }
  }

}


