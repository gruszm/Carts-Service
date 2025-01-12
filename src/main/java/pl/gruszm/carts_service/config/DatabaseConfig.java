package pl.gruszm.carts_service.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig
{
    private static final String DB_NAME = "carts_db";
    private static final String DB_PORT = "5432";

    @Bean
    public DataSource getDataSource()
    {
        String dbUsername = System.getenv("CARTS_DB_USER");
        String dbPassword = System.getenv("CARTS_DB_PASSWORD");
        String dbServiceName = System.getenv("CARTS_DB_SERVICE_NAME");

        System.out.println("Trying to connect to the database with parameters:\n" +
                "\tDB_NAME = " + DB_NAME + "\n" +
                "\tDB_PORT = " + DB_PORT + "\n" +
                "\tdbUsername = " + dbUsername + "\n" +
                "\tdbPassword = " + dbPassword + "\n" +
                "\tdbServiceName = " + dbServiceName);

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(getDataSourceUrl(dbServiceName))
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }

    private String getDataSourceUrl(String dbServiceName)
    {
        return "jdbc:postgresql://"
                + dbServiceName + ":"
                + DB_PORT + "/"
                + DB_NAME;
    }
}
