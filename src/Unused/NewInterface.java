/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;
import Classifier.AbstainTestTrain;
import automated.trading.system.*;
import java.util.Arrays;
import java.util.List;
import java.util.*;

/**
 *
 * @author Christopher
 */
public class NewInterface {
    public static void main(String[] args){
        List<Integer> predictionList = new ArrayList<Integer>();
        AbstainTestTrain test = new AbstainTestTrain();
        Object predictionNB = test.doTask();
        
        String prediction = predictionNB.toString().replace("[", "").replace("]", "");
        List<String> predictions = Arrays.asList(prediction.split("\\s*,\\s*"));
        
        for (String s: predictions){
            predictionList.add(Integer.valueOf(s));
        }

        
    }
    
}
