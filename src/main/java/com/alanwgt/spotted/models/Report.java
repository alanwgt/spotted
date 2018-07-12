package com.alanwgt.spotted.models;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicInsert
@Table(name = "reports")
public class Report {

    @Id
    @Column(columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String uid;

    @Column
    private String reason;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Date created_at;

    public Report() {}

    public Report(Post post, String uid) {
        this.post = post;
        this.uid = uid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setUid(String uid) {
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

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", post=" + post +
                ", uid='" + uid + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
