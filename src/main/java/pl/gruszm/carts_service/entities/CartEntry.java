package pl.gruszm.carts_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_entries")
public class CartEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "productId")
    private long productId;

    @Column(name = "quantity")
    private short quantity;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartEntry()
    {

    }

    public CartEntry(Cart cart, long productId, short quantity)
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

    public long getProductId()
    {
        return productId;
    }

    public void setProductId(long productId)
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
