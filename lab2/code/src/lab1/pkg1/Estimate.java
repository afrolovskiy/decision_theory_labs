package lab1.pkg1;

public class Estimate implements Comparable
{ 
    public Estimate(int indexOfRow, int indexOfColumn, double value)
    {
        this.indexOfRow = indexOfRow;
        this.indexOfColumn = indexOfColumn;
        this.value = value;
    }
    
    public boolean isEqualsIndexOfRow(int indexOfRow)
    {
        return this.indexOfRow == indexOfRow;
    }
    
    public boolean isEqualsIndexOfColumn(int indexOfColumn)
    {
        return this.indexOfColumn == indexOfColumn;
    }
    
    public boolean isEqualsIndexes(int indexOfRow, int indexOfColumn)
    {
        return isEqualsIndexOfRow(indexOfRow) && isEqualsIndexOfColumn(indexOfColumn);
    }
    
    public double getValue()
    {
        return this.value;
    }
    
    public boolean isNegative()
    {
        return value < 0;
    }
    
    public int getIndexOfRow()
    {
        return indexOfRow; 
    }
    
    public int getIndexOfColumn()
    {
        return indexOfColumn;
    }
    
    @Override
    public int compareTo(Object o) 
    {
        if (!(o instanceof Estimate))
            throw new UnsupportedOperationException("Not supported yet.");
        
        Estimate estimate = (Estimate) o;
        double difference = value - estimate.getValue();
        
        if (Math.abs(difference) < 1e-6)
            return 0;
        
        if (difference > 0)
            return 1;
        
        return -1;
    }
    
    public String toString()
    {
        return "d(" + indexOfRow + "," + indexOfColumn + ") = " + value;
    }

            
    private int indexOfRow;
    private int indexOfColumn;
    private double value;
}
