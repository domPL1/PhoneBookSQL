
package main;

import java.io.Serializable;
import java.util.Date;

public class Person implements Comparable<Person>,Serializable {



    @Override
    public int compareTo(Person o) {
        if (this.name.compareTo(o.name)==0){
            return this.birthday.compareTo(o.birthday);
        }
        return this.name.compareTo(o.name);
    }
  
    public enum Sex {
        MALE, FEMALE
    }
  
    private String index;
    private String name; 
    private Date birthday;
    private Sex gender;
    private String emailAddress;
  
    Person(String Index,String nameArg, Date birthdayArg,
        Sex genderArg, String emailArg) {
        index = Index;
        name = nameArg;
        birthday = birthdayArg;
        gender = genderArg;
        emailAddress = emailArg;
    }  


    public String printPerson() {
      return (this.getIndex() + " " + this.getName() + " " + this.getBirthday() + " " + this.getGender() + " " + this.getEmailAddress() + "\n");
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
    
    public Date getBirthday() {
        return birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    
    
}
