package com.xjs.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import com.xjs.mybatis.generator.plugins.base.AbstractSqlGenerator;
import com.xjs.mybatis.generator.plugins.base.AbstractSqlPlugin;
import com.xjs.mybatis.generator.plugins.utils.XmlUtils;

public class SqlPlugin extends AbstractSqlPlugin {

  @Override
  public void initialized(final IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);
    if ("true".equals(this.properties.get("hackSchema"))) {
      introspectedTable.setSqlMapFullyQualifiedRuntimeTableName(
          introspectedTable.getFullyQualifiedTableNameAtRuntime().replace("..", "."));
      introspectedTable.setSqlMapAliasedFullyQualifiedRuntimeTableName(
          introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime().replace("..", "."));
    }
  }

  @Override
  protected void addJavaMethods(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.addMethodInsertList(interfaze, introspectedTable, "insertBatch");
    this.addMethodInsert(interfaze, introspectedTable, "insertIgnore");
    this.addMethodInsert(interfaze, introspectedTable, "insertOrUpdate");
  }

  @Override
  protected void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable) {
    new InsertBatch().setTable(introspectedTable).addElements(document);
    new InsertIgnore().setTable(introspectedTable).addElements(document);
    new InsertOrUpdate().setTable(introspectedTable).addElements(document);
  }

  private class InsertBatch extends AbstractSqlGenerator {

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createInsertElement(); // $NON-NLS-1$

      final StringBuilder insertClause = new StringBuilder();
      final StringBuilder valuesClause = new StringBuilder();

      final XmlElement valuesForeachElement = new XmlElement("foreach"); // $NON-NLS-1$
      valuesForeachElement.addAttribute(new Attribute("item", "item")); // $NON-NLS-1$ //$NON-NLS-2$
      valuesForeachElement.addAttribute(new Attribute("index", "index")); // $NON-NLS-1$ //$NON-NLS-2$
      valuesForeachElement.addAttribute(new Attribute("collection", "list")); // $NON-NLS-1$ //$NON-NLS-2$
      valuesForeachElement.addAttribute(new Attribute("separator", ",")); // $NON-NLS-1$ //$NON-NLS-2$

      insertClause.append("insert into "); // $NON-NLS-1$
      insertClause.append(this.tableName);
      insertClause.append(" ("); // $NON-NLS-1$

      valuesClause.append("("); // $NON-NLS-1$

      final List<IntrospectedColumn> columns = ListUtilities
          .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
      for (int i = 0; i < columns.size(); i++) {
        final IntrospectedColumn introspectedColumn = columns.get(i);

        insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        valuesClause.append(XmlUtils.getParameterClause(introspectedColumn, "item.")); // $NON-NLS-1$
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
  }

  private class InsertIgnore extends AbstractSqlGenerator {

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createInsertElement(); // $NON-NLS-1$

      final StringBuilder insertClause = new StringBuilder();
      final StringBuilder valuesClause = new StringBuilder();

      insertClause.append("insert ignore into "); // $NON-NLS-1$
      insertClause.append(this.tableName);
      insertClause.append(" ("); // $NON-NLS-1$

      valuesClause.append("values ("); // $NON-NLS-1$

      final List<String> valuesClauses = new ArrayList<String>();
      final List<IntrospectedColumn> columns = ListUtilities
          .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
      for (int i = 0; i < columns.size(); i++) {
        final IntrospectedColumn introspectedColumn = columns.get(i);

        insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        valuesClause.append(XmlUtils.getParameterClause(introspectedColumn));
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
  }

  private class InsertOrUpdate extends AbstractSqlGenerator {

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createInsertElement(); // $NON-NLS-1$

      final StringBuilder insertClause = new StringBuilder();
      final StringBuilder valuesClause = new StringBuilder();
      final StringBuilder updateClause = new StringBuilder();

      insertClause.append("insert into "); // $NON-NLS-1$
      insertClause.append(this.tableName);
      insertClause.append(" ("); // $NON-NLS-1$

      valuesClause.append("values ("); // $NON-NLS-1$

      final List<String> valuesClauses = new ArrayList<String>();
      final List<String> updateClauses = new ArrayList<String>();
      final List<IntrospectedColumn> columns = ListUtilities
          .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
      for (int i = 0; i < columns.size(); i++) {
        final IntrospectedColumn introspectedColumn = columns.get(i);

        insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        valuesClause.append(XmlUtils.getParameterClause(introspectedColumn));
        updateClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        updateClause.append(" = "); // $NON-NLS-1$
        updateClause.append(XmlUtils.getParameterClause(introspectedColumn));

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

  }

}
