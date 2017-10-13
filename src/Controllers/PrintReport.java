/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Sale;
import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Fareed
 */
public class PrintReport extends JFrame{
    Connection con=Configs.getConnection();
    String qu;
   HashMap<String,Object> hm;
    ObservableList<Sale> l;
    public PrintReport(String qu,HashMap<String,Object> hm){
        this.hm=hm;
        this.qu=qu;
    }
    
    public void showReport(String invoice){
        try {
            String sourFile="";
            if(invoice.equals("invoice")){
                sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\Report1.jrxml";
            }else if(invoice.equals("dailySale")){
                sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\dailySale.jrxml";
            }else if(invoice.equals("dailySaleByUser")){
                sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\dailySaleByUser.jrxml";
            }else if(invoice.equals("stocksIn")){
               sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\stockIn.jrxml";
            }else if(invoice.equals("stocksOut")){
               sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\stockOut.jrxml";
            }
    
          JasperDesign jasperDesign=JRXmlLoader.load(sourFile);
           JRProperties.setProperty(QueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX+"plsql"
                            ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JasperReport jasperReport=JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,hm,con);
            JRViewer viewer=new JRViewer(jasperPrint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
            } catch (JRException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }
    public void showReport(){
        String sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\inventory.jrxml";
         
            JasperReport jasperReport=null;
        try {
            jasperReport = JasperCompileManager.compileReport(sourFile);
            JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,null,con);
            JRViewer viewer=new JRViewer(jasperPrint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(PrintReport.class.getName()).log(Level.SEVERE, null, ex);
        }
            
       } 

}

