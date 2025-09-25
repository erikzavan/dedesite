package com.zavan.dedesite.service;

import com.zavan.dedesite.model.Post;
import com.zavan.dedesite.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MarkdownService markdownService;

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public void savePost(Post post) {
        String safeHtml = markdownService.toSafeHtml(post.getContent());
        post.setContentHtml(safeHtml);
        postRepository.save(post);
    }
    
    public Page<Post> getPostsPaginated(int page, int size) {
        return postRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }
    
    public void deletePostById(Long id) {
    postRepository.deleteById(id);
    }
}