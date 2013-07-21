import java.util.ArrayList;
class NeuronNetwork{
    private double step;
    private ArrayList<SpikeNeuron> neurons = new ArrayList<SpikeNeuron>();
    private int inputCnt;
    private int neuronNum;


    public NeuronNetwork(int neuronNum, int inputCnt, double step){
        this.step = step;
        this.inputCnt = inputCnt;
        for(int i = 0; i < inputCnt; i++)
            neurons.add(new SpikeNeuron(inputCnt, step));
    }

    public void handleImpulse(int index){
        for(SpikeNeuron neuron:neurons)
            neuron.handleImpulse(index);
    }

	//boolean checkOutputImpulse(int index);   directly written inline where required

    public void update(){
        for(SpikeNeuron neuron: neurons)
            neuron.update();
    }

    public void reset(){
        for(SpikeNeuron neuron: neurons)
            neuron.reset();
    }    

    public double getStep(){
        return this.step;
    }

    
    public int getInputCnt(){
        return this.inputCnt;
    }
    
    public int getNeuronNum() {
        return this.neuronNum;
    }
    
    public SpikeNeuron getNeuron(int index){
        return this.neurons.get(index);
    }
    
    public ArrayList<ArrayList<double[]>> getParameters(){
        ArrayList<ArrayList<double[]>> params = new ArrayList<ArrayList<double[]>>();
        for(SpikeNeuron neuron:this.neurons)
            params.add(neuron.getParameters());
        return params;
    }
    
    public void setParameters(ArrayList<ArrayList<double[]>> params){
        if(params.size() != this.neurons.size()){
            System.out.println("Error!! External param list size not the same as network size!");
            return;
        }
        for(int i =0; i < this.neurons.size(); i++){
            this.neurons.get(i).setParameters(params.get(i));
        }
            
    }
    
    
}
