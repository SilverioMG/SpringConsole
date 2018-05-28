package net.atopecode.springconsole;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.Properties;

//Esta clase expone los métodos necesarios para crear los Beans que permiten trabajar con 'SpringData'.
//Hay que registrar en el 'mvcContext' de la clase 'MyWebAppInitializer' todas las clases 'config' que crean los 'beans'
//del inyector de dependencias de Spring. Es decir, todas las clases que utilicen el decorador '@Configuration' igual que esta.
@Configuration
@ComponentScan("net.atopecode")
@EnableJpaRepositories(basePackages = {"net.atopecode"}) //Paquete inicial desde el que debe empezar a escanear repositorios.
@EnableTransactionManagement
public class RepositoryConfig {
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(){
        EntityManagerFactory factory = entityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.FALSE);


        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("net.atopecode");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", PostgreSQL9Dialect.class.getName());
        jpaProperties.put("hibernate.connection.pool_size","10");
        jpaProperties.put("hibernate.format_sql", false);
        jpaProperties.put("hibernate.max_fetch_depth",5);
        jpaProperties.put("hibernate.jdbc.batch_size",1000);

        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory;
    }


    @Bean(destroyMethod = "close")
    public javax.sql.DataSource dataSource () {
        //Se establece la conexión a la B.D.
        String DB_URL = "jdbc:postgresql://localhost:5432/test";
        String DB_USERNAME = "postgres";
        String DB_PASSWORD = "postgres";

        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");

        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        dataSource.setInitialSize(10);
        dataSource.setMaxActive(100);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(10);

        dataSource.setDefaultAutoCommit(Boolean.TRUE);
        dataSource.setLogValidationErrors(true);

        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationInterval(30);

        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(120000);

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(300);
        dataSource.setLogAbandoned(false);

        dataSource.setJmxEnabled(true);

        dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); //Transaction.READ_UNCOMMITTED);

        return dataSource;
    }



//    @Bean(name = "entityManager")
//    public EntityManager entityManager() {
//
//        return entityManagerFactory().getObject().createEntityManager();
//    }

}