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
    static String priceDataFile = "C:/Users/Christopher/Documents/NetBeansProjects/Automated Trading System/USDJPY_Candlestick_1_D_BID.csv";
    static int shortWindow = 50;
    static int longWindow = 200;
    static int ShareUnits = 100;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Load Data
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
        //System.out.println(priceData.get(1500).getClosePrice());
         
        //Backtest
        
        Asset Position = new Asset("stock1", 0, 0, 0);
        
        for (int i = longWindow; i<priceData.size(); i++){
            //Moving Average Calculation
            double curshortWindow = SMA_EMA.EMA(shortWindow, priceData, i);
            double curlongWindow = SMA_EMA.EMA(longWindow, priceData, i);
            
            //System.out.println(curshortWindow);
            //System.out.println(curlongWindow);
            
            if (curshortWindow >= curlongWindow){
                if (Position.getShares()<=0){
                    //short to long
                    int currPosition = Position.getShares();
                    currPosition = Math.abs(currPosition);
                    
                    
                    
                    Position.setShares(ShareUnits);
                }
            }
            else {
                if (Position.getShares()>=0){
                    
                }
            }

        }
        

    }
         
    
    

}
