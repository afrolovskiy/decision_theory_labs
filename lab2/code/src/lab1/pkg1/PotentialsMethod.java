package lab1.pkg1;

public class PotentialsMethod 
{
    public PotentialsMethod(InitialBASBuilder BASbuilder, TransportTable table)
    {
        this.BASbuilder = BASbuilder;
        this.table = table;
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public Matrix execute() throws Exception
    {
        initializeVariables();
        buildBasisAccessibleSolution();
        return executeMainActions();
    }
    
    private void initializeVariables()
    {
        this.output = "";
    }
    
    private void buildBasisAccessibleSolution()
    {
        BASbuilder.execute(table);
        output += "\nНачальное БДР: \n" + table.toString() + "\n";
    }
    
    private Matrix executeMainActions() throws Exception
    {
        calculateEstimates();
        while (isExistedNegativeEstimates())
        {
            numberOfIteration++;
            improveSolution();
            calculateEstimates();
        }
        return table.getVolumes();
    }
    
    private void improveSolution() throws Exception
    {
        output += "\n" + numberOfIteration + " итерация\n";
        SolutionImprover improver = new SolutionImprover(estimates, table);
        table = improver.execute();
        output += improver.toString();
        
    }
    
    private void calculateEstimates() throws Exception
    {
        estimates = new EstimatesHolder(table);
        this.output += "\nОценки: \n" + estimates.toString() + "\n";
    }
    
    private boolean isExistedNegativeEstimates()
    {
        return estimates.isExistedNegativeEstimates();
    }
    
    public double getCost()
    {
        return table.getSummaryCost();
    }
    
    private EstimatesHolder estimates;
    private TransportTable table;
    private InitialBASBuilder BASbuilder;
    private String output;
    private int numberOfIteration = 0; 
}
