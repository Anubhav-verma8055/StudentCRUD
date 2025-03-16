package com.example.school.Config;

import com.example.school.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig  extends AbstractRoutingDataSource {

    @Autowired
    private TenantService tenantService;
    @Bean
    public DataSource dataSource() {
        Map<Object, Object> tenantDataSources = new HashMap<>();

        // Load existing tenants and create DataSources
        tenantService.getAllTenants().forEach(tenant -> {
            DataSource dataSource = createTenantDataSource(tenant.getDatabaseName());
            tenantDataSources.put(tenant.getId(), dataSource);
        });

        // Set up routing DataSource
        AbstractRoutingDataSource routingDataSource = new MultiTenantConfig();
        routingDataSource.setTargetDataSources(tenantDataSources);
        routingDataSource.setDefaultTargetDataSource(createTenantDataSource("default"));
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }


    private DataSource createTenantDataSource(String dbName) {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:./data/" + dbName + ";DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE")
                .username("sa")
                .password("")
                .build();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenantId();
    }
}

