/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import moa.core.*;
import weka.core.Instance;
import weka.core.Instances;
import java.io.*;
import java.util.ArrayList;
import java.util.*;
import moa.streams.*;
import moa.classifiers.meta.*;
import moa.classifiers.functions.*;
import moa.classifiers.trees.*;
import moa.clusterers.streamkm.*;
import moa.classifiers.bayes.*;
import moa.classifiers.meta.LeveragingBag;
import moa.classifiers.trees.*;
/**
 *
 * @author Christopher
 */
public class MOA_Example {
    
    
    public static void main(String[] args){
        HoeffdingAdaptiveTree Class1 = new HoeffdingAdaptiveTree();
        Class1.prepareForUse();
        ArffFileStream WineT = new ArffFileStream("C:/Users/Christopher/Downloads/GBPUSD_Candlestick_1_D_BID_Final.arff", 10);
        //Class1.setModelContext(null);
        Class1.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
//        ArffFileStream WineD = new ArffFileStream("C:/Users/Christopher/Downloads/wine1train.arff", 14);
//        
//        
//        Instance current;
//        while (WineD.hasMoreInstances() == true){
//            current = WineD.nextInstance();
//            //System.out.println(current);
//            Class1.trainOnInstance(current);  
//        }
        
        Instance currentTest;
        while (WineT.hasMoreInstances() == true){
            currentTest = WineT.nextInstance();
            //System.out.println(current);
            double[] prob = Class1.getVotesForInstance(currentTest);
            Class1.trainOnInstance(currentTest);
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
                if(maxValue/sum <0.50){                
                //System.out.print(prob[0]+" "+ prob[1]);
                System.out.println("No Prediction ");
                predictionsList.add(0);
                }
                else{
                    System.out.println(maxIndex+1);
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 System.out.println("No Prediction - Error");
                 predictionsList.add(0);
            }  //end Catch         
        } //end While
        

        
        //System.out.println("List test: "+predictionsList.get(8));
        
    } //end main
} //end class
