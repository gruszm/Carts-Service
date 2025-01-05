package pl.gruszm.carts_service.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.entities.CartEntry;
import pl.gruszm.carts_service.repositories.CartRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest
{
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldReturnCartForGivenUserId()
    {
        // Given
        long userId = 1L;
        Cart mockCart = new Cart();
        mockCart.setUserId(userId);
        when(cartRepository.getCartByUserId(userId)).thenReturn(mockCart);

        // When
        Cart result = cartService.getCartByUserId(userId);

        // Then
        assertThat(result).isEqualTo(mockCart);
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }

    @Test
    void shouldCreateNewCartAndCartEntry()
    {
        // Given
        long userId = 0L;
        long productId = 0L;
        short quantity = 5;

        when(cartRepository.getCartByUserId(userId)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        Cart result = cartService.addProductToCart(userId, productId, quantity);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getCartEntries());
        assertEquals(1, result.getCartEntries().size());

        CartEntry entry = result.getCartEntries().get(0);

        assertEquals(productId, entry.getProductId());
        assertEquals(quantity, entry.getQuantity());
        assertSame(result, entry.getCart());

        verify(cartRepository, times(1)).getCartByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldFindExistingCartAndCreateCartEntry()
    {
        // Given
        long userId = 0L;
        long productId = 0L;
        short quantity = 5;
        Cart existingCart = new Cart();

        existingCart.setUserId(userId);

        when(cartRepository.getCartByUserId(userId)).thenReturn(existingCart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        Cart result = cartService.addProductToCart(userId, productId, quantity);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getCartEntries());
        assertEquals(1, result.getCartEntries().size());

        CartEntry entry = result.getCartEntries().get(0);

        assertEquals(productId, entry.getProductId());
        assertEquals(quantity, entry.getQuantity());
        assertSame(result, entry.getCart());

        verify(cartRepository, times(1)).getCartByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}