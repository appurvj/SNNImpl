
import java.util.ArrayList;


/**
 * This class simulates a network of spike neurons. All the functionality of the
 * network is captured in this class. This class is one of the main components
 * of the system and is the underlying model behind the operation of the program.
 * 
 * @author Appurv Jain and  Amrith Akula
 */
public class SNClassifierNetwork {
    
    
    public static final double STEP = 0.5;
    public static final int INPUT_LAYER_INDEX = 0;
    public final int OUTPUT_LAYER;
    public static final int MAX_ITERATIONS = 250;
    
    //Network in the form of a list of lists is cleaner than a single list
    //Each array list will represent a layer (input/hidden/output)
    //List of lists - will include the weights and threshholds
    private ArrayList<ArrayList<SpikeNeuron>> network; 
    
    
    /**
     * The constructor generates and initializes the network based on the 
     * dimensions provided. It also links the neurons in a particular layer as
     * inputs for the next layer
     * @param layerSize : Array containing sizes of the network layers
     */
    public  SNClassifierNetwork(int[] layerSize){
        this.OUTPUT_LAYER = layerSize.length - 1;
        network = new ArrayList<ArrayList<SpikeNeuron>>(layerSize.length);
        
        //Create an array for input neuron layer
        ArrayList<SpikeNeuron> neuronLayer = new ArrayList<SpikeNeuron>(layerSize[this.INPUT_LAYER_INDEX]);
        
        //Generate input layer and add to network
        for(int i = 0; i < layerSize[this.INPUT_LAYER_INDEX]; i++)
            neuronLayer.add(new InputNeuron(STEP, this.INPUT_LAYER_INDEX, i));
        network.add(neuronLayer);
        
        //Generate hidden layer(s) and add to network
        for(int i = 1; i < this.OUTPUT_LAYER; i++){
            neuronLayer = new ArrayList<SpikeNeuron>(layerSize[i]);
            for(int j = 0; j < layerSize[i]; j++)
                neuronLayer.add(new HiddenNeuron(STEP, network.get(i-1), i, j));
            network.add(neuronLayer);
        }
        
        //Generate output layer and add to network
        neuronLayer = new ArrayList<SpikeNeuron>(layerSize[this.OUTPUT_LAYER]);
        for(int i = 0; i < layerSize[this.OUTPUT_LAYER]; i++)
            neuronLayer.add(new OutputNeuron(STEP, network.get(this.OUTPUT_LAYER - 1),
                    this.OUTPUT_LAYER, i));
        network.add(neuronLayer);     
    }
    
    
    /**
     * Setter for particular layer
     * @param input
     * @throws ListLengthsDifferentException 
     */
    private void setInputs(double[] input) throws ListLengthsDifferentException{
        ArrayList<SpikeNeuron> inputLayer = this.network.get(INPUT_LAYER_INDEX);
        if(input.length != inputLayer.size())
            throw new ListLengthsDifferentException();
        
        //Set up input neurons
        for(int i = 0; i < input.length; i++)
            inputLayer.get(i).setAxonPotential(input[i]);
    }
    
    /*
     * Set the paramaters of the the network
     */
    public void setParams(ArrayList<ArrayList<ArrayList<Double>>> params)
            throws ListLengthsDifferentException{
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
    
    /**
     * This methods runs the network for the set input and returns the number
     * @param input
     * @return
     * @throws ListLengthsDifferentException 
     */
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
    
    /**
     * Reset each neuron in network
     */
    public void reset(){
        for(ArrayList<SpikeNeuron> layer: this.network){
            for(SpikeNeuron neuron: layer){
                neuron.reset();
            }
        }
    }
    
}
