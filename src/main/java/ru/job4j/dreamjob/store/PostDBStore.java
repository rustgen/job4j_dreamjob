package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class PostDBStore {

    private static final Logger LOG = LogManager.getLogger(PostDBStore.class.getName());
    private static final String FIND_ALL = """
    SELECT * FROM post
    """;
    private static final String ADD = """
            INSERT INTO post(name, description, created, visible, city_id) VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE post SET name = ?, description = ?, created = ?, visible = ?, city_id = ?, where id = ?
            """;
    private static final String FIND_BY_ID = """
    SELECT * FROM post WHERE id = ?
    """;

    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Post post = getSqlPostParam(it);
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setSqlPostParam(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE)) {
            setSqlPostParam(ps, post);
            ps.setInt(6, post.getId());
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return getSqlPostParam(it);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private void setSqlPostParam(PreparedStatement ps, Post post) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
        ps.setBoolean(4, post.isVisible());
        ps.setInt(5, post.getCity().getId());
    }

    private Post getSqlPostParam(ResultSet it) throws SQLException {
        Post post = new Post(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime(),
                new City(it.getInt("city_id"), null)
        );
        post.setVisible(it.getBoolean("visible"));
        return post;
    }
}

