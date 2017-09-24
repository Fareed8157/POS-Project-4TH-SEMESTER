/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Sale;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

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
    
    public void showReport(){
        try {
    String sourFile="C:\\NetBeansProjects\\PosInventory\\src\\Reports\\Report1.jrxml";
          JasperDesign jasperDesign=JRXmlLoader.load(sourFile);
           //JRDesignQuery jquery=new JRDesignQuery();
           //jquery.setText(qu);
           //jasperDesign.setQuery(jquery);
            //HashMap<String,Object> hm=new HashMap<>();
//            hm.put("invoiceId",2);
//            hm.put("cashier","fareed");
            JRProperties.setProperty(QueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX+"plsql"
                            ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JasperReport jasperReport=JasperCompileManager.compileReport(jasperDesign);
//          JasperReport jasperReport=JasperCompileManager.compileReport("C:\\NetBeansProjects\\PosInventory\\src\\Reports\\invoiceReport.jrxml");
            JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,hm,con);
            JRViewer viewer=new JRViewer(jasperPrint);
            //JasperViewer.viewReport(jasperPrint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
            //con.close();
           // viewer.setVisible(true);
           
    
            //con.close();
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
        
    }
}
