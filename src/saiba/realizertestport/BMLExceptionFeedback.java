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

import java.util.Set;

/**
 * Indicates that a failure has occured when executing the BML block with id bmlId.
 * 
 * @author Herwin van Welbergen
 */
public final class BMLExceptionFeedback
{
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

    public final double timeStamp;

    public final Set<String> failedBehaviours;

    public final Set<String> failedConstraints;

    public final String exceptionText;

    public final boolean performanceFailed;

    public BMLExceptionFeedback(String bmlId, double timeStamp, Set<String> failedBehaviours, Set<String> failedConstraints,
            String exceptionText, boolean performanceFailed)
    {
        this("",bmlId, timeStamp, failedBehaviours, failedConstraints,exceptionText, performanceFailed);
    }
    
    public BMLExceptionFeedback(String id, String bmlId, double timeStamp, Set<String> failedBehaviours, Set<String> failedConstraints,
            String exceptionText, boolean performanceFailed)
    {
        this.characterId = id;
        this.bmlId = bmlId;
        this.failedBehaviours = failedBehaviours;
        this.failedConstraints = failedConstraints;
        this.exceptionText = exceptionText;
        this.performanceFailed = performanceFailed;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        return "BMLException " + characterId + " in BML block " + bmlId + " failed behaviors: " + failedBehaviours + " failed constraints "
                + failedConstraints + "\n message: " + exceptionText + "\n";
    }
}
