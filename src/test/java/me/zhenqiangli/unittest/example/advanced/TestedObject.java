package me.zhenqiangli.unittest.example.advanced;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 * TODO 被这是对象
 * @author lizhenqiang
 *
 */
public class TestedObject {

    /**
     * 怎么隔离外部依赖,测试这个方法本身的逻辑正确性?(也就是在getFromRemote返回内容的前面加上"hahaha:")
     * 
     * @return
     * @throws Exception
     */
    public String doSomething() throws Exception {
        return handle(BeInvokedObject.getFromRemote());
    }


    /**
     * 怎么隔离BeInvokedObject构造器的外部依赖?
     * 
     * @return
     * @throws Exception
     */
    public String doSomething_2() throws Exception {
        return handle(new BeInvokedObject("xxx").getFromLocal());
    }


    /**
     * 怎么mock url对应的服务
     * 
     * @return
     * @throws Exception
     */
    public static String doSomething_3(String urlString) throws Exception {
        URL url = new URL(urlString);

        try {
            final InputStream inputStream = url.openStream();
            return handle(IOUtils.toString(inputStream));
        } catch (IOException e) {
            return "error";
        }

    }


    private static String handle(final String text) {
        // 假设在这里进行了一系列业务处理
        return "hahaha:" + text;
    }
}
