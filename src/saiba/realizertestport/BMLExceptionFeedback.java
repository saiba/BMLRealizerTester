package saiba.realizertestport;

import java.util.Set;

/**
 * Indicates that a failure has occured when executing the BML block with id bmlId.
 * 
 * @author Herwin van Welbergen
 */
public final class BMLExceptionFeedback
{
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

    public final double timeStamp;

    public final Set<String> failedBehaviours;

    public final Set<String> failedConstraints;

    public final String exceptionText;

    public final boolean performanceFailed;

    public BMLExceptionFeedback(String bmlId, double timeStamp, Set<String> failedBehaviours, Set<String> failedConstraints,
            String exceptionText, boolean performanceFailed)
    {
        this("",bmlId, timeStamp, failedBehaviours, failedConstraints,exceptionText, performanceFailed);
    }
    
    public BMLExceptionFeedback(String id, String bmlId, double timeStamp, Set<String> failedBehaviours, Set<String> failedConstraints,
            String exceptionText, boolean performanceFailed)
    {
        this.characterId = id;
        this.bmlId = bmlId;
        this.failedBehaviours = failedBehaviours;
        this.failedConstraints = failedConstraints;
        this.exceptionText = exceptionText;
        this.performanceFailed = performanceFailed;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        return "BMLException " + characterId + " in BML block " + bmlId + " failed behaviors: " + failedBehaviours + " failed constraints "
                + failedConstraints + "\n message: " + exceptionText + "\n";
    }
}
