package com.example.praty.uploadimages;

import android.net.Uri;

public class UrlModel {

    private String mUrl;

    public UrlModel(String mUrl) {
        this.mUrl = mUrl;
    }

    public UrlModel() {
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
