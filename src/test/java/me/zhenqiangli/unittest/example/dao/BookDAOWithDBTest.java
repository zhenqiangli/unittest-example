/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.datasetloadstrategy.impl.RefreshLoadStrategy;
import org.unitils.spring.annotation.SpringBeanByType;

import me.zhenqiangli.unittest.example.domain.Book;
import me.zhenqiangli.unittest.example.dao.BookDAO;
import me.zhenqiangli.unittest.example.BaseSpringContextUnitTest;

/**
 * <pre>
 * 连接到数据库进行测试, 使用独立的数据集, 并且测试完成后自动回滚
 * </pre>
 *
 * @see <code>spring-dao.xml中的dataSource设置,以及unitils.properties</code>
 * TODO Comment of BookDAOWithDBTest
 * @author lizhenqiang
 *
 */
public class BookDAOWithDBTest extends BaseSpringContextUnitTest {
    
    @SpringBeanByType
    private BookDAO bookDAO;
    
    /**
     * 指定用于测试的数据集
     */
    @Test
    @DataSet("BookDAOWithDBTest-1.xml")
    public void testGetBookList() {
        final List<Book> books = this.bookDAO.getBookList();

        assertThat(books).isNotNull().containsAll(
                Arrays.asList(new Book("title1", 5, 2), new Book("title2", 6, 2)));
    }

    /**
     * 假设db的数据为空
     */
    @Test
    @DataSet("EmptyBooks.xml")
    public void testGetBookList_EmptyBooks() {
        final List<Book> books = this.bookDAO.getBookList();
        
        assertThat(books).isNotNull().isEmpty();
    }

    /**
     * 
     */
    //CleanInsertLoadStrategy
    //InsertLoadStrategy
    //UpdateLoadStrategy
    @Test
    @DataSet(value = "BookDAOWithDBTest-2.xml", loadStrategy = RefreshLoadStrategy.class)
    public void testGetBookById() {
        final Book book = this.bookDAO.getBookById(111);

        assertThat(book).isNotNull().isEqualTo(new Book("title1", 5, 2));
    }

    /**
     *
     */
    @Test
    @DataSet("EmptyBooks.xml")
    public void testGetBookById_EmptyBooks() {
        final Book book = this.bookDAO.getBookById(111);

        assertThat(book).isNull();
    }

    /**
     *
     */
    @Test
    @DataSet("EmptyBooks.xml")
    public void testAddBook() {
        List<Book> books = this.bookDAO.getBookList();
        assertThat(books).isNotNull().isEmpty();

        final Book book = new Book("testInsert", 6, 3);
        this.bookDAO.addBook(book);

        books = this.bookDAO.getBookList();

        assertThat(book).isEqualTo(books.get(0));
    }

    @Test
    @DataSet(value = "BookDAOWithDBTest-2.xml", loadStrategy = RefreshLoadStrategy.class)
    public void testUpdateBook() {
        final int id = 111;
        Book book = this.bookDAO.getBookById(id);
        assertThat(book).isNotNull().isEqualTo(new Book("title1", 5, 2));

        final int i = this.bookDAO.updateBook(id, "title11", 55, 22);

        assertThat(i).isEqualTo(1);

        book = this.bookDAO.getBookById(id);

        assertThat(book).isNotNull().isEqualTo(new Book("title11", 55, 22));
    }

}
