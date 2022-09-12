package com.neubert.tobias.spring.datajdbc.springdatajdbc.foo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface FooRepository extends CrudRepository<Foo, UUID> {
}
