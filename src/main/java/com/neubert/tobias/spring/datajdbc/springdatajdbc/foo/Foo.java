package com.neubert.tobias.spring.datajdbc.springdatajdbc.foo;

import org.springframework.data.annotation.Id;

import java.util.UUID;


public record Foo(@Id UUID id, String name) {
  public Foo() {
    this(null, null);
  }
  public Foo(String name) {
    this(null, name);
  }

  public Foo withId(UUID id) {
    return new Foo(id, name());
  }

  public Foo withName(String name) {
    return new Foo(id(), name);
  }
}