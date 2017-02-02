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

    public SearchFilter() {

    }

    public SearchFilter(String query, String date, String sort, String category) {
        this.query = query;
        this.date = date;
        this.sort = sort;
        this.category = category;
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

    public void reset() {
        this.query = "";
        this.date = "";
        this.sort = "";
        this.category = "";
    }
}
