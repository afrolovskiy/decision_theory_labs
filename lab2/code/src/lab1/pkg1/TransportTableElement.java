package lab1.pkg1;

import java.util.Locale;

public class TransportTableElement 
{
    public TransportTableElement(){}
    
    public TransportTableElement(double volume, double cost)
    {
        this.volume = volume;
        this.cost = cost;
    }
    
    public double getVolume()
    {
        return this.volume;
    }
    
    public void setVolume(double volume)
    {
        this.volume = volume;
    }
    
    public double getCost()
    {
        return this.cost;
    }
    
    public void setCost(double cost)
    {
        this.cost = cost;
    }
    
    public boolean isIncludedInBasis()
    {
        return this.flgIncludeInBasis;
    }
    
    public void includeInBasis()
    {
        this.flgIncludeInBasis = true;
    }
    
    public void excludeFromBasis()
    {
        this.flgIncludeInBasis = false;
    }
    
    public String toString()
    {
        return  "[ " + printElementVolumeToString() + 
                " | " + printElementCostToString() + " ]\t";
        //return  " [ " + printElementVolumeToString() + 
        //        " | " + printElementCostToString() + " ] ";
    }
    
    private String printElementVolumeToString()
    {
        if (isIncludedInBasis())
            return String.format("%4.0f", volume);//String.format("%8.1f", volume);
        else
            return "     ";//return getSpaces(11);
    }
    
    private String getSpaces(int number)
    {
        String output = "";
        for (int i = 0; i < number; i++)
            output += " ";
        return output;
    }
    
    private String printElementCostToString()
    {
        return String.format("%4.0f", cost);//String.format("%8.1f", cost);
    }
    
    private double volume = 0;
    private double cost = 0;
    private boolean flgIncludeInBasis = false;
}
