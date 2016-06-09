package com.lms.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Clase que conecta con la BD
 **/ //Modificar ahora solo es un main que conecta
public class ConexionBD {
    public static void actualizrBD(String query) {
        String driver = "org.postgresql.Driver";
        String connectString = "jdbc:postgresql://localhost:5432/NGBE";
        String user = "postgres";
        String password = "pass";

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user, password);
            Statement stmt = con.createStatement();
            // ResultSet rs;

            stmt.execute(query);
            //rs=stmt.executeQuery(query); --Cuando la consulta tiene que devolver resultado
    /*while (rs.next()){
        System.out.println("resultado: " + rs.getString("nombre"));
		}*/
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

