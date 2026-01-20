package com.example.jpa.postgresql_schema;

import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostgresqlSchemaLiveTest {

    public static final String POSTGRES_USER = "postgres";
    public static final String POSTGRES_PASSWORD = "password";
    public static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/my_db";

    @BeforeClass
    public static void setup() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("user", POSTGRES_USER);
        properties.setProperty("password", POSTGRES_PASSWORD);
        Connection connection = DriverManager.getConnection(CONNECTION_URL, properties);
        connection.createStatement().execute("DROP TABLE store.product");
        connection.createStatement().execute("DROP SCHEMA store");
        connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS store");
        connection.createStatement().execute("CREATE TABLE store.product(id SERIAL PRIMARY KEY, name VARCHAR(20))");
        connection.createStatement().execute("INSERT INTO store.product VALUES(1, 'test product')");
    }

    @Test
    public void settingUpSchemaUsingJdbcURL() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("user", POSTGRES_USER);
        properties.setProperty("password", POSTGRES_PASSWORD);
        Connection connection = DriverManager.getConnection(CONNECTION_URL, properties);

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM store.product");
        resultSet.next();

        assertThat(resultSet.getInt(1), equalTo(1));
        assertThat(resultSet.getString(2), equalTo("test product"));
    }

    @Test
    public void settingUpSchemaUsingPGSimpleDataSource() throws Exception {
        int port = Integer.parseInt(CONNECTION_URL.substring(CONNECTION_URL.lastIndexOf(":") + 1, CONNECTION_URL.lastIndexOf("/")));
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{"localhost"});
        ds.setPortNumbers(new int[]{port});
        ds.setUser(POSTGRES_USER);
        ds.setPassword(POSTGRES_PASSWORD);
        ds.setDatabaseName("my_db");
        ds.setCurrentSchema("store");

        ResultSet resultSet = ds.getConnection().createStatement().executeQuery("SELECT * FROM product");
        resultSet.next();

        assertThat(resultSet.getInt(1), equalTo(1));
        assertThat(resultSet.getString(2), equalTo("test product"));
    }

//    @Test
//    public void settingUpSchemaUsingTableAnnotation() {
//        Map<String, String> props = new HashMap<>();
//        props.put("hibernate.connection.url", CONNECTION_URL);
//        props.put("hibernate.connection.user", POSTGRES_USER);
//        props.put("hibernate.connection.password", POSTGRES_PASSWORD);
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresql_schema_unit", props);
//        EntityManager entityManager = emf.createEntityManager();
//
//        Product product = entityManager.find(Product.class, 1);
//
//        assertThat(product.getName(), equalTo("test product"));
//    }
}
