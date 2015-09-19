package me.zhenqiangli.unittest.example.advanced;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * TestedObject Tester.
 * 
 * @author jenwang
 * @since <pre>
 * 九月 18, 2014
 * </pre>
 * @version 1.0
 */
@RunWith(PowerMockRunner.class)
//注意
@PrepareForTest({ BeInvokedObject.class, TestedObject.class })
//注意
public class TestedObjectTest {

    @Mock
    BeInvokedObject beInvokedObjectMock;


    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);

        //注意别忘了这个
        mockStatic(BeInvokedObject.class);
    }


    @After
    public void after() throws Exception {
    }


    /**
     * mock静态方法,使得单元测试不依赖于外部环境.任何时候都是可回归的
     */
    @Test
    public void testDoSomething() throws Exception {
        //mock静态方法
        when(BeInvokedObject.getFromRemote()).thenReturn("iammockcontent");

        String result = new TestedObject().doSomething();

        //验证处理逻辑
        assertThat(result).isEqualTo("hahaha:iammockcontent");
    }


    /**
     * mock构造器,使得单元测试不依赖于外部环境.任何时候都是可回归的.
     * 
     * @throws Exception
     */
    @Test
    public void testDosomething_2() throws Exception {
        // mock构造器
        whenNew(BeInvokedObject.class).withArguments("xxx").thenReturn(beInvokedObjectMock);
        when(beInvokedObjectMock.getFromLocal()).thenReturn("xxx");

        String result = new TestedObject().doSomething_2();

        assertThat(result).isEqualTo("hahaha:xxx");
    }


    @Test
    public void testDoSomething_3() throws Exception {
        Runner runner = null;
        try {

            HttpServer server = httpserver(6666); //开启一个端口为6666的http服务
            server.response("xxx"); //返回我们设定的内容
            runner = Runner.runner(server);
            runner.start();

            String result = TestedObject.doSomething_3("http://localhost:6666");

            assertThat(result).isEqualTo("hahaha:xxx");
        } finally {
            if (runner != null) {
                runner.stop();
            }
        }
    }

    @Test
    public void testDoSomething_3_505() throws Exception {
        Runner runner = null;
        try {

            HttpServer server = httpserver(6666); //开启一个端口为6666的http服务
            server.response(status(505)); //假设远程服务器出错
            runner = Runner.runner(server);
            runner.start();

            String result = TestedObject.doSomething_3("http://localhost:6666");

            assertThat(result).isEqualTo("error");
        } finally {
            if (runner != null) {
                runner.stop();
            }
        }
    }

}
