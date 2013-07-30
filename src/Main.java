
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Appurv_Air
 */
public class Main {    
    static int[] hiddenLayerSize = {10,8};
    static int totalSize = hiddenLayerSize.length + 2; //a layer each for input and output
    static int[] layerSize = new int[totalSize];
    static int NO_OF_RUNS = 30000;
    static double MIN_ACCURACY = 0.95;
    static String dataFolder = "/Users/Appurv_Air/Dropbox/Coursework/CurrentCourses/AI/SNNCode/SNNImpl/src/";
    static String dataLocn = "irisModified.data";//"irisModified.data";
    static int CLASSIFIER_COL = 4;
    static double TRAINING_RATIO = 0.5;
    
    
    public static void main(String[] args){ 
        try{
            
            
            Data data = new Data(Main.dataFolder + Main.dataLocn , CLASSIFIER_COL, TRAINING_RATIO); 
            Main.layerSize[0] = data.totalInputCols;
            for(int i = 1; i <= hiddenLayerSize.length; i++)
                Main.layerSize[i] = hiddenLayerSize[i-1];
            HashSet<Integer> trainingClassSet = new HashSet<Integer>();
            for(int value: data.trainingClass)
                trainingClassSet.add(value);
            int classSetSize = trainingClassSet.size();
System.out.println("Class Set Size: " + classSetSize);
            Main.layerSize[totalSize - 1] = classSetSize;
            SNNLearningController classifier = new SNNLearningController(Main.layerSize, data);
            long time = System.currentTimeMillis();
            //classifier.run(NO_OF_RUNS);
            classifier.runTillAccuracy(MIN_ACCURACY);
   
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Training Accuracy: " + classifier.getBestAccuracy());
            System.out.println("Test Accuracy: " + classifier.getTestAccuracy(classifier.getBestIndiv()));
            System.out.println("Time Taken : " + ((System.currentTimeMillis() - time)/1000) + " seconds" );
       
            
            
            
        }catch(ListLengthsDifferentException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }catch(NumberFormatException n){
            System.out.println("Number Format excep caught!\n" + n.getMessage());
            n.printStackTrace();           
        }catch(Exception g){
            System.out.println("Exception Called in main: " + g.getClass());
            g.printStackTrace();
        }
   }
}
