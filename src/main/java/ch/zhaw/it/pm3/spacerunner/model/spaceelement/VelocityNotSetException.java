package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

public class VelocityNotSetException extends Exception {
    public VelocityNotSetException(String errorMessage)
    {
        super(errorMessage);
    }
}
