package org.anvard.introtojava.jdbc;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.anvard.introtojava.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class test_verificate {

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement s;

    @Mock
    private ResultSet resultset;

    private doctor_login_info doctor_login_info;


    @Before
    public void setUp() throws Exception {
        //assertNotNull(ds);
        when(conn.prepareStatement(any(String.class))).thenReturn(s);
        doctor_login_info = new doctor_login_info();
        doctor_login_info.passw(1);
        doctor_login_info.usern(2);

        when(resultset.first()).thenReturn(true);
        when(resultset.getInt(1)).thenReturn(1);
        when(resultset.getString(2)).thenReturn(doctor_login_info.getpassw());
        when(resultset.getString(3)).thenReturn(doctor_login_info.getusern());
        when(s.executeQuery()).thenReturn(resultset);
    }









}
