package com.example.android.popularmoviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepika on 5/7/2016.
 */
public class Movie implements Parcelable {

    public Movie(){

    }

    private int mId;

    private boolean mAdult;

    private String mOverview;

    private String mTitle;

    private float mPopularity;

    private boolean mVideo;

    private String mPosterPath;

    private String mReleaseDate;

    public List<Integer> getmGenreIds() {
        return mGenreIds;
    }

    public void setmGenreIds(List<Integer> mGenreIds) {
        this.mGenreIds = mGenreIds;
    }


    private List<Integer> mGenreIds = new ArrayList<Integer>();

    private String mOriginalTitle;

    private String mOriginalLanguage;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public void setmAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public float getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(float mPopularity) {
        this.mPopularity = mPopularity;
    }

    public boolean ismVideo() {
        return mVideo;
    }

    public void setmVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setmOriginalLanguage(String mOriginalLanguage) {
        this.mOriginalLanguage = mOriginalLanguage;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }


    private String mBackdropPath;

    private int mVoteCount;

    private double mVoteAverage;
    public Movie(Parcel in ) {
        readFromParcel( in );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in ) {
            return new Movie( in );
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mOverview);
        dest.writeString(mPosterPath);
        dest.writeDouble(mVoteAverage);
    }

    public void readFromParcel(Parcel in){

        // The order in which its written in parcel should be the same as the order in which u read.
        mId = in.readInt();
        mOriginalTitle = in.readString();
        mReleaseDate = in.readString();
        mOverview = in.readString();

        mPosterPath = in.readString();
        mVoteAverage = in.readDouble();

    }
}
