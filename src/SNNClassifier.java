
import java.util.ArrayList;


/**
 * This class simulates a network of spike neurons. All the functionality of the
 * network is captured in this class. This class will interact with the class 
 * responsible for differential evolution while running the network.
 * @author Appurv Jain and   Amrith Akula
 */
public class SNNClassifier {
    //Network in the form of a list of arrays will cleaner than a single list
    //Each array will represent a layer (input, hidden, output)
    
    public static final double step = 1;
    public final int INPUT_LAYER = 0;
    public final int OUTPUT_LAYER;
    public final int MAX_ITERATIONS = 20;
    private ArrayList<SpikeNeuron[]> network; 
    
    //Array of lists - will include the weights and threshholds
    //private ArrayList<Double>[] params; 
    
    public  SNNClassifier(int[] layerSize){
        this.OUTPUT_LAYER = layerSize.length - 1;
        network = new ArrayList<SpikeNeuron[]>(layerSize.length);
        
        //Create an array for input neuron layer
        SpikeNeuron[] neuronLayer = new SpikeNeuron[layerSize[this.INPUT_LAYER]];
        
        for(int i = 0; i < layerSize[this.INPUT_LAYER]; i++)
            neuronLayer[i] = new InputNeuron(step, this.INPUT_LAYER, i);
        network.add(neuronLayer);
        
        for(int i = 1; i < this.OUTPUT_LAYER; i++){
            neuronLayer = new SpikeNeuron[layerSize[i]];
            for(int j = 0; j < layerSize[i]; j++)
                neuronLayer[j] = new HiddenNeuron(step, network.get(i-1), i, j);
            network.add(neuronLayer);
        }
        
        neuronLayer = new SpikeNeuron[layerSize[this.OUTPUT_LAYER]];
        for(int i = 0; i < layerSize[this.OUTPUT_LAYER]; i++)
            neuronLayer[i] = new OutputNeuron(step, network.get(this.OUTPUT_LAYER - 1),
                    this.OUTPUT_LAYER, i);
        network.add(neuronLayer);     
    }
    
    
    
    private void setInputs(double[] input) throws ListLengthsDifferentException{
        SpikeNeuron[] inputLayer = this.network.get(INPUT_LAYER);
        if(input.length != inputLayer.length)
            throw new ListLengthsDifferentException();
        
        //Set up input neurons
        for(int i = 0; i < input.length; i++)
            inputLayer[i].setAxonPotential(input[i]);
    }
    
    
    //Each list (except input) will include decayTime, threshholds and weights
    //Input layer params consist solely of decay time
    public void setParams(ArrayList<Double> params[][])throws ListLengthsDifferentException{
        for(int i = 0; i < params.length; i++){
            for(int j = 0; j < params[0].length; j++){
                try{
                    this.network.get(i)[j].setParams(params[i][j]);
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
            for(SpikeNeuron[] layer:network){
                for(SpikeNeuron neuron:layer){
                    neuron.update();

                    if(neuron.layerNo == this.OUTPUT_LAYER){
                        if(((OutputNeuron)neuron).hasSpiked());
                            return neuron.neuronNo + 1;
                    }
                }
            }
        }  
        //if the network does not spike even after the Maximum set number of itrns
        //then something might be wrong
        return -1;   
    }
    
    
    public void reset(){
        for(SpikeNeuron[] layer: this.network)
            for(SpikeNeuron neuron: layer)
                neuron.reset();
    }
    
}
