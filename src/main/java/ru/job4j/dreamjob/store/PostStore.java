package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(4);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Soft develop", LocalDateTime.now(), new City(1, "Paris")));
        posts.put(2, new Post(2, "Middle Java Job",
                "Strong develop", LocalDateTime.now(), new City(2, "Dubai")));
        posts.put(3, new Post(3, "Senior Java Job",
                "Hard develop", LocalDateTime.now(), new City(3, "Tokyo")));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post add(Post post) {
        post.setId(atomicInteger.getAndIncrement());
        return posts.putIfAbsent(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }
}

