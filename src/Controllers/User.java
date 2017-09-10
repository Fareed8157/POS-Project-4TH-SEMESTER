/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class User {
    private final ReadOnlyStringWrapper userName;
    private StringProperty pass;

    public User() {
       userName = new ReadOnlyStringWrapper(this, "userName", "ABC");
       pass = new SimpleStringProperty(this, "password", "");
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
