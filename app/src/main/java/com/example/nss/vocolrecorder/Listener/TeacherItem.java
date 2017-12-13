package com.example.nss.vocolrecorder.Listener;

import android.os.Parcelable;

/**
 * Created by NSS on 2017-12-05.
 */

public class TeacherItem {

    private String teacher_nick;

    public TeacherItem(String teacher_nick) {
        this.teacher_nick = teacher_nick;
    }

    public String getTeacher_nick() {
        return teacher_nick;
    }

    public void setTeacher_nick(String teacher_nick) {
        this.teacher_nick = teacher_nick;
    }

}
