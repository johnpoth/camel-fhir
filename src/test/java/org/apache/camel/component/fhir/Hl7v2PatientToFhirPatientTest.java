package org.apache.camel.component.fhir;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Test;

/**
 * This test demonstrates how to convert a HL7V2 patient to a FHIR dtsu2 Patient.
 */
public class Hl7v2PatientToFhirPatientTest extends FhirSimpleServerITest {

    /*          Segment	Purpose	        FHIR Resource
        MSH	    Message header	        MessageHeader
        PID	    Patient Identification	Patient
        PV1	    Patient Visit	        Not used in this example
        PV2	    Patient Visit           – Additional data	Not used in this example
        ORC	    Common Order	        Not used in this example
        OBR	    Observation             Request	Observation
        OBX	    Observation	            ObservationProvider
        
        See https://fhirblog.com/2014/10/05/mapping-hl7-version-2-to-fhir-messages for more information
    */
    private static final String HL7_MESSAGE = "MSH|^~\\&|Amalga HIS|BUM|New Tester|MS|20111121103141||ORU^R01|2847970-201111211031|P|2.4|||AL|NE|764|ASCII|||\r" +
        "PID||100005056|100005056||Dasher^Mary^\"\"^^\"\"|\"\"|19810813000000|F||CA|Street 1^\"\"^\"\"^\"\"^34000^SGP^^\"\"~\"\"^\"\"^\"\"^\"\"^Danling Street 5th^THA^^\"\"||326-2275^PRN^PH^^66^675~476-5059^ORN^CP^^66^359~(123)456-7890^ORN^FX^^66^222~^NET^X.400^a@a.a~^NET^X.400^dummy@hotmail.com|(123)456-7890^WPN^PH^^66|UNK|S|BUD||BP000111899|D99999^\"\"||CA|Bangkok|||THA||THA|\"\"|N\r" +
        "PV1||OPD   ||||\"\"^\"\"^\"\"||||CNSLT|||||C|VIP|||6262618|PB1||||||||||||||||||||||||20101208134638\r" +
        "PV2|||^Unknown|\"\"^\"\"||||\"\"|\"\"|0||\"\"|||||||||||||||||||||||||||||HP1\r" +
        "ORC|NW|\"\"|BMC1102771601|\"\"|CM||^^^^^\"\"|||||||||\"\"^\"\"^^^\"\"\r" +
        "OBR|1|\"\"|BMC1102771601|\"\"^Brain (CT)||20111028124215||||||||||||||||||CTSCAN|F||^^^^^ROUTINE|||\"\"||||||\"\"|||||||||||^\"\"\r" +
        "OBX|1|FT|\"\"^Brain (CT)||++++ text of report goes here +++|||REQAT|||FINAL|||20111121103040||75929^Gosselin^Angelina";
    private HL7DataFormat hl7 = new HL7DataFormat();
    
    @Test
    public void testUnmarshalWithExplicitUTF16Charset() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:unmarshal");
        // Message with explicit encoding in MSH
        String charset = "ASCII";
        byte[] body = HL7_MESSAGE.getBytes(Charset.forName(charset));
        template.sendBodyAndHeader("direct:unmarshal", new ByteArrayInputStream(body), Exchange.CHARSET_NAME, charset);

        final MethodOutcome results = (MethodOutcome) mock.getExchanges().get(0).getIn().getBody();
        Patient patient = (Patient) results.getResource();
        assertNotNull(client.read(patient.getId()));
        assertEquals("Dasher",patient.getNameFirstRep().getFamily().get(0).getValue());
    }

    protected RouteBuilder createRouteBuilder() throws Exception {

        return new RouteBuilder() {
            public void configure() throws Exception {
                
                getContext().getRegistry(JndiRegistry.class).bind("fhirClient", client);
                getContext().getRegistry(JndiRegistry.class).bind("fhirContext", fhirContext);

                Processor patientProcessor = new PatientProcessor(client, getContext());
                from("direct:unmarshal")
                    .unmarshal(hl7)
                    .setProperty(FhirTypeConverter.FHIR_CONTEXT_EXCHANGE_PROPERTY_NAME, constant(fhirContext))
                    .process(patientProcessor)
                    .to("fhir://?client=#fhirClient&context=#fhirContext")
                    .log("Received ${body}")
                    .to("mock:unmarshal");
            }
        };
    }
}
