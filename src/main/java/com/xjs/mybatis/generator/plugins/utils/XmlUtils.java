package com.xjs.mybatis.generator.plugins.utils;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.internal.util.StringUtility;

public final class XmlUtils {

  private XmlUtils() {}

  /**
   * Gets the parameter clause.
   *
   * @param introspectedColumn the introspected column
   * @return the parameter clause
   */
  public static String getParameterClause(final IntrospectedColumn introspectedColumn) {
    return XmlUtils.getParameterClause(introspectedColumn, null);
  }

  /**
   * Gets the parameter clause.
   *
   * @param introspectedColumn the introspected column
   * @param prefix the prefix
   * @return the parameter clause
   */
  public static String getParameterClause(final IntrospectedColumn introspectedColumn,
      final String prefix) {
    final StringBuilder sb = new StringBuilder();

    sb.append("#{"); //$NON-NLS-1$
    sb.append(introspectedColumn.getJavaProperty(prefix));
    sb.append(",jdbcType="); //$NON-NLS-1$
    sb.append(introspectedColumn.getJdbcTypeName());

    final String typeHandler = introspectedColumn.getTypeHandler();
    if (StringUtility.stringHasValue(typeHandler)) {
      if ("org.apache.ibatis.type.EnumOrdinalTypeHandler".equals(typeHandler)) {
        sb.append(",javaType="); //$NON-NLS-1$
        sb.append(introspectedColumn.getFullyQualifiedJavaType());
      }
      sb.append(",typeHandler="); //$NON-NLS-1$
      sb.append(typeHandler);
    }

    sb.append('}');

    return sb.toString();
  }
}
