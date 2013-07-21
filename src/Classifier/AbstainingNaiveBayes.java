/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import moa.core.*;
import weka.core.Instance;
import weka.core.Instances;
import java.io.*;
import java.util.*;
import moa.streams.*;
import moa.classifiers.bayes.*;
/**
 *
 * @author Christopher
 */
public class AbstainingNaiveBayes {
    
    
    public static List<Integer> abstainNB(String filename, int index, double threshold){
        NaiveBayes OBA = new NaiveBayes();
        OBA.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
        
        ArffFileStream WineT = new ArffFileStream(filename, index);
        Instance currentTest;
        while (WineT.hasMoreInstances() == true){
            currentTest = WineT.nextInstance();
            //System.out.println(current);
            double[] prob = OBA.getVotesForInstance(currentTest);
            OBA.trainOnInstance(currentTest);
            //System.out.println(prob[0]+" "+ prob[1]+" "+ prob[2]);
            
            int maxIndex = -1;
            double maxValue = Double.NEGATIVE_INFINITY;
            
            for (int i=0; i<prob.length; i++){
                if(prob[i]>maxValue){
                    maxIndex = i;
                    maxValue = prob[i];
                }
            }
            
            double sum =0;
            for (double i: prob){
                sum += i;
            }
            try{
                if(maxValue/sum <threshold){                
                //System.out.print(prob[0]+" "+ prob[1]);
                //System.out.println("No Prediction ");
                predictionsList.add(0);
                }
                else{
                    //System.out.println(maxIndex+1);
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 //System.out.println("No Prediction - Error");
                 predictionsList.add(0);
            }  //end Catch         
        } //end While
        
        //System.out.println("List test: "+predictionsList.get(8));
        
        return predictionsList;
        
    } //end abNBPred method
} // end Class
