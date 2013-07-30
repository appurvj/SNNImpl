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
    public static final int OTHER_PARAMS = 3;
    
    
    public HiddenNeuron(double step, ArrayList<SpikeNeuron> preNeurons, 
            int layer, int neuron) {  
        
        super(SNType.HIDDEN, step, layer, neuron);
        this.preSynapticNeurons = preNeurons;
    }
    
    @Override
    public void setParams(ArrayList<Double> params) throws ListLengthsDifferentException{
        //the params list contains weights and thresholds as the last two in the list
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