package com.neubert.tobias.spring.datajdbc.springdatajdbc;

import com.neubert.tobias.spring.datajdbc.springdatajdbc.environment.PostgresContainerExtension;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.foo.Foo;
import com.neubert.tobias.spring.datajdbc.springdatajdbc.foo.FooRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith({PostgresContainerExtension.class})
class SpringDataJdbcApplicationTests {
  @Autowired
  private FooRepository fooRepository;

  @BeforeAll
  static void init() {
    System.setProperty("spring.datasource.url", PostgresContainerExtension.CONTAINER.getJdbcUrl());
  }

  @Test
  void saveFoo() {
    Foo foo = new Foo("foo");

    Foo saved = fooRepository.save(foo);
    assertThat(saved.id()).isNotNull();
    assertThat(saved.name()).isEqualTo("foo");

    Optional<Foo> found = fooRepository.findById(saved.id());
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(saved.id());
    assertThat(found.get().name()).isEqualTo(foo.name());
  }
}
