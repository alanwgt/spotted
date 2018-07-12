package com.alanwgt.spotted.models;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@DynamicInsert
@Table(name = "posts")
public class Post {

    @Id
    @Column(columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String uid;

    @Column(length = 300, nullable = false)
    private String message;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Date created_at;

    @Column
    private String deleted_by = null;

    @OneToMany(mappedBy = "post")
    private List<Upvote> upvotes;

    public Post() {}

    public Post(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getDeleted_by() {
        return deleted_by;
    }

    public List<Upvote> getUpvotes() {
        return upvotes;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", message='" + message + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
