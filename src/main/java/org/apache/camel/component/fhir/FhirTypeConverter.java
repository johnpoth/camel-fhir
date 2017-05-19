package org.apache.camel.component.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class FhirTypeConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FhirTypeConverter.class.getName());
    public static final String FHIR_CONTEXT_EXCHANGE_PROPERTY_NAME = "FhirContext";
    
    private FhirTypeConverter() {
        // Helper class
    }

    @Converter
    public static String resourceToString(IBaseResource iBaseResource, Exchange exchange){
        if(exchange == null) {
            LOGGER.warn("Exchange must be preset in order to retrieve FhirContext");
            return null;
        }
        FhirContext fhirContext = exchange.getProperty(FHIR_CONTEXT_EXCHANGE_PROPERTY_NAME, FhirContext.class);
        return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(iBaseResource);
    }

    @Converter
    public static String bundleToString(Bundle bundle, Exchange exchange){
        if(exchange == null) {
            LOGGER.warn("Exchange must be preset in order to retrieve FhirContext");
            return null;
        }
        FhirContext fhirContext = exchange.getProperty(FHIR_CONTEXT_EXCHANGE_PROPERTY_NAME, FhirContext.class);
        return fhirContext.newJsonParser().setPrettyPrint(true).encodeBundleToString(bundle);
    }
    
}
