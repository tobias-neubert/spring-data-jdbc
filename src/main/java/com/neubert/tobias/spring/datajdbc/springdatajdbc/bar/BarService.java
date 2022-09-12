package com.neubert.tobias.spring.datajdbc.springdatajdbc.bar;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;


@Service
public class BarService {
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public BarService(@Qualifier("barJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Bar> findAll() {
    return jdbcTemplate.query(
      "SELECT * FROM bar",
      (rs, rowNum) -> new Bar((UUID) rs.getObject("id"), rs.getString("name"))
    );
  }

  @Transactional(transactionManager = "barTransactionManager")
  public Bar createBar(String name) {
    jdbcTemplate.update(
      "INSERT INTO bar (name) VALUES (:name) ON CONFLICT (name) DO UPDATE SET name = :name",
      Map.of("name", name)
    );

    return findByName(name).orElseThrow(() -> new RuntimeException(format("Bar named %s could not be created", name)));
  }

  public Optional<Bar> findByName(String name) {
    return Optional.ofNullable(
      jdbcTemplate.queryForObject(
        "SELECT * FROM bar WHERE name = :name",
        Map.of("name", name),
        (rs, row) -> new Bar((UUID) rs.getObject("id"), rs.getString("name"))
      ));
  }
}
