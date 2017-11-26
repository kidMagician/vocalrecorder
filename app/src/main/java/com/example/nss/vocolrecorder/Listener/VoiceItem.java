package com.example.nss.vocolrecorder.Listener;

import android.os.Parcel;
import android.os.Parcelable;
import android.speech.tts.Voice;

/**
 * Created by NSS on 9/27/2017.
 */

public class VoiceItem implements Parcelable {

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private int length;
    private String fileName;
    private String filePath;
    private Long mTime;

    public Long getmTime() {
        return mTime;
    }

    public void setmTime(Long mTime) {
        this.mTime = mTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public VoiceItem(){}

    public VoiceItem(Parcel in){

        length =in.readInt();
        fileName=in.readString();
        filePath=in.readString();
        mTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeLong(length);
        dest.writeString(filePath);
        dest.writeLong(mTime);
    }

    public static final Parcelable.Creator<VoiceItem> CREATOR = new Parcelable.Creator<VoiceItem>(){
        public VoiceItem createFromParcel(Parcel in) {
            return new VoiceItem(in);
        }

        public VoiceItem[] newArray(int size) {
            return new VoiceItem[size];
        }

    };
}
