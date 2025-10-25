package com.example.jdbc.connectionpool;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class BasicConnectionPoolUnitTest {
    
    private static ConnectionPool connectionPool;

    @BeforeEach
    public void setUpBasicConnectionPoolInstance() throws SQLException {
        connectionPool = BasicConnectionPool.create("jdbc:h2:mem:test", "user", "password");
    }
    
    @Test
    public void givenBasicConnectionPoolInstance_whenCalledgetConnection_thenCorrect() throws Exception {
        assertTrue(connectionPool.getConnection().isValid(1));
    }
    
    @Test
    public void givenBasicConnectionPoolInstance_whenCalledreleaseConnection_thenCorrect() throws Exception {
        Connection connection = connectionPool.getConnection();
        assertThat(connectionPool.releaseConnection(connection)).isTrue();
    }
    
    @Test
    public void givenBasicConnectionPoolInstance_whenCalledgetUrl_thenCorrect() {
        assertThat(connectionPool.getUrl()).isEqualTo("jdbc:h2:mem:test");
    }
    
    @Test
    public void givenBasicConnectionPoolInstance_whenCalledgetUser_thenCorrect() {
        assertThat(connectionPool.getUser()).isEqualTo("user");
    }
    
    @Test
    public void givenBasicConnectionPoolInstance_whenCalledgetPassword_thenCorrect() {
        assertThat(connectionPool.getPassword()).isEqualTo("password");
    }

    @Test
    public void givenBasicConnectionPoolInstance_whenAskedForMoreThanMax_thenError() throws Exception {
        assertThrows(RuntimeException.class, () -> {
            // code that throws RuntimeException
            // this test needs to be independent so it doesn't share the same connection pool as other tests
            ConnectionPool cp = BasicConnectionPool.create("jdbc:h2:mem:test", "user", "password");
            final int MAX_POOL_SIZE = 20;
            for (int i = 0; i < MAX_POOL_SIZE + 1; i++) {
                cp.getConnection();
            }
            fail();
        });
    }

    @Test
    public void givenBasicConnectionPoolInstance_whenSutdown_thenEmpty() throws Exception {
        BasicConnectionPool cp = BasicConnectionPool.create("jdbc:h2:mem:test", "user", "password");
        assertThat(cp.getSize()).isEqualTo(10);

        cp.shutdown();
        assertThat(cp.getSize()).isEqualTo(0);
    }
}
