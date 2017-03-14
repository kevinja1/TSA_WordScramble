package application;

import java.awt.List;
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

public class ScrambleWordModel{
	
	//private final SimpleStringProperty CreatorName;
	//private final SimpleStringProperty GameName;
	private final SimpleStringProperty Word;
	private final SimpleStringProperty Hint;
	private final SimpleIntegerProperty wordID;
	
    Connection connection;
	
    private Statement statement;
    private ResultSet resultSet;
    
    //Constructors
	public ScrambleWordModel(String word, String hint, int wordID){
		this.Word = new SimpleStringProperty(word);
		this.Hint = new SimpleStringProperty(hint);
		this.wordID = new SimpleIntegerProperty(wordID);
	}
	
	public ScrambleWordModel(){
		this.Word = new SimpleStringProperty("");
		this.Hint = new SimpleStringProperty("");
		this.wordID = new SimpleIntegerProperty(0);
	}
	

	//Adds the employees to an Observable List to eventually display on the table 
	public ObservableList getDataFromSqlAndAddToObservableList(String query){
			//ArrayList<Integer> gameIDs = new ArrayList<Integer>();
			ObservableList<ScrambleWordModel> gameInfoTableData = FXCollections.observableArrayList();
	        try {
	        	int ID;
	        	connection = SqliteConnection.Connector();
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery(query); 
	            if(resultSet.isBeforeFirst()){    
	            	while(resultSet.next()){
	            		ID = resultSet.getInt("ID");
	            		gameInfoTableData.add(new ScrambleWordModel(
		                        shuffle(resultSet.getString("Words")),
		                        resultSet.getString("Hints"),
		                        ID
		                        ));
	            		//gameIDs.add(ID);
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
	
	public ObservableList getNonAnsweredWordsAndAddToTable(String query, ArrayList<Integer> usedWords){
		//ArrayList<Integer> gameIDs = new ArrayList<Integer>();
		ObservableList<ScrambleWordModel> gameInfoTableData = FXCollections.observableArrayList();
        try {
        	int ID;
        	connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); 
            if(resultSet.isBeforeFirst()){    
            	while(resultSet.next()){
            		ID = resultSet.getInt("ID");
            		if(usedWords.indexOf(ID) == -1){
                		gameInfoTableData.add(new ScrambleWordModel(
    	                        shuffle(resultSet.getString("Words")),
    	                        resultSet.getString("Hints"),
    	                        ID
    	                        ));
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
	
	public int getObservableListLength(String query){
		int length = 0;
		ObservableList<PlayGameModel> gameInfoTableData = FXCollections.observableArrayList();
        try {
        	int ID;
        	connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); 
            if(resultSet.isBeforeFirst()){    
            	while(resultSet.next()){
            		length++;
            	}
            		
	            }
            statement.close();
            resultSet.close();
            connection.close();
        }
	    
        catch (SQLException e) {
            e.printStackTrace();
        }
        return length;

    }
	
	public String shuffle(String input){
        ArrayList<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
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
	    
	    public int getwordID() {
	        return wordID.get();
	    }

	    public SimpleIntegerProperty wordID() {
	        return wordID();
	    }

	    public void setwordID(int ID) {
	        this.wordID.set(ID);
	    }
}