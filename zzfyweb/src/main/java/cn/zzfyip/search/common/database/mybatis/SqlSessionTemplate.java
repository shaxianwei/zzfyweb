/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 实现描述：TODO 类描述待完成
 * 
 * @author chaoyi.he
 * @version v1.0.0
 * @see
 * @since 2013年11月14日 下午8:54:23
 */
public class SqlSessionTemplate extends org.mybatis.spring.SqlSessionTemplate {

    /**
     * @param sqlSessionFactory
     */
    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType(),
                new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                        true));
    }

}
