/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import automated.trading.system.AutomatedTradingSystem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import moa.classifiers.Classifier;
import moa.core.Measurement;
import moa.core.ObjectRepository;
import moa.core.TimingUtils;
import moa.evaluation.ClassificationPerformanceEvaluator;
import moa.evaluation.LearningCurve;
import moa.evaluation.LearningEvaluation;
import moa.options.ClassOption;
import moa.options.FileOption;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import weka.core.Instance;
import moa.tasks.*;


/**
 *
 * @author Christopher
 */
public class AbstainTestTrain extends MainTask {
    
    public double abstainRatio = AutomatedTradingSystem.GlobalabstainRatio;
    
    @Override
    public String getPurposeString() {
        return "Evaluates a classifier on a stream by testing then training with each example in sequence with abstaining.";
    }

    private static final long serialVersionUID = 1L;

    public ClassOption learnerOption = new ClassOption("learner", 'l',
            "Classifier to train.", Classifier.class, AutomatedTradingSystem.classMethodcmd);

    public ClassOption streamOption = new ClassOption("stream", 's',
            "Stream to learn from.", InstanceStream.class,
            AutomatedTradingSystem.arffFile);

    public ClassOption evaluatorOption = new ClassOption("evaluator", 'e',
            "Classification performance evaluation method.",
            ClassificationPerformanceEvaluator.class,
            AutomatedTradingSystem.performanceEval);

    public IntOption instanceLimitOption = new IntOption("instanceLimit", 'i',
            "Maximum number of instances to test/train on  (-1 = no limit).",
            10000, -1, Integer.MAX_VALUE);

    public IntOption timeLimitOption = new IntOption("timeLimit", 't',
            "Maximum number of seconds to test/train for (-1 = no limit).", -1,
            -1, Integer.MAX_VALUE);

    public IntOption sampleFrequencyOption = new IntOption("sampleFrequency",
            'f',
            "How many instances between samples of the learning performance.",
            200, 0, Integer.MAX_VALUE);

    public IntOption memCheckFrequencyOption = new IntOption(
            "memCheckFrequency", 'q',
            "How many instances between memory bound checks.", 100000, 0,
            Integer.MAX_VALUE);

    public FileOption dumpFileOption = new FileOption("dumpFile", 'd',
            "File to append intermediate csv reslts to.", AutomatedTradingSystem.predResultsStatFile, "csv", true);
    
    

    @Override
    public Class<?> getTaskResultType() {
        return LearningCurve.class;
    }

    @Override
    protected Object doMainTask(TaskMonitor monitor, ObjectRepository repository) {
        Classifier learner = (Classifier) getPreparedClassOption(this.learnerOption);
        InstanceStream stream = (InstanceStream) getPreparedClassOption(this.streamOption);
        ClassificationPerformanceEvaluator evaluator = (ClassificationPerformanceEvaluator) getPreparedClassOption(this.evaluatorOption);
        learner.setModelContext(stream.getHeader());
        List<Integer> predictionsList = new ArrayList();
        int maxInstances = this.instanceLimitOption.getValue();
        long instancesProcessed = 0;
        int maxSeconds = this.timeLimitOption.getValue();
        int secondsElapsed = 0;
        monitor.setCurrentActivity("Evaluating learner...", -1.0);
        LearningCurve learningCurve = new LearningCurve(
                "learning evaluation instances");
        File dumpFile = this.dumpFileOption.getFile();
        PrintStream immediateResultStream = null;
        if (dumpFile != null) {
            try {
                if (dumpFile.exists()) {
                    immediateResultStream = new PrintStream(
                            new FileOutputStream(dumpFile, true), true);
                } else {
                    immediateResultStream = new PrintStream(
                            new FileOutputStream(dumpFile), true);
                }
            } catch (Exception ex) {
                throw new RuntimeException(
                        "Unable to open immediate result file: " + dumpFile, ex);
            }
        }
        boolean firstDump = true;
        boolean preciseCPUTiming = TimingUtils.enablePreciseTiming();
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
        long lastEvaluateStartTime = evaluateStartTime;
        double RAMHours = 0.0;
        while (stream.hasMoreInstances()
                && ((maxInstances < 0) || (instancesProcessed < maxInstances))
                && ((maxSeconds < 0) || (secondsElapsed < maxSeconds))) {
            Instance trainInst = stream.nextInstance();
            Instance testInst = (Instance) trainInst.copy();
            int trueClass = (int) trainInst.classValue();
            //testInst.setClassMissing();
            double[] prediction = learner.getVotesForInstance(testInst);
            //evaluator.addClassificationAttempt(trueClass, prediction, testInst.weight());

            try{
                int maxIndex = -1;
                double maxValue = Double.NEGATIVE_INFINITY;

                for (int i=0; i<prediction.length; i++){
                    if(prediction[i]>maxValue){
                        maxIndex = i;
                        maxValue = prediction[i];
                    }
                }

                double sum =0;
                for (double i: prediction){
                    sum += i;
                }
                
                if(maxValue/sum >abstainRatio){
                    evaluator.addResult(testInst, prediction);
                    predictionsList.add(maxIndex+1);
                    learner.trainOnInstance(trainInst);                
                    instancesProcessed++;
                    if (instancesProcessed % this.sampleFrequencyOption.getValue() == 0 ||  stream.hasMoreInstances() == false) {
                        long evaluateTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
                        double time = TimingUtils.nanoTimeToSeconds(evaluateTime - evaluateStartTime);
                        double timeIncrement = TimingUtils.nanoTimeToSeconds(evaluateTime - lastEvaluateStartTime);
                        double RAMHoursIncrement = learner.measureByteSize() / (1024.0 * 1024.0 * 1024.0); //GBs
                        RAMHoursIncrement *= (timeIncrement / 3600.0); //Hours
                        RAMHours += RAMHoursIncrement;
                        lastEvaluateStartTime = evaluateTime;
                        learningCurve.insertEntry(new LearningEvaluation(
                                new Measurement[]{
                                    new Measurement("learning evaluation instances", instancesProcessed),
                                    new Measurement("evaluation time (" + (preciseCPUTiming ? "cpu ": "") + "seconds)",time),
                                    new Measurement("model cost (RAM-Hours)",RAMHours)
                                },
                                evaluator, learner));
                        if (immediateResultStream != null) {
                            if (firstDump) {
                                immediateResultStream.println(learningCurve.headerToString());
                                firstDump = false;
                            }
                            immediateResultStream.println(learningCurve.entryToString(learningCurve.numEntries() - 1));
                            immediateResultStream.flush();
                        }
                    }
                    
                }
                else{
                    learner.trainOnInstance(trainInst);
                    predictionsList.add(0);
                    if (stream.hasMoreInstances() == false) {
                        long evaluateTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
                        double time = TimingUtils.nanoTimeToSeconds(evaluateTime - evaluateStartTime);
                        double timeIncrement = TimingUtils.nanoTimeToSeconds(evaluateTime - lastEvaluateStartTime);
                        double RAMHoursIncrement = learner.measureByteSize() / (1024.0 * 1024.0 * 1024.0); //GBs
                        RAMHoursIncrement *= (timeIncrement / 3600.0); //Hours
                        RAMHours += RAMHoursIncrement;
                        lastEvaluateStartTime = evaluateTime;
                        learningCurve.insertEntry(new LearningEvaluation(
                                new Measurement[]{
                                    new Measurement("learning evaluation instances", instancesProcessed),
                                    new Measurement("evaluation time (" + (preciseCPUTiming ? "cpu ": "") + "seconds)",time),
                                    new Measurement("model cost (RAM-Hours)",RAMHours)
                                },
                                evaluator, learner));
                        if (immediateResultStream != null) {
                            if (firstDump) {
                                immediateResultStream.println(learningCurve.headerToString());
                                firstDump = false;
                            }
                            immediateResultStream.println(learningCurve.entryToString(learningCurve.numEntries() - 1));
                            immediateResultStream.flush();
                        }
                    }
                }
            }
            
            catch(Exception e){
                //System.out.println(e);
                learner.trainOnInstance(trainInst);
                predictionsList.add(0);
                if (stream.hasMoreInstances() == false) {
                        long evaluateTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
                        double time = TimingUtils.nanoTimeToSeconds(evaluateTime - evaluateStartTime);
                        double timeIncrement = TimingUtils.nanoTimeToSeconds(evaluateTime - lastEvaluateStartTime);
                        double RAMHoursIncrement = learner.measureByteSize() / (1024.0 * 1024.0 * 1024.0); //GBs
                        RAMHoursIncrement *= (timeIncrement / 3600.0); //Hours
                        RAMHours += RAMHoursIncrement;
                        lastEvaluateStartTime = evaluateTime;
                        learningCurve.insertEntry(new LearningEvaluation(
                                new Measurement[]{
                                    new Measurement("learning evaluation instances", instancesProcessed),
                                    new Measurement("evaluation time (" + (preciseCPUTiming ? "cpu ": "") + "seconds)",time),
                                    new Measurement("model cost (RAM-Hours)",RAMHours) 
                                },
                                evaluator, learner));
                        if (immediateResultStream != null) {
                            if (firstDump) {
                                immediateResultStream.println(learningCurve.headerToString());
                                firstDump = false;
                            }
                            immediateResultStream.println(learningCurve.entryToString(learningCurve.numEntries() - 1));
                            immediateResultStream.flush();
                        }
                 }
            }
            
            
            
                     
            //evaluator.addResult(testInst, prediction);
            
            if (instancesProcessed % INSTANCES_BETWEEN_MONITOR_UPDATES == 0) {
                if (monitor.taskShouldAbort()) {
                    return null;
                }
                long estimatedRemainingInstances = stream.estimatedRemainingInstances();
                if (maxInstances > 0) {
                    long maxRemaining = maxInstances - instancesProcessed;
                    if ((estimatedRemainingInstances < 0)
                            || (maxRemaining < estimatedRemainingInstances)) {
                        estimatedRemainingInstances = maxRemaining;
                    }
                }
                monitor.setCurrentActivityFractionComplete(estimatedRemainingInstances < 0 ? -1.0
                        : (double) instancesProcessed
                        / (double) (instancesProcessed + estimatedRemainingInstances));
                if (monitor.resultPreviewRequested()) {
                    monitor.setLatestResultPreview(learningCurve.copy());
                }
                secondsElapsed = (int) TimingUtils.nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread()
                        - evaluateStartTime);
            }
        }
        if (immediateResultStream != null) {
            immediateResultStream.close();
        }
        return predictionsList;
    }
}
