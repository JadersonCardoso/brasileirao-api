package br.com.brasileirao.api.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.hikari.pool-name}")
    private String poolName;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private Integer minimumIdle;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long connectionTimeout;
    @Value("${spring.datasource.hikari.idle-timeout}")
    private Long idleTimeout;
    @Value("${spring.datasource.hikari.max-lifetime}")
    private Long maxlifetime;

    @Bean
    public DataSource getDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(this.driverClassName);
        config.setJdbcUrl(this.url);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setPoolName(this.poolName);
        config.setMinimumIdle(this.minimumIdle);
        config.setMaximumPoolSize(this.maximumPoolSize);
        config.setConnectionTimeout(this.connectionTimeout);
        config.setIdleTimeout(this.idleTimeout);
        config.setMaxLifetime(this.maxlifetime);
        return new HikariDataSource(config);
    }
}
