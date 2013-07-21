
import java.util.ArrayList;

/**
 * Captures the functionality of the neurons that will be make up the outputl
 * layer of the network. Since we are building a classifier, the class of the 
 * input is decided by the first firing output neuron. Therefore, the output
 * neurons will only fire once.
 * @author Appurv Jain, Amrith
 */
public class OutputNeuron extends HiddenNeuron {    
    protected boolean spiked = false;
    public OutputNeuron(double step, double potentialDecayTime, 
            double upperPot, double lowerPot, ArrayList<SpikeNeuron> preNeurons, 
            ArrayList<Double> weights, int layer, int neuron) throws ListLengthsDifferentException{

        super(step, potentialDecayTime, upperPot, lowerPot, preNeurons, weights,layer, neuron);
        if(this.weightList.size() != this.preSynapticNeurons.size())
            throw new ListLengthsDifferentException();
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

    public boolean hasSpiked(){
        return spiked;
    }

}
