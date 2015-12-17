package org.neo4j.ft;

import org.json.JSONObject;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.database.CypherExecutor;

import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by laeg on 17/12/2015.
 */
@javax.ws.rs.Path("/write")
public class writer {

    private final GraphDatabaseService graphDatabaseService;


    public writer(@Context GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    @POST
    @Produces("application/json")
    @javax.ws.rs.Path("/person/{uuid}/{name}/{facesetId}/{birthyear}/{salutation}")
    public Response get(@Context CypherExecutor cypherExecutor, @PathParam("uuid") final String uuid, @PathParam("name") final String name, @PathParam("facesetId") final String facesetId, @PathParam("birthyear") final String birthYear, @PathParam("salutation") final String salutation) {
        JSONObject jObj = new JSONObject();
        Node node = null;

        try (Transaction tx = graphDatabaseService.beginTx()) {

            String queryString = "MERGE (p:Person:Concept {uuid: {uuid} })" +
                    " SET p += {  name:{name} , factsetIdentifier: {factsetIdentifier} , birthYear:{birthYear} , salutation:{salutation} } RETURN p ";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("uuid", uuid);
            parameters.put("name", name);
            parameters.put("factsetIdentifier", facesetId);
            parameters.put("birthYear", birthYear);
            parameters.put("salutation", salutation);
            ResourceIterator<Node> resultIterator = graphDatabaseService.execute(queryString, parameters).columnAs("p");
            node = resultIterator.next();

            if (node != null) {
                jObj.put("uuid", node.getProperty("uuid"));
                jObj.put("name", node.getProperty("name"));
                jObj.put("factsetIdentifier", node.getProperty("factsetIdentifier"));
                jObj.put("birthYear", node.getProperty("birthYear"));
                jObj.put("salutation", node.getProperty("salutation"));

            }
            tx.success();
        }


        return Response.ok().entity(jObj.toString()).build();
    }
}
