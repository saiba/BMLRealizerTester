package bml.realizertestport;

/**
 * Indicates the start of execution for a BML block.
 * @author Herwin van Welbergen
 */
public class BMLPerformanceStartFeedback
{
    public final double timeStamp;
    public String characterId;

    public String getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    public final String bmlId;
    public final double predictedEnd;

    public BMLPerformanceStartFeedback(String characterId, String bmlId, double timeStamp, double predictedEnd)
    {
        this.characterId = characterId;
        this.bmlId = bmlId;
        this.timeStamp = timeStamp;
        this.predictedEnd = predictedEnd;
    }

    public BMLPerformanceStartFeedback(String bmlId, double timeStamp, double predictedEnd)
    {
        this("", bmlId, timeStamp, predictedEnd);
    }

    @Override
    public final String toString()
    {
        return "Performance Start of " + bmlId + " at " + timeStamp + " predicted end time " + predictedEnd + "\n";
    }
}
