package pl.gruszm.carts_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gruszm.carts_service.DTOs.UserDTO;
import pl.gruszm.carts_service.services.CartService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController
{
    @Autowired
    private CartService cartService;

    @PostMapping("/secure/carts/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable("productId") long productId, @PathVariable("quantity") short quantity, @RequestHeader("user") UserDTO user)
    {
        Map<String, String> errorResponse = new HashMap<>();

        try
        {
            cartService.addProductToCart(user.getId(), productId, quantity);

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
