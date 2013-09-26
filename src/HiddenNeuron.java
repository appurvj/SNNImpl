import java.util.ArrayList;

/**
 * This class simulates the functionality of a neuron in the hidden layer of the 
 * network. These neurons keep on 'accumulating' the potential from the neurons 
 * feeding them till they cross a threshold and fire.
 * @author Appurv Jain and Amrith Akula
 */
public class HiddenNeuron extends SpikeNeuron{
    protected double reSpikeThreshhold;
    protected double spikeThreshhold;
    protected ArrayList<SpikeNeuron> preSynapticNeurons;
    protected ArrayList<Double> weightList;
    protected double internalPotential = 0;
    public static final int OTHER_PARAMS = 3;
    
    
    public HiddenNeuron(double step, ArrayList<SpikeNeuron> preNeurons, 
            int layer, int neuron) {  
        
        super(SNType.HIDDEN, step, layer, neuron);
        this.preSynapticNeurons = preNeurons;
    }
    
    
    /**
     * This method sets the weights and thresholds of the neuron.
     * The params list contains decay time and thresholds at the start of the
     * list followed by the dendrite weights.
     */
    @Override
    public void setParams(ArrayList<Double> params) throws ListLengthsDifferentException{
        ArrayList<Double> newList = new ArrayList<Double>(params);
        this.potentialDecayTime = newList.get(0);
        this.spikeThreshhold = newList.get(1);
        this.reSpikeThreshhold = newList.get(2);
        for(int i = 0; i < 3; i++)
            newList.remove(0);
        this.weightList  = newList;
        if(this.weightList.size() != this.preSynapticNeurons.size())
            throw new ListLengthsDifferentException(" List lengths dont match in setParams of Hidden Neuron");
    }

    
    
    /**
     * Updates the internal potential and spikes the neuron if internal potential
     * is above threshold. When neuron fires, the axon potential becomes 1 and
     * the internal potential becomes 0 again. After firing once, the neuron will
     * not update till the axon potential falls below a certain threshold.
     */
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
    
    /**
     * Reset neuron potential values
     */
    @Override
    public void reset(){
        super.axonPotential = 0;
        this.internalPotential = 0;
        this.spiked = false;
    }
    
    public boolean hasSpiked(){
        return spiked;
    }
}