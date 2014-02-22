package saiba.realizertest;

import java.util.Collection;

/**
 * BMLSyncPointProgressFeedbackNotFoundException bmlId:behaviorId:syncId not found
 * @author Herwin
 *
 */
public class BMLSyncPointNotFoundException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BMLSyncPointNotFoundException(String bmlId, String behaviorId, String syncId, Collection<?> allSyncs)
    {
        super("BMLSyncPointProgressFeedbackNotFoundException "+bmlId+":"+behaviorId+":"+syncId+" available syncs: "+allSyncs);
    }
}
