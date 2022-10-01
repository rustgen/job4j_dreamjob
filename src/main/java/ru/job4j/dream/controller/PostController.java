package ru.job4j.dream.controller;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PostStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PostController {

    private final PostStore store = PostStore.instOf();

    @GetMapping("/formAddPost")
    public String posts(Model model) {
        model.addAttribute("post", new Post(0, "Junior Java Job",
                "Soft develop", LocalDateTime.now()));
        return "addPost";
    }
}
