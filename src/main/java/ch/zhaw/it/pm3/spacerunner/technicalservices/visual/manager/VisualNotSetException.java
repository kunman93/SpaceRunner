package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

/**
 * Exception for case where there is no visual set.
 */
public class VisualNotSetException extends Exception {
    public VisualNotSetException(String errorMessage) {
        super(errorMessage);
    }
}
