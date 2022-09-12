package com.neubert.tobias.spring.datajdbc.springdatajdbc;

import com.neubert.tobias.spring.datajdbc.springdatajdbc.bar.Bar;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.bar.BarService;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.environment.PostgresContainerExtension;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.foo.Foo;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.foo.FooRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith({PostgresContainerExtension.class})
class SpringDataJdbcApplicationTests {
  @Autowired
  private FooRepository fooRepository;

  @Autowired
  private BarService barService;

  @Autowired
  @Qualifier("barDb")
  private NamedParameterJdbcTemplate jdbcTemplate;

  @BeforeAll
  static void init() {
    String fooJdbcUrl = PostgresContainerExtension.CONTAINER.getJdbcUrl();
    System.setProperty("spring.datasource.url", format("%s&%s", fooJdbcUrl, "currentSchema=foo"));
    System.setProperty("spring.datasource.bar.url", format("%s&%s", fooJdbcUrl.replaceAll("foo", "bar"), "currentSchema=bar"));
  }

  @Test
  void saveAndReadFoo() {
    Foo foo = new Foo("foo");

    Foo saved = fooRepository.save(foo);
    assertThat(saved.id()).isNotNull();
    assertThat(saved.name()).isEqualTo("foo");

    Optional<Foo> found = fooRepository.findById(saved.id());
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(saved.id());
    assertThat(found.get().name()).isEqualTo(foo.name());

    fooRepository.delete(found.get());
    assertThat(fooRepository.findById(found.get().id())).isEmpty();
  }

  @Test
  void saveAndReadBar() throws SQLException {
    Connection connection = jdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    try(ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})){
      while(resultSet.next()) {
        String tableName = resultSet.getString("TABLE_NAME");
        String remarks = resultSet.getString("REMARKS");
      }
    }



    assertThat(barService.findAll()).isEmpty();

    Bar bar = barService.createBar("tobby");
    assertThat(bar.id()).isNotNull();

    Optional<Bar> tobby = barService.findByName("tobby");
    assertThat(tobby).isPresent();
    assertThat(tobby.get()).isEqualTo(bar);

  }
}
