package cn.zzfyip.search.common.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务Exception<br>
 * 纯业务的异常，意思是有程序分支可以处理的，业务场景中的一种，而非系统的错误异常。
 * 
 * @author shuo.chang
 * @version
 * @see
 * @since
 */
public class BusinessException extends Exception {

    private static Map<Integer, String> CODE_MAP = null;

    private static final String ERROR_CODE_0_MESSAGE = "未知系统错误";

    private static final String EXCEPTION_MESSAGE_PROPERTIES = "business-exception-message.properties";

    private final static Logger logger = LoggerFactory.getLogger(BusinessException.class);

    private static final long serialVersionUID = 2190862418939755316L;

    static {
        BusinessException.initCodeMap();
    }

    /**
     * 从properties配置文件中初始化CodeMap
     */
    private synchronized static void initCodeMap() {
        if (null == BusinessException.CODE_MAP) {
            // 初始化CODE_MAP
            BusinessException.CODE_MAP = new HashMap<Integer, String>();
            BusinessException.CODE_MAP.put(0, BusinessException.ERROR_CODE_0_MESSAGE);

            Configuration config = null;
            try {
                config = new PropertiesConfiguration(BusinessException.EXCEPTION_MESSAGE_PROPERTIES);
            } catch (ConfigurationException e) {
                BusinessException.logger.error("ERROR ## read from the properties happend error, the trace is ", e);
            }
            BusinessException.logger.info("---------------初始化异常信息开始---------------");

            if (config != null) {
                Iterator<?> keyIterator = config.getKeys();
                while (keyIterator.hasNext()) {
                    String key = (String) keyIterator.next();
                    if (NumberUtils.isNumber(key)) {
                        BusinessException.CODE_MAP.put(NumberUtils.createInteger(key), config.getString(key));
                        BusinessException.logger.info("添加code=" + key + ",message=" + config.getString(key));
                    } else {
                        BusinessException.logger.info("跳过添加code=" + key + ",message=" + config.getString(key));
                    }
                }
            }

            BusinessException.logger.info("---------------初始化异常信息完毕---------------");
        }
    }

    /**
     * 附加描述信息
     */
    private String additionMessage = null;

    /**
     * 本异常错误代码
     */
    private Integer errorCode = 0;

    public BusinessException(int errCode, String additionMessage) {
        super(additionMessage);
        this.additionMessage = additionMessage;
        this.errorCode = errCode;
    }

    public BusinessException(Integer erroCode) {
        super();
        errorCode = erroCode;
    }

    public BusinessException(Integer erroCode, String additionMessage, Throwable cause) {
        super(cause);
        errorCode = erroCode;
        this.additionMessage = additionMessage;
    }

    public BusinessException(Integer erroCode, Throwable cause) {
        super(cause);
        errorCode = erroCode;
    }

    /**
     * 获取异常错误代码
     * 
     * @return
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getLocalizedMessage() {
        String localMessage = null;
        if (null != this.additionMessage) {
            localMessage = this.additionMessage;
        } else {
            localMessage = BusinessException.CODE_MAP.get(this.errorCode) + "。";
        }
        if (null == this.additionMessage && StringUtils.isEmpty(BusinessException.CODE_MAP.get(this.errorCode))) {
            localMessage = "未知业务错误";
        }
        return localMessage;
    }

}
