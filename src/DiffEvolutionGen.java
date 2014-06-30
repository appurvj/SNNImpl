
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
    public static final int POPULATION_SIZE = 300;
    public static final double MAX_JUMP = 0.5;
    public static final double MUTATION_PROB = 0.20;
    
    private int[] layerSize;
    private Random randGen = new Random();
    private Random mutationGen = new Random(System.nanoTime());
    
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

    
    /**
     * Getter for current population 
     */
    ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> getCurrPopulation() {
        return this.population;
    }


    /**
     * Generate a new generation from the current population
     * @return list of all individuals in new generation 
     */
    public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>
            generateNewGen(){
        ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> newGen
                = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>(DiffEvolutionGen.POPULATION_SIZE);
        for(ArrayList<ArrayList<ArrayList<Double>>> indiv: population)
            newGen.add(this.generateChild(indiv));
        return newGen;
    }


    /**
     * Generate a random individual in in the population. Uses PRNGs.
     * @return random Individual
     */
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
     * Generates a new individual 'close' to the parent in features(values). 
     * @param parent: individual from which the child will be generated
     */
    private ArrayList<ArrayList<ArrayList<Double>>> 
            generateChild(ArrayList<ArrayList<ArrayList<Double>>> parent){
        if(this.shouldMutate())
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
    
    
    /**
     * Used to decide if mutation should occur
     * @return true if mutation should take place, else false
     */
    public boolean shouldMutate(){
        return mutationGen.nextDouble() < DiffEvolutionGen.MUTATION_PROB;
    }

    /**
     * Swaps the particular individual in the population with the individual
     * passed in
     * @param i: Index of the individual to be replaced
     * @param newIndiv: The new individual that will replace current at the index 
     */
    void replaceIndiv(int i, ArrayList<ArrayList<ArrayList<Double>>> newIndiv) {
        this.population.set(i, newIndiv);
    }
    
   
}
