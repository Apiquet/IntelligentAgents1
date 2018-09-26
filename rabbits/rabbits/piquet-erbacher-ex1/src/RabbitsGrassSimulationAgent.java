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

	public void draw(SimGraphics G){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("C:\\Users\\antho\\Pictures\\rabbit.png"));
		} catch (IOException e) {
		}
		G.drawImageToFit(img);
		//drawFastRoundRect(Color.white);
	}

	public void report(){
		System.out.println(getID() + 
				" at " + 
				x + ", " + y + 
				" has " +
				getStepsToLive() + " steps to live.");
	}

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

	public void DecreaseStepsToLiveFromReproduction(int reproductionCost){
		stepsToLive-=reproductionCost;
	}

	private boolean tryMove(int newX, int newY){
		return cdSpace.moveAgentAt(x, y, newX, newY);
	}

	private void setVxVy(){
		vX = 0;
		vY = 0;
		while((vX == 0) && ( vY == 0) || (vX == 1) && ( vY == 1)|| (vX == -1) && ( vY == -1)|| (vX == -1) && ( vY == 1)|| (vX == 1) && ( vY == -1)){
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
