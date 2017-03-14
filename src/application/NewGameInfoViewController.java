package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.RadioButton;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class NewGameInfoViewController implements Initializable {
	
	public NewGameInfoModel Word_Table_Screen = new NewGameInfoModel();
	
	//Features of the UI
	@FXML
	private Button txtAdd;
	@FXML
	private Button MainMenuSaveButton;
	@FXML
	private Button MainMenuClearButton;
	@FXML
	private TextField txtWord;
	@FXML
	private TextField txtHint;
	@FXML
	private TextField txtCreatorName;
	@FXML
	private TextField txtGameTitle;
	@FXML
	private TextField txtGameID;
	@FXML
	private Label txtTitleChart;

	
	@FXML
	private TableView<NewGameInfoModel> TableWords;
	@FXML
	private TableColumn<NewGameInfoModel, String> WordColumn;
	@FXML
	private TableColumn<NewGameInfoModel, String> HintColumn;
	
	
	private boolean isMainMenuAddNewButtonClick;
	private boolean isMainMenuEditButtonClick;
	private boolean GameIDPass;
	
	Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    private int temp;
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//"Configures" the value of each column in the table

        WordColumn.setCellValueFactory(new PropertyValueFactory<NewGameInfoModel, String>("Word")); 
        HintColumn.setCellValueFactory(new PropertyValueFactory<NewGameInfoModel, String>("Hint"));
       
        GameIDPass = false;
        //Sets the values of the table from the Employee Screen
        //TableWords.setItems(Word_Table_Screen.getDataFromSqlAndAddToObservableList("SELECT * FROM WordGames where ParentGame ID ="));

    }
	
    //Method that runs when user wishes to add a new employee
	@FXML
    private void setMainMenuAddNewButtonClick(Event event){
        MainMenuSetAllEnable();
        MainMenuSetAllClear();
        isMainMenuAddNewButtonClick = true;
    }
	
	//Enables the field so the user can type the employee information
	private void MainMenuSetAllEnable(){
        txtWord.setDisable(false);
        txtHint.setDisable(false);
        

        MainMenuSaveButton.setDisable(false);
        MainMenuClearButton.setDisable(false);

    }
	
	//Disables all text fields
	private void MainMenuSetAllDisable(){
		 txtWord.setDisable(true);
	     txtHint.setDisable(true);
        

        MainMenuSaveButton.setDisable(true);
        MainMenuClearButton.setDisable(true);

    }
	
	 //Clears all text fields
	 private void MainMenuSetAllClear(){
		 	txtWord.clear();
		 	txtHint.clear();      	        
	    }
	 
	 //Method called when clear button is clicked
	 @FXML
	 private void MainMenuSetAllClear(Event event){
		 	txtWord.clear();
		 	txtHint.clear(); 
	        
	        txtWord.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
			txtHint.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
	       	        
	    }
	
	//Saves the given employee information to the Employee database and loads it into the table
	 @FXML
	    private void setMainMenuSaveButtonClick(Event event){
	        try{	       
	  
	            if(validateWord() && validateHint() && validateCreatorName() && validateGameName() && (validateParentID() || GameIDPass)){
	            	
	            	connection = SqliteConnection.Connector();
		            statement = connection.createStatement();
		            
	            	if(isMainMenuAddNewButtonClick){
		                int rowsAffected = statement.executeUpdate("insert into`WordGames` "+
		                        "(`Creator Name`,`Game Name`,`Words`,`Hints`,`ParentGameID`) "+
		                        "values ('"+txtCreatorName.getText()+"','"+txtGameTitle.getText()+"','"+txtWord.getText()
		                        +"','"+txtHint.getText()+"','" + txtGameID.getText()
		                        +"'); ");
		           
		            }
		            else if (isMainMenuEditButtonClick){

		                int rowsAffected = statement.executeUpdate("update WordGames set "+
		                        "Words = '"+txtWord.getText()+"',"+
		                        "Hints = '"+txtHint.getText()+"'"+                      
		                        " where ID = "+
		                        temp+";");	               
		            }

		            statement.close();
		            connection.close();
		            
		            MainMenuSetAllClear();
			        MainMenuSetAllDisable();
			        txtCreatorName.setDisable(true);
			        txtGameTitle.setDisable(true);
			        txtGameID.setDisable(true);
			        TableWords.setItems(Word_Table_Screen.getDataFromSqlAndAddToObservableList("SELECT * FROM WordGames where ParentGameID = "+txtGameID.getText()+";"));
			        txtTitleChart.setText(txtGameTitle.getText() + " by "+ txtCreatorName.getText());
			        
			        isMainMenuEditButtonClick=false;
			        isMainMenuAddNewButtonClick = false;
			        
					txtWord.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
					txtHint.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
					txtCreatorName.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
					txtGameTitle.setStyle("-fx-border-color: ccc; -fx-border-width: 1px ;");
					
	            }
	        }
		        catch (SQLException e){
		            e.printStackTrace();
		        }
		        
		    }
	            
	            
	            
	 //Loads in the information into the text fields of the employee that is trying to be edited
	 @FXML
	    private void setMainMenuEditButtonClick(Event event){
	        
	     if(TableWords.getSelectionModel().getSelectedItem()!=null) {
	    	    NewGameInfoModel getSelectedRow = TableWords.getSelectionModel().getSelectedItem();
	        	String sqlQuery = "select * FROM WordGames where ID = "+getSelectedRow.getID()+";";
	        	 
	        try {
	        	 connection = SqliteConnection.Connector();
		         statement = connection.createStatement();
	             resultSet = statement.executeQuery(sqlQuery);
	        
	             MainMenuSetAllEnable();
	             while(resultSet.next()) {
	                 txtWord.setText(resultSet.getString("Words"));
	                 txtHint.setText(resultSet.getString("Hints"));
	     
	                 temp = resultSet.getInt("ID");
	 	             isMainMenuEditButtonClick = true;
	            }

	            
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }

	     }
	     else{
	    	    NotificationType notificationType = NotificationType.ERROR;
	            TrayNotification tray = new TrayNotification();
	            tray.setTitle("No Word Selected");
	            tray.setMessage("To edit, please select a word from the table");
	            tray.setNotificationType(notificationType);
	            tray.showAndDismiss(Duration.millis(5000));
	     }
		 		
	    }
	 
	 //Deletes an employee from the Employee Database and refreshes the table
	 @FXML
	    private void setMainMenuDeleteButtonClick(Event event){
		 	TableWords.setPlaceholder(new Label("No Words"));
		 	if(TableWords.getSelectionModel().getSelectedItem()!=null){
		 		NewGameInfoModel getSelectedRow = TableWords.getSelectionModel().getSelectedItem();
		        String sqlQuery = "delete from WordGames where ID = '"+getSelectedRow.getID()+"';";
		        
		        try {
		        	connection = SqliteConnection.Connector();
			        statement = connection.createStatement();
		             
		            statement.executeUpdate(sqlQuery);
		       
		            TableWords.setItems(Word_Table_Screen.getDataFromSqlAndAddToObservableList("SELECT * FROM WordGames where ParentGameID =" + txtGameID.getText() +";"));
		            statement.close();
		            connection.close();

		        }
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
		 	}
		 	else{
		 		NotificationType notificationType = NotificationType.ERROR;
	            TrayNotification tray = new TrayNotification();
	            tray.setTitle("No Word Selected");
	            tray.setMessage("To delete, please select a Word from the table");
	            tray.setNotificationType(notificationType);
	            tray.showAndDismiss(Duration.millis(5000));
		 	}        
	    }
	 
	
	 private boolean validateWord(){
		 
		 if(!txtWord.getText().trim().equals("")){
			 return true;
		 }
		 else{
			 Alert alert = new Alert(AlertType.WARNING);
			 alert.setTitle("Validate Word");
			 alert.setHeaderText(null);
			 alert.setContentText("Please Enter a Word");
			 alert.showAndWait();
			 txtWord.clear();
			 txtWord.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			 txtWord.requestFocus();
			 
			 return false;
		 }
	 }
	 
	 private boolean validateHint(){
		 
		 if(!txtHint.getText().trim().equals("")){
			 return true;
		 }
		 else{
			 Alert alert = new Alert(AlertType.WARNING);
			 alert.setTitle("Validate Hint");
			 alert.setHeaderText(null);
			 alert.setContentText("Please Enter a Hint");
			 alert.showAndWait();
			 txtHint.clear();
			 txtHint.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			 txtHint.requestFocus();
			 
			 return false;
		 }
		}
	 
	  private boolean validateCreatorName(){
		 
		 if(!txtCreatorName.getText().trim().equals("")){
			 return true;
		 }
		 else{
			 Alert alert = new Alert(AlertType.WARNING);
			 alert.setTitle("Validate Name");
			 alert.setHeaderText(null);
			 alert.setContentText("Please Enter Creator Name");
			 alert.showAndWait();
			 txtCreatorName.clear();
			 txtCreatorName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			 txtCreatorName.requestFocus();
			 
			 return false;
		 }
		}
	  
	    private boolean validateGameName(){
			 
			 if(!txtGameTitle.getText().trim().equals("")){
				 return true;
			 }
			 else{
				 Alert alert = new Alert(AlertType.WARNING);
				 alert.setTitle("Validate Title");
				 alert.setHeaderText(null);
				 alert.setContentText("Please Enter Title");
				 alert.showAndWait();
	
				 txtGameTitle.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				 txtGameTitle.requestFocus();
				 
				 return false;
			 }
		}
	    
	    private boolean validateParentID() throws SQLException{
	    	Pattern p = Pattern.compile("[0-9]*");
			 Matcher m = p.matcher(txtGameID.getText());
			 if(m.find() && m.group().equals(txtGameID.getText())){
				 
			 }
			 else{
				 Alert alert = new Alert(AlertType.WARNING);
				 alert.setTitle("Validate ID");
				 alert.setHeaderText(null);
				 alert.setContentText("Please Enter a Number for the ID");
				 alert.showAndWait();
				 
				 return false;
			 }	 
			
			connection = SqliteConnection.Connector();
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery("select * from WordGames"); 
	        if(resultSet.isBeforeFirst()){    
	        	while(resultSet.next()){
	        		if(Integer.parseInt(txtGameID.getText()) == resultSet.getInt("ParentGameID") && !GameIDPass){
	        			
	        			Alert alert = new Alert(AlertType.WARNING);
	                    alert.setTitle("Validate GameID");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Please Enter Another Game ID, this one is taken");
	                    alert.showAndWait();
	                    
	                    statement.close();
	                    resultSet.close();
	                    connection.close();	
	                    
	                    return false;
	        		}		
	        	}	  
	        }
	        
	        statement.close();
            resultSet.close();
            connection.close();	
            GameIDPass = true;
            return true;
	    }
	    
	    
	    @FXML
	    private void launchWelcome(Event event) throws IOException{
	    	((Node)event.getSource()).getScene().getWindow().hide();
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("WelcomeView.fxml"));
	        loader.load();
	        Parent p = loader.getRoot();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(p));
	        stage.setTitle("Welcome");
	        stage.show();
	    }
	    
}

	 
	


	

