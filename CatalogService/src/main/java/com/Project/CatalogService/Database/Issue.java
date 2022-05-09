package com.Project.CatalogService.Database;

import javax.persistence.*;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @ManyToOne
    @JoinColumn(name = "Series", referencedColumnName = "ID")
    Series series;

    @Column(name = "Cover", length = 512)
    String cover;

    @ManyToOne
    @JoinColumn(name = "Writer", referencedColumnName = "ID")
    Writer writer;

    @Column(name = "link_preview", length = 512)
    String linkPreview;

    @Column(name = "Issue")
    int issue;

    @Column(name = "Review")
    int review;

    @Column(name = "Title", length = 100)
    String title;

    @Column(name = "Price", nullable = false)
    float price;

    @Column(name = "Sold", nullable = false)
    int sold;

    @Transient
    String link;

    @Transient
    boolean hasLink;

    @Transient
    int amount;

    @Transient
    String addLink;

    @Transient
    String removeLink;

    public Issue() {
    }

    public Issue(Series series, String cover, Writer writer, String linkPreview, int issue, String title, float price, int sold) {

        this.series = series;
        this.cover = cover;
        this.writer = writer;
        this.linkPreview = linkPreview;
        this.issue = issue;
        this.title = title;
        this.price = price;
        this.sold = sold;

    }

    public Issue(Series series, String cover, Writer writer, String linkPreview, int issue, int review, String title, float price, String link, boolean hasLink, int amount, String addLink, String removeLink, int sold) {

        this.series = series;
        this.cover = cover;
        this.writer = writer;
        this.linkPreview = linkPreview;
        this.issue = issue;
        this.review = review;
        this.title = title;
        this.price = price;
        this.link = link;
        this.hasLink = hasLink;
        this.amount = amount;
        this.addLink = addLink;
        this.removeLink = removeLink;
        this.sold = sold;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLinkPreview() {
        return linkPreview;
    }

    public void setLinkPreview(String linkPreviw) {
        this.linkPreview = linkPreviw;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHasLink() {
        return hasLink;
    }

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAddLink() {
        return addLink;
    }

    public void setAddLink(String addLink) {
        this.addLink = addLink;
    }

    public String getRemoveLink() {
        return removeLink;
    }

    public void setRemoveLink(String removeLink) {
        this.removeLink = removeLink;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}