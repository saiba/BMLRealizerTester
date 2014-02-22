package bml.realizertest;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages and obtains feedback timing for Realizers that do not handle this within their feedback messages.<br>
 * Usage:<br>
 * Whenever a blockstart feedback occurs, call setBlockStart.<br>
 *  
 * @author welberge
 */
public class FeedbackTimingManager
{
    private Map<String,Double>globalBlockTime = new HashMap<String,Double>();
    
    private static class NoBlockStartFeedbackReceivedException extends RuntimeException
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public NoBlockStartFeedbackReceivedException(String bmlBlock)
        {
            super("NoBlockStartFeedbackReceivedException for bml block:"+bmlBlock);
        }
    }
    
    public void setBlockStart(String bmlId, double globalTime)
    {
        globalBlockTime.put(bmlId,globalTime);
    }
    
    public double getBlockTime(String bmlId, double globalTime)
    {
        Double blockTime = globalBlockTime.get(bmlId);
        if(blockTime==null)
        {
            throw new NoBlockStartFeedbackReceivedException(bmlId);
        }
        return globalTime-blockTime;
    }
}
