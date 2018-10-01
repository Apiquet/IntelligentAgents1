import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;



/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 *
 * @author 
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {		

	/***
	 	DECLARATION 
	 ***/

	private Schedule schedule;
	private RabbitsGrassSimulationSpace cdSpace;
	private DisplaySurface displaySurf;
	private ArrayList agentList;
	private OpenSequenceGraph amountOfGrassInSpace;
	private OpenSequenceGraph numberOfAgentsInSpace;
	private OpenSequenceGraph analysisAgentNumberInFunctionGrassEnergy;

	// Default Values
	private static final int AGENTS_NUMBER = 1;
	private static final int GRASS_GROWTH_RATE = 1;
	private static final int WORLD_X_SIZE = 20;
	private static final int WORLD_Y_SIZE = 20;
	private static final int TOTAL_GRASS_BEGINNING = 50;
	private static final int BIRTH_THRESHOLD = 100;
	private static final int ENERGY_FROM_GRASS = 50;
	private static final int REPRODUCTION_ENERGY = 800;
	private static final int REPRODUCTION_COST = 750;

	//Variables
	private int numAgents = AGENTS_NUMBER;
	private int grass = TOTAL_GRASS_BEGINNING;
	private int grassGrowthRate = GRASS_GROWTH_RATE;
	private int agentBirthThreshold = BIRTH_THRESHOLD;
	private int worldXSize = WORLD_X_SIZE;
	private int worldYSize = WORLD_Y_SIZE;
	private int energyFromGrass = ENERGY_FROM_GRASS;
	private int reproductionEnergy = REPRODUCTION_ENERGY;
	private int reproductionCost = REPRODUCTION_COST;
	
	/***
 		GRAPHS CLASSES 
	 ***/
	/*
	 * Classes for graphs
	 * Execute is called by the graph and has to return the value to display
	 */
	class grassInSpace implements DataSource, Sequence {
		//called at each tick
		public Object execute() {
			return new Double(getSValue());
		}
		//method to fetch the value to display
		public double getSValue() {
			return (double)cdSpace.getTotalGrass();
		}
	}

	class agentsInSpace implements DataSource, Sequence {
		public Object execute() {
			return new Double(getSValue());
		}
		public double getSValue() {
			return (double)countLivingAgents();
		}
	}
	
	class grassEnergy implements DataSource, Sequence {
		public Object execute() {
			return new Double(getSValue());
		}
		public double getSValue() {
			return (double)energyFromGrass;
		}
	}

	/***
 		Initialisation functions 
	 ***/

	public void begin() {
		buildModel();
		buildSchedule();
		buildDisplay();
		displaySurf.display();
	    amountOfGrassInSpace.display();
	    numberOfAgentsInSpace.display();
	    analysisAgentNumberInFunctionGrassEnergy.display();
	}
	/*
	 * (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getInitParam()
	 * Allows Repast to know which parameters can be set by the user
	 * Output: String Array
	 */
	public String[] getInitParam() {
		String[] initParams = { "NumAgents", "WorldXSize", "WorldYSize", "Grass", "AgentBirthThreshold", "GrassGrowthRate", "EnergyFromGrass", "ReproductionCost", "ReproductionEnergy"};
		return initParams;
	}
	/*
	 * (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#setup()
	 * setting up graph, register display
	 */
	public void setup() {
		System.out.println("Running setup");
	    cdSpace = null;
	    agentList = new ArrayList();
	    schedule = new Schedule(1);

	    // Tear down Displays
	    if (displaySurf != null){
	      displaySurf.dispose();
	    }
	    displaySurf = null;

	    if (amountOfGrassInSpace != null){
	    	amountOfGrassInSpace.dispose();
	    }
	    amountOfGrassInSpace = null;

	    if (numberOfAgentsInSpace != null){
	    	numberOfAgentsInSpace.dispose();
	    }
	    numberOfAgentsInSpace = null;
	    
	    if (analysisAgentNumberInFunctionGrassEnergy != null){
	    	analysisAgentNumberInFunctionGrassEnergy.dispose();
	    }
	    analysisAgentNumberInFunctionGrassEnergy = null;

	    // Create Displays
	    displaySurf = new DisplaySurface(this, "Carry Drop Model Window 1");
	    amountOfGrassInSpace = new OpenSequenceGraph("Amount Of Grass In Space",this);
	    numberOfAgentsInSpace = new OpenSequenceGraph("Number of Agents In Space",this);
	    analysisAgentNumberInFunctionGrassEnergy = new OpenSequenceGraph("Number of Agents and Grass Energy",this);
	    amountOfGrassInSpace.setAxisTitles("time", "grass amount");
	    numberOfAgentsInSpace.setAxisTitles("time", "agents number");
	    
	    // Register Displays
	    registerDisplaySurface("Carry Drop Model Window 1", displaySurf);
	    this.registerMediaProducer("Plot", amountOfGrassInSpace);
	    this.registerMediaProducer("Plot", numberOfAgentsInSpace);
	    this.registerMediaProducer("Plot", analysisAgentNumberInFunctionGrassEnergy);
	}
	/*
	 * Building model, initial number of agent and grass
	 */
	public void buildModel(){
		System.out.println("Running BuildModel");
		cdSpace = new RabbitsGrassSimulationSpace(worldXSize, worldYSize);
		cdSpace.spreadGrass(grass);

		for(int i = 0; i < numAgents; i++){
			addNewAgent();
		}
		for(int i = 0; i < agentList.size(); i++){
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
			cda.report();
		}
	}
	/*
	 * Build how to represent the display on the space and set the map color
	 */
	public void buildDisplay(){
		System.out.println("Running BuildDisplay");
		ColorMap map = new ColorMap();
		//Adding several tints for the grass (useful to display the density difference)
		for(int i = 1; i<16; i++){
			map.mapColor(i, new Color(0,70 + 110/i,0));
		}
		//map color, the ground
		map.mapColor(0, new Color(204,153,51));
		Value2DDisplay displayGrass = new Value2DDisplay(cdSpace.getCurrentGrassSpace(), map);
		Object2DDisplay displayAgents = new Object2DDisplay(cdSpace.getCurrentAgentSpace());
		displayAgents.setObjectList(agentList);
		//adding grass and agents to the display
		displaySurf.addDisplayable(displayGrass, "Grass");
		displaySurf.addDisplayable(displayAgents, "Agents");
		//adding 2 plots, grass and agents in the space
		amountOfGrassInSpace.addSequence("Grass In Space", new grassInSpace());
	    numberOfAgentsInSpace.addSequence("Agents In Space", new agentsInSpace());
	    analysisAgentNumberInFunctionGrassEnergy.addSequence("Agents Number", new agentsInSpace());
	    analysisAgentNumberInFunctionGrassEnergy.addSequence("Grass Energy", new grassEnergy());
	    analysisAgentNumberInFunctionGrassEnergy.addSequence("Grass Energy", new grassInSpace());
	}
	
	/***
		Functions 
	 ***/
	/*
	 * Updating simulation at each tick
	 */
	public void buildSchedule(){
		System.out.println("Running BuildSchedule");
		class CarryDropStep extends BasicAction {
			public void execute() {
				//adding grass based on the grass growth rate
				cdSpace.spreadGrass(grassGrowthRate);
				SimUtilities.shuffle(agentList);
				for(int i =0; i < agentList.size(); i++){
					RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
					cda.step(energyFromGrass);
				}
				//reproduction and kill
				int reproducibleAgentsCount = ReproducibleAgentsCount();
				reapDeadAgents();
				for(int i =0; i < reproducibleAgentsCount; i++){
					addNewAgent();
				}
				displaySurf.updateDisplay();
			}
		}
		schedule.scheduleActionBeginning(0, new CarryDropStep());
		//classes for graphs
		class CarryDropCountLiving extends BasicAction {
			public void execute(){
				countLivingAgents();
			}
		}
		class CarryDropUpdateGrassInSpace extends BasicAction {
			public void execute(){
				amountOfGrassInSpace.step();
			}
		}
		class CarryDropUpdateAgentsInSpace extends BasicAction {
			public void execute(){
				numberOfAgentsInSpace.step();
			}
		}
		class CarryDropUpdateAnalysisAgentGrassEnergy extends BasicAction {
			public void execute(){
				analysisAgentNumberInFunctionGrassEnergy.step();
			}
		}
		//updating graphs
		schedule.scheduleActionAtInterval(10, new CarryDropCountLiving());
	    schedule.scheduleActionAtInterval(10, new CarryDropUpdateGrassInSpace());
	    schedule.scheduleActionAtInterval(10, new CarryDropUpdateAgentsInSpace());
	    schedule.scheduleActionAtInterval(10, new CarryDropUpdateAnalysisAgentGrassEnergy());

	}
	/*
	 * Adding agent to the map
	 */
	private void addNewAgent(){
		RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(agentBirthThreshold);
		if(cdSpace.addAgent(a)) agentList.add(a);

	}
	/*
	 * Count the number of reproduction to proceed 
	 * Decrease the live of each reproducible rabbit (based on the reproduction cost)
	 * Output: INT reproducible count
	 */
	private int ReproducibleAgentsCount(){
		int count = 0;
		for(int i = (agentList.size() - 1); i >= 0 ; i--){
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
			if(cda.getStepsToLive() > reproductionEnergy){
				count++;
				cda.DecreaseStepsToLiveFromReproduction(reproductionCost);
			}
		}
		return count;
	}
	/*
	 * Kill rabbit that have less than 1 live
	 * Output: INT count
	 */
	private int reapDeadAgents(){
		int count = 0;
		for(int i = (agentList.size() - 1); i >= 0 ; i--){
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
			if(cda.getStepsToLive() < 1){
				cdSpace.removeAgentAt(cda.getX(), cda.getY());
				agentList.remove(i);
				count++;
			}
		}
		return count;
	}
	/*
	 * Count the current agent living
	 * Output: Count
	 */
	private int countLivingAgents(){
		int livingAgents = 0;
		for(int i = 0; i < agentList.size(); i++){
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
			if(cda.getStepsToLive() > 0) livingAgents++;
		}
		System.out.println("Number of living agents is: " + livingAgents);

		return livingAgents;
	}

	/***
	   Get/Set functions 
	 ***/

	public int getNumAgents(){
		return numAgents;
	}

	public void setNumAgents(int na){
		numAgents = na;
	}

	public String getName() {
		return "Rabbit Grass Simulation";
	}

	public Schedule getSchedule() {
		return schedule;
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
	
	public int getEnergyFromGrass(){
		return energyFromGrass;
	}

	public void setEnergyFromGrass(int efg){
		energyFromGrass = efg;
	}
	public int getGrassGrowthRate() {
		return grassGrowthRate;
	}

	public void setGrassGrowthRate(int i) {
		grassGrowthRate = i;
	}

	public int getGrass() {
		return grass;
	}

	public void setGrass(int i) {
		grass = i;
	}
	public int getAgentBirthThreshold() {
		return agentBirthThreshold;
	}

	public void setAgentBirthThreshold(int i) {
		agentBirthThreshold = i;
	}
	public int getReproductionCost() {
		return reproductionCost;
	}

	public void setReproductionCost(int i) {
		reproductionCost = i;
	}
	public int getReproductionEnergy() {
		return reproductionEnergy;
	}

	public void setReproductionEnergy(int i) {
		reproductionEnergy = i;
	}

	/***
	   	Main
	 ***/
	public static void main(String[] args) {			
		SimInit init = new SimInit();
		//Instantiates the model
		RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
		//Loading the model
		init.loadModel(model, "", false);	
	}
}
