/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import me.zhenqiangli.unittest.example.domain.Book;
import me.zhenqiangli.unittest.example.dao.BookDAOTest;
import me.zhenqiangli.unittest.example.dao.BookDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.unitils.UnitilsJUnit4TestClassRunner;

/**
 * 单纯dao逻辑的调用, mock掉真实的数据库连接. 这里使用JdbcTemplate, 用其他持久层框架的mock也类似
 * TODO Comment of BookDAOTest
 * @author lizhenqiang
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookDAOTest {
    
    @Mock
    private JdbcTemplate mockJt;

    /**
     * 被测对象
     */
    private BookDAO      bookDAO;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.bookDAO = new BookDAO() {
            @Override
            protected void replaceJtForTest() {
                this.jt = BookDAOTest.this.mockJt;
            }
        };
        this.bookDAO.replaceJtForTest();
    }
    
    @SuppressWarnings("serial")
    @Test(expected = DataAccessException.class)
    public void testGetBookList_Exception() {

        when(
                this.mockJt.query(Mockito.eq("select title,price,discount from book"),
                        Mockito.same(BookDAO.BOOK_ROW_MAPPER))).thenThrow(
                new DataAccessException("only test") {
                });

        this.bookDAO.getBookList();

        Mockito.verify(this.mockJt).query(Mockito.anyString(),
                Mockito.same(BookDAO.BOOK_ROW_MAPPER));
    }
    
    /**
     * 假设DB返回空数据
     */
    @Test
    public void testGetBookList_NoBook() {

        when(
                this.mockJt.query(Mockito.eq("select title,price,discount from book"),
                        Mockito.same(BookDAO.BOOK_ROW_MAPPER))).thenReturn(Collections.<Book> emptyList());

        final List<Book> books = this.bookDAO.getBookList();

        // verify
        assertThat(books).isNotNull().isEmpty();
        Mockito.verify(this.mockJt).query("select title,price,discount from book",
                BookDAO.BOOK_ROW_MAPPER);
    }
    
    @Test
    public void testGetBookById_NoBook() {
        final int id = 1;
        when(
                this.mockJt.queryForObject(
                        Mockito.eq("select title,price,discount from book where id=?"),
                        Mockito.eq(new Object[] { id }), Mockito.same(BookDAO.BOOK_ROW_MAPPER)))
                .thenThrow(new EmptyResultDataAccessException(1));

        final Book book = this.bookDAO.getBookById(id);

        assertThat(book).isNull();

        verify(this.mockJt).queryForObject(
                Mockito.eq("select title,price,discount from book where id=?"),
                Mockito.eq(new Object[] { id }), Mockito.same(BookDAO.BOOK_ROW_MAPPER));
    }

}
