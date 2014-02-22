package saiba.realizertestport;

/**
 * Interface to a realizer implementation
 * @author welberge
 */
public interface RealizerTestPort
{
    /**
     * Asks the realizer to perform a BML block. Non-blocking: this call will NOT block until the BML 
     * has been completely performed! It may block until the BML has been scheduled, though -- this is undetermined.
     */
    void performBML(String bmlString);
    
    void addBMLExceptionListener(BMLExceptionListener l);
    void addBMLWarningListener(BMLWarningListener l);
    void addBMLFeedbackListener(BMLFeedbackListener l);
}
