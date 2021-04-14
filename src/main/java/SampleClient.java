import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SampleClient {
    private static final Logger log = LoggerFactory.getLogger(SampleClient.class);

    public static void main(String[] theArgs) throws IOException {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new StopWatchInterceptor());

        List<String> lastNames = getLastNamesFromFile("names.txt");

        // Search for Patient resources base on lastName from file
        lastNames.forEach(lastName -> {
            log.info("\n--> Searching on lastName={}", lastName);
            Bundle response = client
                    .search()
                    .forResource("Patient")
                    .where(Patient.FAMILY.matches().value(lastName))
                    .sort().ascending("given")
                    .returnBundle(Bundle.class)
                    .execute();

            log.info("First Name    |   Last Name  |    DOB   ");
            log.info("----------------------------------------");
            response.getEntry()
                    .stream()
                    .map(Bundle.BundleEntryComponent::getResource)
                    .map(r -> (Patient) r)
                    .map(PatientDecorator::new)
                    .forEach(decorator -> log.info("{}               |  {}          |  {}", decorator.getFirstName(),
                            decorator.getLastName(), decorator.getDob()));
        });
    }

    private static List<String> getLastNamesFromFile(String fileName) throws IOException {
        File file = new File(fileName);

        try (Stream<String> linesStream = Files.lines(file.toPath())) {
            return linesStream.collect(Collectors.toList());
        }
    }

}
