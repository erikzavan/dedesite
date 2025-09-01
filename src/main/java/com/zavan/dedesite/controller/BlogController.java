package com.zavan.dedesite.controller;

import com.zavan.dedesite.model.Post;
import com.zavan.dedesite.model.User;
import com.zavan.dedesite.service.PostService;
import com.zavan.dedesite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.zavan.dedesite.dto.PostDTO;

import java.util.List;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showBlog(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 5; // 5 posts por página
        Page<Post> postPage = postService.getPostsPaginated(page, size);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", postPage.hasNext());
        return "blog";
    }

    // Apenas usuários ADMIN podem acessar o formulário
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("postDto", new PostDTO());
        return "new_post";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String savePost(@ModelAttribute PostDTO postDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(user);
        postService.savePost(post);
        return "redirect:/blog";
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        return "redirect:/blog";
    }
}