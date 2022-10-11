package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class CandidateDBStore {

    private static final Logger LOG = LogManager.getLogger(CandidateDBStore.class.getName());
    private static final String FIND_ALL = """
                                   SELECT * FROM candidate
                                   """;
    private static final String ADD = """
             INSERT INTO candidate(name, description, created, photo) VALUES (?, ?, ?, ?)
             """;
    private static final String UPDATE = """
            UPDATE candidate SET name = ?, description = ?, created = ?, photo = ?, where id = ?
            """;
    private static final String FIND_BY_ID = """
    SELECT * FROM candidate WHERE id = ?
    """;

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate candidate = getSqlCandidateParam(it);
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setSqlCandidateParam(ps, candidate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE)) {
            setSqlCandidateParam(ps, candidate);
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return getSqlCandidateParam(it);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private void setSqlCandidateParam(PreparedStatement ps, Candidate candidate) throws SQLException {
        ps.setString(1, candidate.getName());
        ps.setString(2, candidate.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
        ps.setBytes(4, candidate.getPhoto());
    }

    private Candidate getSqlCandidateParam(ResultSet it) throws SQLException {
        Candidate candidate = new Candidate(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime()
        );
        candidate.setPhoto(it.getBytes("photo"));
        return candidate;
    }
}

