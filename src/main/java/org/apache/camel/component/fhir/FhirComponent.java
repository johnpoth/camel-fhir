package org.apache.camel.component.fhir;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link FhirEndpoint}.
 */
public class FhirComponent extends UriEndpointComponent {
    
    public FhirComponent() {
        super(FhirEndpoint.class);
    }

    public FhirComponent(CamelContext context) {
        super(context, FhirEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new FhirEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
