package com.alanwgt.spotted.dao;

import com.alanwgt.spotted.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Post> getPosts(String uid) {
        Session session = sessionFactory.getCurrentSession();
        Query<Post> query = session.createQuery("FROM Post WHERE deleted_by IS NULL AND id NOT IN (SELECT post FROM Report r WHERE r.uid=:uid) ORDER BY created_at DESC", Post.class);
        query.setParameter("uid", uid);
        return query.getResultList();
    }

    public List<Post> getPostsWithReports() {
        Session session = sessionFactory.getCurrentSession();
        Query<Post> query = session.createQuery("FROM Post WHERE deleted_by IS NULL AND id IN (SELECT post FROM Report) ORDER BY created_at DESC", Post.class);
        return query.getResultList();
    }

    public void create(Post post) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(post);
    }

    public void delete(Post post) {
        sessionFactory.getCurrentSession().delete(post);
    }

    public Post findById(Long id) {
        return sessionFactory.getCurrentSession().load(Post.class, id);
    }

    public void update(Post post) {
        sessionFactory.getCurrentSession().update(post);
    }

}
