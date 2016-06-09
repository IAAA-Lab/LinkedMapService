import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Hace un clean de los datos exportados de la BD NGBE, y genera un fichero cvs válido para cargarlo
 * en la BD del proyecto
 **/
public class crearCVSngbe {
    private Connection con;
    private Statement s;

    public crearCVSngbe(Connection con, Statement s) {
        this.con = con;
        this.s = s;
    }

    public static void main(String args[]) {
        Properties properties = new Properties();
        try {

            //   String fuente = "/home/elena/Desktop/NGBEv2013/datos.txt";
            String url = "jdbc:postgresql://127.0.0.1:5432/NGBE";
            Connection con = DriverManager.getConnection(url, "user", "user");
            Statement s = con.createStatement();
            String fuente = "C:/Users/Elena/Desktop/NGBEv2013.txt";
            crearCVSngbe ecd = new crearCVSngbe(con, s);
            ecd.procesar(fuente);
            System.out.println("FIN");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
        }
    }

    public void leerfichero(String archivo) throws IOException, SQLException {
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        FileWriter salida = new FileWriter("C:/Users/Elena/Desktop/datosClean.txt");
        PrintWriter fw = new PrintWriter(salida);
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
        String cabecera;
        int cont = 0;
        int cont2 = 0;
        aux = br.readLine();
        String[] procesarCabecera = aux.split(";");
        cabecera = procesarCabecera[0];
        cont++;
        while (cont < 29) {
            aux2 = cabecera + ";" + procesarCabecera[cont];
            cont++;
            cabecera = aux2;
        }
        cont = 35;
        while (cont < 38) {
            aux2 = cabecera + ";" + procesarCabecera[cont];
            cont++;
            cabecera = aux2;
        }
        fw.println(cabecera);
        cont = 0;

        while ((linea = br.readLine()) != null) {
            cont++;
            if (cont == 1000) {
                cont2++;
                System.out.println(cont2 + "X" + cont);
                cont = 0;
            }
            //linea = br.readLine();
            String[] procesar = linea.split(";");
            aux = procesar[0];
            aux2 = aux.replace(",", ".");
            id = (int) Float.parseFloat(aux2);
            //System.out.println("ID:"+id);
            nombre_extendido = procesar[1];
            //System.out.println("NOMBRE_EXTENDIDO:"+nombre_extendido);
            iditneficador_geografico = procesar[2];
            //System.out.println("ID_GEO:"+iditneficador_geografico);
            nombre_alternativo_2 = procesar[3];
            //System.out.println("N_ALT2:"+nombre_alternativo_2);
            nombre_alternativo_3 = procesar[4];
            //System.out.println("N_ALT3:"+nombre_alternativo_3);
            nombre_variante_1 = procesar[5];
            //System.out.println("N_V1:"+nombre_variante_1);
            nombre_variante_2 = procesar[6];
            //System.out.println("N_V2:"+nombre_variante_2);
            nombre_variante_3 = procesar[7];
            //System.out.println("N_V3:"+nombre_variante_3);
            fuente_nombre_extendido = procesar[8];
            //System.out.println("F_N_EXT:"+fuente_nombre_extendido);
            fuente_identificador_geografico = procesar[9];
            //System.out.println("F_ID_GEO:"+fuente_identificador_geografico);
            fuente_alternativo_2 = procesar[10];
            //System.out.println("F_A_2:"+fuente_alternativo_2);
            fuente_alternativo_3 = procesar[11];
            //System.out.println("F_A_3:"+fuente_alternativo_3);
            fuente_variante_1 = procesar[12];
            //System.out.println("F_V_1:"+fuente_variante_1);
            fuente_variante_2 = procesar[13];
            //System.out.println("F_V_2:"+fuente_variante_2);
            fuente_variante_3 = procesar[14];
            //System.out.println("F_V_3:"+fuente_variante_3);
            idioma_nombre_extendido = procesar[15];
            //System.out.println("I_N_E:"+idioma_nombre_extendido);
            idioma_identificador_geografico = procesar[16];
            //System.out.println("I_I_G:"+idioma_identificador_geografico);
            idioma_alternativo_2 = procesar[17];
            //System.out.println("I_A_2:"+idioma_alternativo_2);
            idioma_alternativo_3 = procesar[18];
            //System.out.println("I_A_3:"+idioma_alternativo_3);
            idioma_variante_1 = procesar[19];
            //System.out.println("I_V_1:"+idioma_variante_1);
            idioma_variante_2 = procesar[20];
            //System.out.println("I_V_2:"+idioma_variante_2);
            idioma_variante_3 = procesar[21];
            //System.out.println("I_V_3:"+idioma_variante_3);
            estatus_nombre_extendido = procesar[22];
            //System.out.println("E_N_E:"+estatus_nombre_extendido);
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
            //System.out.println("LONG:"+longitud_etrs89_regcan95);
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
            //System.out.println("LAT:"+latitud_etrs89_regcan95);
            huso_etrs89_regcan95 = Integer.parseInt(procesar[25]);
            //System.out.println("HUSO:"+huso_etrs89_regcan95);
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
            //System.out.println("XUTM:"+xutm_etrs89_regcan95);
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
            //System.out.println("YUTM:"+yutm_etrs89_regcan95);
            sistema_referencia_etrs89_regcan95 = procesar[28];
            //System.out.println("SIST_REF:"+sistema_referencia_etrs89_regcan95);
            hojamtn_25 = procesar[35];
            //System.out.println("HOJA:"+hojamtn_25);
            codigo_ine = procesar[36];
            //System.out.println("COD_INE:"+codigo_ine);
            codigo_ngbe = procesar[37];
            //System.out.println("COD_NGBE:"+codigo_ngbe);

            fw.println(id + ";" +
                    nombre_extendido + ";" +
                    iditneficador_geografico + ";" +
                    nombre_alternativo_2 + ";" +
                    nombre_alternativo_3 + ";" +
                    nombre_variante_1 + ";" +
                    nombre_variante_2 + ";" +
                    nombre_variante_3 + ";" +
                    fuente_nombre_extendido + ";" +
                    fuente_identificador_geografico + ";" +
                    fuente_alternativo_2 + ";" +
                    fuente_alternativo_3 + ";" +
                    fuente_variante_1 + ";" +
                    fuente_variante_2 + ";" +
                    fuente_variante_3 + ";" +
                    idioma_nombre_extendido + ";" +
                    idioma_identificador_geografico + ";" +
                    idioma_alternativo_2 + ";" +
                    idioma_alternativo_3 + ";" +
                    idioma_variante_1 + ";" +
                    idioma_variante_2 + ";" +
                    idioma_variante_3 + ";" +
                    estatus_nombre_extendido + ";" +
                    longitud_etrs89_regcan95 + ";" +
                    latitud_etrs89_regcan95 + ";" +
                    huso_etrs89_regcan95 + ";" +
                    xutm_etrs89_regcan95 + ";" +
                    yutm_etrs89_regcan95 + ";" +
                    sistema_referencia_etrs89_regcan95 + ";" +
                    hojamtn_25 + ";" +
                    codigo_ine + ";" +
                    codigo_ngbe);
        }
        salida.close();
    }

    private void procesar(String fuente) throws IOException, SQLException {

        leerfichero(fuente);

    }

}
