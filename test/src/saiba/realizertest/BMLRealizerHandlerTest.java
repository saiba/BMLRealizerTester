/*******************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2011 University of Twente, Bielefeld University
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package saiba.realizertest;

import org.junit.Test;

import saiba.realizertest.BMLRealizerHandler;
import saiba.realizertestport.BMLPerformanceStartFeedback;
import saiba.realizertestport.BMLPerformanceStopFeedback;
import saiba.realizertestport.BMLSyncPointProgressFeedback;

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
