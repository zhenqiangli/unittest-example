package me.zhenqiangli.unittest.example.advanced;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * TODO 依赖于外部环境,并且被其他对象调用到
 * @author lizhenqiang
 *
 */
public class BeInvokedObject {

    private String txt;

    /**
     * 假设构造器依赖于外部环境
     * 
     * @throws Exception
     */
    public BeInvokedObject(String someString) throws Exception {
        URL url = new URL("http://www.meizu.com");
        final InputStream inputStream = url.openStream();

        this.txt = IOUtils.toString(inputStream);

        System.out.println(someString);
        System.out.println("new BeInvokedObject :" + txt);

    }

    public String getFromLocal() {
        return this.txt;
    }

    /**
     * 静态方法中调用了外部环境
     * 
     * @return
     * @throws Exception
     */
    public static String getFromRemote() throws Exception {
        return getFromRemote("http://www.meizu.com");
    }

    private static String getFromRemote(String urlString) throws Exception {
        URL url = new URL(urlString);
        final InputStream inputStream = url.openStream();
        return IOUtils.toString(inputStream);
    }
}
