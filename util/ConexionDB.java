import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static ConexionDB instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotecadigital";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    private conexionDB() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static ConexionDB getInstance() throws SQLException {
        if (instance == null){
            synchronized (ConexionDB.class){
                if(instance == null) {
                    instance new ConexionDB();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}