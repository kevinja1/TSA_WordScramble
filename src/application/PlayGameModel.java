package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

public class PlayGameModel{
	
	//private final SimpleStringProperty CreatorName;
	//private final SimpleStringProperty GameName;
	private final SimpleStringProperty GameName;
	private final SimpleStringProperty CreatorName;
	private final SimpleIntegerProperty GameID;
	
    Connection connection;
	
    private Statement statement;
    private ResultSet resultSet;
    
    //Constructors
	public PlayGameModel(String GameName, String GameCreatorName, int GameID){
		this.GameName = new SimpleStringProperty(GameName);
		this.CreatorName = new SimpleStringProperty(GameCreatorName);
		this.GameID = new SimpleIntegerProperty(GameID);
	}
	
	public PlayGameModel(){
		this.GameName = new SimpleStringProperty("");
		this.CreatorName = new SimpleStringProperty("");
		this.GameID = new SimpleIntegerProperty(0);
	}
	

	//Adds the employees to an Observable List to eventually display on the table 
	public ObservableList getDataFromSqlAndAddToObservableList(String query){
			ArrayList<Integer> gameIDs = new ArrayList<Integer>();
			ObservableList<PlayGameModel> gameInfoTableData = FXCollections.observableArrayList();
	        try {
	        	int ID;
	        	connection = SqliteConnection.Connector();
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery(query); 
	            if(resultSet.isBeforeFirst()){    
	            	while(resultSet.next()){
	            		ID = resultSet.getInt("ParentGameID");
	            		if(gameIDs.indexOf(ID) == -1){
	            			gameInfoTableData.add(new PlayGameModel(
			                        resultSet.getString("Game Name"),
			                        resultSet.getString("Creator Name"),
			                        ID
			                        ));
		            		gameIDs.add(ID);
	            		}
	            		
		            }
	            } 	
	            else{
	            	return null;
	            }
	            
	            statement.close();
	            resultSet.close();
	            connection.close();
	        } 
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return gameInfoTableData;

	    }
	
	

	    //getters and setters
	    
	    public String getGameName() {
	        return GameName.get();
	    }

	    public SimpleStringProperty GameName() {
	        return GameName();
	    }

	    public void setGameName(String GameName) {
	        this.GameName.set(GameName);
	    }
	    
	    public String getCreatorName() {
	        return CreatorName.get();
	    }

	    public SimpleStringProperty CreatorName() {
	        return CreatorName();
	    }

	    public void setCreatorName(String CreatorName) {
	        this.CreatorName.set(CreatorName);
	    }
	    
	    public int getGameID() {
	        return GameID.get();
	    }

	    public SimpleIntegerProperty GameID() {
	        return GameID();
	    }

	    public void setGameID(int ID) {
	        this.GameID.set(ID);
	    }
}