import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Value2DDisplay;


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
	private DisplaySurface displaySurf;
	private ArrayList agentList;

	// Default Values
	private static final int NUMAGENTS = 100;
	private static final int WORLDXSIZE = 40;
	private static final int WORLDYSIZE = 40;
	private static final int TOTALMONEY = 1000;
	private static final int AGENT_MIN_LIFESPAN = 30;
	private static final int AGENT_MAX_LIFESPAN = 50;

	//Number of agent
	private int numAgents = NUMAGENTS;
	private int money = TOTALMONEY;

	private int agentMinLifespan = AGENT_MIN_LIFESPAN;
	private int agentMaxLifespan = AGENT_MAX_LIFESPAN;
	//World size	
	private int worldXSize = WORLDXSIZE;
	private int worldYSize = WORLDYSIZE;
	
	public void begin() {
		// TODO Auto-generated method stub
		buildModel();
		buildSchedule();
		buildDisplay();
	    displaySurf.display();
	}

	public String[] getInitParam() {
		// TODO Auto-generated method stub
		String[] initParams = { "NumAgents", "WorldXSize", "WorldYSize", "Money", "AgentMinLifespan", "AgentMaxLifespan" };
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
		cdSpace = null;
	    agentList = new ArrayList();

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
	    cdSpace.spreadMoney(money);
	    
	    for(int i = 0; i < numAgents; i++){
	        addNewAgent();
	      }
	}

	public void buildSchedule(){
		System.out.println("Running BuildSchedule");
	}

	public void buildDisplay(){
		System.out.println("Running BuildDisplay");
		ColorMap map = new ColorMap();

	    for(int i = 1; i<16; i++){
	      map.mapColor(i, new Color((int)(i * 8 + 127), 0, 0));
	    }
	    map.mapColor(0, Color.white);

	    Value2DDisplay displayMoney = 
	        new Value2DDisplay(cdSpace.getCurrentMoneySpace(), map);

	    displaySurf.addDisplayable(displayMoney, "Money");
	}
	private void addNewAgent(){
		RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(agentMinLifespan, agentMaxLifespan);
		agentList.add(a);
	    cdSpace.addAgent(a);

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
	public int getAgentMaxLifespan() {
		return agentMaxLifespan;
	}

	public int getAgentMinLifespan() {
		return agentMinLifespan;
	}

	public void setAgentMaxLifespan(int i) {
		agentMaxLifespan = i;
	}

	public void setAgentMinLifespan(int i) {
		agentMinLifespan = i;
	}
	public static void main(String[] args) {			
		SimInit init = new SimInit();
		//Instantiates the model
		RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
		//Loading the model
	    init.loadModel(model, "", false);	
	}
}
