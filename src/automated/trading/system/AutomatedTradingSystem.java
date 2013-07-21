/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

import Indicators.SMA_EMA;
import Data.Validation.QuoteData;
import java.io.*;
import java.util.*;
import java.math.*;
import Classifier.AbstainingNaiveBayes;
/**
 *
 * @author Christopher
 */
public class AutomatedTradingSystem {
    static String priceDataFile = "C:/Users/Christopher/Documents/NetBeansProjects/Automated Trading System/EURUSD_Candlestick_1_D_BID_Clean.csv";
    static String arffDataFile = "C:/Users/Christopher/Downloads/EURUSD_Candlestick_1_D_BID_Final.arff";
    static int shortWindow = 20;
    static int longWindow = 50;
    static int longShareUnits = 1;
    static int shortShareUnits = -1;
    static int arffIndex = 10;
    static double abstainRatio = 0.52;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Load Data
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
           
        //Do Stream Learning and Prediction
        List<Integer> predictionsNB = AbstainingNaiveBayes.abstainNB(arffDataFile, arffIndex, abstainRatio);
        //System.out.println("NB Test: " + predictionsNB.get(8));         
        
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
                if (Position.getShares()<0 & predictionsNB.get(i) == 2){
                    //short to long
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*shortShareUnits;
                    totPerRet += Ret;
                    Position.setShares(longShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    System.out.println("Gain/Loss " + Ret + " Short to Long @ " + Position.getCurrPrice() + " at time " + i + " num shares " + Position.getShares());
                    totTrades++;
                }
                else if (Position.getShares()==0){
                    Position.setShares(longShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    System.out.println("Long @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                    totTrades++;
                }
            }
            else {
                if (Position.getShares()>0 & predictionsNB.get(i) == 1){
                    //long to short
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*longShareUnits;
                    totPerRet += Ret;
                    Position.setShares(shortShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    System.out.println("Gain/Loss " + Ret + " Long to Short @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                    totTrades++;
                }
                else if (Position.getShares()==0){
                    Position.setShares(shortShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    System.out.println("Short @ " + priceData.get(i).getClosePrice()+ " at time " + i + " num shares " + Position.getShares());
                    totTrades++;
                }
            }//end Else for going Long
            
            
        } //end For loop for testing historical data
        System.out.println("Total Return: " + totPerRet + " Total Trades: " + totTrades + " Avg. Ret/Trade: " + totPerRet/totTrades);
        

    } //end Main
} // end Class
