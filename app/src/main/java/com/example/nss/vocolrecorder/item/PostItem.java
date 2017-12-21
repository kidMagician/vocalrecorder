package com.example.nss.vocolrecorder.item;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by NSS on 2017-12-20.
 */

public class PostItem {

    public String getTile() {
        return tile;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished_date() {
        return published_date;
    }

    public PostItem(String tile, String content, String author, String published_date) {
        this.tile = tile;
        this.content = content;
        this.author = author;
        this.published_date = published_date;
    }

    String tile;
    String content;
    String author;
    String published_date;

    public void setTile(String tile) {
        this.tile = tile;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public static ArrayList<PostItem> convertToList(String json) {

        ArrayList<PostItem> list = new ArrayList<>();
        try {

            if (json != null) {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    String title = jsonArray.getJSONObject(i).getString("title");
                    String content = jsonArray.getJSONObject(i).getString("content");
                    String author = jsonArray.getJSONObject(i).getString("author");
                    String published_date = jsonArray.getJSONObject(i).getString("published_date");

                    PostItem postItem = new PostItem(title, content, author, published_date);

                    list.add(postItem);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
