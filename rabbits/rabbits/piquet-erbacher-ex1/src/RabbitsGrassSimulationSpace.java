import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * @author 
 */

public class RabbitsGrassSimulationSpace {
	private Object2DGrid moneySpace;
	private Object2DGrid agentSpace;

	public RabbitsGrassSimulationSpace(int xSize, int ySize){
		moneySpace = new Object2DGrid(xSize, ySize);
	    agentSpace = new Object2DGrid(xSize, ySize);

	    for(int i = 0; i < xSize; i++){
			for(int j = 0; j < ySize; j++){
				moneySpace.putObjectAt(i,j,new Integer(0));
			}
		}
	}
	public void spreadMoney(int money){
	    // Randomly place money in moneySpace
	    for(int i = 0; i < money; i++){

	      // Choose coordinates
	      int x = (int)(Math.random()*(moneySpace.getSizeX()));
	      int y = (int)(Math.random()*(moneySpace.getSizeY()));

	      // Get the value of the object at those coordinates
	      int currentValue = getMoneyAt(x, y);
	      // Replace the Integer object with another one with the new value
	      moneySpace.putObjectAt(x,y,new Integer(currentValue + 1));
	    }
	  }

	  public int getMoneyAt(int x, int y){
	    int i;
	    if(moneySpace.getObjectAt(x,y)!= null){
	      i = ((Integer)moneySpace.getObjectAt(x,y)).intValue();
	    }
	    else{
	      i = 0;
	    }
	    return i;
	  }
	  public Object2DGrid getCurrentMoneySpace(){
		  return moneySpace;
	  }
	  public Object2DGrid getCurrentAgentSpace(){
		  return agentSpace;
	  }
	  public boolean isCellOccupied(int x, int y){
		  boolean retVal = false;
		  if(agentSpace.getObjectAt(x, y)!=null) retVal = true;
		  return retVal;
	  }

	  public boolean addAgent(RabbitsGrassSimulationAgent agent){
		  boolean retVal = false;
		  int count = 0;
		  int countLimit = 10 * agentSpace.getSizeX() * agentSpace.getSizeY();

		  while((retVal==false) && (count < countLimit)){
			  int x = (int)(Math.random()*(agentSpace.getSizeX()));
			  int y = (int)(Math.random()*(agentSpace.getSizeY()));
			  if(isCellOccupied(x,y) == false){
				  agentSpace.putObjectAt(x,y,agent);
				  agent.setXY(x,y);
				  retVal = true;
			  }
			  count++;
		  }

		  return retVal;
	  }
}
