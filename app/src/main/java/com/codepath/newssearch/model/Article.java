package com.codepath.newssearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chanis on 1/30/17.
 */

public class Article {
    private String headline;
    private String webUrl;
    private String thumbnail;

    private Article(JSONObject articleJson) throws JSONException {
        this.headline = articleJson.getJSONObject("headline").optString("main");
        this.webUrl = articleJson.getString("web_url");

        JSONArray multimedia = articleJson.getJSONArray("multimedia");
        if (multimedia != null && multimedia.length() > 0) {
            JSONObject multimediaFirst = multimedia.getJSONObject(0);
            this.thumbnail = "http://www.nytimes.com/" + multimediaFirst.getString("url");
        }
    }

    private Article (String headline, String webUrl, String thumbnail) {
        this.headline = headline;
        this.webUrl = webUrl;
        this.thumbnail = thumbnail;
    }

    public static Article makeArticle(String headline, String webUrl, String thumbnail) {
        return new Article(headline, webUrl, thumbnail);

    }

    public String getWebUrl() {
        return webUrl;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public String getHeadline() {
        return headline;
    }

    public static List<Article> fromJSONArray(JSONArray responseArray) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++){
            try {
                articles.add(new Article(responseArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return articles;
    }
}
