package bml.realizertest;

import java.io.IOException;

import org.junit.Test;

import bml.bmlinfo.DefaultSyncPoints;

/**
 * Abstract BML Realizer test, contains only BML test cases
 * 
 * @author Herwin
 * 
 */
public abstract class AbstractDraft1RealizerTest extends AbstractRealizerTest
{
    @Override
    protected String getTestDirectory()
    {
        return "draft1";
    }
    
    @Test
    public void testAppend() throws InterruptedException, IOException
    {
        String bmlString1 = readTestFile("core/testspeech.xml");
        String bmlString2 = readTestFile("core/testspeechappend.xml");

        realizerHandler.performBML(bmlString1);
        realizerHandler.performBML(bmlString2);
        realizerHandler.waitForBMLEndFeedback("bml2");
        realizerHandler.assertSyncsInOrder("bml1", "speech1", DefaultSyncPoints.getDefaultSyncPoints("speech"));
        realizerHandler.assertSyncsInOrder("bml2", "speech1", DefaultSyncPoints.getDefaultSyncPoints("speech"));
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartLinkedToBlockStop("bml2","bml1");
        realizerHandler.assertDurationGreaterThanZero("bml1");
        realizerHandler.assertDurationGreaterThanZero("bml2");       
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1", "bml2");
    }

    @Test
    public void testAppendReplace() throws InterruptedException, IOException
    {
        final double DELAY = 3;
        String bmlString1 = readTestFile("core/appendandreplace/testspeech1.xml");
        String bmlString2 = readTestFile("core/appendandreplace/testspeech2.xml");
        String bmlString3 = readTestFile("core/appendandreplace/replace.xml");

        realizerHandler.performBML(bmlString1);
        realizerHandler.performBML(bmlString2);
        Thread.sleep((long) (DELAY * 1000));
        realizerHandler.performBML(bmlString3);

        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.waitForBMLEndFeedback("bml2");
        realizerHandler.waitForBMLEndFeedback("bml3");
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1", "bml2", "bml3");

        realizerHandler.clearFeedbackLists();
        
        realizerHandler.performBML(bmlString1);
        realizerHandler.performBML(bmlString2);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.waitForBMLEndFeedback("bml2");

        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1", "bml2");
    }
    
    @Test
    public void testPersistence() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/testpersistence.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForFeedback("bml1", "pose1", "stroke");
        Thread.sleep(1000);

        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartFeedbacks("bml1");
        realizerHandler.assertBlockStopFeedbacks();
        
        String bmlString2 = "<bml id=\"bml2\" composition=\"replace\"/>";
        realizerHandler.performBML(bmlString2);
        realizerHandler.waitForBMLEndFeedback("bml2");
        realizerHandler.assertSyncsInOrder("bml1", "pose1", DefaultSyncPoints.getDefaultSyncPoints("posture"));
        realizerHandler.assertValidSyncTimes();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1", "bml2");
    }
    
    @Test
    public void testReplace() throws InterruptedException, IOException
    {
        String bmlString1 = readTestFile("core/replace/testlongspeechandnod.xml");
        String bmlString2 = readTestFile("core/replace/replace.xml");
        realizerHandler.performBML(bmlString1);
        Thread.sleep(700);
        realizerHandler.performBML(bmlString2);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.waitForBMLEndFeedback("bml3");
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1", "bml3");
    }
    
    @Test
    public void testSpeechNodTimedToSyncConstraintSpec() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeechandnod_synctimed.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertSyncsInOrder("bml1", "speech1", "start", "s1", "end");
        realizerHandler.assertSyncsInOrder("bml1", "nod1", DefaultSyncPoints.getDefaultSyncPoints("head"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertRelativeSyncTime("bml1", "speech1", "s1", 10);
        realizerHandler.assertLinkedSyncs("bml1", "speech1", "start", "bml1", "nod1", "start");
        realizerHandler.assertSyncBeforeRelativeTime("bml1","speech1","start",10);
        realizerHandler.assertSyncAfterRelativeTime("bml1","speech1","end",10);
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }
}
