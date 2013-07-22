import java.util.ArrayList;
/**
 *
 * @author Appurv_Air
 */
public class HiddenNeuron extends SpikeNeuron{
    protected double reSpikeThreshhold;
    protected double spikeThreshhold;
    protected SpikeNeuron[] preSynapticNeurons;
    protected Double[] weightList;
    protected double internalPotential = 0;
    
    
    public HiddenNeuron(double step, SpikeNeuron[] preNeurons, 
            int layer, int neuron) {  
        
        super(SNType.HIDDEN, step, layer, neuron);
        this.preSynapticNeurons = preNeurons;
    }
    
    
    public void setParams(ArrayList<Double> params) throws ListLengthsDifferentException{
        //the params list contains weights and thresholds as the last two in the list
        this.potentialDecayTime = params.get(0);
        this.spikeThreshhold = params.get(1);
        this.reSpikeThreshhold = params.get(2);
        for(int i = 0; i < 3; i++)
            params.remove(0);
        this.weightList  = params.toArray(weightList);
        if(this.weightList.length != this.preSynapticNeurons.length)
            throw new ListLengthsDifferentException(" List lengths dont match in setParams");
    }

    @Override
    public void update() {
        super.axonPotential *= super.potentialRelaxation;
        if(super.axonPotential >= this.reSpikeThreshhold)
            return;
        for(int i = 0; i < this.weightList.length; i++){
            this.internalPotential += this.weightList[i]
                    * this.preSynapticNeurons[i].axonPotential;
            if(this.internalPotential > this.spikeThreshhold){
                super.axonPotential = 1;
                this.internalPotential = 0;                
                return;
            }
        }
    }   
}


