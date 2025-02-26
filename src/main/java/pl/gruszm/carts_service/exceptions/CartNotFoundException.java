package pl.gruszm.carts_service.exceptions;

public class CartNotFoundException extends Exception
{
    public CartNotFoundException(String message)
    {
        super(message);
    }
}
