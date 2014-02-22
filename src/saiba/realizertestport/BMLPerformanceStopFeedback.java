package saiba.realizertestport;

/**
 * Indicates that a BML block has stopped playing. 
 * @author Herwin van Welbergen
 */
public final class BMLPerformanceStopFeedback
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
    public final String reason;
    
    public BMLPerformanceStopFeedback(String characterId, String bmlId, String reason, double timeStamp)
    {
        this.characterId = characterId;
        this.bmlId = bmlId;
        this.reason = reason;
        this.timeStamp = timeStamp;
    }
    
    public BMLPerformanceStopFeedback(String bmlId, String reason, double timeStamp)
    {
        this("",bmlId,reason,timeStamp);
    }
    
    @Override
    public String toString()
    {
        return "Performance Stop of "+bmlId+" at "+timeStamp+"\n"; 
    }
}
