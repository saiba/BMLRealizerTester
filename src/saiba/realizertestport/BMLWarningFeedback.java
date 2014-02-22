package saiba.realizertestport;

import java.util.Set;

/**
 * Indicates that a warning has occured when executing some BML behaviors.
 * modifiedBehaviours, modifiedConstraints and the warningText provide information on the recovery
 * strategy taken by the player/planner/scheduler. 
 * @author Herwin van Welbergen
 */
public final class BMLWarningFeedback
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
    public final Set<String> modifiedBehaviours;
    public final Set<String> modifiedConstraints;
    public final String warningText;
    public final double timeStamp;

    public BMLWarningFeedback(String bmlId, double timeStamp, Set<String> modifiedBehaviours,
            Set<String> modifiedConstraints, String warningText)
    {
        this("", bmlId, timeStamp, modifiedBehaviours, modifiedConstraints, warningText);
    }
    public BMLWarningFeedback(String id, String bmlId, double timeStamp, Set<String> modifiedBehaviours,
            Set<String> modifiedConstraints, String warningText)
    {
        this.characterId = id;
        this.bmlId = bmlId;
        this.modifiedBehaviours = modifiedBehaviours;
        this.modifiedConstraints = modifiedConstraints;
        this.warningText = warningText;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        return "BMLWarning " + characterId + " in BML block " + bmlId + " modified behaviors: "
                + modifiedBehaviours + " modified constraints " + modifiedConstraints
                + "\n message: " + warningText + "\n";
    }
}
