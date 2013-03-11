package lab1.pkg1;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NorthWesternCornerMethod extends InitialBASBuilder
{
    @Override
    public void execute(TransportTable table)
    {
        try 
        {
            this.table = table;
            initializeVariable();
            buildBAS();
        }
        catch (Exception ex) 
        {
            Logger.getLogger(NorthWesternCornerMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeVariable() throws Exception
    {
        this.curIndexOfRow = 0;
        this.curIndexOfColumn = 0;
        this.sourcePowerBalances = table.getSourcePowers();
        this.numberOfSources = table.getNumberOfSources();
        this.flowPowerBalances = table.getFlowPowers();
        this.numberOfFlows = table.getNumberOfFlows();
        excludeAllTablElementsFromBasis();
    }
    
    private void excludeAllTablElementsFromBasis() throws Exception
    {
        for (int i = 0; i < this.numberOfSources; i++)
            for (int j = 0; j < this.numberOfFlows; j++)
                table.excludeFromBasis(i, j);
    }
    
    private void buildBAS() throws Exception
    {
        while (isNotSatisfiedStopConditions())
            addElementToBasis();
    }
    
    private boolean isNotSatisfiedStopConditions()
    {
        return (curIndexOfRow < numberOfSources) && (curIndexOfColumn < numberOfFlows);
    }

    private void addElementToBasis() throws Exception
    {
        double transferredValue = getTransferredValue();
        changeCurrentPowerBalances(transferredValue);
        includeCurrentElementInBasis(transferredValue);
        changeIndexes();
    }
    
    private double getTransferredValue()
    {
        currentSourcePower = this.sourcePowerBalances[this.curIndexOfRow];
        currentFlowPower = this.flowPowerBalances[this.curIndexOfColumn];
        return Math.min(currentSourcePower, currentFlowPower);
    }
    
    private void changeCurrentPowerBalances(double min)
    {
        this.sourcePowerBalances[this.curIndexOfRow] = currentSourcePower - min;
        this.flowPowerBalances[this.curIndexOfColumn] = currentFlowPower - min;
    }
    
    private void includeCurrentElementInBasis(double min) throws Exception
    {
        table.includeInBasis(this.curIndexOfRow, this.curIndexOfColumn);
        table.setVolume(this.curIndexOfRow, this.curIndexOfColumn, min);
    }
    
    private void changeIndexes()
    {
        if (currentSourcePower > currentFlowPower)
            this.curIndexOfColumn++;
        else
            this.curIndexOfRow++;
    }
    
    
    private int numberOfSources;
    private int numberOfFlows;
    private double[] sourcePowerBalances;
    private double[] flowPowerBalances;
    private double currentSourcePower;
    private double currentFlowPower;
    private int curIndexOfRow;
    private int curIndexOfColumn;
    private TransportTable table;
}
