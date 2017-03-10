package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class WelcomeViewController implements Initializable {
		
		
		@FXML
		private RadioButton rdExisting;
		@FXML
		private RadioButton rdNew;
		@FXML
		private ToggleGroup group;
		
		@Override
		public void initialize(URL location, ResourceBundle resources){
			rdNew.setSelected(true);
		}
		
		//Method that launches the next screen when sign in button is pressed
		//and if login is successful, if not, it gives an alert
		@FXML
		public void Go (ActionEvent event) throws IOException, SQLException
		{
			if(rdNew.isSelected()){
				//load new game screen
			}
			else if(rdExisting.isSelected()){
				//load up an existing game
			}
		}	
	
}