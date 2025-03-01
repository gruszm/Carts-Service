package pl.gruszm.carts_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gruszm.carts_service.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>
{
    Cart getCartByUserId(long userId);
}
