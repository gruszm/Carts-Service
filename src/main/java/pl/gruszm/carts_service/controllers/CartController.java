package pl.gruszm.carts_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.services.CartService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController
{
    @Autowired
    private CartService cartService;

    @PostMapping("/secure/carts/{userId}/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable("userId") long userId, @PathVariable("productId") long productId, @PathVariable("quantity") short quantity)
    {
        Map<String, String> errorResponse = new HashMap<>();

        try
        {
            cartService.addProductToCart(userId, productId, quantity);

            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e)
        {
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
        catch (Exception e)
        {
            errorResponse.put("message", "Internal server error");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
