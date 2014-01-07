package cn.zzfyip.search.base;

import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * 实现描述：实现自己的classrunner实现lazy init
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-7-12 下午3:18:57
 */
public class BaseSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    private final static Logger logger = LoggerFactory.getLogger(BaseSpringJUnit4ClassRunner.class);

    public BaseSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        if (BaseSpringJUnit4ClassRunner.logger.isDebugEnabled()) {
            BaseSpringJUnit4ClassRunner.logger.debug("BaseSpringJUnit4ClassRunner constructor called with [" + clazz
                    + "].");
        }
    }

    @Override
    protected String getDefaultContextLoaderClassName(Class<?> clazz) {
        return LazyInitGenericXmlContextLoader.class.getName();
    }

}
