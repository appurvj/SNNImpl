
/**
 * This class denotes the exception that will be thrown if the format of the
 * provided parameters provide to a neuron does not match with what is required.
 * 
 * @author Appurv Jain and Amrith Akula
 */

public class ListLengthsDifferentException extends Exception {
    public  ListLengthsDifferentException(){}

    public ListLengthsDifferentException(String message){
        super (message);
    }
}
