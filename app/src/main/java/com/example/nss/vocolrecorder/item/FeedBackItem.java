package com.example.nss.vocolrecorder.item;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by NSS on 2017-12-18.
 */

public class FeedBackItem {

    public FeedBackItem(URI voice_url, long length, String savedDate, String teacher_nick, String subject) {
        this.voice_url = voice_url;
        this.length = length;
        this.savedDate = savedDate;
        this.teacher_nick = teacher_nick;
        this.subject = subject;
    }

    public void setVoice_url(URI voice_url) {
        this.voice_url = voice_url;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public void setTeacher_nick(String teacher_nick) {
        this.teacher_nick = teacher_nick;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private URI voice_url;
    private long length;
    private String savedDate;
    private String teacher_nick;
    private String subject;

    public URI getVoice_url() {
        return voice_url;
    }

    public long getLength() {
        return length;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public String getTeacher_nick() {
        return teacher_nick;
    }

    public String getSubject() {
        return subject;
    }

    public static ArrayList<FeedBackItem> convertToList(String json){

        ArrayList<FeedBackItem> list =new ArrayList<>();
        try {
            JSONArray jsonArray= new JSONArray(json);

            for(int i=0;i<jsonArray.length();i++){

                String voiceFile = jsonArray.getJSONObject(i).getString("voiceFile");
                String savedDate =jsonArray.getJSONObject(i).getString("savedDate");
                String subject = jsonArray.getJSONObject(i).getString("subject");

                FeedBackItem feedBackItem = new FeedBackItem(new URI(voiceFile),0,savedDate,"not yey",subject);

                list.add(feedBackItem);
            }

        }catch (JSONException e){

            e.printStackTrace();
        }catch (URISyntaxException e){
            e.printStackTrace();
        }




        return list;
    }


}

