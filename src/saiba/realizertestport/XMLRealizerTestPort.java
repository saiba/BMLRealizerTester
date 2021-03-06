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
package saiba.realizertestport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import saiba.bml.feedback.BMLFeedback;
import saiba.bml.feedback.BMLFeedbackParser;

import com.google.common.collect.ImmutableSet;

/**
 * Wraps bml 1.0 feedback into the feedbacklisteners of the RealizerTester
 * @author Herwin
 *
 */
public abstract class XMLRealizerTestPort implements RealizerTestPort
{
    private List<BMLExceptionListener> exceptionListeners = new ArrayList<BMLExceptionListener>();
    private List<BMLWarningListener> warningListeners = new ArrayList<BMLWarningListener>();
    private List<BMLFeedbackListener> feedbackListeners = new ArrayList<BMLFeedbackListener>();

    private String getBMLIdFromId(String id)
    {
        return id.split(":")[0];
    }

    private String getBehIdFromId(String id)
    {
        String idSplit[] = id.split(":");
        if (idSplit.length >= 2)
        {
            return idSplit[1];
        }
        return "";
    }

    public void feedback(String feedback)
    {
        BMLFeedback fb;
        try
        {
            fb = BMLFeedbackParser.parseFeedback(feedback);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        if (fb instanceof saiba.bml.feedback.BMLPredictionFeedback)
        {
            // not used yet
        }
        else if (fb instanceof saiba.bml.feedback.BMLBlockProgressFeedback)
        {
            saiba.bml.feedback.BMLBlockProgressFeedback fbb = (saiba.bml.feedback.BMLBlockProgressFeedback) fb;
            for (BMLFeedbackListener l : feedbackListeners)
            {
                if (fbb.getSyncId().equals("start"))
                {
                    l.performanceStart(new saiba.realizertestport.BMLPerformanceStartFeedback(fbb.getCharacterId(), fbb.getBmlId(), fbb
                            .getGlobalTime(), fbb.getGlobalTime()));
                }
                else if (fbb.getSyncId().equals("end"))
                {
                    l.performanceStop(new saiba.realizertestport.BMLPerformanceStopFeedback(fbb.getCharacterId(), fbb.getBmlId(), "", fbb
                            .getGlobalTime()));
                }
            }
        }
        else if (fb instanceof saiba.bml.feedback.BMLSyncPointProgressFeedback)
        {
            saiba.bml.feedback.BMLSyncPointProgressFeedback spp = (saiba.bml.feedback.BMLSyncPointProgressFeedback) fb;
            BMLSyncPointProgressFeedback spf = new BMLSyncPointProgressFeedback(spp.getBMLId(), spp.getBehaviourId(), spp.getSyncId(),
                    spp.getTime(), spp.getGlobalTime());
            for (BMLFeedbackListener fpp : feedbackListeners)
            {
                fpp.syncProgress(spf);
            }
        }
        else if (fb instanceof saiba.bml.feedback.BMLWarningFeedback)
        {
            saiba.bml.feedback.BMLWarningFeedback wfb = (saiba.bml.feedback.BMLWarningFeedback) fb;
            BMLWarningFeedback be = new BMLWarningFeedback(wfb.getCharacterId(), getBMLIdFromId(wfb.getId()), 0, ImmutableSet.of(getBehIdFromId(wfb.getId())),
                    new HashSet<String>(), wfb.getDescription());

            for (BMLWarningListener l : warningListeners)
            {
                l.warn(be);
            }
        }
    }

    public void addBMLExceptionListener(BMLExceptionListener l)
    {
        exceptionListeners.add(l);
    }

    public void addBMLWarningListener(BMLWarningListener l)
    {
        warningListeners.add(l);
    }

    public void addBMLFeedbackListener(BMLFeedbackListener l)
    {
        feedbackListeners.add(l);
    }
}
