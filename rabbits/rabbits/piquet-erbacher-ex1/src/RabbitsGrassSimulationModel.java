import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;


/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 *
 * @author 
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {		

	private Schedule schedule;
	private RabbitsGrassSimulationSpace cdSpace;

	// Default Values
	private static final int NUMAGENTS = 100;
	private static final int WORLDXSIZE = 40;
	private static final int WORLDYSIZE = 40;
	private static final int TOTALMONEY = 1000;

	//Number of agent
	private int numAgents = NUMAGENTS;
	private int money = TOTALMONEY;

	//World size	
	private int worldXSize = WORLDXSIZE;
	private int worldYSize = WORLDYSIZE;
	
	public void begin() {
		// TODO Auto-generated method stub
		buildModel();
		buildSchedule();
		buildDisplay();
	}

	public String[] getInitParam() {
		// TODO Auto-generated method stub
		String[] initParams = { "NumAgents", "WorldXSize", "WorldYSize", "Money" };
		return initParams;
	}
	public int getNumAgents(){
		return numAgents;
	}

	public void setNumAgents(int na){
		numAgents = na;
	}
	public String getName() {
		// TODO Auto-generated method stub
		return "Rabbit Grass Simulation";
	}

	public Schedule getSchedule() {
		// TODO Auto-generated method stub
		return schedule;
	}

	public void setup() {
		// TODO Auto-generated method stub
		System.out.println("Running setup");
	}
	public void buildModel(){
		System.out.println("Running BuildModel");
	}

	public void buildSchedule(){
		System.out.println("Running BuildSchedule");
	}

	public void buildDisplay(){
		System.out.println("Running BuildDisplay");
	}
	
	public int getWorldXSize(){
		return worldXSize;
	}

	public void setWorldXSize(int wxs){
		worldXSize = wxs;
	}

	public int getWorldYSize(){
		return worldYSize;
	}

	public void setWorldYSize(int wys){
		worldYSize = wys;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int i) {
		money = i;
	}
	
	public static void main(String[] args) {			
		SimInit init = new SimInit();
		//Instantiates the model
		RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
		//Loading the model
	    init.loadModel(model, "", false);	
	}
}
