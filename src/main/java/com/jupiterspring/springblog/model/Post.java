package com.jupiterspring.springblog.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) unsigned")
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String body;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    private User owner;

    public Post(){}

    public Post(long id, String a, String t, String b, Date d, User owner){
        this.id = id;
        this.author = a;
        this.title = t;
        this.body = b;
        this.date = d;
        this.owner = owner;
    }
    public Post(String a, String t, String b, Date d, User owner){
        this.author = a;
        this.title = t;
        this.body = b;
        this.date = d;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
