/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

import java.util.*;
import java.util.ArrayList;
/**
 *
 * @author Christopher
 */
public class Portfolio {
    private String name;
    private Market market;
    
    private List<Asset> stocks = new ArrayList<Asset>();

 
  // this method gets the market value for each stock, sums it up and returns the total value of the portfolio.
  
    public Double getTotalValue() {
     Double value = 0.0;
     for (Asset stock : this.stocks) {
      value += (market.getPrice(stock.getName()) * stock.getQuantity());
     }
     return value;
    }

    public String getName() {
     return name;
    }

    public void setName(String name) {
     this.name = name;
    }

    public List<Asset> getStocks() {
     return stocks;
    }

    public void setStocks(List<Asset> stocks) {
     this.stocks = stocks;
    }

    public void addStock(Asset stock) {
     stocks.add(stock);
    }

    public Market getStockMarket() {
     return market;
    }

    public void setStockMarket(Market market) {
     this.market = market;
    }
}
