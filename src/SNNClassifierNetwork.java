
import java.util.ArrayList;


/**
 * This class simulates a network of spike neurons. All the functionality of the
 * network is captured in this class. This class will interact with the class 
 * responsible for differential evolution while running the network.
 * @author Appurv Jain and  Amrith Akula
 */
public class SNNClassifierNetwork {
    //Network in the form of a list of arrays will cleaner than a single list
    //Each array will represent a layer (input, hidden, output)
    
    public static final double STEP = 0.5;
    public static final int INPUT_LAYER_INDEX = 0;
    public final int OUTPUT_LAYER;
    public static final int MAX_ITERATIONS = 250;
    private ArrayList<ArrayList<SpikeNeuron>> network; 
    
    //Array of lists - will include the weights and threshholds
    //private ArrayList<Double>[] params; 
    
    public  SNNClassifierNetwork(int[] layerSize){
        this.OUTPUT_LAYER = layerSize.length - 1;
        network = new ArrayList<ArrayList<SpikeNeuron>>(layerSize.length);
        
        //Create an array for input neuron layer
        ArrayList<SpikeNeuron> neuronLayer = new ArrayList<SpikeNeuron>(layerSize[this.INPUT_LAYER_INDEX]);
        
        for(int i = 0; i < layerSize[this.INPUT_LAYER_INDEX]; i++)
            neuronLayer.add(new InputNeuron(STEP, this.INPUT_LAYER_INDEX, i));
        network.add(neuronLayer);
        
        for(int i = 1; i < this.OUTPUT_LAYER; i++){
            neuronLayer = new ArrayList<SpikeNeuron>(layerSize[i]);
            for(int j = 0; j < layerSize[i]; j++)
                neuronLayer.add(new HiddenNeuron(STEP, network.get(i-1), i, j));
            network.add(neuronLayer);
        }
        
        neuronLayer = new ArrayList<SpikeNeuron>(layerSize[this.OUTPUT_LAYER]);
        for(int i = 0; i < layerSize[this.OUTPUT_LAYER]; i++)
            neuronLayer.add(new OutputNeuron(STEP, network.get(this.OUTPUT_LAYER - 1),
                    this.OUTPUT_LAYER, i));
        network.add(neuronLayer);     
    }
    
    
    
    private void setInputs(double[] input) throws ListLengthsDifferentException{
        ArrayList<SpikeNeuron> inputLayer = this.network.get(INPUT_LAYER_INDEX);
        if(input.length != inputLayer.size())
            throw new ListLengthsDifferentException();
        
        //Set up input neurons
        for(int i = 0; i < input.length; i++)
            inputLayer.get(i).setAxonPotential(input[i]);
    }
    
    /*
     * Params are stored in the following form:
     * An array of lists of neuron params;each neuron param is also a list
     * Every element in the array represents the combined params of a single layer,
     * Each list (except input) will include decayTime, threshholds and weights
     * Input layer params consist solely of decay time
     */
    public void setParams(ArrayList<ArrayList<ArrayList<Double>>> params)throws ListLengthsDifferentException{
        for(int i = 0; i < params.size(); i++){
            for(int j = 0; j < params.get(i).size(); j++){
                try{
                    SpikeNeuron neuron = this.network.get(i).get(j);
                    neuron.setParams(params.get(i).get(j));
                }catch(ListLengthsDifferentException e){
                    throw new ListLengthsDifferentException(e.getMessage() +
                            "\nLayer: " + i + "\nNeuron: " + j);
                }
            }
        }
    }
    
    
    public int run(double[] input)throws ListLengthsDifferentException{
        this.reset();
        
        try{
            this.setInputs(input);
        }catch(ListLengthsDifferentException e){
            throw e;
        }
        
        int itr = this.MAX_ITERATIONS;
        while(itr-- > 0){
            for(ArrayList<SpikeNeuron> layer:network){
                for(SpikeNeuron neuron:layer){
                    neuron.update();

                    if(neuron.layerNo == this.OUTPUT_LAYER){
                        if(((OutputNeuron)neuron).hasSpiked()){
                            return neuron.neuronNo + 1;
                        }
                    }
                }
            }
        }  
        //if the network does not spike even after the Maximum set number of itrns
        //then something might be wrong
        return -1;   
    }
    
    
    public void reset(){
        for(ArrayList<SpikeNeuron> layer: this.network){
            for(SpikeNeuron neuron: layer){
                neuron.reset();
            }
        }
    }
    
}
