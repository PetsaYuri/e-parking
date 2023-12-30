package API.eparking.Exceptions.Cars;

public class CarWithThisNumbersAlreadyExistsException extends RuntimeException {
    public CarWithThisNumbersAlreadyExistsException()   {
        super("A car with this numbers already exists");
    }
}
