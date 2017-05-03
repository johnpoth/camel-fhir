//package org.apache.camel.component.fhir;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//
//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.model.api.Bundle;
//import ca.uhn.fhir.rest.client.IGenericClient;
//import org.apache.camel.test.AvailablePortFinder;
//import org.apache.camel.test.junit4.CamelTestSupport;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
//import org.hamcrest.core.StringContains;
//import ca.uhn.fhir.model.dstu2.resource.Patient;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class FhirSimpleServerITest{
//
//    private static Integer ourPort;
//    private static Server ourServer;
//    private static FhirContext ourCtx;
//    private static IGenericClient ourClient;
//
//    @AfterClass
//    public static void afterClass() throws Exception {
//        if (ourServer != null) {
//            ourServer.stop();
//        }
//
//        System.clearProperty("ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS");
//
//    }
//
//    /**
//     * Tests here have some weird windows inconsistency relating to the path for finding the WAR file. Since this test isn't really important to work multiplatform, we can skip it
//     */
//    public static boolean isWindows() {
//        return System.getProperty("os.name").startsWith("Windows");
//    }
//
//    @Test
//    public void test01Search() throws Exception {
//        if (isWindows()) {
//            return;
//        }
//
//        Bundle results = ourClient.search().forResource(Patient.class).execute();
//        assertEquals(1, results.size());
//    }
//
//    @Test
//    public void test02Read() throws Exception {
//        if (isWindows()) {
//            return;
//        }
//
//        Patient results = ourClient.read(Patient.class, "1");
//        assertThat(results.getNameFirstRep().getGivenAsSingleString(), StringContains.containsString("PatientOne"));
//    }
//
//
//
//    @BeforeClass
//    public static void beforeClass() throws Exception {
//        if (isWindows()) {
//            return;
//        }
//
//        if (ourPort != null) {
//            return;
//        }
//
//        ourPort = AvailablePortFinder.getNextAvailable();
//        ourServer = new Server(ourPort);
//
//        String base = "http://localhost:" + ourPort + "/fhir";
//        System.setProperty("ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS", "example , Restful Server Example , " + base);
//
//        WebAppContext root = new WebAppContext();
//        root.setAllowDuplicateFragmentNames(true);
//
//        root.setWar(new File("target/fhir-simple-server/restful-server-example.war").toURI().toString());
//        root.setContextPath("/");
//        root.setAttribute(WebAppContext.BASETEMPDIR, "target/tempextrtact");
//        root.setParentLoaderPriority(false);
//        root.setCopyWebInf(true);
//        root.setCopyWebDir(true);
//
//        ourServer.setHandler(root);
//
//        ourServer.start();
//
//        ourCtx = FhirContext.forDstu2();
//        ourClient = ourCtx.newRestfulGenericClient(base);
//
//    }
//
//}