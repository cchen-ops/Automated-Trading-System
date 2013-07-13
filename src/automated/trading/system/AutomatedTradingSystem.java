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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
        List<QuoteData> priceData = QuoteData.loadData(priceDataFile);   
        System.out.println(priceData.get(25).getClosePrice());

    }
         
    
    

}
