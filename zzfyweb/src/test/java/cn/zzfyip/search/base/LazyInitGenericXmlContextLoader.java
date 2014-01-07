package cn.zzfyip.search.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * 
 * 实现描述：实现获取 lazy init beanfactory 功能的 contextLoader
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-7-12 下午3:18:39
 */
public class LazyInitGenericXmlContextLoader extends GenericXmlContextLoader {

    private final static Logger logger = LoggerFactory.getLogger(LazyInitGenericXmlContextLoader.class);

    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.setEventListener(new EmptyReaderEventListener() {

            @Override
            public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
                if (!(defaultsDefinition instanceof DocumentDefaultsDefinition)) {
                    logger.error("defaultDefinition is not DocumentDefaultsDefinition, can't use lazy-init");
                }
                DocumentDefaultsDefinition docDefaults = (DocumentDefaultsDefinition) defaultsDefinition;
                docDefaults.setLazyInit("true");
            }
        });

        return reader;
    }

}
