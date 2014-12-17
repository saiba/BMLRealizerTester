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
