import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .sort().ascending("given")
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("First Name    |   Last Name  |    DOB   ");
        System.out.println("----------------------------------------");
        final String format = "%-14s | %14s | %10s";
        response.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map( r -> (Patient) r)
                .map(PatientDecorator::new)
                .forEach( decorator -> System.out.println(String.format(format, decorator.getFirstName(),
                                                            decorator.getLastName(), decorator.getDob())));
    }

}
