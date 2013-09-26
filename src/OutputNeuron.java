
import java.util.ArrayList;


/**
 * Captures the functionality of the neurons that will be make up the output
 * layer of the network. These are just include a minor mutation from Hidden 
 * Neurons. Since we are building a classifier, the class of the 
 * input is decided by the first firing output neuron. Therefore, the output
 * neurons will only fire once.
 * 
 * @author Appurv Jain and Amrith Akula
 */
public class OutputNeuron extends HiddenNeuron {    
    public OutputNeuron(double step, ArrayList<SpikeNeuron> preNeurons, 
             int layer, int neuron){

        super(step, preNeurons, layer, neuron);
        this.neuronType = SNType.OUTPUT;
        
    }
    
@Override
    public void update(){
        for(int i = 0; i < this.weightList.size(); i++){
            this.internalPotential += this.weightList.get(i)
                    * this.preSynapticNeurons.get(i).axonPotential;
            if(this.internalPotential > this.spikeThreshhold){
                this.spiked = true;
                return;
            }
        }
    }


}
