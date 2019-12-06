import java.io.Serializable;

public class patients extends Person implements Serializable {
    String age, notes;
    boolean admit_status;
    String phonenumber;
    public patients(int id, String firstname, String lastname, String phonenumber, String identitynumber, String age, String notes, boolean admit_status) {
        super(id, firstname, lastname, identitynumber);
        this.age=age;
        this.notes=notes;
        this.admit_status=admit_status;
        this.phonenumber=phonenumber;
    }
    public String getAge(){return age;}
    public String getNotes(){return notes;}
    public boolean getAdmit_status(){return admit_status;}
    public String  getPhonenumber(){return phonenumber;}
}
