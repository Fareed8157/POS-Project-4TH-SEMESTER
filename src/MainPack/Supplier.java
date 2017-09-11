/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class Supplier {
    private SimpleStringProperty sid;
    private SimpleStringProperty name;
    private SimpleStringProperty lname;
    private SimpleStringProperty phno;
    private SimpleStringProperty email;

    public Supplier(String sid, String name, String lname, String email, String phno) {
        this.sid = new SimpleStringProperty(sid);
        this.name =new SimpleStringProperty( name);
        this.lname =new SimpleStringProperty( lname);
        this.email =new SimpleStringProperty( email);
        this.phno =new SimpleStringProperty( phno);
    }

    //sid getter and setter
    public String getSid() {
        return sid.get();
    }

    public void setSid(String sid) {
        this.sid.set(sid);
    }
    
    public StringProperty sidProperty(){
        return this.sid;
    }
   
    //name getter and setter
    public String getName() {
        return name.get();
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
   
    public StringProperty nameProperty(){
        return this.name;
    }
    //address getter and setter
    
    public String getLname() {
        return lname.get();
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }
    
     public StringProperty lnameProperty(){
        return this.lname;
    }
    //phone no getter and setter
     
    public String getPhno() {
        return phno.get();
    }

    public void setPhno(String phno) {
        this.phno.set(phno);
    }
    
    public StringProperty phnoProperty(){
        return this.phno;
    }
    
    //email getter and setter
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
    
    public StringProperty emailProperty(){
        return this.email;
    }
}
