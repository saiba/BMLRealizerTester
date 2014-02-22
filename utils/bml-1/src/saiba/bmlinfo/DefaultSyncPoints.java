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
