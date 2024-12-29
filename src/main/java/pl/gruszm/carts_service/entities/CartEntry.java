package pl.gruszm.carts_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cart_entries")
public class CartEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "productId")
    @NotNull
    private Long productId;

    @Column(name = "quantity")
    private short quantity;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartEntry(Cart cart, Long productId, short quantity)
    {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public @NotNull Long getProductId()
    {
        return productId;
    }

    public void setProductId(@NotNull Long productId)
    {
        this.productId = productId;
    }

    public short getQuantity()
    {
        return quantity;
    }

    public void setQuantity(short quantity)
    {
        this.quantity = quantity;
    }

    public Cart getCart()
    {
        return cart;
    }

    public void setCart(Cart cart)
    {
        this.cart = cart;
    }
}
