/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Appurv_Air
 */
public class Data {
    public final double[][] trainingInput;
    public final int[] trainingClass;
    public final double[][] testInput;
    public final int[] testClass;
    public Data(double[][] train, int[] trainClass,  double[][] test, int[] testClass){
        this.trainingInput = train;
        this.testInput = test;
        this.trainingClass = trainClass;
        this.testClass = testClass;
    }
}
