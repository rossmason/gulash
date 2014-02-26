package org.mule.module.core;


public enum TimePeriod
{

    MILLIS(null, 1), SECONDS(MILLIS, 1000L), MINUTES(SECONDS, 60L), HOURS(MINUTES, 60L), DAYS(HOURS, 24L);

    private final long convertRateToMillis;

    /**
     * @param parent              the parent time period
     * @param convertRateToMillis the convert rate that should be applied
     *                            to the parent rate to convert it to millis.
     *                            in case that parent is null, uses just it.
     */
    private TimePeriod(TimePeriod parent, long convertRateToMillis)
    {
        if (parent == null)
        {
            this.convertRateToMillis = convertRateToMillis;
        }
        else
        {
            this.convertRateToMillis = parent.getConvertRateToMillis() * convertRateToMillis;
        }
    }

    /**
     * Returns the convert rate to millis.
     *
     * @return the convert rate to millis
     */
    public long getConvertRateToMillis()
    {
        return convertRateToMillis;
    }

    /**
     * Converts the given {@code duration} to millis.
     *
     * @param duration the duration to be converted to millis
     * @return the durations in millis
     */
    public long toMillis(long duration)
    {
        return duration * getConvertRateToMillis();
    }
}
