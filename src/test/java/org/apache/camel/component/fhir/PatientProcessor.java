package org.apache.camel.component.fhir;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.PID;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Patient precessor that converts the Patient segment of a {@link ORU_R01} message into a FHIR dtsu2 {@link Patient}.
 * It then creates a Patient {@link IClientExecutable} request to be processed by the {@link FhirProducer}.
 */
public class PatientProcessor implements Processor {
    private final Logger log = LoggerFactory.getLogger(PatientProcessor.class.getName());
    private final IGenericClient client;
    private final CamelContext camelContext;

    public PatientProcessor(IGenericClient client, CamelContext camelContext) {
        this.client = client;
        this.camelContext = camelContext;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ORU_R01 msg = exchange.getIn().getBody(ORU_R01.class);
        //map to Patient
        Patient patient = getPatient(msg);
        log.debug("Mapped Dtsu2 patient {}", camelContext.getTypeConverter().convertTo(String.class, exchange, patient));
        final IClientExecutable clientExecutable = client.create().resource(patient).prefer(PreferReturnEnum.REPRESENTATION);
        exchange.getIn().getHeaders().put(FhirProducer.FHIR_REQUEST_EXECUTABLE_HEADER_NAME,clientExecutable);
    }

    private Patient getPatient(ORU_R01 msg) {
        Patient patient = new Patient();
        final PID pid = msg.getPATIENT_RESULT().getPATIENT().getPID();
        String surname = pid.getPatientName()[0].getFamilyName().getFn1_Surname().getValue();
        String name = pid.getPatientName()[0].getGivenName().getValue();
        String patientId = msg.getPATIENT_RESULT().getPATIENT().getPID().getPatientID().getCx1_ID().getValue();
        patient.addName()
            .addGiven(name);
        patient.getNameFirstRep().addFamily(surname);
        patient.setId(patientId);
        return patient;
    }
}
