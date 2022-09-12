package com.neubert.tobias.spring.datajdbc.springdatajdbc.bar;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
public class BarDbConfig {
  @Bean
  @Qualifier("barDb")
  @ConfigurationProperties("spring.datasource.bar")
  public DataSourceProperties barDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Qualifier("barDb")
  @ConfigurationProperties("spring.datasource.bar.hikari")
  public HikariDataSource barDataSource(@Qualifier("barDb") DataSourceProperties dataSourceProperties) {
    HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    return dataSource;
  }

  @Bean
  @Qualifier("barDb")
  public NamedParameterJdbcTemplate barJdbcTemplate(@Qualifier("barDb") DataSource dataSource) {
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    return jdbcTemplate;
  }

  @Bean
  @Qualifier("barDb")
  public PlatformTransactionManager barTransactionManager(@Qualifier("barDb") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
