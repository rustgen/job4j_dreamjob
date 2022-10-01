package ru.job4j.dream.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CandidateStore;

import java.time.LocalDateTime;

@Controller
public class CandidateController {

    private final CandidateStore candidatesStore = CandidateStore.instOf();

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candidatesStore.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String formAddCandidate(Model model) {
        model.addAttribute("candidate", new Candidate(0, "Fill in the field",
                "Fill in the field", LocalDateTime.now()));
        return "addCandidate";
    }
}
