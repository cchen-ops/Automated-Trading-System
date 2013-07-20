/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import moa.core.*;
import weka.core.Instance;
import weka.core.Instances;
import java.io.*;
import java.util.*;
import moa.streams.*;
import moa.classifiers.meta.*;
import moa.classifiers.functions.*;
import moa.classifiers.trees.*;
import moa.clusterers.streamkm.*;
/**
 *
 * @author Christopher
 */
public class MOA_Example {
    
    
    public static void main(String[] args){
        ArffFileStream WineD = new ArffFileStream("C:/Users/Christopher/Downloads/wine1train.arff", 14);
        StreamKM OBA = new StreamKM();
        OBA.resetLearningImpl();
        WineD.prepareForUse();
        
        Instance current;
        while (WineD.hasMoreInstances() == true){
            current = WineD.nextInstance();
            //System.out.println(current);
            OBA.trainOnInstance(current);  
        }
        
        ArffFileStream WineT = new ArffFileStream("C:/Users/Christopher/Downloads/wine1testa.arff", 14);
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
            if(maxValue/sum <0.35){
                System.out.print("No Prediction ");
                System.out.println(prob[0]+" "+ prob[1]+" "+ prob[2]);
            }
            else{System.out.println(maxIndex+1);}           
        }
        
    }
}