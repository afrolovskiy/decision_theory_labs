package lab1.pkg1;

import java.util.ArrayList;
import java.util.List;

public class CycleBuilder 
{
    public CycleBuilder(TransportTable table)
    {
        this.table = table;
    }
    
    public Cycle execute(CycleElement startElement) throws Exception
    {
        cycle = new Cycle();
        cycle.add(startElement);
        Cycle newCycle = executeStep(startElement, cycle, TypeStep.HorizontalStep);
        if (newCycle == null)
            newCycle = executeStep(startElement, cycle, TypeStep.VerticalStep);
        return newCycle;
    }
    
    private Cycle executeStep(CycleElement currentElement, Cycle cycle, TypeStep typeStep) throws Exception
    {
        List<CycleElement> nextElementsList = getNextElementsList(cycle, currentElement, 
                                                                  typeStep.getPerpendicularStepDiraction());
        if (executingStopConditions(cycle, nextElementsList))
            return cycle;
        return addElements(cycle, nextElementsList, typeStep);
        
    }
    
    private Cycle addElements(Cycle currentCycle, List<CycleElement> nextElementsList, TypeStep typeStep) throws Exception
    {
        Cycle newCycle = currentCycle.clone();
        for  (CycleElement element: nextElementsList)
        {
            Cycle workList = currentCycle.clone(); 
            workList.add(element); 
            Cycle resultList = executeStep(element, workList, typeStep.getPerpendicularStepDiraction());
            if (resultList != null)
                return resultList;
        }
        return null;
    }
    
    private boolean executingStopConditions(Cycle cycle, List<CycleElement> nextElementsList) throws Exception
    {
        CycleElement startElement = cycle.getStartElement();
        if (cycle.getNumberOfElements() > 1 && nextElementsList.contains(startElement))
            return true;
        return false;
    }
    
    private List<CycleElement> getNextElementsList(Cycle currentCycle, CycleElement currentElement, TypeStep typeStep) throws Exception
    {
        ArrayList<CycleElement> elementsList = new ArrayList<CycleElement>();
        
        switch (typeStep)
        {
            case HorizontalStep:
                fillNextHorizontalElementsList(elementsList, currentElement, currentCycle);
                break;
            case VerticalStep:
                fillNextVerticalElementsList(elementsList, currentElement, currentCycle);
                break;
        }
            
        return elementsList;
    }
    
    private void fillNextVerticalElementsList(List<CycleElement> elementsList, CycleElement startElement, Cycle currentCycle) throws Exception
    {
        for (int i = 0; i < table.getNumberOfSources(); i++)
        {
            CycleElement element = new CycleElement(i, startElement.getIndexOfColumn());
            if (isSuitableElement(currentCycle, element))
                elementsList.add(element);
        }
    }
    
    private boolean isSuitableElement(Cycle currentCycle, CycleElement element) throws Exception 
    {
        CycleElement startElement = currentCycle.getStartElement();
        return  (element.isEquals(startElement) && currentCycle.getNumberOfElements() > 1 ) || 
                ( !currentCycle.containElement(element) && 
                table.isIncludedInBasis(element.getIndexOfRow(), element.getIndexOfColumn()));
    }
    
    private void fillNextHorizontalElementsList(List<CycleElement> elementsList, CycleElement startElement, Cycle currentCycle) throws Exception
    {
        for (int j = 0; j < table.getNumberOfFlows(); j++)
        {
            CycleElement element = new CycleElement(startElement.getIndexOfRow(), j);
            if (isSuitableElement(currentCycle, element))
                elementsList.add(element);
        }
    }  
    
    private enum TypeStep
    {
        HorizontalStep, VerticalStep;
        public TypeStep getPerpendicularStepDiraction()
        {
            if (this.equals(HorizontalStep))
                return VerticalStep;
            return HorizontalStep;
        }
    }; 
    
    private Cycle cycle;
    private TransportTable table;
}
