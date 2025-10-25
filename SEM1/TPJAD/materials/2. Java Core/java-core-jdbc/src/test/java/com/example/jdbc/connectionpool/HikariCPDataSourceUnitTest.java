package com.example.jdbc.connectionpool;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.SQLException;



public class HikariCPDataSourceUnitTest {
    
    @Test
    public void givenHikariDataSourceClass_whenCalledgetConnection_thenCorrect() throws SQLException {
        assertTrue(HikariCPDataSource.getConnection().isValid(1));
    }   
}