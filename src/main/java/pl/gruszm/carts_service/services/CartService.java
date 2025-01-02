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

    public Cart getCartByUserId(Long userId)
    {
        return cartRepository.getCartByUserId(userId);
    }

    public Cart addProductToCart(Long userId, Long productId, short quantity)
    {
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

        for (CartEntry singleEntry : cart.getCartEntries())
        {
            if (singleEntry.getProductId() == productId)
            {
                existingEntry = singleEntry;
                break;
            }
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
