import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.tdb.sys.TDBInternal;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class crearTDB {
    public static void crearTDB() {
        FileManager fm = FileManager.get();
        fm.addLocatorClassLoader(crearTDB.class.getClassLoader());
        //Cargar unTDB en memoria
        InputStream in = fm.open("./src/resources/pruebaTTLfromBD.nt");

        Location location = Location.create("./TDB");

        // Load some initial data
        TDBLoader.load(TDBInternal.getBaseDatasetGraphTDB(TDBFactory.createDatasetGraph(location)), in, false);

    }
}
