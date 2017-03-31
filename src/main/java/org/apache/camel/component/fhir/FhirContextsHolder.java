package org.apache.camel.component.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public class FhirContextsHolder {

    //TODO: implement other fhircontexts
    private static volatile FhirContext dstu1FhirContext = null;
    private static volatile FhirContext dstu2FhirContext = null;
    private static volatile FhirContext dstu2Hl7orgFhirContext = null;
    private static volatile FhirContext dstu21hirContext = null;
    private static volatile FhirContext dstu3FhirContext = null;

    private FhirContextsHolder() {}

    public static FhirContext getDstu3FhirContext() {
        if (dstu3FhirContext == null) {
            synchronized(FhirVersionEnum.DSTU3) {
                if (dstu3FhirContext == null) {
                    dstu3FhirContext = FhirContext.forDstu3();
                }
            }
        }
        return dstu3FhirContext;
    }

    public static FhirContext getFhirContext( FhirVersionEnum fhirVersionEnum) {
        if (fhirVersionEnum == null) {
            //TODO use Java property for default?
            return getDstu3FhirContext();
        }
        return dstu3FhirContext;
    }
}
