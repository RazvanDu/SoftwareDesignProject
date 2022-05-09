package com.Project.OrderService.Utilities;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;

public class Issue {


    private int id;
    private String title;
    private double price;

    private int review;
    private int issue;
    private String linkPreview;
    private String cover;
    private JsonObject writer;
    private JsonObject series;

    private Integer recommendId;

    public void setRecommendId(Integer recommendId) {

        this.recommendId = recommendId;

    }

    private int amount;


    public Issue(int id, String title, double price, int amount, Integer recommendId) {

        this.id = id;
        this.title = title;
        this.price =  Double.parseDouble(new DecimalFormat("#.##").format(price));
        this.amount = amount;
        this.recommendId = recommendId;

    }

    public Issue() {
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount() {
        this.amount++;
    }

    public void decreaseAmount() {
        this.amount--;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = Double.parseDouble(new DecimalFormat("#.##").format(price));
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getLinkPreview() {
        return linkPreview;
    }

    public void setLinkPreview(String linkPreview) {
        this.linkPreview = linkPreview;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public JsonObject getWriterId() {
        return writer;
    }

    public void setWriterId(JsonObject writer) {
        this.writer = writer;
    }

    public JsonObject getSeriesId() {
        return series;
    }

    public void setSeriesId(JsonObject series) {
        this.series = series;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Issue))
            return false;

        Issue issue = (Issue) obj;

        return issue.getId() == this.id &&
                issue.getTitle().equals(this.title) && Double.compare(issue.getPrice(), this.price) == 0;

    }
}
