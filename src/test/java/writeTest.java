import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;
import java.net.URI;

/**
 * Created by laeg on 17/12/2015.
 */



public class writeTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule().withExtension("/ft", "org.neo4j.ft");


    @Test
    public void shouldTestExtensionIsAlive() {

        URI serverURI = neo4j.httpsURI();

        // http://localhost:7474/ft/person/{uuid}/{name}/{facesetId}/{birthyear}/{salutation}
        HTTP.Response res = HTTP.POST(serverURI.resolve("/ft/write/person/12345asd/lukeg/anw1/1991/none").toString());

        System.out.println(res.rawContent());
    }

}
