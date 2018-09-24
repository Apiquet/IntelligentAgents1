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

	private Schedule schedule;
	private RabbitsGrassSimulationSpace cdSpace;
	private DisplaySurface displaySurf;
	private ArrayList agentList;

	// Default Values
	private static final int NUMAGENTS = 40;
	private static final int WORLDXSIZE = 40;
	private static final int WORLDYSIZE = 40;
	private static final int TOTALGRASS = 500;
	private static final int AGENT_MIN_LIFESPAN = 40;
	private static final int AGENT_MAX_LIFESPAN = 60;

	//Number of agent
	private int numAgents = NUMAGENTS;
	private int grass = TOTALGRASS;

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
		String[] initParams = { "NumAgents", "WorldXSize", "WorldYSize", "Grass", "AgentMinLifespan", "AgentMaxLifespan" };
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

	public void buildSchedule(){
		System.out.println("Running BuildSchedule");
		class CarryDropStep extends BasicAction {
			public void execute() {
				SimUtilities.shuffle(agentList);
				for(int i =0; i < agentList.size(); i++){
					RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
					cda.step();
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
	private void addNewAgent(){
		RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(agentMinLifespan, agentMaxLifespan);
		agentList.add(a);
	    cdSpace.addAgent(a);

	}
	private int ReproducibleAgentsCount(){
		int count = 0;
		for(int i = (agentList.size() - 1); i >= 0 ; i--){
			RabbitsGrassSimulationAgent cda = (RabbitsGrassSimulationAgent)agentList.get(i);
			if(cda.getStepsToLive() > 50){
				count++;
				cda.DecreaseStepsToLiveFromReproduction();
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
				cdSpace.spreadGrass(cda.getGrass());
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

	public int getGrass() {
		return grass;
	}

	public void setGrass(int i) {
		grass = i;
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
