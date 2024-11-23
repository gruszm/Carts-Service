package pl.gruszm.carts_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
}
