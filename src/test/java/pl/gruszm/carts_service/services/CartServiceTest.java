package pl.gruszm.carts_service.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.repositories.CartRepository;

import static org.assertj.core.api.Assertions.assertThat;
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
        Long userId = 1L;
        Cart mockCart = new Cart();
        mockCart.setUserId(userId);
        when(cartRepository.getCartByUserId(userId)).thenReturn(mockCart);

        // When
        Cart result = cartService.getCartByUserId(userId);

        // Then
        assertThat(result).isEqualTo(mockCart);
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }
}