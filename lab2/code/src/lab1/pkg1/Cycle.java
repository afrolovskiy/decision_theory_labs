package lab1.pkg1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cycle implements Cloneable
{
    public Cycle()
    {
        elements = new ArrayList<CycleElement>();
    }
    
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }
    
    public boolean containElement(int indexOfRow, int indexOfColumn)
    {
        for (CycleElement element: elements)
            if (element.isEquals(indexOfRow, indexOfColumn))
                return true;
        return false;
    }
    
    public boolean containElement(CycleElement element)
    {
        return containElement(element.getIndexOfRow(), element.getIndexOfColumn());
    }
    
    public void add(CycleElement element) throws Exception
    {
        if (containElement(element))
            throw new Exception("Error: Cycle contain yet this element!!!");
        elements.add(element);
    }
    
    public Iterator getIterator()
    {
        return elements.iterator();
    }
    
    @Override
    public Cycle clone() throws CloneNotSupportedException 
    {
        try {
            Cycle cycle = new Cycle();
            for (CycleElement element: elements)
                cycle.add(element.clone());
            return cycle;
        } 
        catch (Exception ex) 
        {
            throw new CloneNotSupportedException(); 
        }
    }
    
    public int getNumberOfElements()
    {
        return elements.size();
    }
    
    public CycleElement getElement(int index) throws Exception
    {
        if (isIncorrectIndex(index))
            throw new Exception("Error: Incorrect index of cycle element");
        return elements.get(index);
    }
    
    private boolean isIncorrectIndex(int index)
    {
        return index < 0 && index >= elements.size();
    }
  
    public CycleElement getStartElement() throws Exception
    {
        return getElement(0); 
    }
    
    public String toString()
    {
        String output = "";
        Iterator iter = elements.iterator();
        while (iter.hasNext())
            output += printCycleElementToString((CycleElement) iter.next());
        return output;
    }
    
    private String printCycleElementToString(CycleElement element)
    {
        return element.toString();
    }
    
    private List<CycleElement> elements;
}
