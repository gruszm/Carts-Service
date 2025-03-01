package pl.gruszm.carts_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gruszm.carts_service.entities.CartEntry;

public interface CartEntryRepository extends JpaRepository<CartEntry, Long>
{
    CartEntry getCartEntryById(long cartEntryId);
}
