package com.example.android.popularmoviesapp.pojo;

/**
 * Created by P01242 on 6/11/2016.
 */
public class MovieReview {
    public String getMauthor() {
        return mauthor;
    }

    public void setMauthor(String mauthor) {
        this.mauthor = mauthor;
    }

    public String getMreviewContent() {
        return mreviewContent;
    }

    public void setMreviewContent(String mreviewContent) {
        this.mreviewContent = mreviewContent;
    }

    String mauthor;
    String mreviewContent;
}
