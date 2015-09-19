/**
 * Project: unittest
 * 
 * File Created at 2015-9-16
 * $Id$
 * 
 */
package me.zhenqiangli.unittest.example.domain;

import me.zhenqiangli.unittest.example.Objects;

/**
 * TODO Comment of Book
 * @author lizhenqiang
 */
public class Book {

    private String title;
    private int    price;
    private int    discount;

    public Book() {

    }

    public Book(final String title, final int price, final int discount) {
        this.title = title;
        this.price = price >= 0 ? price : 0;
        this.discount = price < discount ? price : discount;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(final int price) {
        this.price = price > 0 ? price : 0;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(final int discount) {
        this.discount = discount;
    }

    public int getDiscountPrice() {
        if (this.price < this.discount) {
            throw new IllegalStateException("price < discount");
        }

        return this.discount < 0 ? this.price : this.price - this.discount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Book) {
            final Book other = (Book) obj;
            if (!Objects.equals(this.title, other.title)) {
                return false;
            }

            if (this.price != other.price) {
                return false;
            }

            if (this.discount != other.discount) {
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.price, this.discount);
    }
}
