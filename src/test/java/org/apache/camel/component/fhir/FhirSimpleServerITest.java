package org.apache.camel.component.fhir;

import java.io.File;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Starts a simple Fhir server.
 */
public abstract class FhirSimpleServerITest extends CamelTestSupport{

    private static Server server;
    static FhirContext fhirContext;
    static IGenericClient client;

    @AfterClass
    public static void afterClass() throws Exception {
        if (server != null) {
            server.stop();
        }
    }


    @BeforeClass
    public static void beforeClass() throws Exception {
        Integer port = AvailablePortFinder.getNextAvailable();
        server = new Server(port);

        String base = "http://localhost:" + port + "/fhir";

        WebAppContext root = new WebAppContext();
        root.setAllowDuplicateFragmentNames(true);

        root.setWar(new File("target/restful-server-example.war").toURI().toString());
        root.setContextPath("/");
        root.setParentLoaderPriority(false);
        root.setCopyWebInf(true);
        root.setCopyWebDir(true);

        server.setHandler(root);

        server.start();

        fhirContext = FhirContext.forDstu2();
        client = fhirContext.newRestfulGenericClient(base);
    }

}