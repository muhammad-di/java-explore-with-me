package ru.practicum.repository.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.dao.CustomizedEndpointHitRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomizedEndpointHitRepositoryImpl implements CustomizedEndpointHitRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ViewStats> viewStatsForUrisUniqueIps(Collection<String> uris, LocalDateTime start, LocalDateTime end) {
        String urisJoined = String.join(", ", uris);

        String sqlQuery = "select \n" +
                "eh.app as app,\n" +
                "eh.uri as uri,\n" +
                "count(eh.uri) as hits\n" +
                "from (select distinct \n" +
                "\t  e.app,\n" +
                "\t  e.uri,\n" +
                "\t  e.ip\n" +
                "\t  from endpoint_hit e\n" +
                "\t  where \n" +
                "\t  hit_time between ? and ?\n";
        if (uris.isEmpty()) {
            sqlQuery = sqlQuery.concat(
                    "   group by eh.app, eh.uri\n" +
                            "order by hits desc"
            );
            return jdbcTemplate.query(sqlQuery, this::mapRowToDirector, start, end);
        }
        sqlQuery = sqlQuery.concat(
                "   and\n" +
                        "e.uri in (?)) as eh\n" +
                        "group by eh.app, eh.uri\n" +
                        "order by hits desc"
        );

        List<ViewStats> list = jdbcTemplate.query(sqlQuery, this::mapRowToDirector, start, end, urisJoined);
        return list;
    }


    private ViewStats mapRowToDirector(ResultSet rs, Integer rn) throws SQLException {
        String app = rs.getString("app");
        String uri = rs.getString("uri");
        Long hits = rs.getLong("hits");

        return ViewStats.builder()
                .app(app)
                .uri(uri)
                .hits(hits)
                .build();

    }
}