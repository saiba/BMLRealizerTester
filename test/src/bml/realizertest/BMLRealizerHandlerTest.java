package bml.realizertest;

import org.junit.Test;

import bml.realizertestport.BMLPerformanceStartFeedback;
import bml.realizertestport.BMLPerformanceStopFeedback;
import bml.realizertestport.BMLSyncPointProgressFeedback;

/**
 * Unit tests for the BMLRealizerHandler
 * @author hvanwelbergen
 *
 */
public class BMLRealizerHandlerTest
{
    private BMLRealizerHandler handler = new BMLRealizerHandler();
    
    @Test(expected = AssertionError.class)
    public void assertSyncsInOrderFailsWithMissingSync()
    {
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "start", 0, 0));
        handler.assertSyncsInOrder("bml1","beh1","start","end");
    }
    
    @Test
    public void assertSyncsInOrderPasses()
    {
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "start", 0, 0));
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "relax", 0, 0));
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "end", 0, 0));
        handler.assertSyncsInOrder("bml1","beh1","start","end");
    }
    
    @Test(expected = AssertionError.class)
    public void assertOnlyTheseSyncsInOrderFailure()
    {
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "start", 0, 0));
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "relax", 0, 0));
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "end", 0, 0));
        handler.assertOnlyTheseSyncsInOrder("bml1","beh1","start","end");
    }
    
    @Test
    public void assertOnlyTheseSyncsInOrder()
    {
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "start", 0, 0));
        handler.syncProgress(new BMLSyncPointProgressFeedback("bml1", "beh1", "end", 0, 0));
        handler.assertOnlyTheseSyncsInOrder("bml1","beh1","start","end");
    }
    
    @Test
    public void assertDirectAfterBlock()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 1, 3));
        handler.assertAfterBlock("bml1", "bml2");
    }
    
    @Test
    public void assertAfterBlock()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 1.1, 3));
        handler.assertAfterBlock("bml1", "bml2");
    }
    
    @Test
    public void assertDirectAfterBlockSpecDelay()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 1, 3));
        handler.assertAfterBlock("bml1", "bml2", 0.1);
    }
    
    @Test
    public void assertDelayedAfterBlockSpecDelay()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 1.1, 3));
        handler.assertAfterBlock("bml1", "bml2", 0.5);
    }
    
    @Test(expected = AssertionError.class)
    public void assertDelayedAfterBlockSpecDelayTooLate()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 1.6, 3));
        handler.assertAfterBlock("bml1", "bml2", 0.5);
    }
    
    @Test(expected = AssertionError.class)
    public void assertAfterBlockNotAfterSpecDelay()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 0.9, 3));
        handler.assertAfterBlock("bml1", "bml2", 0.5);
    }
    
    @Test(expected = AssertionError.class)
    public void assertAfterBlockNotAfter()
    {
        handler.performanceStop(new BMLPerformanceStopFeedback("char1", "bml1", "", 1));
        handler.performanceStart(new BMLPerformanceStartFeedback("char1", "bml2", 0.9, 3));
        handler.assertAfterBlock("bml1", "bml2");
    }
}
