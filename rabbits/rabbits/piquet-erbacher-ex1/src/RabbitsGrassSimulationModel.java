import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.gui.Object2DDisplay;
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

	// Default Values
	private static final int AGENTS_NUMBER = 1;
	private static final int GRASS_GROWTH_RATE = 1;
	private static final int WORLD_X_SIZE = 20;
	private static final int WORLD_Y_SIZE = 20;
	private static final int TOTAL_GRASS_BEGINNING = 200;
	private static final int BIRTH_THRESHOLD = 800;
	private static final int ENERGY_FROM_GRASS = 5500;
	private static final int REPRODUCTION_ENERGY = 1000;
	private static final int REPRODUCTION_COST = 100;

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
 		Initialisation functions 
	 ***/

	public void begin() {
		buildModel();
		buildSchedule();
		buildDisplay();
		displaySurf.display();
	}

	public String[] getInitParam() {
		String[] initParams = { "NumAgents", "WorldXSize", "WorldYSize", "Grass", "AgentBirthThreshold", "GrassGrowthRate", "EnergyFromGrass", "ReproductionCost", "ReproductionEnergy"};
		return initParams;
	}

	public void setup() {
		System.out.println("Running setup");
		cdSpace = null;
		agentList = new ArrayList();
		schedule = new Schedule(1);

		if (displaySurf != null){
			displaySurf.dispose();
		}
		displaySurf = null;

		displaySurf = new DisplaySurface(this, "Carry Drop Model Window 1");

		registerDisplaySurface("Carry Drop Model Window 1", displaySurf);
	}

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

	public void buildDisplay(){
		System.out.println("Running BuildDisplay");
		ColorMap map = new ColorMap();

		for(int i = 1; i<16; i++){
			map.mapColor(i, new Color(34,139,34));
		}
		map.mapColor(0, Color.orange);

		Value2DDisplay displayGrass = 
				new Value2DDisplay(cdSpace.getCurrentGrassSpace(), map);

		Object2DDisplay displayAgents = new Object2DDisplay(cdSpace.getCurrentAgentSpace());
		displayAgents.setObjectList(agentList);

		displaySurf.addDisplayable(displayGrass, "Grass");
		displaySurf.addDisplayable(displayAgents, "Agents");
	}
	
	/***
		Functions 
	 ***/
	public void buildSchedule(){
		System.out.println("Running BuildSchedule");
		class CarryDropStep extends BasicAction {
			public void execute() {
				cdSpace.spreadGrass(grassGrowthRate);
				SimUtilities.shuffle(agentList);
				for(int i =0; i < agentList.size(); i++){
					RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
					cda.step(energyFromGrass);
				}
				int reproducibleAgentsCount = ReproducibleAgentsCount();
				reapDeadAgents();
				for(int i =0; i < reproducibleAgentsCount; i++){
					addNewAgent();
				}
				displaySurf.updateDisplay();
			}
		}
		schedule.scheduleActionBeginning(0, new CarryDropStep());
		class CarryDropCountLiving extends BasicAction {
			public void execute(){
				countLivingAgents();
			}
		}

		schedule.scheduleActionAtInterval(10, new CarryDropCountLiving());
	}
	private void addNewAgent(){
		RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(agentBirthThreshold);
		agentList.add(a);
		cdSpace.addAgent(a);

	}
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
