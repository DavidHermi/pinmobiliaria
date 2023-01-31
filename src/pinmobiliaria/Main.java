package pinmobiliaria;

import java.io.*;
import java.sql.*;

public class Main {

    public static Connection conexion() throws SQLException {
        String driver = "jdbc:postgresql:";
        String host = "//localhost:";
        String porto = "5432";
        String sid = "postgres";
        String usuario = "dam2a";
        String password = "castelao";
        String url = driver + host + porto + "/" + sid;
        Connection conn = DriverManager.getConnection(url, usuario, password);
        return conn;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        File archivoLec = new File("/home/dam2a/Escritorio/listap");
        FileInputStream lectura = new FileInputStream(archivoLec);
        ObjectInputStream lec = new ObjectInputStream(lectura);
        FileWriter esc = new FileWriter("listap.txt");



        Listap aux;

        String serializable = "" ;
        while ((aux = (Listap) lec.readObject()) != null) {
            System.out.println(aux.toString());
            String nif = aux.getNifp();

            int contadorpisos = 0;
            Statement instruccionesNif = conexion().createStatement();
            ResultSet rsNif;
            Statement instruccionesCodz = conexion().createStatement();
            ResultSet rsCodz;
            int operacionParcial = 0;
            int totalfinal = 0;
            rsNif = instruccionesNif.executeQuery("SELECT * FROM pisos WHERE nif = " + "'" + nif + "'" + ";");

            while (rsNif.next()) {
                int m2 = rsNif.getInt("m2");
                System.out.println("metros : " + m2);
                String codz = rsNif.getString("codz");

                rsCodz = instruccionesCodz.executeQuery("SELECT * FROM zonas WHERE codz=" + "'" + codz + "'" + ";");
                while (rsCodz.next()) {

                    int prezom2 = rsCodz.getInt("prezom2");
                    System.out.println("prezo m2 : " + prezom2);
                    operacionParcial = m2 * prezom2;
                    System.out.println("Total parcial : " + operacionParcial);
                    totalfinal = operacionParcial + totalfinal;
                    // totalfinal += operacionParcial
                    contadorpisos++;
                }
            }
            System.out.println("NumeroPisos: " +contadorpisos+ " totalfinal: " + totalfinal);
            serializable += nif +">"+ contadorpisos + ">" + totalfinal +"\n" ;

        }
        esc.write(serializable);
        esc.flush();
        lectura.close();
    }


}



