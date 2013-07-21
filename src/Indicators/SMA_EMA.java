package Indicators;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import Data.Validation.QuoteData;
import java.io.*;
import java.util.*;
/**
 *
 * @author Christopher
 */
public class SMA_EMA {

      public static double SMA(int Period_Length, List<QuoteData> priceData, int Time) {
         
        //The total number of periods to generate data for.
        final int PERIODS_AVERAGE = Period_Length;
        final int TOTAL_PERIODS = priceData.size();
        double[] closePrice = new double[TOTAL_PERIODS];
        double[] out = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        for (int i = 0; i < TOTAL_PERIODS; i++) {
          closePrice[i] = (double) priceData.get(i).getClosePrice();
        }

        Core c = new Core();
        RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);

//        if (retCode == RetCode.Success) {
//          System.out.println("Output Begin:" + begin.value);
//          System.out.println("Output Begin:" + length.value);

//          for (int i = begin.value; i < closePrice.length; i++) {
//            StringBuilder line = new StringBuilder();
//            line.append("Period #");
//            line.append(i+1);
//            line.append(" close= ");
//            line.append(closePrice[i]);
//            line.append(" mov avg=");
//            line.append(out[i-begin.value]);
//            System.out.println(line.toString());
//          }
//        }
//        else {
//        System.out.println("Error");
//        }

        return out[(Time-begin.value)];
      
    }
    
    public static double EMA(int Period_Length, List<QuoteData> priceData, int Time) {
         
        //The total number of periods to generate data for.
        final int PERIODS_AVERAGE = Period_Length;
        final int TOTAL_PERIODS = priceData.size();
        double[] closePrice = new double[TOTAL_PERIODS];
        double[] out = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        for (int i = 0; i < TOTAL_PERIODS; i++) {
          closePrice[i] = (double) priceData.get(i).getClosePrice();
        }

        Core c = new Core();
        RetCode retCode = c.ema(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);

//        if (retCode == RetCode.Success) {
//          System.out.println("Output Begin:" + begin.value);
//          System.out.println("Output Begin:" + length.value);

//          for (int i = begin.value; i < closePrice.length; i++) {
//            StringBuilder line = new StringBuilder();
//            line.append("Period #");
//            line.append(i+1);
//            line.append(" close= ");
//            line.append(closePrice[i]);
//            line.append(" mov avg=");
//            line.append(out[i-begin.value]);
//            System.out.println(out[i-begin.value]);
//          }
//        }
//        else {
//        System.out.println("Error");
//        }

        return out[(Time-begin.value)];
      
    }
}
