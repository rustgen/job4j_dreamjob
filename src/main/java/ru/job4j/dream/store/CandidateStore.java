package ru.job4j.dream.store;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.job4j.dream.model.Candidate;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Alex",
                "Software Engineer", LocalDateTime.now()));
        candidates.put(2, new Candidate(2, "Kevin",
                "Java / Python Software Developer", LocalDateTime.now()));
        candidates.put(3, new Candidate(3, "Sara",
                "Full stack web developer", LocalDateTime.now()));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
