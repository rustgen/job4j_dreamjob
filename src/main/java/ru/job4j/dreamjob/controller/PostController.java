package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PostController {

    private final PostStore store = PostStore.instOf();

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", store.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String formAddPost(Model model) {
        model.addAttribute("post", new Post(0, "Fill in the field",
                "Fill in the field", LocalDateTime.now()));
        return "addPost";
    }
}
