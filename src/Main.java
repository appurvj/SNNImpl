
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Appurv_Air
 */
public class Main {
    
    
    static int[] layerSize = {3,8,8,6};
    static String dataLocn = "/Users/Appurv_Air/Dropbox/Coursework/CurrentCourses/AI/SNNCode/SNNImpl/src/TelaguVowel.txt";
    public static void main(String[] args){ 
        try{
System.out.println("Reading File");
            Data data = new Data(Main.dataLocn , 0);
System.out.println("Data Ready");            

            SNNController classifier = new SNNController(Main.layerSize, data);
System.out.println("SNN controller object created ");
        }catch(ListLengthsDifferentException e){
            System.out.println(e.getMessage());
        }catch(NumberFormatException n){
            System.out.println("Number Formet excep caught!\n" + n.getMessage());
            
        }catch(Exception g){
System.out.println("Exception Called in main...");        
            System.out.println(g.getStackTrace());
        }
   }
   
    /*
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String dataLocn = "/Users/Appurv_Air/Dropbox/Coursework/CurrentCourses/AI/SNNCode/SNNImpl/src/TelaguVowel.txt";
        BufferedReader file;
        file = new BufferedReader(new FileReader(dataLocn));
        for(int i = 0; i < 10; i++)
            System.out.println(file.readLine());
    }
    * 
    * */
}
