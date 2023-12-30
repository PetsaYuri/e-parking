package API.eparking.Exceptions.PromoCodes;

public class PromoCodeWithoutRequiredFieldInTheBodyException extends Exception{
    public PromoCodeWithoutRequiredFieldInTheBodyException() {
        super("The body is missing a required field");
    }
}
