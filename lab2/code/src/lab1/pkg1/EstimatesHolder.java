package lab1.pkg1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EstimatesHolder
{
    public EstimatesHolder(TransportTable table) throws Exception
    {
        estimates = new ArrayList<Estimate>();
        this.table = table;
        calculateEstimates();
    }
    
    private void calculateEstimates() throws Exception
    {
        initializeSLAE();
        buildSLAE();
        solveSLAE();
        buildEstimates();
    }
    
    private void initializeSLAE()
    {
        numberOfSources = table.getNumberOfSources();
        numberOfFlows  = table.getNumberOfFlows();
        numberOfBasisVariables = numberOfSources + numberOfFlows - 1;
        a = new double[numberOfBasisVariables][numberOfBasisVariables];
        b = new double[numberOfBasisVariables];
    }
    
    private void buildSLAE() throws Exception
    {
        int numberOfRows = table.getNumberOfSources();
        int numberOfColumns = table.getNumberOfFlows();
        for (int i = 0; i < numberOfRows; i++)
            for (int j = 0; j < numberOfColumns; j++)
                addEquationIfElementIncludingInBasis(i, j);
    }
    
    private void addEquationIfElementIncludingInBasis(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isBasisElement(indexOfRow, indexOfColumn))
            addEquation(indexOfRow, indexOfColumn);
    }
    
    private boolean isBasisElement(int indexOfRow, int indexOfColumn) throws Exception
    {
        return table.isIncludedInBasis(indexOfRow, indexOfColumn);
    }
    
    private void addEquation(int indexOfRow, int indexOfColumn) throws Exception
    {
        zeroingExtendedMatrixRow();
        addUi(indexOfRow);
        addVj(indexOfColumn);
        addRightPartOfEquation(indexOfRow, indexOfColumn);
        indexOfAddedEquations++;
    }
    
    private void zeroingExtendedMatrixRow()
    {
        for (int j = 0; j < numberOfBasisVariables; j++)
            a[indexOfAddedEquations][j] = 0;
        b[indexOfAddedEquations] = 0;
    }
    
    private void addUi(int indexOfRow)
    {
        a[indexOfAddedEquations][indexOfRow] = 1.0;
    }
    
    private void addVj(int indexOfColumn)
    {
        if (isUnzeroingElement(indexOfColumn))
            a[indexOfAddedEquations][getIndexOfVj(indexOfColumn)] = 1.0;
    }
    
    private boolean isUnzeroingElement(int indexOfColumn)
    {
        return getIndexOfVj(indexOfColumn) < (numberOfBasisVariables);
    }
    
    private int getIndexOfVj(int indexOfColumn)
    {
        return numberOfSources + indexOfColumn;
    }
    
    private void addRightPartOfEquation(int indexOfRow, int indexOfColumn) throws Exception
    {
        b[indexOfAddedEquations] = table.getCost(indexOfRow, indexOfColumn);
    }
    
    private void solveSLAE()
    {
        GaussMethod solver = new GaussMethod();
        x = solver.execute(a, b);
    }
    
    private void buildEstimates() throws Exception
    {
        for (int i = 0; i < numberOfSources; i++)
            for (int j = 0; j < numberOfFlows; j++)
                addEstimateIfElementNotIncludingInBasis(i, j);
    }
    
    private void addEstimateIfElementNotIncludingInBasis(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (!isBasisElement(indexOfRow, indexOfColumn))
            addEstimate(indexOfRow, indexOfColumn);
    }
    
    private void addEstimate(int indexOfRow, int indexOfColumn) throws Exception
    {
        double cij = table.getCost(indexOfRow, indexOfColumn);
        double ui =  getValueOfUi(indexOfRow);
        double vj = getValueOfVj(indexOfColumn);
        double dij = cij - ui - vj;
        estimates.add(new Estimate(indexOfRow, indexOfColumn, dij));
    }
    
    private double getValueOfUi(int indexOfRow)
    {
        return x[indexOfRow];
    }
    
    private double getValueOfVj(int indexOfColumn)
    {
        if (isUnzeroingElement(indexOfColumn))
            return x[getIndexOfVj(indexOfColumn)];
        return 0.0;
    }
    
    public boolean isExistedNegativeEstimates()
    {
        Iterator iter = getIterator();
        while (iter.hasNext())
        {
            Estimate estimate = (Estimate) iter.next();
            if (estimate.isNegative())
                return true;
        }
        return false;
    }
    
    public Iterator getIterator()
    {
        return estimates.iterator();
    }
    
    public Estimate getMinEstimate()
    {
        return Collections.min(estimates);
    }
    
    public String toString()
    {
        String output = "";
        Iterator iter = estimates.iterator();
        while (iter.hasNext())
        {
            Estimate estimate = (Estimate) iter.next();
            output += estimate.toString() + "\n";
        }
        return output;
    }
    

    
    private List<Estimate> estimates;
    private TransportTable table;
    private int indexOfAddedEquations = 0;
    private int numberOfBasisVariables;
    private int numberOfSources;
    private int numberOfFlows;
    private double[][] a;
    private double[] b;
    private double[] x;
}
