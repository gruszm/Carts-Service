package pl.gruszm.carts_service.DTOs;

import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.entities.CartEntry;

import java.util.List;

public class CartResponse
{
    private List<CartEntry> cartEntries;

    public CartResponse(Cart cart)
    {
        this.cartEntries = cart.getCartEntries();
    }

    public List<CartEntry> getCartEntries()
    {
        return cartEntries;
    }

    public void setCartEntries(List<CartEntry> cartEntries)
    {
        this.cartEntries = cartEntries;
    }
}
