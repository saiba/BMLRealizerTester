package bml.realizertestport;

/**
 * Indicates that a certain sync point is passed.
 * @author Herwin van Welbergen
 *
 */
public final class BMLSyncPointProgressFeedback
{
    public final double timeStamp;      //global time
    public final double bmlBlockTime;   //bml block time
    private String characterId;
    
    public String getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    public final String bmlId;    
    public final String behaviorId;
    public final String syncId;
    
    public BMLSyncPointProgressFeedback(BMLSyncPointProgressFeedback spp)
    {
        this.characterId = spp.characterId;
        this.bmlId = spp.bmlId;
        this.behaviorId = spp.behaviorId;
        this.syncId = spp.syncId;
        this.bmlBlockTime = spp.bmlBlockTime;
        this.timeStamp = spp.timeStamp;
    }
    
    public BMLSyncPointProgressFeedback(String bmlId, String behaviorId, String syncId, double bmlBlockTime, double timeStamp)
    {
        this("", bmlId, behaviorId, syncId, bmlBlockTime, timeStamp);
    }
    
    public BMLSyncPointProgressFeedback(String characterId, String bmlId, String behaviorId, String syncId, double bmlBlockTime, double timeStamp)
    {
        this.characterId = characterId;
        this.bmlId = bmlId;
        this.behaviorId = behaviorId;
        this.syncId = syncId;
        this.bmlBlockTime = bmlBlockTime;
        this.timeStamp = timeStamp;
    }
    
    @Override
    public String toString()
    {
        return "Sync-Point Progress: "+bmlId+":"+behaviorId+":"+syncId+
        " at local time: "+bmlBlockTime+
        " , global time: " +timeStamp+"\n";
    }
}
