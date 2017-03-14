
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.prism.paint.Color;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.RadioButton;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class PlayGameViewController implements Initializable {
	
	public PlayGameModel Game_Table_Screen = new PlayGameModel();
	public ScrambleWordModel Word_Table_Screen = new ScrambleWordModel();
	private int numCorrect = 0;
	private ArrayList<Integer> usedWords= new ArrayList<Integer>();
	private int gameID;
	
	@FXML
	private Label txtTitleChart;
	@FXML
	private Label lbFinalScore;
	@FXML
	private TextField txtPlayerName;

	@FXML
	private TextField txtScrambledWord;
	@FXML
	private Button btSubmit;
	@FXML
	private Button btLoadGame;

	
	@FXML
	private TableView<PlayGameModel> TableGames;
	@FXML
	private TableColumn<PlayGameModel, String> GameColumn;
	@FXML
	private TableColumn<PlayGameModel, String> CreatorColumn;	
	
	@FXML
	private TableView<ScrambleWordModel> TableWords;
	@FXML
	private TableColumn<ScrambleWordModel, String> ScrambleWordColumn;
	@FXML
	private TableColumn<ScrambleWordModel, String> HintColumn;	
	
	
	Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    private int temp;
    
    
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//"Configures" the value of each column in the table

    	TableWords.setPlaceholder(new Label("No Words"));
    	TableGames.setPlaceholder(new Label("No Games Made Yet"));
    	GameColumn.setCellValueFactory(new PropertyValueFactory<PlayGameModel, String>("GameName")); 
        CreatorColumn.setCellValueFactory(new PropertyValueFactory<PlayGameModel, String>("CreatorName"));
        
        ScrambleWordColumn.setCellValueFactory(new PropertyValueFactory<ScrambleWordModel, String>("Word")); 
        HintColumn.setCellValueFactory(new PropertyValueFactory<ScrambleWordModel, String>("Hint"));
       
        
        //Sets the values of the table from the Employee Screen
        TableGames.setItems(Game_Table_Screen.getDataFromSqlAndAddToObservableList("SELECT * FROM WordGames"));
        lbFinalScore.setVisible(false);
 
    }
    
    @FXML
    private void Go(Event event){
    	if(TableGames.getSelectionModel().getSelectedItem()!=null) {
    	    PlayGameModel getSelectedRow = TableGames.getSelectionModel().getSelectedItem();
        	String sqlQuery = "select * FROM WordGames where parentGameID = "+ getSelectedRow.getGameID()+";";
        	gameID = getSelectedRow.getGameID();
        
        
     
             TableWords.setItems(Word_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery));
             TableGames.setDisable(true);
             btLoadGame.setDisable(true);
        
       
    	}
     else{
    	    NotificationType notificationType = NotificationType.ERROR;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("No Game Selected");
            tray.setMessage("To play, please select a game from the table");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(5000));
     }
    }
    
    @FXML
    private void Submit(Event event){
    	if(TableWords.getSelectionModel().getSelectedItem()!=null) {
    	    ScrambleWordModel getSelectedRow = TableWords.getSelectionModel().getSelectedItem();
        	String sqlQuery = "select * FROM WordGames where ID = "+ getSelectedRow.getwordID()+";";
        	try{ 
        	connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery); 
            if(resultSet.isBeforeFirst()){    
            	while(resultSet.next()){
            		if(usedWords.indexOf(resultSet.getInt("ID")) == -1){
            			if(resultSet.getString("Words").equals(txtScrambledWord.getText())){
            		
            			numCorrect++;
            			
            		}
            			usedWords.add(resultSet.getInt("ID"));
            			
            			
            		
            		}
            		else{
            			NotificationType notificationType = NotificationType.ERROR;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("Word already submitted");
                        tray.setMessage("Please select another word");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(5000));
            		}
            		
            		}
             }
            
            String sqlQuery1 = "select * FROM WordGames where parentGameID = "+gameID+";";
        
            TableWords.setItems(Word_Table_Screen.getNonAnsweredWordsAndAddToTable(sqlQuery1, usedWords));
            txtScrambledWord.clear();
            statement.close();
            resultSet.close();
            connection.close();
            
            if(getSelectedRow.getObservableListLength("select * FROM WordGames where parentGameID = "+ gameID+";") == usedWords.size()){
            	lbFinalScore.setVisible(true);
            	lbFinalScore.setText("Final Score: " + numCorrect + " out of " + usedWords.size() + ", or " + ((double)numCorrect/usedWords.size())*100 + "% ");
            	TableWords.setDisable(true);
            	btSubmit.setDisable(true);
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
            tray.setMessage("Please select a word");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(5000));
     }
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
    
    @FXML
	 public void handleMouseClick(MouseEvent mouseEvent) throws IOException {
	        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	     
	            	if(TableWords.getSelectionModel().getSelectedItem()!=null){
	    			 	txtScrambledWord.setPromptText(TableWords.getSelectionModel().getSelectedItem().getWord());
	    		 }
	            }
	        
	 }
    
    
    
    
}

	 
	


	

