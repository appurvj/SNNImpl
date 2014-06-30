
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * This class uses controls the differential evolution based parameter generation
 * based on the accuracies obtained by running it through the SNClassifierNetwork. 
 * This class is where the learning takes place and is controlled.
 * 
 * @author Appurv Jain and Amrith Akula
 */
public class SNNLearningController {
    public static int newSolnCount = 0;
    private DiffEvolutionGen networkParamGen;
    private SNClassifierNetwork network;
    private Data data;
    private long count = 0;
    
    private ArrayList<Double> currAccuracies 
            = new ArrayList<Double>(DiffEvolutionGen.POPULATION_SIZE);
    public final double IDEAL_MIN_ACCURACY = 0.7;
    
    private ArrayList<ArrayList<ArrayList<Double>>> bestIndiv;
    private double bestIndivAccuracy = 0;
    
    /**
     * Constructor: Sets class parameters and initializes accuracies
     * @param layerSizes
     * @param data
     * @throws ListLengthsDifferentException 
     */
    public SNNLearningController(int[] layerSizes, Data data) 
            throws ListLengthsDifferentException{
        this.network = new SNClassifierNetwork(layerSizes);
        this.data = data;
        this.networkParamGen = new DiffEvolutionGen(layerSizes);
        this.initpopulationAccuracies();
    }
    
    /**
     * Calculates training accuracies for the data
     * @param params: network parameters
     * @return
     * @throws ListLengthsDifferentException 
     */
    public double getTrainAccuracy(ArrayList<ArrayList<ArrayList<Double>>> params) 
            throws ListLengthsDifferentException{
        network.setParams(params);
        double currAccuracy = 1;
        final double accuReducn = (double)1/(data.trainingClass.length);
        for(int i = 0; i < this.data.trainingClass.length; i++){
            if(this.network.run(this.data.trainingInput[i]) != this.data.trainingClass[i])
                currAccuracy -= accuReducn;
        }
        return currAccuracy;
    }
    
    /**
     * Calculates test accuracies for the data
     * @param params: network parameters
     * @return
     * @throws ListLengthsDifferentException 
     */
    public double getTestAccuracy(ArrayList<ArrayList<ArrayList<Double>>> params) throws ListLengthsDifferentException{
        network.setParams(params);
        double currAccuracy = 1;
        final double accuReducn = (double)1/(data.testClass.length);
        for(int i = 0; i < this.data.testClass.length; i++){
            if(this.network.run(this.data.testInput[i]) != this.data.testClass[i])
                currAccuracy -= accuReducn;
        }
        return currAccuracy;        
    }
    
    /**
     * Initializes population accuracies to accuracies of initial population
     * @throws ListLengthsDifferentException 
     */
    private void initpopulationAccuracies() 
            throws ListLengthsDifferentException{
        double currAccuracy;
        for(ArrayList<ArrayList<ArrayList<Double>>> param : this.networkParamGen.getCurrPopulation()){
            currAccuracy = this.getTrainAccuracy(param);
            this.currAccuracies.add(currAccuracy);
            if(currAccuracy > this.bestIndivAccuracy){
                this.bestIndiv = param;
                this.bestIndivAccuracy = currAccuracy;
            }
        }
    }
    
    /**
     * Calculates accuracies for the new generation and makes replacements
     * in case of individuals that perform better than corresponding parents
     * @throws ListLengthsDifferentException 
     */
    private void runNewGeneration() 
            throws ListLengthsDifferentException{
        ArrayList<Double> accuracyList = new ArrayList<Double>();
        ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen;
        newGen = this.networkParamGen.generateNewGen();
        for(ArrayList<ArrayList<ArrayList<Double>>> individual: newGen)
            accuracyList.add(this.getTrainAccuracy(individual));
        this.makeAppropriateReplacements(newGen, accuracyList);
    }
    
    /**
     * Replaces individuals whose accuracies are worse than their children
     * @param newGen: New generation of individuals(network weights)
     * @param accuracyList: list of accuracies for the new generation
     */
    private void makeAppropriateReplacements
            (ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen,ArrayList<Double> accuracyList) {
        for(int i = 0; i < accuracyList.size(); i++){
            if(accuracyList.get(i) > this.currAccuracies.get(i)){
                this.currAccuracies.set(i, accuracyList.get(i));
                this.networkParamGen.replaceIndiv(i, newGen.get(i));
                if(accuracyList.get(i) > this.bestIndivAccuracy){
                    double solnTime = ((double)(System.currentTimeMillis() - Main.time)/1000);
//For debugging
System.out.printf("Found Better Solution: %d, Accuracy: %.3f ",++newSolnCount, accuracyList.get(i));
Main.displayTime(solnTime);
if(solnTime>60)
Toolkit.getDefaultToolkit().beep();
                    this.bestIndiv = newGen.get(i);
                    this.bestIndivAccuracy = accuracyList.get(i);
                }
            }
        }
    }
    
    /*
     * Getter for best network parameters
     */
    public ArrayList<ArrayList<ArrayList<Double>>> getBestIndiv(){
        return this.bestIndiv;
    }

    
    /*
     * Getter for best Accuracy corresponding to the best netwrok parameters
     */
    public double getBestAccuracy(){
        return this.bestIndivAccuracy;
    }
    
    
    /**
     * Runs the learning algorithm for the number of minutes specified
     * @param mins: Number of minutes
     * @throws ListLengthsDifferentException 
     */
    public void runForMins(int mins) throws ListLengthsDifferentException{
        long time = System.currentTimeMillis();
        while((System.currentTimeMillis() - time) < (mins * 60000))
            this.runNewGeneration();
    }
    
    
    /**
     * Runs the program till the required training accuracy is achieved
     * @param minAccuracy
     * @throws ListLengthsDifferentException 
     */
    public void runTillAccuracy(double minAccuracy) throws ListLengthsDifferentException{
        while(this.bestIndivAccuracy < minAccuracy)
            this.runNewGeneration();
    }
    
    /**
     * Runs program till ideal min training accuracy is reached
     * @throws ListLengthsDifferentException 
     */
    public void run() throws ListLengthsDifferentException{
        this.runTillAccuracy(this.IDEAL_MIN_ACCURACY);
    }
    
    /**
     * Runs the program for a set number of iterations
     * @param count
     * @throws ListLengthsDifferentException 
     */
    public void run(int count) throws ListLengthsDifferentException{
        while(count-- > 0){
            this.runNewGeneration();
        }
    }

}
