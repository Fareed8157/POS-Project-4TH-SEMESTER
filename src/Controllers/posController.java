/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class posController implements Initializable{

    @FXML
    private Label subTotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label grandTotalLab;

    @FXML
    private Label itemNoLabel;

    @FXML
    private JFXButton printInvoice;

    @FXML
    private JFXButton discount;

    @FXML
    private JFXButton pay;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton deletItem;

    @FXML
    private Label invoiceNoLabel;

    @FXML
    private TableView<?> posTable;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> desc;

    @FXML
    private TableColumn<?, ?> qt;

    @FXML
    private TableColumn<?, ?> price;

    @FXML
    private JFXTextField search;

    @FXML
    private FontAwesomeIconView close;

    @FXML
    void closeStage(MouseEvent event) {
       Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAction(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
}
