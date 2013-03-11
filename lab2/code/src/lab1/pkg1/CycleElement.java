package lab1.pkg1;

public class CycleElement implements Cloneable
{
    public CycleElement(int indexOfRow, int indexOfColumn)
    {
        this.indexOfRow = indexOfRow;
        this.indexOfColumn = indexOfColumn;
    }
    
    public int getIndexOfRow()
    {
        return indexOfRow;
    }
    
    public int getIndexOfColumn()
    {
        return indexOfColumn;
    }
    
    public boolean isEquals(CycleElement element)
    {
        return isEquals(element.indexOfRow, element.indexOfColumn);
    }
    
    public boolean isEquals(int indexOfRow, int indexOfColumn)
    {
        return isEqualsIndexOfRow(indexOfRow) && isEqualsIndexOfColumn(indexOfColumn);
    }
    
    public boolean isEqualsIndexOfRow(int indexOfRow)
    {
        return this.indexOfRow == indexOfRow;
    }
    
    public boolean isEqualsIndexOfColumn(int indexOfColumn)
    {
        return this.indexOfColumn == indexOfColumn;
    }
    
    @Override
    public CycleElement clone() throws CloneNotSupportedException 
    {
        return new CycleElement(indexOfRow, indexOfColumn);
    }
    
    public boolean equals(Object o) {
        CycleElement element = (CycleElement) o;
        if (indexOfRow == element.indexOfRow && indexOfColumn == element.indexOfColumn)
            return true;
        return false;
    }
    
    public String toString()
    {
        return "(" + indexOfRow + "," + indexOfColumn + ")";
    }
    
    private int indexOfRow;
    private int indexOfColumn;
}
