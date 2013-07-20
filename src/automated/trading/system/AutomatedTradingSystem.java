/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

import Data.Validation.QuoteData;
import java.io.*;
import java.util.*;
import java.math.*;
/**
 *
 * @author Christopher
 */
public class AutomatedTradingSystem {
    static String priceDataFile = "C:/Users/Christopher/Documents/NetBeansProjects/Automated Trading System/VIX.US_Candlestick_1_D_BID.csv";
    static int shortWindow = 10;
    static int longWindow = 20;
    static int longShareUnits = 1;
    static int shortShareUnits = -1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Load Data
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
        //System.out.println(priceData.get(1502).getClosePrice());
         
        //Backtest
        
        Asset Position = new Asset("testAsset", 0, 0, 0); //name, shares, purPrice, curPrice
        //System.out.println(Position.getCurrPrice());
        double totPerRet = 0;
        
        
        for (int i = longWindow; i<priceData.size(); i++){
            //Moving Average Calculation
            double curshortWindow = SMA_EMA.EMA(shortWindow, priceData, i);
            double curlongWindow = SMA_EMA.EMA(longWindow, priceData, i);
            
            //System.out.println(curshortWindow);
            //System.out.println(curlongWindow);
            
            if (curshortWindow >= curlongWindow){
                if (Position.getShares()<0){
                    //short to long
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*shortShareUnits;
                    totPerRet += Ret;
                    Position.setShares(longShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    System.out.println("Gain/Loss " + Ret + " Short to Long @ " + Position.getCurrPrice() + " at time " + i + " num shares " + Position.getShares());
                }
                else if (Position.getShares()==0){
                    Position.setShares(longShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    System.out.println("Long @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                }
            }
            else {
                if (Position.getShares()>0){
                    //long to short
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    double Ret = (Math.log(Position.getCurrPrice()) - Math.log(Position.getPurPrice()))*longShareUnits;
                    totPerRet += Ret;
                    Position.setShares(shortShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    System.out.println("Gain/Loss " + Ret + " Long to Short @ " + priceData.get(i).getClosePrice() + " at time " + i + " num shares " + Position.getShares());
                }
                else if (Position.getShares()==0){
                    Position.setShares(shortShareUnits);
                    Position.setPurPrice(priceData.get(i).getClosePrice());
                    Position.setCurrPrice(priceData.get(i).getClosePrice());
                    System.out.println("Short @ " + priceData.get(i).getClosePrice()+ " at time " + i + " num shares " + Position.getShares());
                }
            }            
        }
        System.out.println(totPerRet);
    }
}
