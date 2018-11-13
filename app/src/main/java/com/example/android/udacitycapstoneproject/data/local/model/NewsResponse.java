
package com.example.android.udacitycapstoneproject.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse implements Parcelable
{
    @SerializedName("articles")
    @Expose
    private List<Article> articles = null;
    public final static Creator<NewsResponse> CREATOR = new Creator<NewsResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewsResponse createFromParcel(Parcel in) {
            return new NewsResponse(in);
        }

        public NewsResponse[] newArray(int size) {
            return (new NewsResponse[size]);
        }

    }
    ;

    protected NewsResponse(Parcel in) {
//        this.status = ((String) in.readValue((String.class.getClassLoader())));
//        this.totalResults = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.articles, (Article.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public NewsResponse() {
    }

    /**
     * @param articles
     */
    public NewsResponse(List<Article> articles) {
        super();
//        this.status = status;
//        this.totalResults = totalResults;
        this.articles = articles;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public int getTotalResults() {
//        return totalResults;
//    }
//
//    public void setTotalResults(int totalResults) {
//        this.totalResults = totalResults;
//    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeValue(status);
//        dest.writeValue(totalResults);
        dest.writeList(articles);
    }

    public int describeContents() {
        return  0;
    }

}
