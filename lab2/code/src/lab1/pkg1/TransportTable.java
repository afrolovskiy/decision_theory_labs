package lab1.pkg1;

public class TransportTable 
{
    public TransportTable(double[] sourcePowers, double[] flowPowers, Matrix costsMatrix) throws Exception
    {
        this.sourcePowers = sourcePowers.clone();
        this.flowPowers = flowPowers.clone();
        this.elements = new TransportTableElement[sourcePowers.length][flowPowers.length];
        setCosts(costsMatrix);
    }
    
    private void setCosts(Matrix costsMatrix) throws Exception
    {
        for (int i = 0; i < sourcePowers.length; i++)
            for (int j = 0; j < flowPowers.length; j++)
            {
                double cost = costsMatrix.getValue(i, j);
                elements[i][j] = new TransportTableElement(0, cost);
            }        
    }

    public int getNumberOfSources()
    {
        return sourcePowers.length;
    }
    
    public double getSourcePower(int index) throws Exception
    {
        if (isNotCorrectIndexOfSource(index))
            throw new Exception("Error: uncorrect index of source");
        return sourcePowers[index];
    }
    
    public int getNumberOfFlows()
    {
        return flowPowers.length;
    }
    
    public double getFlowPower(int index) throws Exception
    {
        if (isNotCorrectIndexOfFlow(index))
            throw new Exception("Erroe: uncorrect index of flow");
        return flowPowers[index];
    }
    
    private boolean isNotCorrectIndexOfSource(int index)
    {
        return index < 0 || index >= sourcePowers.length;
    }    
    
    private boolean isNotCorrectIndexOfFlow(int index) {
        return index < 0 || index >= flowPowers.length;
    }
    
    public double getCost(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
       
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        
        return element.getCost();
    }
    
    private boolean isCorrectIndexes(int indexOfRow, int indexOfColumn)
    {
        return isNotCorrectIndexOfSource(indexOfRow) || isNotCorrectIndexOfFlow(indexOfColumn);
    }
    
    public double getVolume(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
        
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        
        //if (!element.isIncludedInBasis())
        //    throw new Exception("Error: basis don't contain this element");
        
        return element.getVolume();
    }
    
    public void setVolume(int indexOfRow, int indexOfColumn, double volume) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
        
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        
        //if (!element.isIncludedInBasis())
        //    throw new Exception("Error: basis don't contain this element");
        
        elements[indexOfRow][indexOfColumn].setVolume(volume);
    }
    
    public boolean isIncludedInBasis(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
        
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        
        return element.isIncludedInBasis();
    }
    
    public void includeInBasis(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
        
        elements[indexOfRow][indexOfColumn].includeInBasis();
    }
    
    public void excludeFromBasis(int indexOfRow, int indexOfColumn) throws Exception
    {
        if (isCorrectIndexes(indexOfRow, indexOfColumn))
            throw new Exception("Error: uncorrect indexes of table element");
        
        elements[indexOfRow][indexOfColumn].excludeFromBasis();
    }
    
    public double[] getSourcePowers()
    {
        double[] sourcePowersCopy = new double[this.sourcePowers.length];
        System.arraycopy(this.sourcePowers, 0, sourcePowersCopy, 0, this.sourcePowers.length);
        return sourcePowersCopy;
    }
    
    public double[] getFlowPowers()
    {
        double[] flowPowersCopy = new double[this.flowPowers.length];
        System.arraycopy(this.flowPowers, 0, flowPowersCopy, 0, this.flowPowers.length);
        return flowPowersCopy;
    }
    
    public Matrix getVolumes() throws Exception
    {
        Matrix volumes = initializeVolumesMatrix();
        for (int i = 0; i < sourcePowers.length; i++)
            for (int j = 0; j < flowPowers.length; j++)
                setVolumesValue(i, j, volumes);
        return volumes;
    }
    
    private Matrix initializeVolumesMatrix()
    {
        return new Matrix(sourcePowers.length, flowPowers.length);
    }
    
    private void setVolumesValue(int indexOfRow, int indexOfColumn, Matrix volumes) throws Exception
    {
        double volume = elements[indexOfRow][indexOfColumn].getVolume();
        volumes.setValue(indexOfRow, indexOfColumn, volume);
    }

    @Override
    public String toString()
    {
        String output = "";
        output += printFlowPowers();
        for (int i = 0; i < sourcePowers.length; i++)
            output += printRowToString(i) + "\n";
        return output;
    }
    
    private String printRowToString(int indexOfRow)
    {
        String output = "";
        output += printSourcePower(indexOfRow);
        for (int j = 0; j < flowPowers.length; j++)
            output += "\t"+printElementToString(indexOfRow, j);
        return output;
    }
    
    private String printFlowPowers()
    {
        String output = "\t\t";
        //String output = String.format("%32s", "");
        for (int i = 0; i < flowPowers.length; i++)
            output += printFlowPower(i);
        return output + "\n";
    }
    
    private String printFlowPower(int indexOfFlow)
    {
        String output = String.format("{D[%d]=%4.0f}\t\t", indexOfFlow, flowPowers[indexOfFlow] );
        //String output = "{   D[" + String.format("%2d", indexOfFlow) + "] = " + 
        //                String.format("%7.1f", flowPowers[indexOfFlow]) + "}   ";
        return output;
    }
    
    private String printSourcePower(int indexOfSource)
    {
        return "{S[" + String.format("%2d", indexOfSource) + "] = " +
                String.format("%4.0f", sourcePowers[indexOfSource]) + "}\t";
        //return "{ S[" + String.format("%2d", indexOfSource) + "] = " +
        //        String.format("%7.1f", sourcePowers[indexOfSource]) + "  }";
    }
    
    private String printElementToString(int indexOfRow, int indexOfColumn)
    {
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        return element.toString();
    }
    
    public double getSummaryCost()
    {
        double summaryCost = 0;
        for (int i = 0; i < sourcePowers.length; i++)
            for (int j = 0; j < flowPowers.length; j++)
                summaryCost += getSummaryCostElement(i, j);
        return summaryCost;
    }
 
    private double getSummaryCostElement(int indexOfRow, int indexOfColumn)
    {
        TransportTableElement element = elements[indexOfRow][indexOfColumn];
        if (element.isIncludedInBasis())
            return element.getVolume() * element.getCost();
        return 0;
    }
    
    private double[] sourcePowers;
    private double[] flowPowers;
    private TransportTableElement[][] elements;
}