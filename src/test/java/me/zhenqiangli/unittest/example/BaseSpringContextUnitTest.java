/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example;

import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

/**
 * TODO Comment of BaseSpringContextUnitTest
 * @author lizhenqiang
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:spring-dao.xml" })
@Transactional(TransactionMode.ROLLBACK) // 指定回滚策略
public abstract class BaseSpringContextUnitTest {

    @SpringApplicationContext
    protected ApplicationContext applicationcontext;
}
