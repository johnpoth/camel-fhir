package org.apache.camel.component.fhir;

import static ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
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

    public static FhirContext getDstu1FhirContext() {
        if (dstu3FhirContext == null) {
            synchronized(DSTU1) {
                if (dstu1FhirContext == null) {
                    dstu1FhirContext = FhirContext.forDstu1();
                }
            }
        }
        return dstu1FhirContext;
    }

    public static FhirContext getFhirContext( FhirVersionEnum fhirVersionEnum) {
//        if (fhirVersionEnum == null) {
//            //TODO use Java property for default?
//            return getDstu3FhirContext();
//        } else {
            switch (fhirVersionEnum){
                case DSTU1:
                    return getDstu1FhirContext();
                default: return getDstu3FhirContext();
            }
    }
}
