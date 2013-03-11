package lab1.pkg1;

import java.util.Formatter;

public class Matrix 
{
    public Matrix(int numberOfRows, int numberOfColumns)
    {
        setSize(numberOfRows, numberOfColumns);
        values = new double[numberOfRows][numberOfColumns];
    }
    
    private void setSize(int numberOfRows, int numberOfColumns)
    {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }
    
    public Matrix(double[][] matrix, int numberOfRows, int numberOfColumns)
    {
        setSize(numberOfRows, numberOfColumns);
        values = new double[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++)
            System.arraycopy(matrix[i], 0, values[i], 0, matrix[i].length);
    }
    
    public Matrix(Matrix matrix)
    {
        setSize(matrix.getNumberOfRows(), matrix.getNumberOfColumns());
        values = new double[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++)
            System.arraycopy(matrix.values[i], 0, values[i], 0, numberOfColumns);
    }
    
    public final void setValue(int indexOfRow, int indexOfColumn, double value) throws Exception
    {
        if (!isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of matrix value");
        values[indexOfRow][indexOfColumn] = value;
    }
    
    private boolean isCorrectIndexes(int indexOfRow, int indexOfColumn)
    {
        return indexOfRow >= 0 && indexOfRow < numberOfRows &&
               indexOfColumn >= 0 && indexOfColumn < numberOfColumns; 
    }
    
    public final double getValue(int indexOfRow , int indexOfColumn) throws Exception
    {
        if (!isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of matrix value");
        return values[indexOfRow][indexOfColumn];
    }
    
    public int getNumberOfRows()
    {
        return this.numberOfRows;
    }
    
    public int getNumberOfColumns()
    {
        return this.numberOfColumns;
    }
    
    public void print()
    {
        for (int i = 0; i < numberOfRows; i++)
            printRow(i);
        System.out.printf("\n");
    }
    
    public String printToString()
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < numberOfRows; i++)
            buffer.append(printRowToString(i));
        buffer.append("\n");
        return buffer.toString();
    }
    
    private void printRow(int indexRow)
    {
        System.out.printf("\n");
        for (int j = 0; j < numberOfColumns; j++)
            System.out.printf("%6.1f ", values[indexRow][j]);
    }
    
    private String printRowToString(int indexRow)
    {
        Formatter formatter = new Formatter();
        String outStr = "\n";
        for (int j = 0; j < numberOfColumns; j++)
            formatter.format("%1$8.1f", values[indexRow][j]);
        outStr += formatter.toString();
        return outStr;
    }
    
    public Matrix copy()
    {
        Matrix resMatrix = new Matrix(numberOfRows, numberOfColumns);
        resMatrix.values = copyValues();        
        return resMatrix;
    }
    
    protected double[][] copyValues()
    {
        double[][] res = new double[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i ++)
            System.arraycopy(values[i], 0, res[i], 0, numberOfColumns);
        return res;
    }
    
    protected double[][] values;
    private int numberOfRows;
    private int numberOfColumns;
}
