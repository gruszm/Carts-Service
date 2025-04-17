package pl.gruszm.carts_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.entities.CartEntry;
import pl.gruszm.carts_service.exceptions.EntryNotFoundException;
import pl.gruszm.carts_service.exceptions.CartNotFoundException;
import pl.gruszm.carts_service.exceptions.EntryNotLinkedToCartException;
import pl.gruszm.carts_service.repositories.CartEntryRepository;
import pl.gruszm.carts_service.repositories.CartRepository;

@Service
public class CartService
{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartEntryRepository cartEntryRepository;

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

    public Cart setProductQuantityInCart(long userId, long cartEntryId, short quantity)
            throws IllegalArgumentException, CartNotFoundException, EntryNotFoundException, EntryNotLinkedToCartException
    {
        if (userId < 0L || cartEntryId < 0L || quantity < 0)
        {
            throw new IllegalArgumentException("Illegal argument: userId, cartEntryId and quantity must be non-negative.");
        }

        Cart cart = cartRepository.getCartByUserId(userId);

        // The cart must exist in order to change the quantity
        if (cart == null)
        {
            throw new CartNotFoundException("Cart does not exist for userId: " + userId);
        }

        CartEntry cartEntry = cartEntryRepository.getCartEntryById(cartEntryId);

        // The cart entry with given ID must exist
        if (cartEntry == null)
        {
            throw new EntryNotFoundException("Cart entry with id: " + cartEntryId + " does not exist");
        }

        // The cart entry must be associated with the user's cart
        boolean entryLinkedToCart = cart.getCartEntries().stream().anyMatch(entry -> entry.getId() == cartEntryId);

        if (!entryLinkedToCart)
        {
            throw new EntryNotLinkedToCartException("Entry with id: " + cartEntryId + " is not linked to the cart, which belongs to user with id: " + userId);
        }

        // If the quantity is supposed to be 0, then delete the entry
        if (quantity == 0)
        {
            cart.getCartEntries().remove(cartEntry);
        }
        else
        {
            cartEntry.setQuantity(quantity);
        }

        return cartRepository.save(cart);
    }

    public Cart getCartForUser(long userId) throws IllegalArgumentException, CartNotFoundException
    {
        if (userId < 0L)
        {
            throw new IllegalArgumentException("Illegal argument: userId must be non-negative.");
        }

        Cart cart = cartRepository.getCartByUserId(userId);

        if (cart == null)
        {
            throw new CartNotFoundException("Cart does not exist for userId: " + userId);
        }

        return cart;
    }
}
