
/**
 * Captures the functionality of the neurons that will be make up the outputl
 * layer of the network. Since we are building a classifier, the class of the 
 * input is decided by the first firing output neuron. Therefore, the output
 * neurons will only fire once.
 * @author Appurv Jain, Amrith
 */
public class OutputNeuron extends HiddenNeuron {    
    protected boolean spiked = false;
    public OutputNeuron(double step, SpikeNeuron[] preNeurons, 
             int layer, int neuron){

        super(step, preNeurons, layer, neuron);
        this.neuronType = SNType.OUTPUT;
        
    }
    
@Override
    public void update(){
        for(int i = 0; i < this.weightList.length; i++){
            this.internalPotential += this.weightList[i]
                    * this.preSynapticNeurons[i].axonPotential;
            if(this.internalPotential > this.spikeThreshhold){
                this.spiked = true;
                return;
            }
        }
    }

@Override
    public void reset(){
        super.reset();
        this.spiked = false;
    }


    public boolean hasSpiked(){
        return spiked;
    }

}
