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

/**
 * Indicates that a BML block has stopped playing. 
 * @author Herwin van Welbergen
 */
public final class BMLPerformanceStopFeedback
{
    public final double timeStamp;
    public String characterId;
    public String getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    public final String bmlId;    
    public final String reason;
    
    public BMLPerformanceStopFeedback(String characterId, String bmlId, String reason, double timeStamp)
    {
        this.characterId = characterId;
        this.bmlId = bmlId;
        this.reason = reason;
        this.timeStamp = timeStamp;
    }
    
    public BMLPerformanceStopFeedback(String bmlId, String reason, double timeStamp)
    {
        this("",bmlId,reason,timeStamp);
    }
    
    @Override
    public String toString()
    {
        return "Performance Stop of "+bmlId+" at "+timeStamp+"\n"; 
    }
}
