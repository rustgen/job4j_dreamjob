package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDBStore;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;

@Service
@ThreadSafe
public class CandidateService {

    private final CandidateDBStore store;

    public CandidateService(CandidateDBStore store) {
        this.store = store;
    }

    public Collection<Candidate> findAll() {
        return store.findAll();
    }

    public Candidate add(Candidate candidate) {
        return store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public void update(Candidate candidate) {
        store.update(candidate);
    }
}
