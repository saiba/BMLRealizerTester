package saiba.realizertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import hmi.testutil.rules.TimeoutCallback;
import hmi.testutil.rules.TimeoutWithCallback;
import hmi.util.Resources;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bmlinfo.DefaultSyncPoints;
import saiba.realizertestport.BMLExceptionFeedback;

/**
 * Generic testcases for all realizers
 * @author hvanwelbergen
 * 
 */
public abstract class AbstractRealizerTest
{
    protected abstract String getTestDirectory();
    protected BMLRealizerHandler realizerHandler = new BMLRealizerHandler();

    protected static final Logger logger = LoggerFactory.getLogger("realizertester");

    private class ProgressInfo implements TimeoutCallback
    {
        BMLRealizerHandler handler;
        ProgressInfo(BMLRealizerHandler handler)
        {
            this.handler = handler;
        }
        
        @Override
        public String getProgressInfo()
        {
            return handler.getTrace().toString();
        }
    }

    @Rule
    public MethodRule globalTimeoutCallback = new TimeoutWithCallback(150000, new ProgressInfo(realizerHandler));

    protected String readTestFile(String filename) throws IOException
    {

        Resources res = new Resources(getTestDirectory());
        BufferedReader reader = res.getReader(filename);
        StringBuffer fileData = new StringBuffer(1000);
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    @Test
    public void testEmptyBlock() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/empty.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
    }

    @Test
    public void testInvalidBlock() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/bmlnoid.xml");
        realizerHandler.performBML(bmlString);
        Thread.sleep(500);
        assertEquals(1, realizerHandler.getExceptionList().size());
    }

    @Test
    public void testFace() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/testface.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "face1", DefaultSyncPoints.getDefaultSyncPoints("face"));
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
    }

    @Test
    public void testBeatGesture() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/testbeatgesture.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "g1", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
    }

    @Test
    public void testSpeech() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeech.xml");
        realizerHandler.performBML(bmlString);

        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "speech1", DefaultSyncPoints.getDefaultSyncPoints("speech"));
        realizerHandler.assertRelativeSyncTime("bml1", "speech1", "start", 0);
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testSpeech2() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeech2.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertSyncsInOrder("bml1", "speech1", "start", "syncstart1", "end");
        realizerHandler.assertRelativeSyncTime("bml1", "speech1", "start", 6);
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testSpeechNodTimedToSync() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeech_nodtimedtosync.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertSyncsInOrder("bml1", "speech1", "start", "syncstart1", "end");
        realizerHandler.assertSyncsInOrder("bml1", "nod1", DefaultSyncPoints.getDefaultSyncPoints("head"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertRelativeSyncTime("bml1", "speech1", "start", 6);
        realizerHandler.assertRelativeSyncTime("bml1", "nod1", "end", 9);
        realizerHandler.assertLinkedSyncs("bml1", "speech1", "syncstart1", "bml1", "nod1", "start");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testSpeechNodTimedToSyncInverseOrder() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeech_nodtimedtosyncinverseorder.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();

        realizerHandler.assertSyncsInOrder("bml1", "speech1", "start", "syncstart1", "end");
        realizerHandler.assertSyncsInOrder("bml1", "nod1", DefaultSyncPoints.getDefaultSyncPoints("head"));
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertRelativeSyncTime("bml1", "speech1", "start", 6);
        realizerHandler.assertRelativeSyncTime("bml1", "nod1", "end", 9);
        realizerHandler.assertLinkedSyncs("bml1", "speech1", "syncstart1", "bml1", "nod1", "start");
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testSpeechGesturesSync2x() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeechgesturesync2x.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertSyncsInOrder("bml1", "beat5", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertSyncsInOrder("bml1", "beatstop", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertSyncsInOrder("bml1", "speech1", DefaultSyncPoints.getDefaultSyncPoints("speech"));
        realizerHandler.assertSyncsInOrder("bml1", "speech2", DefaultSyncPoints.getDefaultSyncPoints("speech"));
        realizerHandler.assertLinkedSyncs("bml1", "speech1", "start", "bml1", "beat5", "stroke");
        realizerHandler.assertLinkedSyncs("bml1", "speech2", "start", "bml1", "beatstop", "end");
        realizerHandler.assertLinkedSyncs("bml1", "beatstop", "start", 0, "bml1", "beat5", "end", 3);
    }

    @Test
    public void testNod() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testnodlong.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "nod1", DefaultSyncPoints.getDefaultSyncPoints("head"));
        realizerHandler.assertRelativeSyncTime("bml1", "nod1", "start", 0);
        realizerHandler.assertRelativeSyncTime("bml1", "nod1", "end", 5);
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testGaze() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testgaze.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "gaze1", DefaultSyncPoints.getDefaultSyncPoints("gaze"));
        realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "start", 1);
        realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "end", 10);
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testGazeOverlap() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testgazeoverlap.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertNoDuplicateFeedbacks();
        assertEquals(1, realizerHandler.getExceptionList().size());
        BMLExceptionFeedback exp = realizerHandler.getExceptionList().get(0);
        assertEquals(1, exp.failedBehaviours.size());

        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        if (exp.failedBehaviours.contains("gaze2"))
        {
            realizerHandler.assertSyncsInOrder("bml1", "gaze1", DefaultSyncPoints.getDefaultSyncPoints("gaze"));
            realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "start", 1);
            realizerHandler.assertRelativeSyncTime("bml1", "gaze1", "end", 10);
        }
        else if (exp.failedBehaviours.contains("gaze1"))
        {
            realizerHandler.assertSyncsInOrder("bml1", "gaze2", DefaultSyncPoints.getDefaultSyncPoints("gaze"));
            realizerHandler.assertRelativeSyncTime("bml1", "gaze2", "start", 1);
            realizerHandler.assertRelativeSyncTime("bml1", "gaze2", "end", 10);
        }
        else
        {
            fail("one of gaze1, gaze 2 should have failed, but behavior " + exp.failedBehaviours + " failed instead");
        }
    }

    @Test
    public void testPoint() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testpoint.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
        realizerHandler.assertSyncsInOrder("bml1", "point1", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertRelativeSyncTime("bml1", "point1", "start", 1);
        realizerHandler.assertRelativeSyncTime("bml1", "point1", "end", 10);
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
    }

    @Test
    public void testSpeechGestures() throws InterruptedException, IOException
    {
        String bmlString = readTestFile("core/testspeechgestures.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");
        Thread.sleep(1000);
        realizerHandler.assertNoExceptions();
        realizerHandler.assertNoWarnings();
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertSyncsInOrder("bml1", "welkom", "start","deicticheart1","beat1b1","end");
        realizerHandler.assertSyncsInOrder("bml1", "g1", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertSyncsInOrder("bml1", "relaxleft", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertSyncsInOrder("bml1", "transleft", DefaultSyncPoints.getDefaultSyncPoints("gesture"));
        realizerHandler.assertRelativeSyncTime("bml1", "g1", "start", 2);
        realizerHandler.assertRelativeSyncTime("bml1", "g1", "end", 4);
        realizerHandler.assertRelativeSyncTime("bml1", "welkom", "deicticheart1", 2);
        realizerHandler.assertRelativeSyncTime("bml1", "transleft", "start", 0);
        realizerHandler.assertRelativeSyncTime("bml1", "transleft", "end", 2);
        realizerHandler.assertRelativeSyncTime("bml1", "relaxleft", "start", 4.5);
        realizerHandler.assertRelativeSyncTime("bml1", "relaxleft", "end", 6.8);
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
    }

    @Test
    public void testVeryShortBehaviour() throws IOException, InterruptedException
    {
        String bmlString = readTestFile("core/testshortbeh.xml");
        realizerHandler.performBML(bmlString);
        realizerHandler.waitForBMLEndFeedback("bml1");

        // A 'never executed exception' could occur since the behavior is very short.
        // assertNoExceptions();
        
        realizerHandler.assertNoDuplicateFeedbacks();
        realizerHandler.assertBlockStartAndStopFeedbacks("bml1");
    }

}
