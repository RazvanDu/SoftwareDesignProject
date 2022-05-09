package com.Project.CatalogService.Database;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "series")
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "Title", length = 100, nullable = false)
    String title;

    @ManyToOne
    @JoinColumn(name = "Publisher", referencedColumnName = "ID")
    Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", referencedColumnName = "ID")
    Writer createdBy;

    @Column(name = "Ongoing", nullable = false)
    boolean ongoing;

    @ManyToOne
    @JoinColumn(name = "Schedule", referencedColumnName = "ID")
    Schedule schedule;

    @Column(name = "StartDate")
    Date startDate;

    @Column(name = "Cover", length = 512)
    String cover;

    @ManyToOne
    @JoinColumn(name = "Rating", referencedColumnName = "ID")
    Rating rating;

    @Transient
    String link;

    public Series() {
    }

    public Series(String title){
        this.title = title;
    }

    public Series(String title, Publisher publisher, Writer createdBy, boolean ongoing, Schedule schedule, Date startDate, String cover, Rating rating, String link) {

        this.title = title;
        this.publisher = publisher;
        this.createdBy = createdBy;
        this.ongoing = ongoing;
        this.schedule = schedule;
        this.startDate = startDate;
        this.cover = cover;
        this.rating = rating;
        this.link = link;

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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Writer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Writer createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
