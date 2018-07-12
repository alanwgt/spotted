package com.alanwgt.spotted.services;

import com.alanwgt.spotted.dao.ReportDAO;
import com.alanwgt.spotted.models.Post;
import com.alanwgt.spotted.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private PostService postService;

    @Transactional
    public List<Report> getAllReports() {
        return reportDAO.getAllReports();
    }

    @Transactional
    public List<Report> getUserReports(String uid) {
        return reportDAO.getUserReports(uid);
    }

    @Transactional
    public void create(Report report, String post_id) {
        Long postId = Long.valueOf(post_id);
        Post p = postService.findById(postId);
        report.setPost(p);

        reportDAO.create(report);
    }

}
