import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation agent for the rabbits grass simulation.

 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {

	/***
 		DECLARATION 
	 ***/

	private int x;
	private int y;
	private int vX;
	private int vY;
	private int stepsToLive;
	private static int IDNumber = 0;
	private int ID;
	private RabbitsGrassSimulationSpace cdSpace;


	/***
 		Constructor 
	 ***/
	public RabbitsGrassSimulationAgent(int birthThreshold){
		x = -1;
		y = -1;
		setVxVy();
		stepsToLive = birthThreshold;
		IDNumber++;
		ID = IDNumber;
	}

	/***
	   	 Functions 
	 ***/
	/*
	 * (non-Javadoc)
	 * @see uchicago.src.sim.gui.Drawable#draw(uchicago.src.sim.gui.SimGraphics)
	 * Choose how to represent the agent
	 * Input: SimGraphics
	 */
	public void draw(SimGraphics G){
		BufferedImage img = null;
		try {
			//loading the image (can be an url)
		    img = ImageIO.read(new File("C:\\Users\\antho\\Pictures\\rabbit.jpg"));
		} catch (IOException e) {
		}
		//draw image to fit adapt the image size to the cell
		G.drawImageToFit(img);		
		//drawFastRoundRect(Color.white);
	}
	/*
	 * Adding message in the console
	 */
	public void report(){
		System.out.println(getID() + 
				" at " + 
				x + ", " + y + 
				" has " +
				getStepsToLive() + " steps to live.");
	}
	/*
	 * Function called at each step to allow the agents to move
	 * Input: amount of energy per grass eaten (set up by the user)
	 */
	public void step(int energyFromGrass){
		setVxVy();
		int newX = x + vX;
		int newY = y + vY;

		Object2DGrid grid = cdSpace.getCurrentAgentSpace();
		newX = (newX + grid.getSizeX()) % grid.getSizeX();
		newY = (newY + grid.getSizeY()) % grid.getSizeY();

		if(tryMove(newX, newY)){
			int grassValue = cdSpace.takeGrassAt(x, y);
			if(grassValue>0) {
				stepsToLive += grassValue*energyFromGrass;
			}			
		}
		else{
			setVxVy();
		}
		stepsToLive--;
	}
	/*
	 * Decrease live after a reproduction
	 * Input: INT Reproduction cost 
	 */
	public void DecreaseStepsToLiveFromReproduction(int reproductionCost){
		stepsToLive-=reproductionCost;
	}
	/*
	 * Try moving to new location
	 * Input: INT newX, INT NewY
	 */
	private boolean tryMove(int newX, int newY){
		return cdSpace.moveAgentAt(x, y, newX, newY);
	}
	/*
	 * Find the next location randomly
	 */
	private void setVxVy(){
		vX = 0;
		vY = 0;
		//the moves allowed are N,S,E,O so we cannot have NewX!=0 and NewY!=0 because in this case, the rabbit will go in diagonal
		while((vX == 0) && ( vY == 0) || (vX != 0) && ( vY != 0)){
			vX = (int)Math.floor(Math.random() * 3) - 1;
			vY = (int)Math.floor(Math.random() * 3) - 1;
		}
	}

	/***
	   Get/Set functions 
	 ***/

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setXY(int newX, int newY){
		x = newX;
		y = newY;
	}

	public void setCarryDropSpace(RabbitsGrassSimulationSpace cds){
		cdSpace = cds;
	}

	public String getID(){
		return "A-" + ID;
	}
	public int getStepsToLive(){
		return stepsToLive;
	}
}
