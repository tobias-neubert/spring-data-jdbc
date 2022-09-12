package com.neubert.tobias.spring.datajdbc.springdatajdbc.environment;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;


public class PostgresContainerExtension implements AfterAllCallback, BeforeAllCallback {
  private static final Logger LOGGER = LoggerFactory.getLogger(PostgresContainerExtension.class);
  public static final PostgreSQLContainer<?> CONTAINER =
    new PostgreSQLContainer<>(DockerImageName.parse("postgres:10.7"))
      .withLabel("TestContainer", "true")
      .withImagePullPolicy(imageName -> true)
      .withNetwork(Network.SHARED)
      .withNetworkAliases("postgres")
      .withDatabaseName("foo")
      .withUsername("foo")
      .withPassword("foo")
      .withClasspathResourceMapping("init-sql/", "/docker-entrypoint-initdb.d/", BindMode.READ_ONLY)
      .withExposedPorts(5432)
      .withLogConsumer(new Slf4jLogConsumer(LOGGER));

  PostgresContainerExtension() { }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    CONTAINER.start();
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    CONTAINER.stop();
  }
}