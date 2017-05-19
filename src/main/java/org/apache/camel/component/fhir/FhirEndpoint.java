package org.apache.camel.component.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a fhir endpoint.
 */
@UriEndpoint(scheme = "fhir", title = "fhir", syntax="fhir://?client=#fhirClient&context=#fhirContext", label = "fhir")
public class FhirEndpoint extends DefaultEndpoint {
    
    @UriPath(name = "client", description = "the Fhir client reference to use") @Metadata(required = "true")
    private IGenericClient client;
    @UriParam(name = "context", description = "the Fhir context reference to use") @Metadata(required = "true")
    private FhirContext context;

    public FhirEndpoint(String uri, FhirComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new FhirProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Fhir Consumers aren't supported yet.");
    }

    public boolean isSingleton() {
        return true;
    }

    public IGenericClient getClient() {
        return client;
    }

    public void setClient(IGenericClient client) {
        this.client = client;
    }

    public FhirContext getContext() {
        return context;
    }

    public void setContext(FhirContext context) {
        this.context = context;
    }
}
