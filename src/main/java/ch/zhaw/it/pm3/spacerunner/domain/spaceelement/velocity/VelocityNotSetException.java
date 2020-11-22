package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity;

/**
 * Exception to be thrown if there is no visual set
 */
public class VelocityNotSetException extends Exception {
    public VelocityNotSetException(String errorMessage) {
        super(errorMessage);
    }
}
