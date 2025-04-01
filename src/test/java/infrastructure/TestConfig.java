package infrastructure;

import domain.GetPriceForADateService;
import domain.PriceRepository;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration
@EnableWebMvc
public class TestConfig {

  @Bean
  public PriceRepository priceRepository(JpaPriceRepository jpaPriceRepository) {
    return new SpringPriceRepository(jpaPriceRepository);
  }

  @Bean
  public GetPriceForADateService getPriceForADateService(PriceRepository priceRepository) {
    return new application.GetPriceForADateService(priceRepository);
  }

  @Bean
  public DataSource dataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("org.h2.Driver");
    dataSourceBuilder.url("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    dataSourceBuilder.username("SA");
    dataSourceBuilder.password("");
    return dataSourceBuilder.build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setPackagesToScan("infrastructure");
    factory.setDataSource(dataSource);
    factory.setJpaVendorAdapter(vendorAdapter);
    return factory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory);
    return txManager;
  }
}
