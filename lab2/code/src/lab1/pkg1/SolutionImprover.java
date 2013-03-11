package lab1.pkg1;

public class SolutionImprover 
{
    public SolutionImprover(EstimatesHolder estimates, TransportTable table)
    {
        this.estimates = estimates;
        this.table = table;
    }

    public TransportTable execute() throws Exception
    {
        initializeVariables();
        return executeMainActions();
    }
    
    private void initializeVariables()
    {
        this.output = "";
    }
    
    private TransportTable executeMainActions() throws Exception
    {
        includeVariableInBasis();
        buildCycle();
        exceptVariableFromBasis();
        swapping();
        output += "\nПолученная транспортная таблица:\n" + table.toString();
        return table;
    }
    
    private void includeVariableInBasis() throws Exception
    {
        findMinEstimate();
        includeElementWithMinEstimateInBasis();
        CycleElement element = new CycleElement(minEstimate.getIndexOfRow(), minEstimate.getIndexOfColumn());
        output += "\nВ базис включена переменная " + element.toString() + "\n";
    }
    
    private void findMinEstimate()
    {
        minEstimate = estimates.getMinEstimate();
    }
    
    private void includeElementWithMinEstimateInBasis() throws Exception
    {
        table.includeInBasis(minEstimate.getIndexOfRow(), minEstimate.getIndexOfColumn());
    }
    
    private void buildCycle() throws Exception
    {
        CycleBuilder builder = new CycleBuilder(table);
        int startElementRowIndex = minEstimate.getIndexOfRow();
        int startElementColumnIndex = minEstimate.getIndexOfColumn();
        cycle = builder.execute(new CycleElement(startElementRowIndex, startElementColumnIndex));
        output += "\nПостроенный цикл: " + cycle.toString() + "\n";
    }
    
    private void exceptVariableFromBasis() throws Exception
    {
        findVariableForException();
        exceptFindedVariable();
        output += "\nИз базиса исключена переменная " + exceptedVariable.toString() + "\n";
    }
    
    private void findVariableForException() throws Exception
    {
        CycleElement minElement = cycle.getElement(1);
        for (int  i = 3; i < cycle.getNumberOfElements(); i += 2)
        {
            CycleElement curElement = cycle.getElement(i);
            if (isCurElementLessMinElement(curElement, minElement))
                minElement = curElement;
        }
        exceptedVariable = minElement;
    }
    
    private boolean isCurElementLessMinElement(CycleElement curElement, CycleElement minElement) throws Exception
    {
        double curValue = table.getVolume(curElement.getIndexOfRow(), curElement.getIndexOfColumn());
        double minValue = table.getVolume(minElement.getIndexOfRow(), minElement.getIndexOfColumn());
        return  curValue < minValue;
    }

    private void exceptFindedVariable() throws Exception
    {
        table.excludeFromBasis(exceptedVariable.getIndexOfRow(), exceptedVariable.getIndexOfColumn());
    }
    
    private void swapping() throws Exception
    {
        double omega = getOmega(); 
        output += "\nТранспортная таблица до перекачки:\n" + table.toString();
        output += "\nомега = " + omega;
        int sizeOfCycle = cycle.getNumberOfElements();
        for (int i = 0; i < sizeOfCycle; i++)
            changeOmegaFromCycleElementVolume(i, omega);
    }
    
    private double getOmega() throws Exception
    {
        return table.getVolume(exceptedVariable.getIndexOfRow(), exceptedVariable.getIndexOfColumn());
    }
    
    private void changeOmegaFromCycleElementVolume(int indexOfCycleElement, double omega) throws Exception
    {
        CycleElement element = cycle.getElement(indexOfCycleElement);
        int indexOfRow = element.getIndexOfRow();
        int indexOfColumn = element.getIndexOfColumn();
        if (isOddIndex(indexOfCycleElement))
            subValueFromElementVolume(indexOfRow, indexOfColumn, omega);
        else
            addValueToElementVolume(indexOfRow, indexOfColumn, omega);
    }
    
    private boolean isOddIndex(int index)
    {
        return index % 2 == 1;
    }
    
    
    private void subValueFromElementVolume(int indexOfRow, int indexOfColumn, double subtractedValue) throws Exception
    {
        double  oldValue = table.getVolume(indexOfRow, indexOfColumn);
        double newValue = oldValue - subtractedValue;
        table.setVolume(indexOfRow, indexOfColumn, newValue);
    }
    
    private void addValueToElementVolume(int indexOfRow, int indexOfColumn, double subtractedValue) throws Exception
    {
        double  oldValue = table.getVolume(indexOfRow, indexOfColumn);
        double newValue = oldValue + subtractedValue;
        table.setVolume(indexOfRow, indexOfColumn, newValue);
    }
    
    @Override
    public String toString()
    {
        return output;
    }
    
    
    private Cycle cycle;
    private Estimate minEstimate;
    private EstimatesHolder estimates;
    private TransportTable table;
    private CycleElement exceptedVariable;
    private String output;
}
