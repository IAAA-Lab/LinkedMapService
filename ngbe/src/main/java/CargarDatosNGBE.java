import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Carga los datos en la BD directamente exportando los datos proporcionados por el NGBE a un
 * fichero txt
 **/
public class CargarDatosNGBE {
    private Connection con;
    private Statement s;

    public CargarDatosNGBE(Connection con, Statement s) {
        this.con = con;
        this.s = s;
    }

    public static void main(String args[]) {
        try {


            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://127.0.0.1:5432/NGBE";
            Connection con = DriverManager.getConnection(url, "user", "user");
            Statement s = con.createStatement();
            System.out.println("Crea conexion BD");


            String fuente = "/home/elena/Desktop/NGBEv2013/datos.txt";

            CargarDatosNGBE ecd = new CargarDatosNGBE(con, s);
            ecd.procesar(fuente);
            System.out.println("FIN");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void leerfichero(String archivo) throws IOException, SQLException {
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        Integer id;
        String nombre_extendido;
        String iditneficador_geografico;
        String nombre_alternativo_2;
        String nombre_alternativo_3;
        String nombre_variante_1;
        String nombre_variante_2;
        String nombre_variante_3;
        String fuente_nombre_extendido;
        String fuente_identificador_geografico;
        String fuente_alternativo_2;
        String fuente_alternativo_3;
        String fuente_variante_1;
        String fuente_variante_2;
        String fuente_variante_3;
        String idioma_nombre_extendido;
        String idioma_identificador_geografico;
        String idioma_alternativo_2;
        String idioma_alternativo_3;
        String idioma_variante_1;
        String idioma_variante_2;
        String idioma_variante_3;
        String estatus_nombre_extendido;
        float longitud_etrs89_regcan95;
        float latitud_etrs89_regcan95;
        Integer huso_etrs89_regcan95;
        float xutm_etrs89_regcan95;
        float yutm_etrs89_regcan95;
        String sistema_referencia_etrs89_regcan95;
        String hojamtn_25;
        String codigo_ine;
        String codigo_ngbe;

        String linea;
        String aux, aux2;

        int cont = 0;
        while (cont < 11) {
            // while ((linea = br.readLine()) != null) {
            cont++;
            linea = br.readLine();
            System.out.println(linea);

            String[] procesar = linea.split(";");

            id = Integer.parseInt(procesar[0]);
            nombre_extendido = procesar[1];
            iditneficador_geografico = procesar[2];
            nombre_alternativo_2 = procesar[3];
            nombre_alternativo_3 = procesar[4];
            nombre_variante_1 = procesar[5];
            nombre_variante_2 = procesar[6];
            nombre_variante_3 = procesar[7];
            fuente_nombre_extendido = procesar[8];
            fuente_identificador_geografico = procesar[9];
            fuente_alternativo_2 = procesar[10];
            fuente_alternativo_3 = procesar[11];
            fuente_variante_1 = procesar[12];
            fuente_variante_2 = procesar[13];
            fuente_variante_3 = procesar[14];
            idioma_nombre_extendido = procesar[15];
            idioma_identificador_geografico = procesar[16];
            idioma_alternativo_2 = procesar[17];
            idioma_alternativo_3 = procesar[18];
            idioma_variante_1 = procesar[19];
            idioma_variante_2 = procesar[20];
            idioma_variante_3 = procesar[21];
            estatus_nombre_extendido = procesar[22];
            //longitud
            if (procesar[23].startsWith("-")) {
                aux = procesar[23].substring(1);
                aux2 = aux.replace(",", ".");
                longitud_etrs89_regcan95 = Float.parseFloat(aux2);
                longitud_etrs89_regcan95 = -longitud_etrs89_regcan95;
            } else {
                aux = procesar[23];
                aux2 = aux.replace(",", ".");
                longitud_etrs89_regcan95 = Float.parseFloat(aux2);
            }
            //latitud
            if (procesar[24].startsWith("-")) {
                aux = procesar[24].substring(1);
                aux2 = aux.replace(",", ".");
                latitud_etrs89_regcan95 = Float.parseFloat(aux2);
                latitud_etrs89_regcan95 = -latitud_etrs89_regcan95;
            } else {
                aux = procesar[24];
                aux2 = aux.replace(",", ".");
                latitud_etrs89_regcan95 = Float.parseFloat(aux2);
            }

            huso_etrs89_regcan95 = Integer.parseInt(procesar[25]);
            //XUTM
            if (procesar[26].startsWith("-")) {
                aux = procesar[26].substring(1);
                aux2 = aux.replace(",", ".");
                xutm_etrs89_regcan95 = Float.parseFloat(aux2);
                xutm_etrs89_regcan95 = -xutm_etrs89_regcan95;
            } else {
                aux = procesar[26];
                aux2 = aux.replace(",", ".");
                xutm_etrs89_regcan95 = Float.parseFloat(aux2);
            }
            //YUTM
            if (procesar[27].startsWith("-")) {
                aux = procesar[27].substring(1);
                aux2 = aux.replace(",", ".");
                yutm_etrs89_regcan95 = Float.parseFloat(aux2);
                yutm_etrs89_regcan95 = -yutm_etrs89_regcan95;
            } else {
                aux = procesar[27];
                aux2 = aux.replace(",", ".");
                yutm_etrs89_regcan95 = Float.parseFloat(aux2);
            }

            sistema_referencia_etrs89_regcan95 = procesar[28];
            hojamtn_25 = procesar[35];
            codigo_ine = procesar[36];
            codigo_ngbe = procesar[37];


            insertar(id,
                    nombre_extendido,
                    iditneficador_geografico,
                    nombre_alternativo_2,
                    nombre_alternativo_3,
                    nombre_variante_1,
                    nombre_variante_2,
                    nombre_variante_3,
                    fuente_nombre_extendido,
                    fuente_identificador_geografico,
                    fuente_alternativo_2,
                    fuente_alternativo_3,
                    fuente_variante_1,
                    fuente_variante_2,
                    fuente_variante_3,
                    idioma_nombre_extendido,
                    idioma_identificador_geografico,
                    idioma_alternativo_2,
                    idioma_alternativo_3,
                    idioma_variante_1,
                    idioma_variante_2,
                    idioma_variante_3,
                    estatus_nombre_extendido,
                    longitud_etrs89_regcan95,
                    latitud_etrs89_regcan95,
                    huso_etrs89_regcan95,
                    xutm_etrs89_regcan95,
                    yutm_etrs89_regcan95,
                    sistema_referencia_etrs89_regcan95,
                    hojamtn_25,
                    codigo_ine,
                    codigo_ngbe);


        }

    }

    private void procesar(String fuente) throws IOException, SQLException {

        leerfichero(fuente);

    }


    private void insertar(Integer id,
                          String nombre_extendido,
                          String iditneficador_geografico,
                          String nombre_alternativo_2,
                          String nombre_alternativo_3,
                          String nombre_variante_1,
                          String nombre_variante_2,
                          String nombre_variante_3,
                          String fuente_nombre_extendido,
                          String fuente_identificador_geografico,
                          String fuente_alternativo_2,
                          String fuente_alternativo_3,
                          String fuente_variante_1,
                          String fuente_variante_2,
                          String fuente_variante_3,
                          String idioma_nombre_extendido,
                          String idioma_identificador_geografico,
                          String idioma_alternativo_2,
                          String idioma_alternativo_3,
                          String idioma_variante_1,
                          String idioma_variante_2,
                          String idioma_variante_3,
                          String estatus_nombre_extendido,
                          float longitud_etrs89_regcan95,
                          float latitud_etrs89_regcan95,
                          Integer huso_etrs89_regcan95,
                          float xutm_etrs89_regcan95,
                          float yutm_etrs89_regcan95,
                          String sistema_referencia_etrs89_regcan95,
                          String hojamtn_25,
                          String codigo_ine,
                          String codigo_ngbe) throws SQLException {


        PreparedStatement statement = con.prepareStatement("INSERT INTO ngbev2013(id," +
                " nombre_extendido,"
                + " iditneficador_geografico,"
                + "  nombre_alternativo_2,"
                + "  nombre_alternativo_3,"
                + "  nombre_variante_1,"
                + "  nombre_variante_2,"
                + "  nombre_variante_3,"
                + "  fuente_nombre_extendido,"
                + "  fuente_identificador_geografico,"
                + "  fuente_alternativo_2,"
                + "  fuente_alternativo_3,"
                + "  fuente_variante_1,"
                + "  fuente_variante_2,"
                + "  fuente_variante_3,"
                + " idioma_nombre_extendido,"
                + "idioma_identificador_geografico,"
                + "  idioma_alternativo_2,"
                + "  idioma_alternativo_3,"
                + "  idioma_variante_1,"
                + "  idioma_variante_2,"
                + "  idioma_variante_3,"
                + "  estatus_nombre_extendido,"
                + "  longitud_etrs89_regcan95,"
                + "  latitud_etrs89_regcan95,"
                + "  huso_etrs89_regcan95,"
                + "  xutm_etrs89_regcan95,"
                + "  yutm_etrs89_regcan95,"
                + "  sistema_referencia_etrs89_regcan95,"
                + " hojamtn_25,"
                + "  codigo_ine,"
                + "  codigo_ngbe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


        statement.setInt(1, id);
        statement.setString(2, nombre_extendido);
        statement.setString(3, iditneficador_geografico);
        statement.setString(4, nombre_alternativo_2);
        statement.setString(5, nombre_alternativo_3);
        statement.setString(6, nombre_variante_1);
        statement.setString(7, nombre_variante_2);
        statement.setString(8, nombre_variante_3);
        statement.setString(9, fuente_nombre_extendido);
        statement.setString(10, fuente_identificador_geografico);
        statement.setString(11, fuente_alternativo_2);
        statement.setString(12, fuente_alternativo_3);
        statement.setString(13, fuente_variante_1);
        statement.setString(14, fuente_variante_2);
        statement.setString(15, fuente_variante_3);
        statement.setString(16, idioma_nombre_extendido);
        statement.setString(17, idioma_identificador_geografico);
        statement.setString(18, idioma_alternativo_2);
        statement.setString(19, idioma_alternativo_3);
        statement.setString(20, idioma_variante_1);
        statement.setString(21, idioma_variante_2);
        statement.setString(22, idioma_variante_3);
        statement.setString(23, estatus_nombre_extendido);
        statement.setFloat(24, longitud_etrs89_regcan95);
        statement.setFloat(25, latitud_etrs89_regcan95);
        statement.setInt(26, huso_etrs89_regcan95);
        statement.setFloat(27, xutm_etrs89_regcan95);
        statement.setFloat(28, yutm_etrs89_regcan95);
        statement.setString(29, sistema_referencia_etrs89_regcan95);
        statement.setString(30, hojamtn_25);
        statement.setString(31, codigo_ine);
        statement.setString(32, codigo_ngbe);


        statement.execute();
    }
}
