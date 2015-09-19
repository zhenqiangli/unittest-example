/**
 * Project: unittest
 * 
 * File Created at 2015-9-16
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * TODO Comment of BaseJdbcTemplateDAO
 * @author lizhenqiang
 *
 */
public abstract class BaseJdbcTemplateDAO {
    
    protected JdbcTemplate jt;

    @Autowired
    @Lazy
    public void setDataSource(final DataSource dataSource) {
        this.jt = new JdbcTemplate(dataSource);
    }

    //for unit test only
    protected void replaceJtForTest() {
    }

}
