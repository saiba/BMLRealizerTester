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
 * Indicates that a warning has occured when executing some BML behaviors.
 * modifiedBehaviours, modifiedConstraints and the warningText provide information on the recovery
 * strategy taken by the player/planner/scheduler. 
 * @author Herwin van Welbergen
 */
public final class BMLWarningFeedback
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
    public final Set<String> modifiedBehaviours;
    public final Set<String> modifiedConstraints;
    public final String warningText;
    public final double timeStamp;

    public BMLWarningFeedback(String bmlId, double timeStamp, Set<String> modifiedBehaviours,
            Set<String> modifiedConstraints, String warningText)
    {
        this("", bmlId, timeStamp, modifiedBehaviours, modifiedConstraints, warningText);
    }
    public BMLWarningFeedback(String id, String bmlId, double timeStamp, Set<String> modifiedBehaviours,
            Set<String> modifiedConstraints, String warningText)
    {
        this.characterId = id;
        this.bmlId = bmlId;
        this.modifiedBehaviours = modifiedBehaviours;
        this.modifiedConstraints = modifiedConstraints;
        this.warningText = warningText;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        return "BMLWarning " + characterId + " in BML block " + bmlId + " modified behaviors: "
                + modifiedBehaviours + " modified constraints " + modifiedConstraints
                + "\n message: " + warningText + "\n";
    }
}
