package saiba.realizertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import saiba.bmlinfo.DefaultSyncPoints;
import saiba.realizertestport.BMLWarningFeedback;


/**
 * Abstract RealizerTester to test BML1.0 compliant realizers
 * @author hvanwelbergen
 *
 */
public class AbstractBML1RealizerTest extends AbstractDraft1RealizerTest
{
    @Test
    @Ignore
    public void testPersistence(){}

    @Override
    protected String getTestDirectory()
    {
        return "bml-1";
    }
    
    @Test
    @Override
    public void testGazeOverlap() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testgazeoverlap.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertNoDuplicateFeedbacks();
        assertEquals(1, realizerHandler.getWarningList().size());
        BMLWarningFeedback exp = realizerHandler.getWarningList().get(0);
        assertEquals(1, exp.modifiedBehaviours.size());

        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        if (exp.modifiedBehaviours.contains("gaze2"))
        {
            realizerHandler.assertSyncsInOrder("bml1", "gaze1", DefaultSyncPoints.getDefaultSyncPoints("gaze"));
            realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "start", 1);
            realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "end", 10);
        }
        else if (exp.modifiedBehaviours.contains("gaze1"))
        {
            realizerHandler.assertSyncsInOrder("bml1", "gaze2", DefaultSyncPoints.getDefaultSyncPoints("gaze"));
            realizerHandler.assertRelativeSyncTime("bml1", "gaze2", "start", 1);
            realizerHandler.assertRelativeSyncTime("bml1", "gaze2", "end", 10);
        }
        else
        {
            fail("one of gaze1, gaze 2 should have failed, but behavior " + exp.modifiedBehaviours + " failed instead");
        }
    }
    
    @Test
    @Override
    public void testInvalidBlock() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/bmlnoid.xml");
        realizerHandler.performBML(bmlString);
        Thread.sleep(500);
        assertEquals(1, realizerHandler.getWarningList().size());
    }
    
    @Test
    public void testGazeWaist() throws InterruptedException, IOException
    {
        String str = readTestFile("core/testgazewaist.xml");
        realizerHandler.performBML(str);
        realizerHandler.waitForBMLEndFeedback("bml1");
        
        realizerHandler.assertSyncsInOrder("bml1", "gaze1",DefaultSyncPoints.getDefaultSyncPoints("gaze"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }
    
    @Test
    public void testPostureShift() throws InterruptedException, IOException
    {
        String str = readTestFile("core/testpose.xml");
        realizerHandler.performBML(str);
        realizerHandler.waitForBMLEndFeedback("bml1");
        
        realizerHandler.assertRelativeSyncTime("bml1", "pose1", "start", 5);
        realizerHandler.assertRelativeSyncTime("bml1", "pose1", "end", 8);
        realizerHandler.assertSyncsInOrder("bml1", "pose1",DefaultSyncPoints.getDefaultSyncPoints("postureShift"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }
    
    @Test
    public void testGazeShift() throws InterruptedException, IOException
    {
        String str = readTestFile("core/testgazeshift.xml");
        realizerHandler.performBML(str);
        realizerHandler.waitForBMLEndFeedback("bml1");
        
        realizerHandler.assertSyncsInOrder("bml1", "gaze1",DefaultSyncPoints.getDefaultSyncPoints("gazeShift"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }
}
