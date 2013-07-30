
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Appurv Jain and Amrith Akula
 */
public class Data {
    public static final String SPLIT_REGEX = "[\t ,]+";
    
    public  double[][] trainingInput;
    public  int[] trainingClass;
    public  double[][] testInput;
    public  int[] testClass;
    public int totalInputCols;
    public  double trainingRatio;
    
    public Data(String dataPath, int classifierCol, double trainRat) 
            throws FileNotFoundException, IOException{
        this.trainingRatio = trainRat;
        BufferedReader file = new BufferedReader(new FileReader(dataPath));

        ArrayList<String> dataList = new ArrayList<String>();
        String row = file.readLine();
        int totalCols  = this.getNumElements(row);
        while(row != null){
            dataList.add(row);
            row = file.readLine();
        }
        shuffle(dataList);
        shuffle(dataList);
       
        int lastTrainRowIndex = (int)(dataList.size()*this.trainingRatio);
System.out.println("Total Training Values : " + lastTrainRowIndex);

        
System.out.println("Total cols identified : " + totalCols);
        this.totalInputCols = totalCols - 1; 
        
        this.trainingInput = new double[lastTrainRowIndex + 1][this.totalInputCols];
        this.trainingClass = new int[lastTrainRowIndex + 1];
        
        this.testInput = new double[dataList.size() - lastTrainRowIndex][this.totalInputCols];
        this.testClass = new int[dataList.size() - lastTrainRowIndex];
        

        String[] indivCols;
        for(int i = 0; i <= lastTrainRowIndex; i++){
            indivCols = dataList.get(i).split(Data.SPLIT_REGEX);
            int colIndex = 0;
            for(int j = 0; j < indivCols.length; j++){
                if(j == classifierCol)
                    this.trainingClass[i] = Integer.parseInt(indivCols[j]);
                else
                    this.trainingInput[i][colIndex++] = Double.parseDouble(indivCols[j]);
            }
            
        }
        
        int testRowIndex = lastTrainRowIndex + 1;
        for(int i = testRowIndex; i < dataList.size(); i++){
            indivCols = dataList.get(i).split(Data.SPLIT_REGEX);
            int colIndex = 0;
            for(int j = 0; j < indivCols.length; j++){
                if(j == classifierCol)
                    this.testClass[i - testRowIndex] = Integer.parseInt(indivCols[j]);
                else{
                   BigDecimal num = new BigDecimal(indivCols[j]);   
                    this.testInput[i - testRowIndex][colIndex++] = num.doubleValue();
                }
            }
        }
        
        this.normalize();
    }
    
    private void shuffle(ArrayList<String> list){
        Random random = new Random();
        int index ;
        for(int i = 0; i < list.size(); i++){
            index = random.nextInt(list.size());
            String temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }
    
    private int getNumElements(String row){
        return (row.split(SPLIT_REGEX)).length;       
    }
    
    private void normalize(){
        double[] maxVals = new double[this.trainingInput[0].length];
        for(int i = 0; i < this.trainingInput.length; i++ ){
            for(int j = 0; j < maxVals.length; j++){
                if(maxVals[j] < this.trainingInput[i][j])
                    maxVals[j] = this.trainingInput[i][j];
            }
        }
        
        for(int i = 0; i < this.testInput.length; i++){
            for(int j = 0; j < maxVals.length; j++){
                if(maxVals[j] < this.testInput[i][j])
                    maxVals[j] = this.testInput[i][j];
            }
        }
         
        for(int i = 0; i < this.trainingInput.length; i++ ){
            for(int j = 0; j < maxVals.length; j++){
                this.trainingInput[i][j] /= maxVals[j];
            }
        }
         
        for(int i = 0; i < this.testInput.length; i++){
            for(int j = 0; j < maxVals.length; j++){
                this.testInput[i][j] /= maxVals[j];
            }
        }

    }
}
