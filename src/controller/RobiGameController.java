package controller;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import model.Food;
import model.Robi;
import processing.core.PApplet;

/**
 * the stepstone to your carrier as game developer
 * @author sven
 *
 */
public class RobiGameController extends PApplet{

	Robi playerRobi;
	ArrayList<Food> foodItems;	
	ArrayList<Robi> kiEnemies;
	
	String winner = "";
	Timer tim;
	TimerTask tTask;
	
	private static final int START=0, GAME=1, FINISH=2;
	private int gameState = START;
	
	/**
	 * creates and runs the entire app
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("controller.RobiGameController");
	}

	public void settings() {
		size(800, 600);
	}
	
	

	/**
	 * still abused to create a game session 
	 * 
	 */
	public void setup() {
		playerRobi = new Robi(100,100, 0xFFFF0000, this);
		
		kiEnemies = new ArrayList<Robi>();
		Random r = new Random();
		
		for (int i=0; i < 2; i++) {
			Robi enemy = new Robi(r.nextInt(width),r.nextInt(height),0xFF0000FF,this);
			kiEnemies.add(enemy);			
		}
		
		//create food items and store them away
		foodItems = new ArrayList<Food>();
		for (int i=0; i < 10; i++){
			foodItems.add(new Food(random(width), 
					random(height), 15, this));
		}
		// setup timer triggered game control handling
		tim = new Timer();
		tTask = new TimerTask() {
			@Override
			public void run() { // this method runs complete game logic 
				handleGameLogic();
			}			
		};
		//timer ruft alle 100ms die run-Methode vom timertask objekt auf
		tim.scheduleAtFixedRate(tTask,new Date(), 100);
	}

	/**
	 * this method does all drawing
	 */
	public void draw(){
		switch (gameState) {
		case START:
		case GAME: drawGame(); break;
		case FINISH: drawGameOver(); break;
		}
	}
	
	private void drawGameOver() {
		fill(255);
		textSize(42);
		text("Game Over: " + winner, 100,100);
		noLoop(); // gewusst wie, spart energie :-)
	}
	
	private void drawGame() {
		background(0x202020);
		playerRobi.draw();
		//zeichne KI-Enemies
		for (int i=0; i < kiEnemies.size(); i++) {
			Robi enny = kiEnemies.get(i);
			enny.draw();
		}			
			
		//iterate over all foot items
		for (int i=0; i <foodItems.size(); i++){
			Food tFood = foodItems.get(i); 
			tFood.draw(); //draw i-th food object			
		}
		drawPlayerScores();
	}
	
	private void drawPlayerScores() {
		int fontSize = 20;
		int yPos = 40;
		textSize(20);
		fill(playerRobi.getColor());
		text("Player: "+playerRobi.getScore(), 10,yPos);
		for (int i = 0; i < kiEnemies.size(); i++) {
			Robi r = kiEnemies.get(i);
			fill(r.getColor());
			text("Ki "+i+": "+r.getScore(), width - 80, yPos + i*fontSize); 
		}		
	}
	
	/**
	 * this method runs complete game logic
	 */
	void handleGameLogic() {
		playerRobi.move();
		detectFoodCollisions(playerRobi);
		if (checkScore(playerRobi)) {
			winner = "You";
		}
		//für jeden enemy:
		//  finde nächstegelegene food-objekt (done)
		//	bewege dich zum nächstgelegenen food objekt
		for (int i= 0; i < kiEnemies.size(); i++) {
			Robi ki = kiEnemies.get(i);
			Food nearestFood = null;
			double minimalDistance = Double.MAX_VALUE;
			
			for (Food f : foodItems) { //forEach Konstrukt, yeahh!
				detectFoodCollisions(ki);
				double d = getDistance(ki, f);
				if (d <= minimalDistance) {
					minimalDistance = d;
					nearestFood = f;					
				}
			}
			
			if (Math.abs(ki.getxPos()-nearestFood.getxPos()) > Math.abs(ki.getyPos()-nearestFood.getyPos())){
				if (ki.getxPos()-nearestFood.getxPos() < 0) { //move right
					ki.moveRight();
				} else {
					ki.moveLeft();
				}
			} 
			else {
				if (ki.getyPos()-nearestFood.getyPos() < 0) { //move up
					ki.moveDown();
				} else {
					ki.moveUp();
				}
			}
			ki.move();
			if (checkScore(ki)) {
				winner = "Ki-Robi "+(i);
			}
		}		
	}
	
	/**
	 * if Robi score exceeds 100, 
	 * set GameState GameOver, disable the timertask and return true
	 * @param r
	 * @return
	 */
	private boolean checkScore(Robi r) {
		if (r.getScore() >= 100) {
			gameState = FINISH;
			tim.cancel();
			return true;
		}
		return false;
	}
	
	void detectFoodCollisions(Robi v) {
		int catched = -1;
		for (int i=0; i < foodItems.size(); i++) {
			Food tFood = foodItems.get(i);
			if (checkCollision(v,tFood)){
				v.grow();
				foodItems.set(i, new Food(random(width), 
						random(height), 15, this));	
				v.addScore(tFood.getSize());
			} 	
		}		
	}
	

	public void keyPressed(){
		switch (keyCode){
		case UP: playerRobi.moveUp(); break;
		case DOWN: playerRobi.moveDown(); break;
		case LEFT: playerRobi.moveLeft(); break;
		case RIGHT: playerRobi.moveRight(); break;
		
		}
		
	}

	/**
	 * Was macht echt diese Funktion? Und wie macht sie das? 
	 * Geht das auch übersichtlicher?
	 */
	boolean checkCollision(Robi v, Food f){
		return getDistance(v,f) < (v.getRobiWidth()/2+f.getSize()/2);
	}
	
	float getDistance(Robi v, Food f) {
		float xDiff = v.getxPos() - f.getxPos();
		float yDiff = v.getyPos() - f.getyPos();
		return sqrt(xDiff*xDiff + yDiff*yDiff);
	}
}
