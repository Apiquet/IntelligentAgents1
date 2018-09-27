import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * @author 
 */

public class RabbitsGrassSimulationSpace {

	/***
 		DECLARATION 
	 ***/
	private Object2DGrid grassSpace;
	private Object2DGrid agentSpace;

	/***
		Constructor 
	 ***/
	public RabbitsGrassSimulationSpace(int xSize, int ySize){
		grassSpace = new Object2DGrid(xSize, ySize);
		agentSpace = new Object2DGrid(xSize, ySize);

		for(int i = 0; i < xSize; i++){
			for(int j = 0; j < ySize; j++){
				grassSpace.putObjectAt(i,j,new Integer(0));
			}
		}
	}

	/***
		Functions 
	 ***/
	/*
	 * Adding grass in the simulation
	 * Input: INT grass
	 */
	public void spreadGrass(int grass){
		// Randomly place grass in grassSpace
		for(int i = 0; i < grass; i++){

			// Choose coordinates
			int x = (int)(Math.random()*(grassSpace.getSizeX()));
			int y = (int)(Math.random()*(grassSpace.getSizeY()));

			// Get the value of the object at those coordinates
			int currentValue = getGrassAt(x, y);
			// Replace the Integer object with another one with the new value
			grassSpace.putObjectAt(x,y,new Integer(currentValue + 1));
		}
	}
	/*
	 * Verify if a cell is occupied at a location (x,y)
	 * Input: INT X, INT Y
	 */
	public boolean isCellOccupied(int x, int y){
		boolean retVal = false;
		if(agentSpace.getObjectAt(x, y)!=null) retVal = true;
		return retVal;
	}
	/*
	 * Adding agent in the simulation, randomly but verifying if possible
	 * Input: RabbitsGrassSimulationAgent
	 */
	public boolean addAgent(RabbitsGrassSimulationAgent agent){
		boolean retVal = false;
		int count = 0;
		int countLimit = 10 * agentSpace.getSizeX() * agentSpace.getSizeY();

		while((retVal==false) && (count < countLimit)){
			//random position
			int x = (int)(Math.random()*(agentSpace.getSizeX()));
			int y = (int)(Math.random()*(agentSpace.getSizeY()));
			if(isCellOccupied(x,y) == false){
				agentSpace.putObjectAt(x,y,agent);
				agent.setXY(x,y);
				agent.setCarryDropSpace(this);
				retVal = true;
			}
			count++;
		}

		return retVal;
	}
	/*
	 * Removing agent from the simulation
	 * Input: INT x, INT y
	 */
	public void removeAgentAt(int x, int y){
		agentSpace.putObjectAt(x, y, null);
	}
	/*
	 * Taking grass on the simulation
	 */
	public int takeGrassAt(int x, int y){
		int grass = getGrassAt(x, y);
		grassSpace.putObjectAt(x, y, new Integer(0));
		return grass;
	}
	
	/*
	 * move agent from (x,y) to (newX,newY) if cell (newX,newY) is not occupied
	 * Input: int x, int y, int newX, int newY
	 * Output: boolean result of the move (true if the agent was moved)
	 */
	public boolean moveAgentAt(int x, int y, int newX, int newY){
	    //bool result of the move
		boolean retVal = false;
		if(!isCellOccupied(newX, newY)){
		    //copy the agent
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentSpace.getObjectAt(x, y);
			//remove agent at (x,y)
			removeAgentAt(x,y);
			cda.setXY(newX, newY);
			//put the agent at (newX,newY)
			agentSpace.putObjectAt(newX, newY, cda);
			retVal = true;
		}
		return retVal;
	}
	/***
	   	Get/Set functions 
	 ***/
	public int getGrassAt(int x, int y){
		int i;
		if(grassSpace.getObjectAt(x,y)!= null){
			i = ((Integer)grassSpace.getObjectAt(x,y)).intValue();
		}
		else{
			i = 0;
		}
		return i;
	}
	public Object2DGrid getCurrentGrassSpace(){
		return grassSpace;
	}
	public Object2DGrid getCurrentAgentSpace(){
		return agentSpace;
	}
	public int getTotalGrass(){
		int totalGrass = 0;
		for(int i = 0; i < agentSpace.getSizeX(); i++){
			for(int j = 0; j < agentSpace.getSizeY(); j++){
				totalGrass += getGrassAt(i,j);
			}
		}
		return totalGrass;
	}
}
