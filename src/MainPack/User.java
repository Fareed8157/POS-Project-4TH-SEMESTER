/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class User {
    private ReadOnlyStringWrapper userName;
    private StringProperty pass;
    private SimpleIntegerProperty id;
    private SimpleStringProperty uname;
   // private SimpleStringProperty password;
    
     public User(Integer id, String uname,String pass) {
      this.id=new SimpleIntegerProperty(id);
      this.uname=new SimpleStringProperty(uname);
      this.pass=new SimpleStringProperty(pass);
   }
     
    public User() {
       userName = new ReadOnlyStringWrapper(this, "userName", "ABC");
       pass = new SimpleStringProperty(this, "password", "");
   }
    //id getter and setter
    public Integer getId(){
        return this.id.get();
    }
    public void setId(Integer id){
        this.id.set(id);
    }
    public IntegerProperty idProperty(){
        return this.id;
    }
    
    //name getter and setter
    public String getUname(){
        return this.uname.get();
    }
    public void setUname(String  uname){
        this.uname.set(uname);
    }
    public StringProperty unameProperty(){
        return this.uname;
    }
    
    
    
    public final String getUserName(){
        return userName.get();
    }
    public ReadOnlyStringProperty userNameProperty(){
       return  userName.getReadOnlyProperty();
    }
    public String getPass(){
       return pass.get();
    }
    public StringProperty passProperty(){
        if(pass==null){
           pass=new SimpleStringProperty("");
       }
        return pass;
    }
}
