package com.neubert.tobias.spring.datajdbc.springdatajdbc.foo;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.jdbc.core.convert.BasicJdbcConverter;
import org.springframework.data.jdbc.core.convert.DefaultJdbcTypeFactory;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.convert.RelationResolver;
import org.springframework.data.jdbc.core.dialect.JdbcArrayColumns;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.core.mapping.JdbcSimpleTypes;
import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(
  basePackageClasses = {Foo.class},
  jdbcOperationsRef = "fooJdbcTemplate",
  transactionManagerRef = "fooTransactionManager")
@EnableAutoConfiguration(exclude = {
  DataSourceAutoConfiguration.class,
  JdbcRepositoriesAutoConfiguration.class
})
public class FooDbConfig {
  @Bean
  @Qualifier("fooDb")
  public DataSourceProperties fooDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Qualifier("fooDb")
  @FlywayDataSource
  public HikariDataSource fooDataSource(@Qualifier("fooDb") DataSourceProperties dataSourceProperties) {
    HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    return dataSource;
  }

  @Bean
  @Qualifier("fooDb")
  public NamedParameterJdbcTemplate fooJdbcTemplate(@Qualifier("fooDb") DataSource dataSource) {
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    return jdbcTemplate;
  }

  @Bean
  @Qualifier("fooDb")
  public PlatformTransactionManager fooTransactionManager(@Qualifier("fooDb") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  @Qualifier("fooDb")
  public JdbcMappingContext fooJdbcMappingContext(
    Optional<NamingStrategy> namingStrategy,
    JdbcCustomConversions customConversions)
  {
    JdbcMappingContext mappingContext = new JdbcMappingContext(namingStrategy.orElse(NamingStrategy.INSTANCE));
    mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());

    return mappingContext;
  }

  @Bean
  @Qualifier("fooDb")
  public Dialect fooJdbcDialect(@Qualifier("fooDb") NamedParameterJdbcOperations operations) {
    return DialectResolver.getDialect(operations.getJdbcOperations());
  }

  @Bean
  @Qualifier("fooDb")
  public JdbcCustomConversions jdbcCustomConversions(@Qualifier("fooDb") Dialect dialect) {
    SimpleTypeHolder simpleTypeHolder =
      dialect.simpleTypes().isEmpty()
        ? JdbcSimpleTypes.HOLDER
        : new SimpleTypeHolder(dialect.simpleTypes(), JdbcSimpleTypes.HOLDER);

    return new JdbcCustomConversions(
      CustomConversions.StoreConversions.of(simpleTypeHolder, storeConverters(dialect)), userConverters());
  }

  @Bean
  public JdbcConverter jdbcConverter(
    @Qualifier("fooDb") JdbcMappingContext mappingContext,
    @Qualifier("fooDb") NamedParameterJdbcOperations operations,
    @Lazy RelationResolver relationResolver,
    JdbcCustomConversions conversions,
    @Qualifier("fooDb") Dialect dialect)
  {
    JdbcArrayColumns arrayColumns =
      dialect instanceof JdbcDialect
        ? ((JdbcDialect) dialect).getArraySupport()
        : JdbcArrayColumns.DefaultSupport.INSTANCE;
    DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(operations.getJdbcOperations(), arrayColumns);

    return new BasicJdbcConverter(
      mappingContext, relationResolver, conversions, jdbcTypeFactory, dialect.getIdentifierProcessing());
  }

  private List<?> userConverters() {
    return Collections.emptyList();
  }

  private List<Object> storeConverters(Dialect dialect) {
    List<Object> converters = new ArrayList<>();
    converters.addAll(dialect.getConverters());
    converters.addAll(JdbcCustomConversions.storeConverters());
    return converters;
  }
}
