/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

import Data.Validation.QuoteData;
import java.io.*;
import java.util.*;
/**
 *
 * @author Christopher
 */
public class AutomatedTradingSystem {
    static String priceDataFile = "C:/Users/Christopher/Documents/NetBeansProjects/Automated Trading System/USDJPY_Candlestick_1_D_BID.csv";
    static int shortWindow = 50;
    static int longWindow = 200;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Load Data
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
        //System.out.println(priceData.get(1500).getClosePrice());
        
        //Moving Average Calculation
        SMA_EMA.EMA(longWindow, priceData);
        
        //Backtest
        
        Asset Position = new Asset("stock1", 0);
        
        for (int i = longWindow; i<priceData.size(); i++){
            
        }
        

    }
         
    
    

}
