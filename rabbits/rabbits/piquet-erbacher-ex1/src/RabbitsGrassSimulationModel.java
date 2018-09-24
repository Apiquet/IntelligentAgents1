import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;

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
	private int numAgents;
	public static void main(String[] args) {
			
			System.out.println("Rabbit skeleton");
			
		}

		public void begin() {
			// TODO Auto-generated method stub
			buildModel();
		    buildSchedule();
		    buildDisplay();
		}

		public String[] getInitParam() {
			// TODO Auto-generated method stub
			String[] initParams = { "NumAgents", "AgentStrength" };
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
			
		}
		public void buildModel(){
		
		}

		public void buildSchedule(){
		  
		}

		public void buildDisplay(){
		  
		}
}
