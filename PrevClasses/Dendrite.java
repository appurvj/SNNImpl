
/*value = dendritePotential
 * 
 */

public class Dendrite {
	private double dendriteInput = 0.0f;
	private double dendritePotential = 0.0f;
	private double potentialRelaxation= 0.0f;
	private double weight= 0.0f;
	private double step= 0.0f;
	private double potentialDecayTime= 1.0f;
	public static final int INTERNAL_PARAMETERS = 2;
	
	
	
	public Dendrite(double inputValue, double value, double valueRelaxation,
			double weight, double step, double valueDecayTime) {
		this.dendriteInput = inputValue;
		this.dendritePotential = value;
		this.potentialRelaxation = valueRelaxation;
		this.weight = weight;
		this.step = step;
		this.potentialDecayTime = valueDecayTime;
	}
        
        //Empty Default Constructor
        public Dendrite(){}



	//Sets the Decay Value Time & adjusts relaxation
	public void setPotentialDecayTime(double valueDecayTime){
		if(valueDecayTime < 0.0f){
			System.out.println("Decay time must be greater than 0");
                        return;
		}
                potentialDecayTime = valueDecayTime;
		potentialRelaxation = (double) Math.exp(-this.step/this.potentialDecayTime);
	}
        
        public double getPotentialDecayTime(){
            return this.potentialDecayTime;
        }
	
	//Sets the step and adjusts Relaxation value
	public void setStep(double step){
		if(step > 0.0f){
			System.out.println("Step must be greater than 0.0f");
                        return;
		}
		this.step = step;
		potentialRelaxation = (double) Math.exp(-this.step/this.potentialDecayTime);
		
	}
        
        public double getStep(){
            return this.step;
        }
        
        public void setWeight(double weight){
            this.weight = weight;
        }
        
        public double getWeight(){
            return this.weight;
        }
	
	
	public void handleImpulse(){
		this.dendriteInput = 1.0f;
	}
	
	//Updates the value by taking into consideration weights, and relaxation value
	public void update(){
		if(this.step > 0.0f){
			System.out.println("Step must be greater than 0.0f");
                        return;
		}
		this.dendritePotential = ((this.weight * this.dendriteInput) + (this.potentialRelaxation*this.dendritePotential));
                dendriteInput = 0.0f;
	}
	
	//Resets the value and input value to 0
	public void reset(){
		this.dendritePotential=0.0f;
		this.dendriteInput = 0.0f;
	}
	
	//Gets the number of parameters
	public int getParametersCount(){
		return INTERNAL_PARAMETERS;		
	}
	//Gets the the actual parameters
	public double[] getParameters(){
            double opParams[] = new double[INTERNAL_PARAMETERS];
            opParams[0]= this.weight;
            opParams[1]= this.potentialDecayTime;
            return opParams;
	}
	
	//Sets the parameters and adjusts relaxation value
	public void setParameters(double pParameters[]){
		this.weight = pParameters[0];
		this.potentialDecayTime = pParameters[1];
		
		this.potentialRelaxation = (double) Math.exp(-this.step/this.potentialDecayTime);
	}
	
	//Evaluates ValueDecayTime and checks if its greater than 0.0
	public double evaluateParameters(){
		return this.potentialDecayTime > 0.0?0.0:1.0;
	}
        public double getImpulseValue(){
            return dendritePotential;
        }

}
