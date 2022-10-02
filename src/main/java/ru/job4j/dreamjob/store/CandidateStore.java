package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(4);

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

    public Candidate add(Candidate candidate) {
        candidate.setId(atomicInteger.getAndIncrement());
        return candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidates.computeIfPresent(candidate.getId(), (integer, c) -> candidate);
    }
}
