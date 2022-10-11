package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
@ThreadSafe
public class UserDBStore {

    private static final Logger LOG = LogManager.getLogger(UserDBStore.class.getName());
    private static final String ADD = """
    INSERT INTO users (email, password) VALUES (?, ?)
    """;
    private static final String FIND = """
    SELECT * FROM users WHERE email = ? AND password = ?
    """;

    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> optional = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
            optional = Optional.of(user);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return optional;
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Optional<User> optional = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet id = ps.executeQuery()) {
                if (id.next()) {
                    User user = new User(
                            id.getInt("id"),
                            id.getString("email"),
                            id.getString("password")
                    );
                    optional = Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return optional;
    }
}

