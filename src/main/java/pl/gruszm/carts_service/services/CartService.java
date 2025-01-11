package pl.gruszm.carts_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.entities.CartEntry;
import pl.gruszm.carts_service.repositories.CartRepository;

@Service
public class CartService
{
    @Autowired
    private CartRepository cartRepository;

    public Cart getCartByUserId(long userId)
    {
        return cartRepository.getCartByUserId(userId);
    }

    public Cart addProductToCart(long userId, long productId, short quantity) throws IllegalArgumentException
    {
        if (userId < 0L || productId < 0L || quantity <= 0)
        {
            throw new IllegalArgumentException("Illegal argument: userId and productId must be non-negative, quantity must be positive.");
        }

        Cart cart = cartRepository.getCartByUserId(userId);

        // If there is no cart for this user, create one and add a new cart entry to it
        if (cart == null)
        {
            cart = new Cart();
            cart.setUserId(userId);
            cart.addCartEntry(new CartEntry(cart, productId, quantity));

            return cartRepository.save(cart);
        }

        // If there is a cart, check if there is already a cart entry with this productId in it
        CartEntry existingEntry = null;

        if (cart.getCartEntries() != null)
        {
            existingEntry = cart.getCartEntries()
                    .stream()
                    .filter(entry -> entry.getProductId() == productId).findFirst()
                    .orElse(null);
        }

        // If there is no existing cart entry in the cart, create a new one
        if (existingEntry == null)
        {
            existingEntry = new CartEntry(cart, productId, quantity);
            cart.addCartEntry(existingEntry);
        }
        // If there is a cart entry, sum the quantities
        else
        {
            short newQuantity = (short) (existingEntry.getQuantity() + quantity);

            existingEntry.setQuantity(newQuantity);
        }

        // Save the updated cart
        return cartRepository.save(cart);
    }
}
