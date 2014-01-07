package cn.zzfyip.search.event.task;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

/**
 * @author shuo.chang
 * @version
 * @see
 * @since
 */
@Service
public class MyDetailQuartzJobBean extends QuartzJobBean implements ApplicationContextAware{
    private static final Logger logger = Logger.getLogger(MyDetailQuartzJobBean.class);

    private static ApplicationContext ctx;
    
    private String targetMethod;
    
    private String targetObject;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {

            logger.info("execute [" + targetObject + "] at once>>>>>>");
            Object otargetObject = ctx.getBean(targetObject);
            Method m = null;
            try {
                m = otargetObject.getClass().getMethod(targetMethod, new Class[] {});

                m.invoke(otargetObject, new Object[] {});
            } catch (SecurityException e) {
                logger.error(e);
            } catch (NoSuchMethodException e) {
                logger.error(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new JobExecutionException(e);
        }

    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

}