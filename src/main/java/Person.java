import java.io.Serializable;

public class Person implements Serializable {
    int id;
    String firstname, lastname, identitynumber, email;

    public Person(int id, String firstname, String lastname, String identitynumber, String email){
        this.id=id;
        this.firstname=firstname;
        this.lastname=lastname;
        this.identitynumber=identitynumber;
        this.email=email;
    }

    public int getId(){return id;}
    public String getFirstname(){return firstname;}
    public String getLastname(){ return lastname;}
    public String getIdentitynumber(){return identitynumber;}
    public String getEmail(){return email;}

}
