package org.apache.camel.component.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class FhirTypeConverter {

    private FhirTypeConverter() {
        // Helper class
    }

    @Converter
    public static String toString(IBaseResource iBaseResource, Exchange exchange){
        FhirVersionEnum fhirVersionEnum = exchange.getProperty(FhirVersionEnum.class.getName(), FhirVersionEnum.class);
        FhirContext fhirContext = FhirContextsHolder.getFhirContext(fhirVersionEnum);
        return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(iBaseResource);
    }
    
}
