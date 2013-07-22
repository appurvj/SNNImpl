
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class captures the functionality of the neurons that will serve as input
 * neurons. Input neurons will not spike. They will just have a leaky output
 * relative to the input given to them.
 * @author Appurv_Air
 */
public class InputNeuron extends SpikeNeuron{
       
    public InputNeuron(double step, int layer, int neuron){
        super(SNType.INPUT, step, layer, neuron);
    }
    
            
    @Override
    public void update() {
        super.axonPotential *= super.potentialRelaxation; 
    }

    
    public void reset(){
        super.axonPotential = 0;
    }
    
    public void setParams(ArrayList<Double> params) throws ListLengthsDifferentException{
        this.potentialDecayTime = params.get(0);
    }
    
}
