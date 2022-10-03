package ru.job4j.dreamjob.controller;

import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String formAddPost(Model model) {
        model.addAttribute("post", new Post(0, "Fill in the field",
                "Fill in the field", LocalDateTime.now()));
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        postService.add(post);
        return "redirect:/posts";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        postService.update(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postService.findById(id));
        return "updatePost";
    }
}
