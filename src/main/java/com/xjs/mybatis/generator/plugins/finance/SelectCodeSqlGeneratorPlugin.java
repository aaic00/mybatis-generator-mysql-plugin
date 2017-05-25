package com.xjs.mybatis.generator.plugins.finance;

import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.xjs.mybatis.generator.plugins.AbstractSqlGeneratorPlugin;
import com.xjs.mybatis.generator.plugins.ColumnContext;
import com.xjs.mybatis.generator.plugins.StringUtils;

public class SelectCodeSqlGeneratorPlugin extends AbstractSqlGeneratorPlugin {

  private ColumnContext columnCode;

  @Override
  public void setProperties(final Properties properties) {
    super.setProperties(properties);
    this.columnCode = new ColumnContext(properties.getProperty("codeColumnName"));
  }

  @Override
  protected void addJavaMethods(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.addMethodSelectList(interfaze, introspectedTable, this.getEntityType(), "selectCode",
        this.columnCode.getParameter());
  }

  @Override
  protected void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable) {
    this.addSelectCodeXmlElement(document, introspectedTable);
  }


  private void addSelectCodeXmlElement(final Document document,
      final IntrospectedTable introspectedTable) {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", "selectCode")); //$NON-NLS-1$
    answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
        introspectedTable.getBaseResultMapId()));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("select ")); //$NON-NLS-1$
    answer.addElement(this.getBaseColumnListElement(introspectedTable));
    answer.addElement(new TextElement(
        "from ".concat(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
    answer.addElement(new TextElement(StringUtils.join("where ", this.columnCode.getColumnName(),
        " = #{", this.columnCode.getJavaProperty(), ",jdbcType=VARCHAR}")));
    answer.addElement(new TextElement("order by trade_date asc"));
    document.getRootElement().addElement(answer);
  }



}
