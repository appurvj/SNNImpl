
import java.util.ArrayList;

/**
 * This class captures the functionality of the neurons that will serve as input
 * neurons in the network. Input neurons will not spike. They will just have a leaky output
 * relative to the input given to them.
 * 
 * @author Appurv Jain and Amrith Akula
 */
public class InputNeuron extends SpikeNeuron{
       
    public InputNeuron(double step, int layer, int neuron){
        super(SNType.INPUT, step, layer, neuron);
    }
    
    
    /**
     * This method is used to simulate the leaky nature of the axon potentials
     */        
    @Override
    public void update() {
        super.axonPotential *= super.potentialRelaxation; 
    }

    
    /**
     * Resets the axon potential
     */
    @Override
    public void reset(){
        super.axonPotential = 0;
    }
    
    
    /**
     * This method sets the neuron input weights based on the list provided
     * @param params
     * @throws ListLengthsDifferentException 
     */
    @Override
    public void setParams(ArrayList<Double> params) throws ListLengthsDifferentException{
        this.potentialDecayTime = params.get(0);
    }    
}
