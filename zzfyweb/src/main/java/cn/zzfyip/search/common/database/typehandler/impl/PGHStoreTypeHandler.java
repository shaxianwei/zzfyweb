/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cnm ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.typehandler.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.database.typehandler.ITypeHandler;
import cn.zzfyip.search.common.support.PGHStore;

/**
 * 
 * 实现描述：PGHStore typehandler
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-17 下午6:42:17
 */
public class PGHStoreTypeHandler implements ITypeHandler<String, PGHStore> {

    private final static Logger logger = LoggerFactory.getLogger(PGHStoreTypeHandler.class);

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
     */
    @Override
    public PGHStore getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transform(cs.getString(columnIndex));
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
     */
    @Override
    public PGHStore getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transform(rs.getString(columnIndex));
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
     */
    @Override
    public PGHStore getResult(ResultSet rs, String columnName) throws SQLException {
        return transform(rs.getString(columnName));
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object,
     * org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, PGHStore parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setObject(i, null);
            return;
        }
        ps.setObject(i, parameter);
    }

    /*
     * (non-Javadoc)
     * @see cn.zzfyip.search.common.typehandler.ITypeHandler#transform(java.lang.Object)
     */
    @Override
    public PGHStore transform(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        PGHStore store = null;
        try {
            store = new PGHStore(value);
        } catch (SQLException e) {
            PGHStoreTypeHandler.logger.error("String cover PGHStore error, trace ", e);
        }
        return store;
    }

}
