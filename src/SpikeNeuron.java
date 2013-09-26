
import java.util.ArrayList;

/**
 * This is an abstract class meant to capture the basic features required in any 
 * neuron. This will later be extended to accommodate the different requirements
 * from Neuron in the Input, Hidden, and Output layers respectively.
 * 
 * @author Appurv Jain and Amrith Akula
 */


public abstract class SpikeNeuron{
    public SNType neuronType;
    protected double axonPotential = 0;
    protected double potentialRelaxation;
    protected double step;
    protected double potentialDecayTime;
    protected final int layerNo;
    protected final int neuronNo;
    public boolean spiked = false;

    public SpikeNeuron(SNType nType, double step, int layer, int neuron){
        neuronType = nType; 
        this.step = step;
        this.potentialRelaxation = Math.exp(-this.step/this.potentialDecayTime);
        this.layerNo = layer;
        this.neuronNo = neuron;
        setPotentialDecayTime(1);
        
    }
    
    /* 
     * Below are some setter abd getter methods
     */
    
    public void setPotentialDecayTime(double potDecT){
        if(potDecT < 0){
                System.out.println("Decay time must be greater than 0");
                return;
        }
        potentialDecayTime = potDecT;
        potentialRelaxation = Math.exp(-this.step/this.potentialDecayTime);
    }
    
    
    public double getAxonPotential(){
        return axonPotential;
    }

    public void setAxonPotential(double pot){
        this.axonPotential = pot;
    }
    
    public SNType getType(){
        return neuronType;
    }
    
    
    /*
     * The methods listed below would perform diffrent functions based on 
     * whether the neuron is at the input layer, hidden layer or output layer.
     * Therefore, these have been left as abstract and will be overridden in 
     * the derived classes
     */
    public abstract void reset();
    public abstract void update();
    public abstract void setParams(ArrayList<Double> params) throws ListLengthsDifferentException;
      
}
