package pl.gruszm.carts_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController
{
    @PostMapping("/secure/carts/{userId}/{productId}/{quantity}")
    public ResponseEntity<Void> addProductToCart(@PathVariable("userId") long userId, @PathVariable("productId") long productId, @PathVariable("quantity") short quantity)
    {

    }
}
