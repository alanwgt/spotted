package com.alanwgt.spotted.models;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicInsert
@Table(name = "upvotes")
public class Upvote {

    @Id
    @Column(columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String uid;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Date created_at;

    public Upvote() {}

    public Upvote(Post post, String uid) {
        this.post = post;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public String getUid() {
        return uid;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Upvote{" +
                "id=" + id +
                ", post=" + post +
                ", uid='" + uid + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
