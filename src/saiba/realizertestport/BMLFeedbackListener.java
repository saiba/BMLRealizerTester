package saiba.realizertestport;

/**
 * Captures BML feedback callbacks. 
 * @author Herwin van Welbergen
 */
public interface BMLFeedbackListener
{
    void syncProgress(BMLSyncPointProgressFeedback spp);
    void performanceStop(BMLPerformanceStopFeedback psf);
    void performanceStart(BMLPerformanceStartFeedback psf);
}
