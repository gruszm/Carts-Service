package pl.gruszm.carts_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gruszm.carts_service.DTOs.CartResponse;
import pl.gruszm.carts_service.DTOs.UserHeader;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.exceptions.CartNotFoundException;
import pl.gruszm.carts_service.services.CartService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController
{
    @Autowired
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @DeleteMapping("/secure/carts")
    public ResponseEntity<?> clearCart(@RequestHeader("X-User") String userHeaderJson)
    {
        Map<String, String> errorResponse = new HashMap<>();

        try
        {
            UserHeader userHeader = objectMapper.readValue(userHeaderJson, UserHeader.class);
            Cart updatedCart = cartService.clearCart(userHeader.getId());

            if (updatedCart == null)
            {
                return ResponseEntity.internalServerError().build();
            }

            return ResponseEntity.ok().build();
        }
        catch (JsonProcessingException e)
        {
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
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

    @PostMapping("/secure/carts/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable("productId") long productId, @PathVariable("quantity") short quantity, @RequestHeader("X-User") String userHeaderJson)
    {
        Map<String, String> errorResponse = new HashMap<>();
        UserHeader userHeader;

        try
        {
            userHeader = objectMapper.readValue(userHeaderJson, UserHeader.class);
        }
        catch (JsonProcessingException e)
        {
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }

        try
        {
            cartService.addProductToCart(userHeader.getId(), productId, quantity);

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

    @GetMapping("/secure/carts/user")
    public ResponseEntity<?> getCart(@RequestHeader("X-User") String userHeaderJson)
    {
        Map<String, String> errorResponse = new HashMap<>();

        try
        {
            UserHeader userHeader = objectMapper.readValue(userHeaderJson, UserHeader.class);
            Cart cart = cartService.getCartForUser(userHeader.getId());

            return ResponseEntity.ok(new CartResponse(cart));
        }
        catch (JsonProcessingException e)
        {
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
        catch (CartNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }
}
