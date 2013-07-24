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
import moa.classifiers.trees.*;
import moa.classifiers.meta.*;
import moa.classifiers.functions.*;
import moa.classifiers.drift.SingleClassifierDrift;
/**
 *
 * @author Christopher
 */
public class AbstainingClassifiers {
    
    
    public static List<Integer> abstainNB(String filename, int index, double threshold){
        NaiveBayes OBA = new NaiveBayes();
        OBA.prepareForUse();
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
    
    public static List<Integer> abstainLB(String filename, int index, double threshold){
        int correctClass = 0;
        LeveragingBag OBA = new LeveragingBag();
        OBA.prepareForUse();
        
        OBA.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
        
        ArffFileStream fileStream = new ArffFileStream(filename, index);
        Instance currentTest;
        while (fileStream.hasMoreInstances() == true){
            currentTest = fileStream.nextInstance();
            double[] prob = OBA.getVotesForInstance(currentTest);
            OBA.trainOnInstance(currentTest);
            if(OBA.correctlyClassifies(currentTest)){
                correctClass++;
            }
            
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
                    predictionsList.add(0);
                }
                else{
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 predictionsList.add(-1);
            }  //end Catch         
        } //end While
        
        System.out.println(correctClass);
        return predictionsList;
        
    } //end abLBPred method
    
    public static List<Integer> abstainHT(String filename, int index, double threshold){
        int correctClass = 0;
        HoeffdingAdaptiveTree OBA = new HoeffdingAdaptiveTree();
        OBA.prepareForUse();
        
        OBA.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
        
        ArffFileStream fileStream = new ArffFileStream(filename, index);
        Instance currentTest;
        while (fileStream.hasMoreInstances() == true){
            currentTest = fileStream.nextInstance();
            double[] prob = OBA.getVotesForInstance(currentTest);
            OBA.trainOnInstance(currentTest);
            if(OBA.correctlyClassifies(currentTest)){
                correctClass++;
            }
            
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
                    predictionsList.add(0);
                }
                else{
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 predictionsList.add(-1);
            }  //end Catch         
        } //end While
        
        System.out.println(correctClass);
        return predictionsList;
        
    } //end abHTPred method
    
    public static List<Integer> abstainSCD(String filename, int index, double threshold){
        int correctClass = 0;
        SingleClassifierDrift OBA = new SingleClassifierDrift();
        OBA.prepareForUse();
        
        OBA.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
        
        ArffFileStream fileStream = new ArffFileStream(filename, index);
        Instance currentTest;
        while (fileStream.hasMoreInstances() == true){
            currentTest = fileStream.nextInstance();
            double[] prob = OBA.getVotesForInstance(currentTest);
            OBA.trainOnInstance(currentTest);
            if(OBA.correctlyClassifies(currentTest)){
                correctClass++;
            }
            
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
                    predictionsList.add(0);
                }
                else{
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 predictionsList.add(-1);
            }  //end Catch         
        } //end While
        
        System.out.println(correctClass);
        return predictionsList;
        
    } //end abSCDPred method
    
    public static List<Integer> abstainSGD(String filename, int index, double threshold){
        int correctClass = 0;
        SGD OBA = new SGD();
        OBA.prepareForUse();
        
        OBA.resetLearningImpl();
        List<Integer> predictionsList = new ArrayList();
        
        ArffFileStream fileStream = new ArffFileStream(filename, index);
        Instance currentTest;
        while (fileStream.hasMoreInstances() == true){
            currentTest = fileStream.nextInstance();
            double[] prob = OBA.getVotesForInstance(currentTest);
            OBA.trainOnInstance(currentTest);
            if(OBA.correctlyClassifies(currentTest)){
                correctClass++;
            }
            
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
                    predictionsList.add(0);
                }
                else{
                    predictionsList.add(maxIndex+1);
                }
                
            } //end Try
            
            catch (Exception e){
                 predictionsList.add(-1);
            }  //end Catch         
        } //end While
        
        System.out.println(correctClass);
        return predictionsList;
        
    } //end abSGDPred method
} // end Class
