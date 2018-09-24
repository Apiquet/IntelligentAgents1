import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import java.awt.Color;

/**
 * Class that implements the simulation agent for the rabbits grass simulation.

 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {

	private int x;
	private int y;
	private int money;
	private int stepsToLive;
	private static int IDNumber = 0;
	private int ID;
	private RabbitsGrassSimulationSpace cdSpace;


	public RabbitsGrassSimulationAgent(int minLifespan, int maxLifespan){
		x = -1;
		y = -1;
		money = 0;
		stepsToLive = 
		(int)((Math.random() * (maxLifespan - minLifespan)) + minLifespan);
		IDNumber++;
	    ID = IDNumber;
	}
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void draw(SimGraphics G){
		if(stepsToLive > 10)
			G.drawFastRoundRect(Color.green);
		else
			G.drawFastRoundRect(Color.blue);
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

	public int getMoney(){
		return money;
	}

	public int getStepsToLive(){
		return stepsToLive;
	}

	public void report(){
		System.out.println(getID() + 
				" at " + 
				x + ", " + y + 
				" has " + 
				getMoney() + " dollars" + 
				" and " + 
				getStepsToLive() + " steps to live.");
	}
	public void step(){
	    money += cdSpace.takeMoneyAt(x, y);
		stepsToLive--;
	}
}
