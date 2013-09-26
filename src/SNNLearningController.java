
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
    
    
    public SNNLearningController(int[] layerSizes, Data data) 
            throws ListLengthsDifferentException{
        this.network = new SNClassifierNetwork(layerSizes);
        this.data = data;
        this.networkParamGen = new DiffEvolutionGen(layerSizes);
        this.initpopulationAccuracies();
    }
    
    
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
    
    
    private void runNewGeneration() 
            throws ListLengthsDifferentException{
        
//if(++count%5000 == 0)
//System.out.println("Running Generation: " + count + "  Curr Best Accuracy: " 
  //      + this.bestIndivAccuracy  + "\n Curr Accuracies List: " + this.currAccuracies);
        ArrayList<Double> accuracyList = new ArrayList<Double>();
        ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen;
        newGen = this.networkParamGen.generateNewGen();
        for(ArrayList<ArrayList<ArrayList<Double>>> individual: newGen)
            accuracyList.add(this.getTrainAccuracy(individual));
        this.makeAppropriateReplacements(newGen, accuracyList);
    }
    
    
    private void makeAppropriateReplacements
            (ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen,ArrayList<Double> accuracyList) {
        for(int i = 0; i < accuracyList.size(); i++){
            if(accuracyList.get(i) > this.currAccuracies.get(i)){
                this.currAccuracies.set(i, accuracyList.get(i));
                this.networkParamGen.replaceIndiv(i, newGen.get(i));
                if(accuracyList.get(i) > this.bestIndivAccuracy){
//System.out.printf("Found Better Solution: " + ++newSolnCount + ", Accuracy: " + accuracyList.get(i));
                    double solnTime = ((double)(System.currentTimeMillis() - Main.time)/1000);
System.out.printf("Found Better Solution: %d, Accuracy: %.3f ",++newSolnCount, accuracyList.get(i));
if(solnTime > 3600)
    System.out.printf(", %.3f hours\n", solnTime/(3600));
else if(solnTime > 60)
    System.out.printf(", %.3f minutes\n" ,solnTime/60);
else
    System.out.printf( ", %.3f seconds\n",solnTime);
                    this.bestIndiv = newGen.get(i);
                    this.bestIndivAccuracy = accuracyList.get(i);
                }
            }
        }
    }
    
    public ArrayList<ArrayList<ArrayList<Double>>> getBestIndiv(){
        return this.bestIndiv;
    }
    
    public double getBestAccuracy(){
        return this.bestIndivAccuracy;
    }
    
    public void runTillAccuracy(double minAccuracy) throws ListLengthsDifferentException{
        while(this.bestIndivAccuracy < minAccuracy)
            this.runNewGeneration();
    }
    
    
    public void run() throws ListLengthsDifferentException{
        while(this.bestIndivAccuracy < this.IDEAL_MIN_ACCURACY)
            this.runNewGeneration();
    }
    
    
    public void run(int count) throws ListLengthsDifferentException{
        while(count-- > 0){
            this.runNewGeneration();
        }
    }

}
