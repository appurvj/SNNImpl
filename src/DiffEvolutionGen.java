
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is is used to generate populations of input parameters for the 
 * network. 
 * 
 * @author Appurv Jain and Amrith Akula
 */
public class DiffEvolutionGen {
    public static final double MIN_VAL = 0.000000000000001;
    public static final double OFFSET = 0.1;
    public static final int POPULATION_SIZE = 25;
    public static final double MAX_JUMP = 0.5;
    public static final double MUTATION_PROB = 0.2;
    
    private int[] layerSize;
    private Random randGen = new Random();
    private Random mutationGen = new Random();
    
    private ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> population
            = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> (POPULATION_SIZE);

    
    /**
     * Generates a random population
     * 
     * @param layerSizes: Array of sizes of layers
     */
    DiffEvolutionGen(int[] layerSizes) {
        this.layerSize = layerSizes;
        for(int i = 0; i < DiffEvolutionGen.POPULATION_SIZE; i++){
            this.population.add(this.generateRandomIndividual());
        }
    }

    
    
    ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> getCurrPopulation() {
        return this.population;
    }


    /**
     * Generate a new generation from the current population
     * @return 
     */
    public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>
            generateNewGen(){
        ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen
                = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>(DiffEvolutionGen.POPULATION_SIZE);
        for(ArrayList<ArrayList<ArrayList<Double>>> indiv: population)
            newGen.add(this.generateChild(indiv));
        return newGen;
    }


    private ArrayList<ArrayList<ArrayList<Double>>> generateRandomIndividual(){
        ArrayList<ArrayList<ArrayList<Double>>> newIndiv;
        newIndiv = new ArrayList<ArrayList<ArrayList<Double>>>(this.layerSize.length);
        
        
        ArrayList<ArrayList<Double>> singleLayerParam
                = new ArrayList<ArrayList<Double>>(this.layerSize[SNClassifierNetwork.INPUT_LAYER_INDEX]);
        ArrayList<Double> singleNeuronParam;
        for(int i = 0; i < this.layerSize[SNClassifierNetwork.INPUT_LAYER_INDEX]; i++){
            singleNeuronParam = new ArrayList(1);
            singleNeuronParam.add(this.randGen.nextDouble() + DiffEvolutionGen.MIN_VAL);
            singleLayerParam.add(singleNeuronParam);            
        }
        newIndiv.add(singleLayerParam);
        
        
        for(int i = 1; i < this.layerSize.length; i++){
            singleLayerParam
                = new ArrayList<ArrayList<Double>>(this.layerSize[i]);
            for(int j = 0; j < this.layerSize[i]; j++){
                singleNeuronParam = new ArrayList(layerSize[i-1] + HiddenNeuron.OTHER_PARAMS);
                for(int k = 0; k < layerSize[i-1] + HiddenNeuron.OTHER_PARAMS; k++) 
                    singleNeuronParam.add(this.randGen.nextDouble() + DiffEvolutionGen.MIN_VAL);
                singleLayerParam.add(singleNeuronParam);            
            }
            newIndiv.add(singleLayerParam);
        }
        
        return newIndiv;
    }
    
    
    /**
     * Generates a new individial 'close' to the parent in features. 
     * @param parent
     * @return 
     */
    private ArrayList<ArrayList<ArrayList<Double>>> 
            generateChild(ArrayList<ArrayList<ArrayList<Double>>> parent){
        if(mutationGen.nextDouble() < DiffEvolutionGen.MUTATION_PROB)
            return this.generateRandomIndividual();
        ArrayList<ArrayList<ArrayList<Double>>> child
                = new ArrayList<ArrayList<ArrayList<Double>>>(parent.size());
        ArrayList<ArrayList<Double>> singleLayer;
        ArrayList<Double> singleNeuron;
        for(ArrayList<ArrayList<Double>>layer: parent){
            singleLayer = new ArrayList<ArrayList<Double>>(layer.size());
            for(ArrayList<Double> neuron: layer){
                singleNeuron = new ArrayList<Double>(neuron.size());
                for(double param: neuron){
                    double addValue = param * (randGen.nextDouble() + 
                            OFFSET) * DiffEvolutionGen.MAX_JUMP;
                    double newParam = this.randGen.nextBoolean()?param+addValue:param-addValue;
                    
                    if(newParam < 0)
                        newParam = -newParam;
                    if(newParam > 1)
                        newParam -=1;

                   
                    singleNeuron.add(newParam);
                }
                singleLayer.add(singleNeuron);
            }
            child.add(singleLayer);
        }
        return child;       
    }

    void replaceIndiv(int i, ArrayList<ArrayList<ArrayList<Double>>> newIndiv) {
        this.population.set(i, newIndiv);
    }
    
   
}
