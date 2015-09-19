/**
 * Project: unittest
 * 
 * File Created at 2015-9-16
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.service;

import java.util.Collections;
import java.util.List;

import me.zhenqiangli.unittest.example.dao.BookDAO;
import me.zhenqiangli.unittest.example.domain.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO Comment of BookService
 * @author lizhenqiang
 */
@Service
public class BookService {

    @Autowired
    private BookDAO bookDAO;

    public List<Book> getBookList() {
        List<Book> books = this.bookDAO.getBookList();
        if (books == null) {
            books = Collections.emptyList();
        }

        return books;
    }

    public Book getBookById(final int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must not less than 0");
        }

        return this.bookDAO.getBookById(id);
    }

    public void addBook(final Book book) {
        this.bookDAO.addBook(book);
    }


    /**
     * @param id
     * @param title
     * @param price
     * @param discount
     */
    public void updateBook(final int id, final String title, final int price, int discount) {
        if (price < 0) {
            throw new IllegalArgumentException("price must not less than 0");
        }

        if (discount > price) {
            discount = price;
        }
        this.bookDAO.updateBook(id, title, price, discount);
    }

}
