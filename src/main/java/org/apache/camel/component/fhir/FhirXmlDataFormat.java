package org.apache.camel.component.fhir;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirXmlDataFormat implements DataFormat {

    @Override
    public void marshal(Exchange exchange, Object o, OutputStream outputStream) throws Exception {
        FhirContext context = getFhirContext(exchange);
        IBaseResource iBaseResource = exchange.getContext().getTypeConverter().convertTo(IBaseResource.class, exchange, o);
        context.newXmlParser().encodeResourceToWriter(iBaseResource, new OutputStreamWriter(outputStream));
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream inputStream) throws Exception {
        FhirContext fhirContext = getFhirContext(exchange);
        return fhirContext.newXmlParser().parseResource(new InputStreamReader(inputStream));
    }

    private FhirContext getFhirContext(Exchange exchange) {
        Set<FhirContext> byType = exchange.getContext().getRegistry().findByType(FhirContext.class);
        if(byType.size() == 0){
            return FhirContextsHolder.getDstu3FhirContext();
        }else{
            return byType.iterator().next();
        }
    }
}
