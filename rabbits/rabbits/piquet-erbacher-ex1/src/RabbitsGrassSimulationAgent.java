import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import java.awt.Color;
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
	private int grass;
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
		grass = 0;
		setVxVy();

		stepsToLive = birthThreshold;
		IDNumber++;
		ID = IDNumber;
	}

	/***
	   	 Functions 
	 ***/

	public void draw(SimGraphics G){
		G.drawFastRoundRect(Color.white);
	}

	public void report(){
		System.out.println(getID() + 
				" at " + 
				x + ", " + y + 
				" has " + 
				getGrass() + " dollars" + 
				" and " + 
				getStepsToLive() + " steps to live.");
	}

	public void step(){
		int newX = x + vX;
		int newY = y + vY;

		Object2DGrid grid = cdSpace.getCurrentAgentSpace();
		newX = (newX + grid.getSizeX()) % grid.getSizeX();
		newY = (newY + grid.getSizeY()) % grid.getSizeY();

		if(tryMove(newX, newY)){
			grass += cdSpace.takeGrassAt(x, y);
		}
		else{
			setVxVy();
			stepsToLive++;
		}
		stepsToLive--;
	}

	public void DecreaseStepsToLiveFromReproduction(){
		stepsToLive-=20;
	}

	private boolean tryMove(int newX, int newY){
		return cdSpace.moveAgentAt(x, y, newX, newY);
	}

	private void setVxVy(){
		vX = 0;
		vY = 0;
		while((vX == 0) && ( vY == 0)){
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

	public int getGrass(){
		return grass;
	}

	public int getStepsToLive(){
		return stepsToLive;
	}
}
