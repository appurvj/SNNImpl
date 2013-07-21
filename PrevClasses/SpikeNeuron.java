import java.util.ArrayList;


public class SpikeNeuron {
	
	private static final int INTERNAL_PARAMETERS = 4;
	ArrayList<Dendrite>dendrites = new ArrayList<Dendrite>();
        private int inputSize;
	private double potential;
	private double step;
	private double output;
	private double potentialRelaxation;
	private double outputRelaxation;
	private double inputValue;
	private double refraction;		// n_0
	private double potentialDecayTime;
	private double outputDecayTime;
	private double threshold; 
	Dendrite dendrite;
	
	public SpikeNeuron(int inputNum, double step){
		this.step = step;
		this.potential = 0.0f;
		this.output = 0.0f;
		this.potentialRelaxation = 0.0f;
		this.outputRelaxation = 0.0f;
		this.potentialDecayTime = 1.0f;
		this.outputDecayTime = 1.0f;
		this.threshold = 0.0f;
		this.inputSize = inputNum;
		
                if(this.step < 0){
                    System.out.println("Step Must be greater than 0");
                    return;
                }
                
		for(Dendrite dendrite:dendrites){
			dendrite.setStep(step);
		}
	}
	
	
	
	
	//Handling the impulse - need to add array once created
	public void handleImpulse(int dendriteNo){
            if(dendriteNo < 0 || dendriteNo > dendrites.size()){
                System.out.println("Invalid input index");
                return;
            }
                
		dendrites.get(dendriteNo).handleImpulse();
	}
	
	//Sets the output decay 
	public void setOutputDecayTime(double decayTime){
            if(decayTime < 0){
                    System.out.println("Decay time must be greater than 0");
                    return;
            }
            this.outputDecayTime = decayTime;
            this.outputRelaxation = (double) Math.exp(-this.step/this.outputDecayTime);
	}
        
        public void setPotentialDecayTime(double decayTime){
            if(decayTime < 0){
                    System.out.println("Decay time must be greater than 0");
                    return;
            }
            this.potentialDecayTime = decayTime;
            this.potentialRelaxation = (double) Math.exp(-this.step/this.potentialDecayTime);            
        }
        
	//sets the threshold
	public void setThreshold(double threshold){
		this.threshold = threshold;
	}
	
	//calculating weights and values from input
	public void update(){
		double potentialValue = 0.0f;
		//for each loop --> sum inputValues from Inputs
		for(Dendrite dendrite: dendrites){
                    dendrite.update();
                    potentialValue += dendrite.getImpulseValue();
                }
		potential = potentialRelaxation * potential + inputValue - refraction *
				output - threshold;
		output = potential >= 0.0f?1.0f:output;
	}
	
	public void reset(){
		potential = 0.0f;
		output = 0.0f;
		
		//for each loop reset all the inputs
                for(Dendrite dendrite:dendrites){
                    dendrite.reset();
                }
	}
        
        public double evaluateParameters(){
            double potential = 0;
            for(Dendrite dendrite:dendrites)
                potential += dendrite.evaluateParameters();
            
            potential += this.outputDecayTime > 0?0:1;
            potential += this.potentialDecayTime > 0?0:1;
            potential += this.refraction > 0?0:1;
            potential += this.threshold > 0?0:1;
            
            return potential;
        }
        
        
        public void setParameters(ArrayList<double[]> params){
            double[] thisParams = params.get(0);
            this.outputDecayTime = thisParams[0];
            this.potentialDecayTime = thisParams[1];
            this.refraction = thisParams[2];
            this.threshold = thisParams[3];
            
            this.outputRelaxation = Math.exp(-this.step/this.outputDecayTime);
            this.potentialRelaxation = Math.exp(-this.step/this.potentialDecayTime);
            
            for(int i = 0; i < dendrites.size(); i++)
                dendrites.get(i).setParameters(params.get(i+1));
        }
        
        public ArrayList<double[]> getParameters(){
            ArrayList<double[]> params = new ArrayList<double[]>();
            double[] thisParams = new double[INTERNAL_PARAMETERS];
            thisParams[0] = this.outputDecayTime;
            thisParams[1] = this.potentialDecayTime;
            thisParams[2] = this.refraction;
            thisParams[3] = this.threshold;
            params.add(thisParams);
            
            for(Dendrite dendrite:dendrites)
                params.add(dendrite.getParameters());
            
            return params;
        }	
        
        public boolean isImpulse(){
            return output==1;
        }
	
}
