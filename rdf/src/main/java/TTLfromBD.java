

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TTLfromBD {
    public static void main(String[] args) {
        String driver = "org.postgresql.Driver";
        String connectString = "jdbc:postgresql://localhost:5432/NGBE";
        String user = "postgres";
        String password = "pass";

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("select * from random2");
            ArrayList<String> resultadoFila = new ArrayList<String>();

            while (rs.next()) {
                resultadoFila.add(procesarFila(rs));
            }
         
            ModeloCompleto.procesarModelo(resultadoFila);
            ModeloSpatial.procesarFilaSpatial(resultadoFila);

            stmt.close();
            con.close();
            crearTDB.crearTDB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String procesarFila(ResultSet rs) throws SQLException {
        String resultado;
        resultado = comprobarVacio(rs.getString("id")) + ";"
                + comprobarVacio(rs.getString("nombre_extendido")) + ";"
                + comprobarVacio(rs.getString("identificador_geografico")) + ";" +
                comprobarVacio(rs.getString("nombre_alternativo_2")) + ";" +
                comprobarVacio(rs.getString("nombre_alternativo_3")) + ";" +
                comprobarVacio(rs.getString("nombre_variante_1")) + ";" +
                comprobarVacio(rs.getString("nombre_variante_2")) + ";" +
                comprobarVacio(rs.getString("nombre_variante_3")) + ";" +
                comprobarVacio(rs.getString("fuente_nombre_extendido")) + ";" +
                comprobarVacio(rs.getString("fuente_identificador_geografico")) + ";" +
                comprobarVacio(rs.getString("fuente_alternativo_2")) + ";" +
                comprobarVacio(rs.getString("fuente_alternativo_3")) + ";" +
                comprobarVacio(rs.getString("fuente_variante_1")) + ";" +
                comprobarVacio(rs.getString("fuente_variante_2")) + ";" +
                comprobarVacio(rs.getString("fuente_variante_3")) + ";" +
                comprobarVacio(rs.getString("idioma_nombre_extendido")) + ";" +
                comprobarVacio(rs.getString("idioma_identificador_geografico")) + ";" +
                comprobarVacio(rs.getString("idioma_alternativo_2")) + ";" +
                comprobarVacio(rs.getString("idioma_alternativo_3")) + ";" +
                comprobarVacio(rs.getString("idioma_variante_1")) + ";" +
                comprobarVacio(rs.getString("idioma_variante_2")) + ";" +
                comprobarVacio(rs.getString("idioma_variante_3")) + ";" +
                comprobarVacio(rs.getString("estatus_nombre_extendido")) + ";" +
                rs.getDouble("longitud_etrs89_regcan95") + ";" +
                rs.getDouble("latitud_etrs89_regcan95") + ";" +
                rs.getInt("huso_etrs89_regcan95") + ";" +
                rs.getDouble("xutm_etrs89_regcan95")+ ";" +
                rs.getDouble("yutm_etrs89_regcan95") + ";" +
                comprobarVacio(rs.getString("sistema_referencia_etrs89_regcan95")) + ";" +
                comprobarVacio(rs.getString("hojamtn_25")) + ";" +
                comprobarVacio(rs.getString("codigo_ine")) + ";" +
                comprobarVacio(rs.getString("codigo_ngbe"));
        return resultado;

    }
    private static String comprobarVacio(String rs){
    	if (rs==null){
    		rs="";
    	}
		return rs;
    	
    }
}

