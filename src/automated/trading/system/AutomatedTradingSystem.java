/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

import Classifier.RegClassifier;
import Classifier.AbstainTestTrain;
import Indicators.SMA_EMA;
import Data.Validation.QuoteData;
import java.io.*;
import java.util.*;
import java.math.*;
import Classifier.AbstainingClassifiers;
/**
 *
 * @author Christopher
 */
public class AutomatedTradingSystem {
    //static String arffDataFile = "C:/Users/Christopher/Downloads/USDJPY_Candlestick_1_D_BID_Final.arff";
    static String priceDataFile = "C:/Users/Christopher/Documents/NetBeansProjects/Automated Trading System/USDJPY_Candlestick_1_D_BID_Clean.csv";
    public static String arffFile = "ArffFileStream -f C:/Users/Christopher/Downloads/USDJPY_Candlestick_1_D_BID_Final.arff";
    static String tradeResultsDataFile = "C:/Users/Christopher/Downloads/JPYTradeResultsReg.csv";
    static String predResultsDataFile = "C:/Users/Christopher/Downloads/JPYpredResultsReg.csv";
    public static String predResultsStatFile = "C:/Users/Christopher/Downloads/JPYpredWinStatsReg-HAT.csv";
    public static String classMethodcmd = "trees.HoeffdingAdaptiveTree";
    public static String performanceEval = "WindowRegressionPerformanceEvaluator -w 200";
    static int shortWindow = 20;
    static int longWindow = 50;
    static int longShareUnits = 1;
    static int shortShareUnits = -1;
    static String classMethod = "NB";
    static String currency = "JPYUSD";
    static String movavg = "20/50";
    static String pred_trade = "No";
    static String bullbear = "1/2";
    static int bull = 1;
    static int bear = 2;
    //static int arffIndex = 10;
    public static double GlobalabstainRatio = 0.95;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Load Data
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
           
        //Do Stream Learning and Prediction
//        List<Integer> predictionsNB = AbstainingClassifiers.abstainSCD(arffDataFile, arffIndex, abstainRatio);
//        for (int i=0; i<500; i++){
//            System.out.println("NB Test: " + predictionsNB.get(i));         
//        }
            
            List<Integer> predictionListAbs = new ArrayList<Integer>();
            List<Integer> predictionListReg = new ArrayList<Integer>();

            AbstainTestTrain absTT = new AbstainTestTrain();
            RegClassifier regTT = new RegClassifier();

            Object predictionAbs = absTT.doTask();
            Object predictionReg = regTT.doTask();

            String prediction1 = predictionAbs.toString().replace("[", "").replace("]", "");
//            StringBuilder predString = new StringBuilder();
//            predString.append(currency + "/" +classMethod + "/"+GlobalabstainRatio +", ");
//            predString.append(prediction1);
//            predString.append("\n");
//            FileWriter out1 = new FileWriter(predResultsDataFile, true);
//            out1.append(predString);
//            out1.flush();
//            out1.close();
            
            List<String> predictionsAbs = Arrays.asList(prediction1.split("\\s*,\\s*"));

            String prediction2 = predictionReg.toString().replace("[", "").replace("]", "");
            StringBuilder predString2 = new StringBuilder();
            predString2.append(currency + "/" +classMethod + ", ");
            predString2.append(prediction2);
            predString2.append("\n");
            FileWriter out2 = new FileWriter(predResultsDataFile, true);
            out2.append(predString2);
            out2.flush();
            out2.close();
            
            List<String> predictionsReg = Arrays.asList(prediction2.split("\\s*,\\s*"));

            for (String s: predictionsAbs){
                predictionListAbs.add(Integer.valueOf(s));
            }

            for (String s: predictionsReg){
                predictionListReg.add(Integer.valueOf(s));
            }

            //Backtest

            Asset Position = new Asset("testAsset", 0, 0, 0); //name, shares, purPrice, curPrice
            //System.out.println(Position.getCurrPrice());
            double totPerRet = 0;
            int totTrades = 0;


            for (int i = longWindow; i<priceData.size(); i++){
                //Moving Average Calculation
                double curshortWindow = SMA_EMA.EMA(shortWindow, priceData, i);
                double curlongWindow = SMA_EMA.EMA(longWindow, priceData, i);

                //System.out.println(curshortWindow);
                //System.out.println(curlongWindow);

                if (curshortWindow >= curlongWindow){
                    if (Position.getShares()<0 && (predictionListReg.get(i) == bull )){
                        //short to long
                        Position.setCurrPrice(priceData.get(i).getClosePrice());
                        double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*shortShareUnits;
                        totPerRet += Ret;
                        Position.setShares(longShareUnits);
                        Position.setPurPrice(priceData.get(i).getClosePrice());
                        //System.out.println("Gain/Loss " + Ret + " Short to Long @ " + Position.getCurrPrice() + " at time " + i + " num shares " + Position.getShares());
                        totTrades++;
                    }
                    else if (Position.getShares()==0){
                        Position.setShares(longShareUnits);
                        Position.setPurPrice(priceData.get(i).getClosePrice());
                        Position.setCurrPrice(priceData.get(i).getClosePrice());
                        //System.out.println("Long @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                        totTrades++;
                    }
                }
                else {
                    if (Position.getShares()>0 && (predictionListReg.get(i) == bear )){
                        //long to short
                        Position.setCurrPrice(priceData.get(i).getClosePrice());
                        double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*longShareUnits;
                        totPerRet += Ret;
                        Position.setShares(shortShareUnits);
                        Position.setPurPrice(priceData.get(i).getClosePrice());
                        //System.out.println("Gain/Loss " + Ret + " Long to Short @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                        totTrades++;
                    }
                    else if (Position.getShares()==0){
                        Position.setShares(shortShareUnits);
                        Position.setPurPrice(priceData.get(i).getClosePrice());
                        Position.setCurrPrice(priceData.get(i).getClosePrice());
                        //System.out.println("Short @ " + priceData.get(i).getClosePrice()+ " at time " + i + " num shares " + Position.getShares());
                        totTrades++;
                    }
                }//end Else for going Long


            } //end For loop for testing historical data
            //System.out.println("Total Return: " + totPerRet + " Total Trades: " + totTrades + " Avg. Ret/Trade: " + totPerRet/totTrades);
            System.out.println(totPerRet + ", "+totTrades + ", "+ totPerRet/totTrades);
            
            StringBuilder resultsString = new StringBuilder();
            resultsString.append(currency);
            resultsString.append(",");
            resultsString.append(classMethod);
            resultsString.append(",");
            resultsString.append("Reg");
            resultsString.append(",");
            resultsString.append(movavg);
            resultsString.append(",");
            resultsString.append(pred_trade);
            resultsString.append(",");
            resultsString.append(bullbear);
            resultsString.append(",");
            resultsString.append(totPerRet);
            resultsString.append(",");
            resultsString.append(totTrades);
            resultsString.append(",");
            resultsString.append(totPerRet/totTrades);
            resultsString.append("\n");

            
            FileWriter tradeResults = new FileWriter(tradeResultsDataFile, true);
            tradeResults.append(resultsString);
            tradeResults.flush();
            tradeResults.close();

    } //end Main
} // end Class
