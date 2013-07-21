/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is an abstract class meant to capture the basic features required in any 
 * neuron. This will later be extended to accommodate the different requirements
 * from Neuron in the Input, Hidden, and Output layers respectively.
 * @author Appurv Jain and Amrith Akula
 */
public abstract class SpikeNeuron {
    public SNType neuronType;
    protected double axonPotential = 0;
    protected double potentialRelaxation;
    protected double step;
    protected double potentialDecayTime;
    protected final int layerNo;
    protected final int neuronNo;

    public SpikeNeuron(SNType nType, double step, double potentialDecayTime, int layer, int neuron){
        neuronType = nType; 
        this.step = step;
        this.potentialDecayTime = potentialDecayTime;
        this.potentialRelaxation = Math.exp(-this.step/this.potentialDecayTime);
        this.layerNo = layer;
        this.neuronNo = neuron;
        
    }
    
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
    
    public void reset(){
        this.axonPotential = 0;
    }
    
    
    public abstract void update();
    
    
}
