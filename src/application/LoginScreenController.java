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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class LoginScreenController implements Initializable {
		public LoginScreenModel loginModel = new LoginScreenModel();
		
		@FXML
		private TextField txtUsername;
		
		@FXML 
		private PasswordField txtPassword;
		
		@Override
		public void initialize(URL location, ResourceBundle resources){
			
		}
		
		//Method that launches the next screen when sign in button is pressed
		//and if login is successful, if not, it gives an alert
		public void Login (ActionEvent event) throws IOException, SQLException
		{
			try{
				if(loginModel.isLogin(txtUsername.getText(), txtPassword.getText())){
						loginModel.getConnection().close();
						((Node)event.getSource()).getScene().getWindow().hide();
					    Parent Main_Menu = FXMLLoader.load(getClass().getResource("WelcomeView.fxml"));
					    Scene MainMenu = new Scene(Main_Menu);
                        Stage mainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        mainMenu.hide();
                        mainMenu.setScene(MainMenu);
                        mainMenu.setTitle("Welcome");
                        mainMenu.show();
				} 
				else{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Error Login");
					alert.setHeaderText(null);
					alert.setContentText("Incorrect Username or Password");
					alert.showAndWait();
					 
			        txtUsername.clear();
			        txtPassword.clear();
				}
			} 
			catch (SQLException e){
				e.printStackTrace();
			}
			
		}	
		
		//Method to listen for Enter key and use it as a way to login as well
		@FXML
		public void handleEnterPressed(KeyEvent event) throws IOException, SQLException{
		    if (event.getCode() == KeyCode.ENTER) {
		    	try{
					if(loginModel.isLogin(txtUsername.getText(), txtPassword.getText())){
						loginModel.getConnection().close();
						((Node)event.getSource()).getScene().getWindow().hide();
					    Parent Main_Menu = FXMLLoader.load(getClass().getResource("WelcomeView.fxml"));
					    Scene MainMenu = new Scene(Main_Menu);
	                    Stage mainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                    mainMenu.hide();
	                    mainMenu.setScene(MainMenu);
	                    mainMenu.setTitle("Main Menu");
	                    mainMenu.show();
					} 
					else{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Error Login");
						alert.setHeaderText(null);
						alert.setContentText("Incorrect Username or Password");
						alert.showAndWait();
						 
				        txtUsername.clear();
				        txtPassword.clear();
					}
				} 
				catch (SQLException e){
					e.printStackTrace();
				}
		    }
		}
		
	
}

