/**
 * Project: unittest
 * 
 * File Created at 2015-9-16
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.zhenqiangli.unittest.example.domain.Book;
import me.zhenqiangli.unittest.example.service.BookService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO Comment of BookController
 * 
 * @author lizhenqiang
 */
@Controller
@RequestMapping("/book.do")
public class BookController {

    private static final Log logger = LogFactory.getLog(BookController.class);

    @Autowired
    private BookService      bookService;

    /**
     * book http://xx.xx.com/unittest-example/book.do?method=getBookList
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=getBookList")
    public String getBookList(final HttpServletRequest request, final HttpServletResponse response,
                              final ModelMap modelMap) {
        List<Book> books;
        try {
            books = this.bookService.getBookList();
        } catch (final Exception e) {
            modelMap.addAttribute("err", e.getMessage());
            return "/error";
        }

        if ("json".equals(request.getHeader("type"))) {
            try {
                response.getWriter().write(toJson(books));
                response.getWriter().flush();
            } catch (final IOException e) {
                logger.error("", e);
            }

            return null;
        } else {
            modelMap.addAttribute("books", books);
            return "/list";
        }
    }

    private static String toJson(final List<Book> books) {
        if (books == null) {
            return "";
        }
        // TODO parse to json string
        return books.toString();
    }

    /**
     * http://xx.xx.com/unittest-example/book.do?method=getBookById&id=xx
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "method=getBookById")
    public String getBookById(final HttpServletRequest request, final HttpServletResponse response,
                              @RequestParam("id") final int id, final ModelMap modelMap) {
        final Book book = this.bookService.getBookById(id);
        modelMap.addAttribute("book", book);
        return "/bookDetail";

    }

    /**
     * @param request
     * @param response
     * @param title
     * @param price
     * @param discount
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "method=addBook")
    public String addBook(final HttpServletRequest request, final HttpServletResponse response,
                          @RequestParam(value = "title", required = true) final String title,
                          @RequestParam(value = "price", required = true) final int price,
                          @RequestParam("discount") final int discount) {
        try {
            if (discount > price) {
                return "/add_failed";
            }

            this.bookService.addBook(new Book(title, price, discount));
            return "/list";
        } catch (final Exception e) {
            logger.error("add book failed !", e);
            return "/error";
        }

    }

    /**
     * @param request
     * @param response
     * @param id
     * @param title
     * @param price
     * @param discount
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "method=updateBook")
    public String updateBook(final HttpServletRequest request, final HttpServletResponse response,
                             @RequestParam(value = "id", required = true) final int id,
                             @RequestParam(value = "title", required = true) final String title,
                             @RequestParam(value = "price", required = true) final int price,
                             @RequestParam("discount") final int discount, final ModelMap modelMap) {

        final HttpSession session = request.getSession();
        if (session == null) {
            return "/login";
        }

        try {
            this.bookService.updateBook(id, title, price, discount);
            return "/list";

        } catch (final Exception e) {
            modelMap.addAttribute("err", e.getMessage());
            logger.error("add book failed !", e);
            return "/error";
        }

    }

}
