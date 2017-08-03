package com.xjs.mybatis.generator.plugins.base;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;

import com.xjs.mybatis.generator.plugins.utils.PropertiesUtils;

public abstract class AbstractSqlGenerator {

  protected String sqlName;

  protected Context context;

  protected IntrospectedTable introspectedTable;
  protected Attribute baseResultMapAttribute;
  protected XmlElement baseColumnListElement;
  protected String tableName;
  protected TextElement fromTableElement;

  public AbstractSqlGenerator() {
    final String simpleName = this.getClass().getSimpleName();
    this.sqlName = simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1));
  }

  public AbstractSqlGenerator setTable(final IntrospectedTable introspectedTable) {
    this.context = introspectedTable.getContext();
    this.introspectedTable = introspectedTable;
    this.baseResultMapAttribute = new Attribute("resultMap", //$NON-NLS-1$
        introspectedTable.getBaseResultMapId());
    this.baseColumnListElement = new XmlElement("include"); // $NON-NLS-1$
    this.baseColumnListElement.addAttribute(new Attribute("refid", // $NON-NLS-1$
        introspectedTable.getBaseColumnListId()));
    this.tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
    this.fromTableElement = new TextElement("from ".concat(this.tableName)); // $NON-NLS-1$
    return this;
  }

  public void addElements(final Document document) {
    if (PropertiesUtils.isTrue(this.introspectedTable, this.sqlName)) {
      this.add(document);
    }
  }

  protected abstract void add(final Document document);

  protected XmlElement createInsertElement() {
    final XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", this.sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);
    final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
    if (gk != null) {
      final IntrospectedColumn introspectedColumn =
          this.introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn != null || gk.isJdbcStandard()) {
        answer.addAttribute(new Attribute("keyColumn", // $NON-NLS-1$
            introspectedColumn.getActualColumnName())); // NOSONAR
        answer.addAttribute(new Attribute("keyProperty", // $NON-NLS-1$
            introspectedColumn.getJavaProperty())); // NOSONAR
        answer.addAttribute(new Attribute("useGeneratedKeys", // $NON-NLS-1$
            "true")); // $NON-NLS-2$
      }
    }
    return answer;
  }

  protected XmlElement createDeleteElement() {
    final XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", this.sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("delete ")); //$NON-NLS-1$
    answer.addElement(this.fromTableElement); // NOSONAR
    return answer;
  }

  protected XmlElement createUpdateElement() {
    final XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", this.sqlName)); //$NON-NLS-1$
    this.context.getCommentGenerator().addComment(answer);
    return answer;
  }

  protected XmlElement createSelectElement() {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", this.sqlName)); //$NON-NLS-1$
    answer.addAttribute(this.baseResultMapAttribute);
    this.context.getCommentGenerator().addComment(answer);
    answer.addElement(new TextElement("select ")); //$NON-NLS-1$
    answer.addElement(this.baseColumnListElement);
    answer.addElement(this.fromTableElement);
    return answer;
  }

  protected XmlElement createSelectCountElement() {
    final XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
    answer.addAttribute(new Attribute("id", this.sqlName)); //$NON-NLS-1$
    answer.addAttribute(this.baseResultMapAttribute);
    this.context.getCommentGenerator().addComment(answer);
    answer.addElement(new TextElement("select count(*) ")); //$NON-NLS-1$
    answer.addElement(this.fromTableElement);
    return answer;
  }
}
