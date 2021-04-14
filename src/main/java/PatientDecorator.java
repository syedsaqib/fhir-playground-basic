import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.text.SimpleDateFormat;

public class PatientDecorator {
    private static final SimpleDateFormat DOB_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Patient patient;

    public PatientDecorator(Patient patient) {
        this.patient = patient;
    }

    public String getFirstName() {
        return (patient == null  || patient.getName() == null || patient.getName().size() == 0)
                ? null
                : patient.getName().stream().findFirst()
                        .filter(name -> name.getGiven() != null && name.getGiven().size() > 0)
                        .map(name -> name.getGiven().get(0).toString()).orElse(null);
    }

    public String getLastName() {
        return (patient == null || patient.getName() == null || patient.getName().size() == 0)
                ? null
                : patient.getName().stream().findFirst()
                    .map(HumanName::getFamily).orElse(null);
    }

    public String getDob() {
        return patient == null || patient.getBirthDate() == null
                ? ""
                : DOB_FORMAT.format(patient.getBirthDate());
    }
}
