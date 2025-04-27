package com.example.school.Config;

import com.example.school.model.Tenant;
import com.example.school.multitenancy.TenantContext;
import com.example.school.repository.TenantRepository;
import com.example.school.service.TenantService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableJpaRepositories(basePackages = "com.example.school.repository")
public class MultiTenantDataSourceConfig {

    @Autowired
    private Environment env;

    @Autowired
    private TenantRepository tenantRepository; // ✅ inject repository directly


    @Autowired
    private TenantService tenantService;

    // Central Application Database Configuration (MySQL)
    // Main data source (used for tenant-specific databases)
    @Primary
    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource dataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return TenantContext.getCurrentTenant();  // Retrieves the tenant from the context
            }
        };

        Map<Object, Object> targetDataSources = new HashMap<>();
        List<Tenant> tenants = tenantRepository.findAll(); // ✅ fetch tenants directly
        tenants.forEach(tenant -> {
            DataSource tenantDataSource = createTenantDataSource(tenant);
            targetDataSources.put(tenant.getDatabaseName(), tenantDataSource);
        });

        // Default data source (if no tenant selected)
        Tenant defaultTenant = tenantRepository.findByUsername("defaultTenant")
                .orElseThrow(() -> new RuntimeException("Default tenant not found"));
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(createTenantDataSource(defaultTenant));

        return dataSource;
    }


    // Creates a data source for the tenant
    private DataSource createTenantDataSource(Tenant tenant) {
        // You can dynamically create DataSource based on tenant details here (like the URL, username, password)
        String url = "jdbc:h2:file:./data/tenants/" + tenant.getDatabaseName();
        return new org.apache.tomcat.jdbc.pool.DataSource() {{
            setUrl(url);
            setDriverClassName("org.h2.Driver");
            setUsername("sa");
            setPassword("");
        }};
    }

    // EntityManagerFactory Configuration
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.example.school.model");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaPropertyMap(Map.of(
                "hibernate.hbm2ddl.auto", "update",
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect"
        ));
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    // TransactionManager Configuration
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


}
