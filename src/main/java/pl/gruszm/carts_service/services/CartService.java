package pl.gruszm.carts_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.repositories.CartRepository;

@Service
public class CartService
{
    @Autowired
    private CartRepository cartRepository;

    public Cart getCartByUserId(Long userId)
    {
        return cartRepository.getByUserId(userId);
    }
}
