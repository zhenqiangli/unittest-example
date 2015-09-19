/**
 * Project: unittest
 * 
 * File Created at 2015-9-19
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.domain;

import static org.junit.Assert.*;

import me.zhenqiangli.unittest.example.domain.Book;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO Comment of BookTest
 * @author lizhenqiang
 *
 */
public class BookTest {
    
    private Book book;
    
    @Before
    public void setUp() {
        book = new Book();
    }
    
    @Test
    public void testGetDiscountPrice() {
        this.book.setPrice(10);
        this.book.setDiscount(1);
        assertEquals(9, this.book.getDiscountPrice());
    }
    
    @Test
    public void testGetDiscountPrice_DiscountIsNegative() {
        this.book.setPrice(10);
        this.book.setDiscount(-1);
        assertEquals(10, this.book.getDiscountPrice());
    }
    
    /**
     * 设置不合理的数据,期望抛出特定的异常
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDiscountPrice_PriceLessThanDiscount() {
        this.book.setPrice(5);
        this.book.setDiscount(6);
        this.book.getDiscountPrice();
    }
    
    @Test
    public void testSetPrice() {
        this.book.setPrice(10);
        assertEquals(10, this.book.getDiscountPrice());
        assertEquals(10, this.book.getPrice());
    }

    @Test
    public void testSetPrice_IsNegative() {
        this.book.setPrice(-10);
        assertEquals(0, this.book.getDiscountPrice());
        assertEquals(0, this.book.getPrice());
    }

}
