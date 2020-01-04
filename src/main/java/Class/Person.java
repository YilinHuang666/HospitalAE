package Class;

import java.io.Serializable;

//Person class has the common parameter in both patient and doctor class
public class Person implements Serializable {
    int id;
    String firstname, lastname, identitynumber;

    public Person(int id, String firstname, String lastname, String identitynumber){
        this.id=id;
        this.firstname=firstname;
        this.lastname=lastname;
        this.identitynumber=identitynumber;
    }

    public int getId(){return id;}
    public String getFirstname(){return firstname;}
    public String getLastname(){ return lastname;}
    public String getIdentitynumber(){return identitynumber;}
}
