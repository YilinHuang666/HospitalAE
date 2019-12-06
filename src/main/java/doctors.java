import java.io.Serializable;

public class doctors extends Person implements Serializable {
    String workload;
    boolean availability;
    String email;
    public doctors(int id, String firstname, String lastname, String identitynumber, String email, String workload, boolean availability) {
        super(id, firstname, lastname, identitynumber);
        this.workload=workload;
        this.availability=availability;
        this.email=email;
    }
    public String getWorkload(){return workload;}
    public boolean getAvailability(){return availability;}
    public String getEmail(){return email;}
}
