package com.adobecontest.android.apphub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by brajesh on 26/1/15.
 */
public class AppModel implements Serializable{

    @JsonProperty("name")
    private String mName;

    @JsonProperty("type")
    private String mType;

    @JsonProperty("url")
    private String mUrl;

    @JsonProperty("image")
    private String mImage;

    @JsonProperty("description")
    private String mDescription;

    @JsonProperty("inapp-purchase")
    private String mInAppPurchase;

    @JsonProperty("last updated")
    private String mLastUpdated;

    @JsonProperty("rating")
    private Double mRating;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmInAppPurchase() {
        return mInAppPurchase;
    }

    public void setmInAppPurchase(String mInAppPurchase) {
        this.mInAppPurchase = mInAppPurchase;
    }

    public String getmLastUpdated() {
        return mLastUpdated;
    }

    public void setmLastUpdated(String mLastUpdated) {
        this.mLastUpdated = mLastUpdated;
    }

    public Double getmRating() {
        return mRating;
    }

    public void setmRating(Double mRating) {
        this.mRating = mRating;
    }
}
