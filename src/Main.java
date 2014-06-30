
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This is the main class that runs the entire program
 * @author Appurv Jain ans Amrith Akula
 */
public class Main {    
    static int[] hiddenLayerSize = {3,6,3};
    static int totalSize = hiddenLayerSize.length + 2; //a layer each for input and output
    static int[] layerSize = new int[totalSize];
    static int NO_OF_RUNS = 30000;
    static double MIN_ACCURACY = 0.75;
    static String dataFolder = "/Users/Appurv_Air/Dropbox/Coursework/CurrentCourses/AI/SNNCode/SNNImpl/src/";
    static String dataLocn = "haberman.data";
    static int CLASSIFIER_COL = 3;
    static double TRAINING_RATIO = 0.70;
    static long time;
    
    
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
//for debugging purposes
System.out.println("Number of Classes: " + classSetSize);
            Main.layerSize[totalSize - 1] = classSetSize;
            SNNLearningController classifier = new SNNLearningController(Main.layerSize, data);
            Main.time = System.currentTimeMillis();
            //classifier.run(NO_OF_RUNS);
            classifier.runTillAccuracy(0.85);
            //classifier.runForMins(20);
            
            System.out.println("Training Accuracy: " + classifier.getBestAccuracy() +
              "\nTest Accuracy: " + classifier.getTestAccuracy(classifier.getBestIndiv()));
            
            Main.displayTime((double)(System.currentTimeMillis() - Main.time)/1000);
            
for(int i = 0; i < 20;i++){
    Toolkit.getDefaultToolkit().beep();
    Thread.sleep(40);
}
        }catch(ListLengthsDifferentException e){
            System.out.println(e.getMessage());
            e.printStackTrace();          
        }catch(Exception g){
            System.out.println("Exception Called in main: " + g.getClass());
            g.printStackTrace();
        }
   }
    
    /**
     * Displays time taken, choosing unit based on size
     * @param runTime 
     */
    public static void displayTime(double runTime){
        System.out.print("Time Taken : " );
        if(runTime > 3600)
            System.out.printf("%.3f hours\n", runTime/(3600));
        else if(runTime > 60)
            System.out.printf("%.3f minutes\n" ,runTime/60);
        else
            System.out.printf( "%.3f seconds\n",runTime);
    }
}
