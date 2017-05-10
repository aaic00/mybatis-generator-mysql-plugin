/**
 * Copyright 2006-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.xjs.mybatis.generator.plugins;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * This plugin adds the java.io.Serializable marker interface to all generated model objects.
 * <p>
 * This plugin demonstrates adding capabilities to generated Java artifacts, and shows the proper
 * way to add imports to a compilation unit.
 * <p>
 * Important: This is a simplistic implementation of serializable and does not attempt to do any
 * versioning of classes.
 *
 * @author Jeff Butler
 *
 */
public class SerializablePlugin extends PluginAdapter {

  private final FullyQualifiedJavaType serializable;
  private final FullyQualifiedJavaType gwtSerializable;
  private boolean addGWTInterface;
  private boolean suppressJavaInterface;

  public SerializablePlugin() {
    super();
    this.serializable = new FullyQualifiedJavaType("java.io.Serializable"); //$NON-NLS-1$
    this.gwtSerializable =
        new FullyQualifiedJavaType("com.google.gwt.user.client.rpc.IsSerializable"); //$NON-NLS-1$
  }

  @Override
  public boolean validate(final List<String> warnings) {
    // this plugin is always valid
    return true;
  }

  @Override
  public void setProperties(final Properties properties) {
    super.setProperties(properties);
    this.addGWTInterface = Boolean.valueOf(properties.getProperty("addGWTInterface")); //$NON-NLS-1$
    this.suppressJavaInterface = Boolean.valueOf(properties.getProperty("suppressJavaInterface")); //$NON-NLS-1$
  }

  @Override
  public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.makeSerializable(topLevelClass, introspectedTable);
    return true;
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.makeSerializable(topLevelClass, introspectedTable);
    return true;
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    this.makeSerializable(topLevelClass, introspectedTable);
    return true;
  }

  protected void makeSerializable(final TopLevelClass topLevelClass,
      final IntrospectedTable introspectedTable) {
    if (this.addGWTInterface) {
      topLevelClass.addImportedType(this.gwtSerializable);
      topLevelClass.addSuperInterface(this.gwtSerializable);
    }

    if (!this.suppressJavaInterface) {
      topLevelClass.addImportedType(this.serializable);
      topLevelClass.addSuperInterface(this.serializable);

      final Field field = new Field();
      field.setFinal(true);
      field.setInitializationString("1L"); //$NON-NLS-1$
      field.setName("serialVersionUID"); //$NON-NLS-1$
      field.setStatic(true);
      field.setType(new FullyQualifiedJavaType("long")); //$NON-NLS-1$
      field.setVisibility(JavaVisibility.PRIVATE);
      this.context.getCommentGenerator().addFieldComment(field, introspectedTable);

      topLevelClass.getFields().add(0, field);
      // topLevelClass.addField(field);
    }
  }
}
