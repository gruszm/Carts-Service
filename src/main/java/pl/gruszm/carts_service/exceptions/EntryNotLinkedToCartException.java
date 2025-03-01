package pl.gruszm.carts_service.exceptions;

public class EntryNotLinkedToCartException extends Exception
{
    public EntryNotLinkedToCartException(String message)
    {
        super(message);
    }
}
