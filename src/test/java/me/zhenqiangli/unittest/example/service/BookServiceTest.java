/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import me.zhenqiangli.unittest.example.domain.Book;
import me.zhenqiangli.unittest.example.dao.BookDAO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * TODO 测试bookservice的行为, mock掉dao层
 * @author lizhenqiang
 *
 */
public class BookServiceTest {
    
    @InjectIntoByType(target = "bookservice")
    @Mock
    BookDAO     bookDAO;

    @InjectMocks
    BookService bookService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetBookList_normal() {
        final Book book1 = new Book("test1", 5, 3);
        final Book book2 = new Book("test2", 7, 0);

        // 假设dao返回构造的数据
        when(this.bookDAO.getBookList()).thenReturn(Arrays.asList(book1, book2));

        // run tested method
        final List<Book> books = this.bookService.getBookList();

        // 验证数据和行为
        ReflectionAssert.assertReflectionEquals(Arrays.asList(book1, book2), books);
        verify(this.bookDAO).getBookList();
    }


    /**
     * dao层返回null
     */
    @Test
    public void testGetBookList_nullBook() {
        // 假设dao返回null
        when(this.bookDAO.getBookList()).thenReturn(null);

        final List<Book> books = this.bookService.getBookList();

        // verify data and behavior
        assertThat(books).isNotNull().isEmpty();
        verify(this.bookDAO).getBookList();
    }


    //-----------------End test GetBookList-------------------//

    /**
     *
     */
    @Test
    public void testGetBookById() {
        final int id = 2;

        when(this.bookDAO.getBookById(id)).thenReturn(new Book("test1", 5, 3));

        final Book book = this.bookService.getBookById(id);

        ReflectionAssert.assertReflectionEquals(new Book("test1", 5, 3), book);

        verify(this.bookDAO).getBookById(id);
    }


    /**
     * 传入非法值, 期望抛出异常
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBookById_idLessThan0() {
        final int id = -2;
        this.bookService.getBookById(id);
    }


    /**
     * dao层发生未知错误, 期望异常继续往上抛,而不是被吃掉
     */
    @Test(expected = RuntimeException.class)
    public void testGetBookById_unexpectedErrorOnDAO() {
        final int id = 2;
        when(this.bookDAO.getBookById(id)).thenThrow(new RuntimeException());

        this.bookService.getBookById(id);

        verify(this.bookDAO).getBookById(id);
    }


    //-----------------End test GetBookById-------------------//

    @Test
    public void testUpdateBook() {
        final int id = 9;
        final String title = "test1";
        final int price = 90;
        final int discount = 9;

        when(this.bookDAO.updateBook(id, title, price, discount)).thenReturn(1);

        this.bookService.updateBook(id, title, price, discount);

        verify(this.bookDAO).updateBook(id, title, price, discount);
    }


    /**
     * 非法参数调用:价格小于0,期望抛出IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateBook_priceLessThan0() {
        this.bookService.updateBook(7, "title", -9, 8);
    }


    /**
     * 价格小于折扣价,验证是否把折扣价设为价格
     */
    @Test
    public void testUpdateBook_priceLessThanDiscount() {

        //
        when(this.bookDAO.updateBook(7, "title", 9, 9)).thenReturn(1);

        //price<Discount
        this.bookService.updateBook(7, "title", 9, 18);

        verify(this.bookDAO).updateBook(7, "title", 9, 9);
    }

}
