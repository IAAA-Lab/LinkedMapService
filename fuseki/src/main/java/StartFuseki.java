import org.apache.jena.fuseki.cmd.FusekiCmd;

public class StartFuseki {
    public static void main(String args[]) {
        FusekiCmd.main("--update", "--loc=../rdf/TDB", "/ds");
    }
}
