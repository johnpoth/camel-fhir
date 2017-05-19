package org.apache.camel.component.fhir;

import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * The fhir producer. It simply retrieves a {@link IClientExecutable}, executes it and sets the result in the Camel exchange body.
 */
public class FhirProducer extends DefaultProducer {
    
    public static final String FHIR_REQUEST_EXECUTABLE_HEADER_NAME = "CamelFhirRequest";

    public FhirProducer(FhirEndpoint endpoint) {
        super(endpoint);
    }

    public void process(Exchange exchange) throws Exception {
        IClientExecutable clientExecutable = (IClientExecutable) exchange.getIn().getHeaders().get(FHIR_REQUEST_EXECUTABLE_HEADER_NAME);
        Object result = clientExecutable.execute();
        exchange.getOut().setBody(result);
    }

}
