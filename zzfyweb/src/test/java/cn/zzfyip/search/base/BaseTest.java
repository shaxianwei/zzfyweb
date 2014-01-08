
package cn.zzfyip.search.base;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 
 * <pre>
 * 实现了bean的全局 lazy init
 * </pre>
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-7-12 下午3:18:23
 */
@RunWith(BaseSpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {
    /**
     * <pre>
     * 提前初始化mockit，如果在junit后初始化，在使用Verifications的时候会出现IllegalStateException
     * JMockit wasn't properly initialized; check that jmockit.jar precedes junit.jar in the classpath
     * 另一个解决办法：
     * 修改pom.xml的jmockit位置，必须在junit之前
     * </pre>
     */
    static {
        mockit.internal.startup.Startup.initializeIfPossible();
    }

    @Override
    @Resource(name = "myDataSource")
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

}
