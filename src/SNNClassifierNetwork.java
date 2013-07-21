
import java.util.ArrayList;


/**
 * This is the network of spike neurons that 
 * @author Appurv Jain
 */
public class SNNClassifierNetwork {
    //Network inthe form of ans list of lists will cleaner than a single list
    //Each list will represent a layer
    public final int INPUT_LAYER = 0;
    public final int OUTPUT_LAYER;
    private ArrayList<ArrayList<SpikeNeuron>> network; 
    private ArrayList<Double> inputs;
    private ArrayList<ArrayList<Double>> params; //will include the weights and threshholds
    
    public  SNNClassifierNetwork(int[] layerSize){
        this.OUTPUT_LAYER = layerSize.length - 1;
        for(int i = 0; i < layerSize[this.INPUT_LAYER]; i++){
            //Create an ArrayList of the 
            ArrayList<SpikeNeuron> inputList = new ArrayList<SpikeNeuron>
                                              (layerSize[this.INPUT_LAYER]);
        }
    }
    
}
