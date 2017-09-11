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
    private SimpleStringProperty CompName;
    private SimpleStringProperty addr;
    private SimpleStringProperty phno;
    private SimpleStringProperty email;

    public Supplier(String sid, String name, String CompName, String addr, String phno, String email) {
        this.sid = new SimpleStringProperty(sid);
        this.name =new SimpleStringProperty( name);
        this.CompName =new SimpleStringProperty( CompName);
        this.addr =new SimpleStringProperty( addr);
        this.phno =new SimpleStringProperty( phno);
        this.email =new SimpleStringProperty( email);
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
    //company name getter and setter
    
    public String getCompName() {
        return CompName.get();
    }

    public void setCompName(String CompName) {
        this.CompName.set(CompName);
    }
    
    public StringProperty compNameProperty(){
        return this.CompName;
    }
    
    //address getter and setter
    
    public String getAddr() {
        return addr.get();
    }

    public void setAddr(String addr) {
        this.addr.set(addr);
    }
    
     public StringProperty addrProperty(){
        return this.addr;
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
