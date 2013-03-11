package lab1.pkg1;

public class GaussMethod
{
    public double[] execute(double[][] a, double[] b)
    {
        initializeVariables(a, b);
        forwardPass();
        return backwardPass();
    }
    
    private void initializeVariables(double[][] a, double[] b)
    {
        this.n = a.length;
        allocateMemory();
        copyValues(a, b);
    }
    
    private void allocateMemory()
    {
        this.a = new double[n][n];
        this.b = new double[n];
    }
    
    private void copyValues(double[][] a, double[] b)
    {
        copyMatrixValues(a);
        copyVectorValues(b);
    }
    
    private void copyMatrixValues(double[][] a)
    {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.a[i][j] = a[i][j];
    }
    
    private void copyVectorValues(double[] b)
    {
        for (int i = 0; i < this.n; i++)
            this.b[i] = b[i];
    }
    
    private void forwardPass()
    {
        for (int i = 0; i < this.n; i++)
            forwardPassStep(i);
    }
    
    private void forwardPassStep(int indexOfStep)
    {
        swap(indexOfStep);
        normalizeCurrentRow(indexOfStep);
        zeroCurrentColumn(indexOfStep);
    }

    private void swap(int indexOfCurrentRow)
    {
        int indexOfRowWithMaxElement = getIndexOfRowWithCurrentMaxElement(a, indexOfCurrentRow);
        if (indexOfRowWithMaxElement != indexOfCurrentRow)
            swapRows(indexOfCurrentRow, indexOfRowWithMaxElement);
    }

    private int getIndexOfRowWithCurrentMaxElement(double[][] a, int index)
    {
        int indexOfRowWithMaxCurrentElement = index;
        double currentMaxElement = a[index][index];
        for (int i = index + 1; i < n; i++)
            if (isMaxElement(currentMaxElement, a[i][index]))
            {
                currentMaxElement = a[i][index];
                indexOfRowWithMaxCurrentElement = i;
            }
        return indexOfRowWithMaxCurrentElement;
    }
    
    private boolean isMaxElement(double currentMaxElement, double currentElement)
    {
        return Math.abs(currentMaxElement) < Math.abs(currentElement);
    }
    
    private void swapRows(int indexOfRow1, int indexOfRow2)
    {
        swapMatrixRows(indexOfRow1, indexOfRow2);
        swapVectorValues(indexOfRow1, indexOfRow2);
    }   
    
    private void swapMatrixRows(int indexOfRow1, int indexOfRow2)
    {
        double[] workRow = new double[n];
        for (int j = 0; j < n; j++)
        {
            workRow[j] = a[indexOfRow1][j];
            a[indexOfRow1][j] = a[indexOfRow2][j];
            a[indexOfRow2][j] = workRow[j];
        }
    }
    
    private void swapVectorValues(int indexOfElement1, int indexOfElement2)
    {
        double workVariable = b[indexOfElement1];
        b[indexOfElement1] = b[indexOfElement2];
        b[indexOfElement2] = workVariable;
    }
    
    private void normalizeCurrentRow(int indexOfCurrentRow)
    {
        for (int j = indexOfCurrentRow + 1; j < n; j++)
                a[indexOfCurrentRow][j] /= a[indexOfCurrentRow][indexOfCurrentRow];
        b[indexOfCurrentRow] /= a[indexOfCurrentRow][indexOfCurrentRow];
        a[indexOfCurrentRow][indexOfCurrentRow] = 1.0;
    }
    
    private void zeroCurrentColumn(int indexOfCurrentColumn)
    {
        for (int i = indexOfCurrentColumn + 1; i < n; i++)
            zeroColumnElement(i, indexOfCurrentColumn);
    }

    private void zeroColumnElement(int indexOfRow, int indexOfCurrentColumn)
    {
        double koef = a[indexOfRow][indexOfCurrentColumn];
        for (int j = indexOfCurrentColumn; j < n; j++)
            a[indexOfRow][j] -= koef * a[indexOfCurrentColumn][j];
        b[indexOfRow] -= koef * b[indexOfCurrentColumn];
    }

    private double[] backwardPass()
    {
        double[] resultVector = initializeResultVector();

        for (int i = n - 1; i >= 0; i--)
        {
            for (int j = n - 1; j > i; j--)
                resultVector[i] -= resultVector[j] * a[i][j];
            resultVector[i] += b[i];
        }

        return resultVector;
    }
    
    private double[] initializeResultVector()
    {
        double[] resultVector = new double[n];
        for (int i = 0; i < n; i++)
            resultVector[i] = 0;
        return resultVector;
    }
    
    private double[][] a;
    private double[] b;
    private int n;
}
