/**
 * Project: unittest
 * 
 * File Created at 2015-9-16
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.zhenqiangli.unittest.example.domain.Book;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

/**
 * TODO Comment of BookDAO
 * 
 * @author lizhenqiang
 */
@Repository
public class BookDAO extends BaseJdbcTemplateDAO {

    private static final class BookRowMapper implements ParameterizedRowMapper<Book> {
        public Book mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Book(rs.getString("title"), rs.getInt("price"), rs.getInt("discount"));
        }
    }

    static final BookRowMapper BOOK_ROW_MAPPER = new BookRowMapper();

    public List<Book> getBookList() {
        return this.jt.query("select title,price,discount from book", BOOK_ROW_MAPPER);
    }

    public Book getBookById(final int id) {
        try {
            return this.jt.queryForObject("select title,price,discount from book where id=?",
                    new Object[] { id }, BOOK_ROW_MAPPER);
        } catch (final DataAccessException e) {
            if (!(e instanceof EmptyResultDataAccessException)) {
                throw e;
            }
            return null;
        }
    }


    public int addBook(final Book book) {
        return this.jt.update("insert into book (title,price,discount) values(?,?,?)",
                new PreparedStatementSetter() {
                    int index = 1;

                    public void setValues(final PreparedStatement ps) throws SQLException {
                        ps.setString(this.index++, book.getTitle());
                        ps.setInt(this.index++, book.getPrice());
                        ps.setInt(this.index++, book.getDiscount());
                    }
                });
    }


    public int updateBook(final int id, final String title, final int price, final int discount) {
        return this.jt.update("update book set title=?,price=?,discount=? where id=?",
                new PreparedStatementSetter() {

                    public void setValues(final PreparedStatement ps) throws SQLException {
                        int index = 1;

                        ps.setString(index++, title);
                        ps.setInt(index++, price);
                        ps.setInt(index++, discount);
                        ps.setInt(index++, id);
                    }
                });

    }

}
