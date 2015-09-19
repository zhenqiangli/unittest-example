/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zhenqiangli.unittest.example.domain.Book;
import me.zhenqiangli.unittest.example.controller.BookController;
import me.zhenqiangli.unittest.example.service.BookService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.reflectionassert.ReflectionAssert;


/**
 * TODO controller 层的单元测试演示
 * @author lizhenqiang
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookControllerTest {

    @Mock
    PrintWriter                 printWriter;

    /**
     * mocked request
     */
    @Mock
    private HttpServletRequest  request;

    /**
     * mocked response
     */
    @Mock
    private HttpServletResponse response;

    /**
     * mocked bookService ,and inject into bookcontroller
     */
    @InjectIntoByType(target = "bookController")
    @Mock
    private BookService         bookService;

    private ModelMap            modelMap;

    /**
     * 被测试的对象:bookcontroller
     */
    @TestedObject
    private BookController      bookController;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.modelMap = new ModelMap();
    }
    
    /**
     * 验证bookService返回数据为null时,被测对象bookcontroller的表现是否符合预期
     */
    @Test
    public void testGetBookList_nullbook() {

        // 假设调用bookService返回null
        //        EasyMock.expect(this.bookService.getBookList()).andReturn(null);
        when(this.bookService.getBookList()).thenReturn(null);

        // 假设获取type为null
        //        EasyMock.expect(this.request.getHeader("type")).andReturn(null);
        when(this.request.getHeader("type")).thenReturn(null);

        // no need in mockito
        //        EasyMockUnitils.replay();

        // 开始运行被测方法
        final String page = this.bookController.getBookList(this.request, this.response,
                this.modelMap);

        // 期望返回页面为/list
        assertEquals("/list", page);
        // 期望返回的books数据为null
        assertNull(this.modelMap.get("books"));

        // 验证行为, 是否调用过mock对象的方法
        verify(bookService, Mockito.only()).getBookList();
        //        EasyMockUnitils.verify();

    }


    /**
     * 调用bookService抛出异常时,验证被测对象的表现是否符合预期
     */
    @Test
    public void testGetBookList_unexpectedException() {
        //假设抛出异常
        when(this.bookService.getBookList()).thenThrow(
                new RuntimeException("unexpectedException,just a test"));

        // 运行被测方法
        final String page = this.bookController.getBookList(this.request, this.response,
                this.modelMap);

        // 验证错误信息
        assertEquals("unexpectedException,just a test", this.modelMap.get("err"));
        // 验证返回页面 ,assert4j api ,用起来是否更爽?
        assertThat(page).isEqualTo("/error");
    }

    @Test
    public void testGetBookList_normal() {

        final List<Book> books = new ArrayList<Book>();
        books.add(new Book("book1", 10, 2));
        books.add(new Book("book2", 6, 0));

        // 假设正常返回一些books
        when(this.bookService.getBookList()).thenReturn(books);

        // 假设获取type为null
        when(this.request.getHeader("type")).thenReturn(null);

        // run test method
        final String page = this.bookController.getBookList(this.request, this.response,
                this.modelMap);

        // 验证返回页面,以及数据
        assertThat(page).isEqualTo("/list");
        assertThat(this.modelMap.get("books")).isNotNull();
        ReflectionAssert.assertReflectionEquals(
                Arrays.asList(new Book("book1", 10, 2), new Book("book2", 6, 0)), books);

    }


    /**
     * 验证json方式请求
     * 
     * @throws IOException
     */
    @Test
    public void testGetBookList_normal_json() throws IOException {

        // 构造数据
        final List<Book> books = new ArrayList<Book>();
        books.add(new Book("book1", 10, 2));
        books.add(new Book("book2", 6, 0));

        when(this.bookService.getBookList()).thenReturn(books);

        // 假设希望返回json格式的数据
        when(this.request.getHeader("type")).thenReturn("json");

        // 当调用到response getWriter方法时, 返回一个mocked printWriter
        when(this.response.getWriter()).thenReturn(this.printWriter);

        // run test mothed
        final String page = this.bookController.getBookList(this.request, this.response,
                this.modelMap);

        // 验证返回页面和数据
        assertThat(page).isNull();
        assertThat(this.modelMap.get("books")).isNull();

        // 验证行为: 这些方法是否符合预期地被调用到了
        verify(this.response, Mockito.atLeastOnce()).getWriter();
        verify(this.printWriter, Mockito.atLeastOnce()).write(books.toString());
        verify(this.printWriter).flush();

    }


    //---------------------------End test getbooklist----------------------------------//

    /**
     * 正常流程
     */
    @Test
    public void testAddBook_normal() {
        final String title = "title1";
        final int price = 6;
        final int discount = 0;

//        this.bookService.addBook(new Book(title, price, discount));

        final String page = this.bookController.addBook(this.request, this.response, title, price,
                discount);

        assertThat(page).isEqualTo("/list");
    }


    /**
     * 不合理的价格
     */
    @Test
    public void testAddBook_invalidBookPrice() {
        final String title = "title1";
        final int price = 6;
        final int discount = 7;

//        this.bookService.addBook(new Book(title, price, price));

        final String page = this.bookController.addBook(this.request, this.response, title, price,
                discount);

        assertThat(page).isEqualTo("/add_failed");
    }


    /**
     * add book 时,下层调用抛出异常
     */
    @Test
    public void testAddBook_unexpectedException() {
        final String title = "title1";
        final int price = 6;
        final int discount = 0;

        // 假设抛出Service异常
        Mockito.doThrow(new RuntimeException("don't worry,just a test")).when(this.bookService)
                .addBook(new Book(title, price, discount));

        //
        final String page = this.bookController.addBook(this.request, this.response, title, price,
                discount);

        assertThat(page).isEqualTo("/error");
    }

}
