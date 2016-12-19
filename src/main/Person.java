
package main;

import java.io.Serializable;
public class Person implements Comparable<Person>,Serializable {



    @Override
    public int compareTo(Person o) {
        if (this.name.compareTo(o.name)==0)
        {
            return this.gender.compareTo(o.gender);
        }
        return this.name.compareTo(o.name);
    }
  
    public enum Sex {
        MALE, FEMALE
    }
  
    private String index;
    private String name; 
    private Sex gender;
    private String emailAddress;
  
    Person(String Index,String nameArg,
        Sex genderArg, String emailArg) {
        index = Index;
        name = nameArg;
        gender = genderArg;
        emailAddress = emailArg;
    }  


    public String printPerson() {
      return (this.getIndex() + " " + this.getName() + " "  + this.getGender() + " " + this.getEmailAddress() + "\n");
    }
    
    public Sex getGender() {
        return gender;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String Index) {
        this.index = Index;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    
    
}
