package bml.realizertest;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import bml.realizertestport.RealizerTestPort;
import bml.realizertestport.BMLExceptionFeedback;
import bml.realizertestport.BMLExceptionListener;
import bml.realizertestport.BMLFeedbackListener;
import bml.realizertestport.BMLPerformanceStartFeedback;
import bml.realizertestport.BMLPerformanceStopFeedback;
import bml.realizertestport.BMLSyncPointProgressFeedback;
import bml.realizertestport.BMLWarningFeedback;
import bml.realizertestport.BMLWarningListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;

/**
 * Captures realizer feedback and provides custom assertions on this feedback. 
 * @author Herwin
 *
 */
public class BMLRealizerHandler implements BMLWarningListener, BMLFeedbackListener, BMLExceptionListener
{
    protected static final Logger logger = LoggerFactory.getLogger("realizertester");

    protected List<BMLPerformanceStartFeedback> performanceStartList = Collections
            .synchronizedList(new ArrayList<BMLPerformanceStartFeedback>());
    protected List<BMLWarningFeedback> warningList = Collections.synchronizedList(new ArrayList<BMLWarningFeedback>());
    protected List<BMLExceptionFeedback> exceptionList = Collections.synchronizedList(new ArrayList<BMLExceptionFeedback>());
    protected List<BMLSyncPointProgressFeedback> feedbackProgressList = Collections
            .synchronizedList(new ArrayList<BMLSyncPointProgressFeedback>());
    protected List<BMLPerformanceStopFeedback> performanceStopList = Collections
            .synchronizedList(new ArrayList<BMLPerformanceStopFeedback>());
    protected final double SYNC_EPSILON = 0.1; // allowed time epsilon for syncs that should occur at the same time (=2 frames in 20fps realizer execution)

    private RealizerTestPort realizerTestPort;

    public void setRealizerTestPort(RealizerTestPort rtp)
    {
        realizerTestPort = rtp;
        rtp.addBMLExceptionListener(this);        
        rtp.addBMLWarningListener(this);
        rtp.addBMLFeedbackListener(this);        
    }
    
    public List<BMLWarningFeedback> getWarningList()
    {
        return warningList;
    }
    
    public List<BMLExceptionFeedback> getExceptionList()
    {
        return exceptionList;
    }
    
    public void performBML(String bmlString)
    {
        realizerTestPort.performBML(bmlString);
    }

    public void clearFeedbackLists()
    {
        feedbackProgressList.clear();
        performanceStopList.clear();
        performanceStartList.clear();
        warningList.clear();
        exceptionList.clear();
    }

    public BMLPerformanceStartFeedback getBMLPerformanceStartFeedback(String bmlId)
    {
        synchronized (performanceStartList)
        {
            for (BMLPerformanceStartFeedback bpfs : performanceStartList)
            {
                if (bpfs.bmlId.equals(bmlId))
                {
                    return bpfs;
                }
            }
        }
        return null;
    }

    public BMLPerformanceStopFeedback getBMLPerformanceStopFeedback(String bmlId)
    {
        synchronized (performanceStopList)
        {
            for (BMLPerformanceStopFeedback bpfs : performanceStopList)
            {
                if (bpfs.bmlId.equals(bmlId))
                {
                    return bpfs;
                }
            }
        }
        return null;
    }

    /**
     * throws BMLSyncPointNotFoundException when not found
     */
    protected BMLSyncPointProgressFeedback getBMLSyncPointProgressFeedback(String bmlId, String behaviorId, String syncId)
    {
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb : feedbackProgressList)
            {
                if (fb.bmlId.equals(bmlId) && fb.behaviorId.equals(behaviorId) && fb.syncId.equals(syncId))
                {
                    return fb;
                }
            }
        }
        throw new BMLSyncPointNotFoundException(bmlId, behaviorId, syncId, feedbackProgressList);
    }

    protected boolean hasBMLEndFeedback(String bmlId)
    {
        synchronized (performanceStopList)
        {
            for (BMLPerformanceStopFeedback fb : performanceStopList)
            {
                if (fb.bmlId.equals(bmlId))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean hasBMLEndFeedbacks(String bmlId)
    {
        synchronized (performanceStopList)
        {
            for (BMLPerformanceStopFeedback fb : performanceStopList)
            {
                if (fb.bmlId.equals(bmlId)) return true;
            }
        }
        return false;
    }

    protected boolean hasBMLStartFeedbacks(String bmlId)
    {
        synchronized (performanceStartList)
        {
            for (BMLPerformanceStartFeedback fb : performanceStartList)
            {
                if (fb.bmlId.equals(bmlId)) return true;
            }
        }
        return false;
    }

    protected boolean hasFeedback(String bmlId, String behaviorId, String syncId)
    {
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb : feedbackProgressList)
            {
                if (fb.bmlId.equals(bmlId) && fb.behaviorId.equals(behaviorId) && fb.syncId.equals(syncId))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void waitForFeedback(String bmlId, String behaviorId, String syncId)
    {
        while (!hasFeedback(bmlId, behaviorId, syncId))
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void waitForBMLEndFeedback(String bmlId)
    {
        while (!hasBMLEndFeedbacks(bmlId))
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void waitForBMLStartFeedback(String bmlId) throws InterruptedException
    {
        while (!hasBMLStartFeedbacks(bmlId))
        {
            wait();
        }
    }

    @Override
    public synchronized void warn(BMLWarningFeedback bw)
    {
        warningList.add(bw);
        notifyAll();
    }

    @Override
    public synchronized void exception(BMLExceptionFeedback be)
    {
        exceptionList.add(be);
        notifyAll();
    }

    @Override
    public synchronized void performanceStop(BMLPerformanceStopFeedback psf)
    {
        performanceStopList.add(psf);
        notifyAll();
    }

    @Override
    public synchronized void performanceStart(BMLPerformanceStartFeedback psf)
    {
        performanceStartList.add(psf);
        notifyAll();
    }

    @Override
    public synchronized void syncProgress(BMLSyncPointProgressFeedback spp)
    {
        feedbackProgressList.add(spp);
        notifyAll();
    }

    public void assertValidSyncTimes()
    {
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb1 : feedbackProgressList)
            {
                assertThat(fb1.bmlId + ":" + fb1.behaviorId + ":" + fb1.syncId + " bml block time ", fb1.bmlBlockTime,
                        greaterThanOrEqualTo(0d));
            }
        }
    }

    private Set<String> getFeedbackBehaviorIds(String bmlId)
    {
        Set<String> behIds = new HashSet<String>();
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb : feedbackProgressList)
            {
                if (fb.bmlId.equals(bmlId))
                {
                    behIds.add(fb.behaviorId);
                }
            }
        }
        return behIds;
    }

    /**
     * Assert that feedback only occurred for the following behaviors
     */
    public void assertFeedbackForBehaviors(String bmlId, String... behs)
    {
        assertThat(getFeedbackBehaviorIds(bmlId), containsInAnyOrder(behs));
    }

    protected void assertNoDuplicateStart()
    {
        synchronized (performanceStartList)
        {
            for (BMLPerformanceStartFeedback fb1 : performanceStartList)
            {
                for (BMLPerformanceStartFeedback fb2 : performanceStartList)
                {
                    if (fb1 != fb2)
                    {
                        if (fb1.bmlId.equals(fb2.bmlId))
                        {
                            fail("Duplicate startfeedback: " + fb1 + "List:" + performanceStartList);
                        }
                    }
                }
            }
        }
    }

    protected void assertNoDuplicateStop()
    {
        synchronized (performanceStopList)
        {
            for (BMLPerformanceStopFeedback fb1 : performanceStopList)
            {
                for (BMLPerformanceStopFeedback fb2 : performanceStopList)
                {
                    if (fb1 != fb2)
                    {
                        if (fb1.bmlId.equals(fb2.bmlId))
                        {
                            fail("Duplicate stopfeedback: " + fb1 + "List:" + performanceStopList);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for duplicate syncs in the feedbackProgress. This should not happen.
     */
    public void assertNoDuplicateSyncs()
    {
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb1 : feedbackProgressList)
            {
                for (BMLSyncPointProgressFeedback fb2 : feedbackProgressList)
                {
                    if (fb1 != fb2)
                    {
                        if (fb1.bmlId.equals(fb2.bmlId) && fb1.behaviorId.equals(fb2.behaviorId) && fb1.syncId.equals(fb2.syncId))
                        {
                            fail("Duplicate sync in feedback: " + fb1 + "List:" + feedbackProgressList);
                        }
                    }
                }
            }
        }
    }

    protected List<BMLSyncPointProgressFeedback> getFeedback(String bmlId, String behaviorId)
    {
        List<BMLSyncPointProgressFeedback> syncs = new ArrayList<BMLSyncPointProgressFeedback>();
        synchronized (feedbackProgressList)
        {
            for (BMLSyncPointProgressFeedback fb : feedbackProgressList)
            {
                if (fb.bmlId.equals(bmlId) && fb.behaviorId.equals(behaviorId))
                {
                    syncs.add(fb);
                }
            }
        }
        return syncs;
    }

    public void assertNoDuplicateFeedbacks()
    {
        assertNoDuplicateSyncs();
        assertNoDuplicateStart();
        assertNoDuplicateStop();
    }

    public void assertNoWarnings()
    {
        assertThat(warningList, Matchers.<BMLWarningFeedback> empty());
    }

    public void assertNoExceptions()
    {
        assertThat(exceptionList, Matchers.<BMLExceptionFeedback> empty());
    }

    public void assertBlockStartFeedbacks(String... blockIds)
    {
        assertTrue("Mismatch in length of expected blocks " + Arrays.toString(blockIds) + " and received performance start feedback "
                + performanceStartList, blockIds.length == performanceStartList.size());
        for (int i = 0; i < blockIds.length; i++)
        {
            assertEquals("Mismatch in " + i + "th element of expected block " + Arrays.toString(blockIds)
                    + " and performance start feedback " + performanceStartList, performanceStartList.get(i).bmlId, blockIds[i]);
        }
    }

    public void assertBlockStopFeedbacks(String... blockIds)
    {
        assertTrue("Mismatch in length of expected blocks " + Arrays.toString(blockIds) + " and received performance stop feedback "
                + performanceStopList, blockIds.length == performanceStopList.size());
        for (int i = 0; i < blockIds.length; i++)
        {
            assertEquals("Mismatch in " + i + "th element of expected block " + Arrays.toString(blockIds)
                    + " and performance stop feedback " + performanceStopList, performanceStopList.get(i).bmlId, blockIds[i]);
        }
    }

    public void assertBlockStartAndStopFeedbacks(String... blockIds)
    {
        assertBlockStartFeedbacks(blockIds);
        assertBlockStopFeedbacks(blockIds);
    }

    /**
     * Assert that listed syncs appear in order (other syncs may not be intertwined with them).
     */
    public void assertOnlyTheseSyncsInOrder(String bmlId, String behaviorId, String... syncIds)
    {
        List<BMLSyncPointProgressFeedback> syncs = getFeedback(bmlId, behaviorId);
        assertTrue(bmlId + ":" + behaviorId + " expected syncs " + Arrays.toString(syncIds) + " actual sync feedback " + syncs,
                syncIds.length == syncs.size());
        for (int i = 0; i < syncIds.length; i++)
        {
            assertEquals("Sync mismatch, expected: " + Arrays.toString(syncIds) + " actual feedback: " + syncs, syncIds[i],
                    syncs.get(i).syncId);
        }
    }
    
    /**
     * Assert that listed syncs appear in order (other syncs may be intertwined with them).
     */
    public void assertSyncsInOrder(String bmlId, String behaviorId, String... syncIds)
    {
        List<BMLSyncPointProgressFeedback> syncs = getFeedback(bmlId, behaviorId);
        assertFalse(bmlId + ":" + behaviorId + " expected syncs " + Arrays.toString(syncIds) + " actual sync feedback " + syncs,
                syncIds.length > syncs.size());
        
        int j = 0;
        for (int i = 0; i < syncIds.length; i++)
        {
            while(!syncs.get(j).syncId.equals(syncIds[i]))
            {
                j++;
                if(j>=syncs.size())
                {
                    fail(bmlId + ":" + behaviorId + " expected syncs " + Arrays.toString(syncIds) + " actual sync feedback " + syncs);
                }                
            }
        }
    }
    public void assertDurationGreaterThanZero(String bmlId)
    {
        BMLPerformanceStartFeedback pStart = getBMLPerformanceStartFeedback(bmlId);
        BMLPerformanceStopFeedback pEnd = getBMLPerformanceStopFeedback(bmlId);
        if (pStart == null)
        {
            fail("No start feedback for " + bmlId);
        }
        if (pEnd == null)
        {
            fail("No end feedback for " + bmlId);
        }
        assertThat(pEnd.timeStamp,greaterThan(pStart.timeStamp));                
    }
    
    public void assertAfterBlock(String bmlBeforeId, String bmlAfterId, double maxAfter)
    {
        BMLPerformanceStartFeedback pStart = getBMLPerformanceStartFeedback(bmlAfterId);
        if (pStart == null)
        {
            fail("No start feedback for " + bmlAfterId);
        }
        BMLPerformanceStopFeedback pEnd = getBMLPerformanceStopFeedback(bmlBeforeId);
        if (pEnd == null)
        {
            fail("No end feedback for " + bmlBeforeId);
        }
        assertThat(pStart.timeStamp, greaterThanOrEqualTo(pEnd.timeStamp));
        assertEquals(pEnd.timeStamp, pStart.timeStamp, maxAfter);
    }
    
    public void assertAfterBlock(String bmlBeforeId, String bmlAfterId)
    {
        BMLPerformanceStartFeedback pStart = getBMLPerformanceStartFeedback(bmlAfterId);
        if (pStart == null)
        {
            fail("No start feedback for " + bmlAfterId);
        }
        BMLPerformanceStopFeedback pEnd = getBMLPerformanceStopFeedback(bmlBeforeId);
        if (pEnd == null)
        {
            fail("No end feedback for " + bmlBeforeId);
        }
        assertThat(pStart.timeStamp, greaterThanOrEqualTo(pEnd.timeStamp));
    }
    
    public void assertBlockStartLinkedToBlockStop(String bmlIdStart, String bmlIdStop)
    {
        BMLPerformanceStartFeedback pStart = getBMLPerformanceStartFeedback(bmlIdStart);
        if (pStart == null)
        {
            fail("No start feedback for " + bmlIdStart);
        }
        BMLPerformanceStopFeedback pEnd = getBMLPerformanceStopFeedback(bmlIdStop);
        if (pEnd == null)
        {
            fail("No end feedback for " + bmlIdStop);
        }
        assertEquals(pStart.timeStamp, pEnd.timeStamp, SYNC_EPSILON);
    }

    public void assertSyncLinkedToBlockStart(String bmlId, String behId, String syncId, String bmlRef)
    {
        BMLSyncPointProgressFeedback fb = getBMLSyncPointProgressFeedback(bmlId, behId, syncId);
        BMLPerformanceStartFeedback psf = getBMLPerformanceStartFeedback(bmlRef);
        if (psf == null)
        {
            fail("No feedback for " + bmlRef);
        }
        assertEquals(fb.timeStamp, psf.timeStamp, SYNC_EPSILON);
    }

    public void assertRelativeSyncTime(String bmlId, String behId, String syncId, double time)
    {
        BMLSyncPointProgressFeedback fb = getBMLSyncPointProgressFeedback(bmlId, behId, syncId);
        logger.info("{}:{}:{} = {} was satisfied with epsilon {}",
                new Object[] { bmlId, behId, syncId, time, Math.abs(time - fb.bmlBlockTime) });
        assertEquals(time, fb.bmlBlockTime, SYNC_EPSILON);
    }

    public void assertLinkedSyncs(String bmlId1, String behaviorId1, String syncId1, String bmlId2, String behaviorId2, String syncId2)
    {
        assertLinkedSyncs(bmlId1, behaviorId1, syncId1, 0, bmlId2, behaviorId2, syncId2, 0);
    }

    public void assertLinkedSyncs(String bmlId1, String behaviorId1, String syncId1, double offset1, String bmlId2, String behaviorId2,
            String syncId2, double offset2)
    {
        BMLSyncPointProgressFeedback fb1 = getBMLSyncPointProgressFeedback(bmlId1, behaviorId1, syncId1);
        BMLSyncPointProgressFeedback fb2 = getBMLSyncPointProgressFeedback(bmlId2, behaviorId2, syncId2);
        logger.info("{}:{}:{}+{} = {}:{}:{}+{} was satisfied with epsilon {}", new Object[] { bmlId1, behaviorId1, syncId1, offset1,
                bmlId2, behaviorId2, syncId2, offset2, Math.abs((fb1.timeStamp + offset1) - (fb2.timeStamp + offset2)) });
        assertEquals(fb1.timeStamp + offset1, fb2.timeStamp + offset2, SYNC_EPSILON);
    }

    public void assertSyncBeforeRelativeTime(String bmlId, String behId, String syncId, double time)
    {
        BMLSyncPointProgressFeedback fb = getBMLSyncPointProgressFeedback(bmlId, behId, syncId);
        assertThat(fb.bmlBlockTime, lessThan(time));
    }

    public void assertSyncAfterRelativeTime(String bmlId, String behId, String syncId, double time)
    {
        BMLSyncPointProgressFeedback fb = getBMLSyncPointProgressFeedback(bmlId, behId, syncId);        
        assertThat(fb.bmlBlockTime, greaterThan(time));
    }
    
    public void assertSyncAfterSync(String bmlId1, String behId1, String syncId1, String bmlId2, String behId2, String syncId2)
    {
        BMLSyncPointProgressFeedback fb1 = getBMLSyncPointProgressFeedback(bmlId1, behId1, syncId1);
        BMLSyncPointProgressFeedback fb2 = getBMLSyncPointProgressFeedback(bmlId2, behId2, syncId2);
        assertThat(fb1.bmlBlockTime, greaterThan(fb2.bmlBlockTime));
    }
    
    public void assertSyncBeforeSync(String bmlId1, String behId1, String syncId1, String bmlId2, String behId2, String syncId2)
    {
        BMLSyncPointProgressFeedback fb1 = getBMLSyncPointProgressFeedback(bmlId1, behId1, syncId1);
        BMLSyncPointProgressFeedback fb2 = getBMLSyncPointProgressFeedback(bmlId2, behId2, syncId2);        
        assertThat(fb1.bmlBlockTime, lessThan(fb2.bmlBlockTime));
    }
    
    public StringBuffer getTrace()
    {
        StringBuffer buffer = new StringBuffer();
        if (feedbackProgressList != null)
        {
            buffer.append("------------------------------------------------\n");
            buffer.append("feedback progress: \n");
            buffer.append(feedbackProgressList.toString());
            buffer.append("------------------------------------------------\n");
        }
        if (performanceStartList != null)
        {
            buffer.append("BML performance start: \n");
            buffer.append(performanceStartList);
            buffer.append("------------------------------------------------\n");
        }
        if (performanceStopList != null)
        {
            buffer.append("BML performance stop: \n");
            buffer.append(performanceStopList);
            buffer.append("------------------------------------------------\n");
        }
        if (exceptionList!=null)
        {
            buffer.append("Exceptions: \n");
            buffer.append(exceptionList);
            buffer.append("------------------------------------------------\n");
        }
        if (warningList!=null)
        {
            buffer.append("Warnings: \n");
            buffer.append(warningList);
            buffer.append("------------------------------------------------\n");
        }
        return buffer;
    }
}
