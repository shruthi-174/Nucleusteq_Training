/*You have a file that stores data about players' scores they achieved in different matches against different teams at different points in time, 
You have to identify the highest score of each player among all the matches. If the entry is only a string that is the name of the player, 
and if the entry is pak_55_01_nov that implies the match against Pakistan scored 55 runs on November first.
*/
package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Exercise2 {
    public static void main(String[] args) {    
    	Map<String,Integer>mp=new HashMap<>();
    	
    	try {
    		BufferedReader br=new BufferedReader(new FileReader("E:\\nucleusteq\\files\\Scores.csv"));
    		String str;
    		
    		//fetching the player name and splitting each entry by comma 
    		while((str=br.readLine())!=null) {
    			String []player_entries=str.split(",");
    			String player_name=player_entries[0];
    			int highestScore = 0;
    			
    			//fetching the highest score of each player
    			for(int i=1;i<player_entries.length;i++) {
    				String entry=player_entries[i];
    				if(entry.contains("_")) {
    					int score=Integer.parseInt(entry.split("_")[1]);
    					
    					// max function to store highest score of players in each match
    					highestScore=Math.max(highestScore, score);
    				}
    			}
    			
    			//putting values of player name and their highest score into map
    			mp.put(player_name,highestScore);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	//printing values
    	System.out.println("Players highest score in each match are: ");
    	for(Map.Entry<String, Integer> entry:mp.entrySet()) {
    		System.out.println(entry.getKey()+" "+entry.getValue());
    	}
    }
}

