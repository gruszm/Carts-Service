package pl.gruszm.carts_service.DTOs;

public class UserHeader
{
    private long id;
    private String email;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
