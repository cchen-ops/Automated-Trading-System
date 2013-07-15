/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automated.trading.system;

/**
 *
 * @author Christopher
 */
public class Asset {
    private String name;
    private int shares;
    private double purPrice;
    private double currPrice;
    
    public Asset(String name, int shares, double purPrice, double currPrice){
        this.name = name;
        this.shares = shares;
        this.purPrice = purPrice;
        this.currPrice = currPrice;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getShares(){
        return shares;
    }
    
    public void setShares(int shares){
        this.shares = shares;
    }
    
    public double getCurrPrice(){
        return currPrice;
    }
    
    public void setCurrPrice(double currPrice){
        this.currPrice = currPrice;
    }
    
    public void setPurPrice(double purPrice){
        this.purPrice = purPrice;
    }
    
    public double getPurPrice(){
        return purPrice;
    }
    
    public double PurValue(){
        double PurValue = shares * purPrice;
        return PurValue;
    }
    
    public double getCurrValue(){
        double value = shares * currPrice;
        return value;
    }
    
    public double getGainLoss(){
        double GainLoss = this.getCurrValue() - PurValue();
        return GainLoss;
    }
    
}
