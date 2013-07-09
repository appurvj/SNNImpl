import java.util.LinkedList;
class NeuronNetwork{
    private double step;
    private LinkedList<Soma> somaList = new LinkedList<Soma>();
    private int inputCnt;

    public NeuronNetwork(int inputCnt, double step){
        this.step = step;
        this.inputCnt = inputCnt;
        for(int i = 0; i < inputCnt; i++)
            somaList.add(new Soma(inputCnt, step));
    }

	void handleImpulse(int index){
        for(Soma soma:somaList)
            soma.handleImpulse(index);
    }

	boolean checkOutputImpulse(int index);

	void update(){
        for(Soma soma: somaList)
            soma.update();
    }

	void reset(){
        for(Soma soma: somaList)
            soma.reset();
    }    

    double getStep(){
        return this.step;
    }

    void setStep()
    
}
