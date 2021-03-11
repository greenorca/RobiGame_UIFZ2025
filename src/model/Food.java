package model;
import processing.core.PApplet;
/**
 * missing javadoc hurts. not only in exams.
 * @author sven
 *
 */
public class Food {
	float xPos;
	float yPos;
	int myColor = 0xFFFFAA00; 
	int size;
	PApplet myWindow;
	
	public Food(float xPos, float yPos, int size, PApplet myWindow){
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.myWindow = myWindow; 
	}
	public void draw(){
		myWindow.fill(myColor);
		myWindow.ellipse(getxPos(),getyPos(),getSize(),getSize());
		myWindow.fill(0xFF55AA00);
		myWindow.ellipse(getxPos(),getyPos(),getSize()/2,getSize()/2);    
	}
	public int getSize() {
		return size;
	}
	public float getxPos() {
		return xPos;
	}
	public float getyPos() {
		return yPos;
	}
}
