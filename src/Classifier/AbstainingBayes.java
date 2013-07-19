/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import moa.classifiers.AbstractClassifier;
import moa.classifiers.core.attributeclassobservers.AttributeClassObserver;
import moa.classifiers.core.attributeclassobservers.GaussianNumericAttributeClassObserver;
import moa.classifiers.core.attributeclassobservers.NominalAttributeClassObserver;
import moa.core.AutoExpandVector;
import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.StringUtils;
import weka.core.Instance;
import moa.classifiers.bayes.NaiveBayes;
/**
 *
 * @author Christopher
 */
public class AbstainingBayes extends NaiveBayes{
    
    
    public static double[] doNaiveBayesPrediction(Instance inst, DoubleVector observedClassDistribution, AutoExpandVector<AttributeClassObserver> attributeObservers){
        double[] votes = new double[observedClassDistribution.numValues()];
        double observedClassSum = observedClassDistribution.sumOfValues();
        for (int classIndex = 0; classIndex < votes.length; classIndex++) {
            votes[classIndex] = observedClassDistribution.getValue(classIndex)/ observedClassSum;
            for (int attIndex = 0; attIndex < inst.numAttributes() - 1; attIndex++) {
                int instAttIndex = modelAttIndexToInstanceAttIndex(attIndex, inst);
                AttributeClassObserver obs = attributeObservers.get(attIndex);
                if ((obs != null) && !inst.isMissing(instAttIndex)) {
                    votes[classIndex] *= obs.probabilityOfAttributeValueGivenClass(inst.value(instAttIndex), classIndex);
                }
            }
        }
        return votes;
    }
    
    public static double[] doNaiveBayesPredictionAbstaining(Instance inst, DoubleVector observedClassDistribution, AutoExpandVector<AttributeClassObserver> attributeObservers){
        double [] OrigPrediction = doNaiveBayesPrediction(inst, observedClassDistribution, attributeObservers);
//        if (OrigPrediction.< 0.51 & OrigPrediction > 0.49){
//            OrigPrediction = 
//        } 
        return OrigPrediction;
    }
}
