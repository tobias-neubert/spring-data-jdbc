package com.neubert.tobias.spring.datajdbc.springdatajdbc.foo;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(basePackageClasses = {Foo.class})
public class FooDbConfig extends AbstractJdbcConfiguration {
  @Bean
  public HikariDataSource statusDataSource(DataSourceProperties dataSourceProperties) {
    return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }
}
