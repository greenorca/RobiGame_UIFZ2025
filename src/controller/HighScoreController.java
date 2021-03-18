package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import model.HighScore;

/**
 * verwaltet und speichert die Highscore-Objekte
 * @author sven
 *
 */
public class HighScoreController {

	ArrayList<HighScore> scores;
	
	/**
	 * kleiner "Testrahmen"
	 * @param args
	 */
	public static void main(String[] args) {		
		HighScoreController testController = new HighScoreController();
		testController.readHighscores();
		testController.sortScores();
		testController.printHighscores();
	}
	
	public void printHighscores() {
		for (HighScore hs : scores) {
			System.out.println(hs.getName() + ": " + hs.getScore());
		}
	}
	
	public ArrayList<HighScore> getHighscoreList(){
		return scores;
	}
	
	private void sortScores() {
		
		scores.sort((a,b) -> b.getScore()-a.getScore());
		
		scores.sort(new Comparator<HighScore>() {

			@Override
			public int compare(HighScore a, HighScore b) {
				return b.getScore()-a.getScore();
			}
			
		});
	}
	
	/**
	 * liest alle HighScore-Objekte aus der Datei einlesen
	 */
	public void readHighscores() {
		scores = new ArrayList<>();
		
		File file = new File(System.getProperty("user.home")+"/highscore.lst");
		BufferedReader bfr = null;
		try {
			file.createNewFile();

			FileReader reader = new FileReader(file);
			bfr = new BufferedReader(reader);
			
			while(bfr.ready()) {
				String line = bfr.readLine();
				//System.out.println(line);				
				scores.add(parseHighscore(line)); //HighScore-Objekt parsen und in Liste einfügen
			}			
			
		} catch (IOException ioex) {
			System.out.println(file.getAbsolutePath());
			ioex.printStackTrace();
		} finally {
			try { bfr.close();
			} catch (IOException e) {} 
		}
	}
	
	private HighScore parseHighscore(String line) {
		HighScore result = null;
		String[] parts = line.split(";");
		String name = parts[0];
		int punkte = Integer.parseInt(parts[1]);
		result = new HighScore(name, punkte);
		return result;
	}
	
}
