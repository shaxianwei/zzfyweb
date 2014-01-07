package cn.zzfyip.search.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <pre>
 * 实现描述：用于测试bean的反序列化，序列化对象保存在文件中。
 * 用于mock dao查出来的对象，避免测试产品被删导致testcase失败。
 * </pre>
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-7-9 下午5:29:07
 */
public class BeanUtils {

    private final static String BYTE_DIR_PATH = System.getProperty("user.dir") + "/src/test/resources/bean/byte/";

    /**
     * 如果文件不存在则创建文件
     * 
     * @param filePath
     * @return 文件
     * @throws IOException
     */
    private static File createFileIfNotExist(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 测试用的bean对象，解析文件并反序列化
     * 
     * @param fileName 文件名，不用加路径
     * @return 反序列化的类对象
     */
    public static Object deserializerBean(String fileName) {
        String filePath = BeanUtils.BYTE_DIR_PATH + fileName;

        try {
            File file = BeanUtils.createFileIfNotExist(filePath);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            ois.close();
            fis.close();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试用的bean对象，解析文件并反序列化
     * 
     * @param fileName 文件名，不用加路径
     * @param targetClass 需要强制转换的类型
     * @return 反序列化的类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializerBean(String fileName, Class<T> targetClass) {
        String filePath = BeanUtils.BYTE_DIR_PATH + fileName;

        try {
            File file = BeanUtils.createFileIfNotExist(filePath);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            T object = (T) ois.readObject();
            ois.close();
            fis.close();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存对象到序列化文件中，以用来测试
     * 
     * @param fileName 文件名，不用加路径
     * @param object 需要保存为序列化的对象
     */
    public static void serializerBean(String fileName, Object object) {
        String filePath = BeanUtils.BYTE_DIR_PATH + fileName;
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
