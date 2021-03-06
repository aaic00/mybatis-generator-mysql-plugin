package com.xjs.mybatis.generator.plugins.community;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.xjs.mybatis.generator.plugins.base.AbstractSqlGenerator;
import com.xjs.mybatis.generator.plugins.base.AbstractSqlPlugin;
import com.xjs.mybatis.generator.plugins.utils.JavaUtils;
import com.xjs.mybatis.generator.plugins.utils.XmlUtils;

public class CommunitySqlPlugin extends AbstractSqlPlugin {

  @Override
  protected void addJavaMethods(final Interface interfaze, final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {

    JavaUtils.addImport(interfaze, "org.apache.ibatis.annotations.Param",
        "com.xjs.community.type.CommunityEntityType");

    final FullyQualifiedJavaType entityType = new FullyQualifiedJavaType("CommunityEntityType");
    final FullyQualifiedJavaType string = FullyQualifiedJavaType.getStringInstance();
    final FullyQualifiedJavaType integer = new FullyQualifiedJavaType("Integer");
    final FullyQualifiedJavaType listInteger = new FullyQualifiedJavaType("List");
    listInteger.addTypeArgument(integer);

    this.addMethodSelectList(interfaze, introspectedTable, "selectByIdList",
        JavaUtils.annotationParameter(listInteger, "idList"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectByUser",
        JavaUtils.annotationParameter(integer, "userId"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectByEntityUser",
        JavaUtils.annotationParameter(integer, "entityUserId"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectByEntity",
        JavaUtils.annotationParameter(entityType, "entityType"),
        JavaUtils.annotationParameter(integer, "entityId"),
        JavaUtils.annotationParameter(string, "entityCode"));
    this.addMethodSelectList(interfaze, introspectedTable, "selectByUserAndEntity",
        JavaUtils.annotationParameter(integer, "userId"),
        JavaUtils.annotationParameter(entityType, "entityType"),
        JavaUtils.annotationParameter(integer, "entityId"),
        JavaUtils.annotationParameter(string, "entityCode"));
    this.addMethodSelectType(interfaze, introspectedTable, "selectCountUnreadByUser", integer,
        JavaUtils.annotationParameter(integer, "userId"));
    this.addMethodSelectType(interfaze, introspectedTable, "selectCountUnreadByEntityUser", integer,
        JavaUtils.annotationParameter(integer, "entityUserId"));

  }

  @Override
  protected void addXmlElements(final Document document,
      final IntrospectedTable introspectedTable) {
    new SelectByIdList().setTable(introspectedTable).addElements(document);
    new SelectByUser().setTable(introspectedTable).addElements(document);
    new SelectByEntityUser().setTable(introspectedTable).addElements(document);
    new SelectByEntity().setTable(introspectedTable).addElements(document);
    new SelectByUserAndEntity().setTable(introspectedTable).addElements(document);
    new SelectCountUnreadByUser().setTable(introspectedTable).addElements(document);
    new SelectCountUnreadByEntityUser().setTable(introspectedTable).addElements(document);

  }

  private class SelectByIdList extends AbstractSqlGenerator {

    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(new TextElement("where id in"));
      answer.addElement(XmlUtils.foreach("INTEGER", null, "idList"));
      CommunitySqlPlugin.this.addOrder(answer);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectByUser extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(new TextElement("where user_id = #{userId,jdbcType=INTEGER}"));
      CommunitySqlPlugin.this.addOrder(answer);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectByEntityUser extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(new TextElement("where entity_user_id = #{entityUserId,jdbcType=INTEGER}"));
      CommunitySqlPlugin.this.addOrder(answer);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectByEntity extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(new TextElement(
          "where entity_type = #{entityType,jdbcType=INTEGER,javaType=com.xjs.community.type.CommunityEntityType,typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}"));
      final XmlElement id = new XmlElement("if");
      id.addAttribute(new Attribute("test", "entityId != null"));
      id.addElement(new TextElement("and entity_id = #{entityId,jdbcType=INTEGER}"));
      answer.addElement(id);
      final XmlElement code = new XmlElement("if");
      code.addAttribute(new Attribute("test", "entityCode != null"));
      code.addElement(new TextElement("and entity_code = #{entityCode,jdbcType=VARCHAR}"));
      answer.addElement(code);
      CommunitySqlPlugin.this.addOrder(answer);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectByUserAndEntity extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectElement();
      answer.addElement(new TextElement("where user_id = #{userId,jdbcType=INTEGER}"));
      answer.addElement(new TextElement(
          "and entity_type = #{entityType,jdbcType=INTEGER,javaType=com.xjs.community.type.CommunityEntityType,typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}"));
      final XmlElement id = new XmlElement("if");
      id.addAttribute(new Attribute("test", "entityId != null"));
      id.addElement(new TextElement("and entity_id = #{entityId,jdbcType=INTEGER}"));
      answer.addElement(id);
      final XmlElement code = new XmlElement("if");
      code.addAttribute(new Attribute("test", "entityCode != null"));
      code.addElement(new TextElement("and entity_code = #{entityCode,jdbcType=VARCHAR}"));
      answer.addElement(code);
      code.addElement(new TextElement("and status = 0"));
      CommunitySqlPlugin.this.addOrder(answer);
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectCountUnreadByUser extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectCountElement();
      answer.addElement(new TextElement("where user_id = #{userId,jdbcType=INTEGER}"));
      answer.addElement(new TextElement("and is_read = 0 and status = 0"));
      document.getRootElement().addElement(answer);
    }
  }

  private class SelectCountUnreadByEntityUser extends AbstractSqlGenerator {
    @Override
    protected void add(final Document document) {
      final XmlElement answer = this.createSelectCountElement();
      answer.addElement(new TextElement("where entity_user_id = #{entityUserId,jdbcType=INTEGER}"));
      answer.addElement(new TextElement("and is_read = 0 and status = 0"));
      document.getRootElement().addElement(answer);
    }
  }

}


