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
 * Indicates that a certain sync point is passed.
 * @author Herwin van Welbergen
 *
 */
public final class BMLSyncPointProgressFeedback
{
    public final double timeStamp;      //global time
    public final double bmlBlockTime;   //bml block time
    private String characterId;
    
    public String getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    public final String bmlId;    
    public final String behaviorId;
    public final String syncId;
    
    public BMLSyncPointProgressFeedback(BMLSyncPointProgressFeedback spp)
    {
        this.characterId = spp.characterId;
        this.bmlId = spp.bmlId;
        this.behaviorId = spp.behaviorId;
        this.syncId = spp.syncId;
        this.bmlBlockTime = spp.bmlBlockTime;
        this.timeStamp = spp.timeStamp;
    }
    
    public BMLSyncPointProgressFeedback(String bmlId, String behaviorId, String syncId, double bmlBlockTime, double timeStamp)
    {
        this("", bmlId, behaviorId, syncId, bmlBlockTime, timeStamp);
    }
    
    public BMLSyncPointProgressFeedback(String characterId, String bmlId, String behaviorId, String syncId, double bmlBlockTime, double timeStamp)
    {
        this.characterId = characterId;
        this.bmlId = bmlId;
        this.behaviorId = behaviorId;
        this.syncId = syncId;
        this.bmlBlockTime = bmlBlockTime;
        this.timeStamp = timeStamp;
    }
    
    @Override
    public String toString()
    {
        return "Sync-Point Progress: "+bmlId+":"+behaviorId+":"+syncId+
        " at local time: "+bmlBlockTime+
        " , global time: " +timeStamp+"\n";
    }
}
