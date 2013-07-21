import java.util.ArrayList;
/**
 *
 * @author Appurv_Air
 */
public class HiddenNeuron extends SpikeNeuron{
    protected double reSpikeThreshhold;
    protected double spikeThreshhold;
    protected ArrayList<SpikeNeuron> preSynapticNeurons;
    protected ArrayList<Double> weightList;
    protected double internalPotential = 0;
    
    
    public HiddenNeuron(double step, double potentialDecayTime, 
            double upperPot, double lowerPot, ArrayList<SpikeNeuron> preNeurons, 
            ArrayList<Double> weights, int layer, int neuron) 
            throws ListLengthsDifferentException{  
        
        super(SNType.HIDDEN, step, potentialDecayTime, layer, neuron);
        this.reSpikeThreshhold = upperPot;
        this.spikeThreshhold = lowerPot;
        this.preSynapticNeurons = preNeurons;
        this.weightList = weights;
        if(this.weightList.size() != this.preSynapticNeurons.size())
            throw new ListLengthsDifferentException();
    }
    
    public void setParams(ArrayList<Double> weights){
        this.weightList  = weights;
    }

    @Override
    public void update() {
        super.axonPotential *= super.potentialRelaxation;
        if(super.axonPotential >= this.reSpikeThreshhold)
            return;
        for(int i = 0; i < this.weightList.size(); i++){
            this.internalPotential += this.weightList.get(i)
                    * this.preSynapticNeurons.get(i).axonPotential;
            if(this.internalPotential > this.spikeThreshhold){
                super.axonPotential = 1;
                this.internalPotential = 0;
                
                return;
            }
        }
    }   
}


