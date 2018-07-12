package com.alanwgt.spotted.dao;

import com.alanwgt.spotted.models.Upvote;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UpvoteDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Upvote> getUpvotes(String uid) {
        Session session = sessionFactory.getCurrentSession();
        Query<Upvote> query = session.createQuery("from Upvote where uid=:uid order by created_at desc", Upvote.class);
        query.setParameter("uid", uid);
        return query.getResultList();
    }

    public Upvote find(String uid, Long postId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Upvote> query = session.createQuery("from Upvote where post.id =:postId and uid =:uid", Upvote.class);
        query.setParameter("postId", postId);
        query.setParameter("uid", uid);
        return query.getSingleResult();
    }

    public void create(Upvote upvote) {
        sessionFactory.getCurrentSession().saveOrUpdate(upvote);
    }

    public void delete(Upvote upvote) {
        sessionFactory.getCurrentSession().delete(upvote);
    }
}
