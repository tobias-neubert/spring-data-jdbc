package com.neubert.tobias.spring.datajdbc.springdatajdbc.bar;

import org.springframework.data.annotation.Id;

import java.util.UUID;


public record Bar(@Id UUID id, String name) {
  public Bar() {
    this(null, null);
  }
  public Bar(String name) {
    this(null, name);
  }

  public Bar withId(UUID id) {
    return new Bar(id, name());
  }

  public Bar withName(String name) {
    return new Bar(id(), name);
  }
}