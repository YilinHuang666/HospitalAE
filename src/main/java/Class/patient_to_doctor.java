package Class;

//this class is used for the communication between the local app and the web page. it stores the assigned patient's name
//and the corresponding doctor's name.
public class patient_to_doctor {
    String patient_firstname;
    String patient_lastname;
    String responsible_doctor_firstname;
    String responsible_doctor_lastname;
    public patient_to_doctor(String patient_firstname, String patient_lastname, String responsible_doctor_firstname, String responsible_doctor_lastname){
        this.patient_firstname=patient_firstname;
        this.patient_lastname=patient_lastname;
        this.responsible_doctor_firstname=responsible_doctor_firstname;
        this.responsible_doctor_lastname=responsible_doctor_lastname;
    }

    public String getPatient_firstname(){
        return patient_firstname;
    }

    public String getPatient_lastname(){
        return patient_lastname;
    }
    public String getResponsible_doctor_firstname(){
        return responsible_doctor_firstname;
    }
    public String getResponsible_doctor_lastname(){
        return responsible_doctor_lastname;
    }
}
