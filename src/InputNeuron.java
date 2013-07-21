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
       
    public InputNeuron(double step, double potentialDecayTime, int layer, int neuron){
        super(SNType.INPUT, step, potentialDecayTime, layer, neuron);
    }
    
            
    @Override
    public void update() {
        super.axonPotential *= super.potentialRelaxation; 
    }

    
    public void reset(){
        super.reset();
    }
    
}
