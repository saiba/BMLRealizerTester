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
package saiba.bmlinfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Contains the list of default sync points per behavior
 * @author welberge
 */
public final class DefaultSyncPoints
{
    // /Behaviors that are parsed
    private static final ImmutableMap<String, ImmutableList<String>> DEFAULT_SYNCS = 
        new ImmutableMap.Builder<String, ImmutableList<String>>()
        .put("wait", ImmutableList.of("start","end"))
        .put("face",ImmutableList.of("start", "attackPeak", "relax", "end"))
        .put("faceLexeme",ImmutableList.of("start", "attackPeak", "relax", "end"))
        .put("faceFacs",ImmutableList.of("start", "attackPeak", "relax", "end"))
        .put("faceShift",ImmutableList.of("start", "end"))
        .put("gaze", ImmutableList.of("start","ready","relax","end"))
        .put("gazeShift",ImmutableList.of("start","end"))
        .put("gesture", ImmutableList.of("start","ready","strokeStart","stroke","strokeEnd","relax","end"))
        .put("pointing", ImmutableList.of("start","ready","strokeStart","stroke","strokeEnd","relax","end"))
        .put("head", ImmutableList.of("start","ready","strokeStart","stroke","strokeEnd","relax","end"))
        .put("headDirectionShift", ImmutableList.of("start","end"))
        .put("locomotion", ImmutableList.of("start","end"))
        .put("posture", ImmutableList.of("start","ready","relax","end"))
        .put("postureShift", ImmutableList.of("start","end"))
        .put("speech", ImmutableList.of("start","end"))
        .build();
    
    public static String[] getDefaultSyncPoints(String behaviorType)
    {
        return DEFAULT_SYNCS.get(behaviorType).toArray(new String[DEFAULT_SYNCS.get(behaviorType).size()]);
    }
}
