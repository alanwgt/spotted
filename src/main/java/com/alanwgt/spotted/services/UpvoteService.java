package com.alanwgt.spotted.services;

import com.alanwgt.spotted.dao.UpvoteDAO;
import com.alanwgt.spotted.models.Post;
import com.alanwgt.spotted.models.Upvote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UpvoteService {

    @Autowired
    private UpvoteDAO upvoteDAO;
    @Autowired
    private PostService postService;

    @Transactional
    public List<Upvote> getUpvotes(String uid) {
        return upvoteDAO.getUpvotes(uid);
    }

    @Transactional
    public void create(Upvote upvote, String post_id) {
        Long postId = Long.valueOf(post_id);
        Post p = postService.findById(postId);
        upvote.setPost(p);

        upvoteDAO.create(upvote);
    }

    @Transactional
    public void delete(String uid, Long postId) {
        Upvote upvote = upvoteDAO.find(uid, postId);

        if (upvote != null) {
            upvoteDAO.delete(upvote);
        }
    }
}
