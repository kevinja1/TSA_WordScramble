package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

public class NewGameInfoModel{
	
	//private final SimpleStringProperty CreatorName;
	//private final SimpleStringProperty GameName;
	private final SimpleStringProperty Word;
	private final SimpleStringProperty Hint;
	private final SimpleIntegerProperty ID;
	
    Connection connection;
	
    private Statement statement;
    private ResultSet resultSet;
    
    //Constructors
	public NewGameInfoModel(String word, String hint, int ID){
		this.Word = new SimpleStringProperty(word);
		this.Hint = new SimpleStringProperty(hint);
		this.ID = new SimpleIntegerProperty(ID);
	}
	
	public NewGameInfoModel(){
		this.Word = new SimpleStringProperty("");
		this.Hint = new SimpleStringProperty("");
		this.ID = new SimpleIntegerProperty(0);
	}
	

	//Adds the employees to an Observable List to eventually display on the table 
	public ObservableList getDataFromSqlAndAddToObservableList(String query){
	        ObservableList<NewGameInfoModel> wordInfoTableData = FXCollections.observableArrayList();
	        try {
	        	connection = SqliteConnection.Connector();
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery(query); 
	            if(resultSet.isBeforeFirst()){    
	            	while(resultSet.next()){
	            		wordInfoTableData.add(new NewGameInfoModel(
		                        resultSet.getString("Words"),
		                        resultSet.getString("Hints"),
		                        resultSet.getInt("ID")
		                        ));
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
	        return wordInfoTableData;

	    }

	    //getters and setters
	    
	    public String getWord() {
	        return Word.get();
	    }

	    public SimpleStringProperty Word() {
	        return Word();
	    }

	    public void setWord(String Word) {
	        this.Word.set(Word);
	    }
	    
	    public String getHint() {
	        return Hint.get();
	    }

	    public SimpleStringProperty Hint() {
	        return Hint();
	    }

	    public void setHint(String Hint) {
	        this.Hint.set(Hint);
	    }
	    
	    public int getID() {
	        return ID.get();
	    }

	    public SimpleIntegerProperty ID() {
	        return ID();
	    }

	    public void setID(int ID) {
	        this.ID.set(ID);
	    }
}