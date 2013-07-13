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
    private int quantity;
    
    public Asset(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getQuantity(){
        return quantity;
    }
    
    public void setQuantity(){
        this.quantity = quantity;
    }
    
}
