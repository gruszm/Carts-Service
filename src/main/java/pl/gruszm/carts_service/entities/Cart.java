package pl.gruszm.carts_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "userId")
    @NotNull
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartEntry> cartEntries;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public @NotNull Long getUserId()
    {
        return userId;
    }

    public void setUserId(@NotNull Long userId)
    {
        this.userId = userId;
    }

    public List<CartEntry> getCartEntries()
    {
        return cartEntries;
    }

    public void setCartEntries(List<CartEntry> cartEntries)
    {
        this.cartEntries = cartEntries;
    }

    public void addCartEntry(CartEntry cartEntry)
    {
        if (this.cartEntries == null)
        {
            this.cartEntries = new ArrayList<>();
        }

        this.cartEntries.add(cartEntry);
    }
}
