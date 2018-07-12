package com.alanwgt.spotted.services;

import com.alanwgt.spotted.dao.PostDAO;
import com.alanwgt.spotted.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    @Transactional
    public List<Post> getPosts(String uid) {
        return postDAO.getPosts(uid);
    }

    @Transactional
    public void create(Post post) {
        //TODO: check the user validity
        postDAO.create(post);
    }

    @Transactional
    public Post findById(Long id) {
        return postDAO.findById(id);
    }

    @Transactional
    public void delete(Long postId) {
        Post post = findById(postId);
        postDAO.delete(post);
    }

    @Transactional
    public List<Post> getPostsWithReports() {
        return postDAO.getPostsWithReports();
    }

    @Transactional
    public void softDelete(Long postId, String uid) {
        Post post = findById(postId);
        post.setDeleted_by(uid);
        postDAO.update(post);
    }

}
