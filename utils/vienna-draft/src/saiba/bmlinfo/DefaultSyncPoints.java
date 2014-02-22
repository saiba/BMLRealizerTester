package saiba.bmlinfo;

import com.google.common.collect.ImmutableList;

/**
 * Contains the list of default sync points per behavior
 * @author welberge
 */
public final class DefaultSyncPoints
{
    public static String[] getDefaultSyncPoints(String behaviorType)
    {
        return ImmutableList.of("start","ready","stroke_start","stroke","stroke_end","relax","end").toArray(new String[0]);
    }
}