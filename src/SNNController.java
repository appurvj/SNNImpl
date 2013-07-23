
import java.util.ArrayList;

/**
 *
 * @author Appurv Jain and Amrith Akula
 */
public class SNNController {
    DifferentialEvolution networkParamGen;
    SNNClassifier network;
    Data data;
    public final double IDEAL_MIN_ACCURACY = 0.9;
    
    public SNNController(int[] layerSizes, Data data ) 
            throws ListLengthsDifferentException{
        this.network = new SNNClassifier(layerSizes);
        this.data = data;
        this.networkParamGen = new DifferentialEvolution(layerSizes);
        this.initNetworkParamGenerator();
    }
    
    public double getTrainAccuracy(ArrayList<Double> params[][]) 
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
    
    private void initNetworkParamGenerator() 
            throws ListLengthsDifferentException{
        ArrayList<Double> accuracyList = new ArrayList<Double>();
        for(ArrayList<Double>[][] param : this.networkParamGen.getCurrPopulation())
            accuracyList.add(this.getTrainAccuracy(param));
        this.networkParamGen.setPopulationAccuracy(accuracyList);
        
        this.networkParamGen.setBestIndivAccuracy
                (this.getTrainAccuracy(this.networkParamGen.getBestIndiv()));
    }
    
    private void runNewGeneration() 
            throws ListLengthsDifferentException{
        ArrayList<Double> accuracyList = new ArrayList<Double>();
        ArrayList<ArrayList<Double>[][]> newGen;
        newGen = this.networkParamGen.generateNewGeneration();
        for(ArrayList<Double>[][] individual: newGen)
            accuracyList.add(this.getTrainAccuracy(individual));
        this.networkParamGen.makeAppropriateReplacements(newGen, accuracyList);
    }
    
    public void runTillAccuracy(double minAccuracy) throws ListLengthsDifferentException{
        while(this.networkParamGen.getBestIndivAccuracy() < minAccuracy)
            this.runNewGeneration();
    }
    
    public void run() throws ListLengthsDifferentException{
        while(this.networkParamGen.getBestIndivAccuracy() < this.IDEAL_MIN_ACCURACY)
            this.runNewGeneration();
    }
    
    public void run(int count) throws ListLengthsDifferentException{
        while(count-- > 0)
            this.runNewGeneration();
    }
}
