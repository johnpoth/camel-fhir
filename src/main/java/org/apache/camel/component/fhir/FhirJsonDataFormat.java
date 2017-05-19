package org.apache.camel.component.fhir;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import ca.uhn.fhir.context.FhirContext;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirJsonDataFormat implements DataFormat {

    private final FhirContext fhirContext;

    public FhirJsonDataFormat(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    public void marshal(Exchange exchange, Object o, OutputStream outputStream) throws Exception {
        IBaseResource iBaseResource = exchange.getContext().getTypeConverter().convertTo(IBaseResource.class, exchange, o);
        fhirContext.newJsonParser().encodeResourceToWriter(iBaseResource, new OutputStreamWriter(outputStream));
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream inputStream) throws Exception {
        return fhirContext.newJsonParser().parseResource(new InputStreamReader(inputStream));
    }

}
