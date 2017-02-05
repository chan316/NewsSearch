package com.codepath.newssearch.model;

import org.parceler.Parcel;

/**
 * Created by chanis on 1/31/17.
 */

@Parcel
public class SearchFilter {
    String query = "";
    String date = "";
    String sort = "";
    String category = "";
    int page = 0;

    public SearchFilter() {

    }

    public SearchFilter(String query, String date, String sort, String category, int page) {
        this.query = query;
        this.date = date;
        this.sort = sort;
        this.category = category;
        this.page = page;
    }

    public String getQuery() {
        return this.query;
    }

    public String getDate() {
        return this.date;
    }

    public String getSort() {
        return this.sort;
    }

    public String getCategory() {
        return this.category;
    }

    public int getPage() { return this.page; }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPage(int page) { this.page = page; }

    public void reset() {
        this.query = "";
        this.date = "";
        this.sort = "";
        this.category = "";
        this.page = 0;
    }
}
