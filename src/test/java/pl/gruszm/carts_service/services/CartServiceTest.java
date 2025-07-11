package pl.gruszm.carts_service.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gruszm.carts_service.entities.Cart;
import pl.gruszm.carts_service.entities.CartEntry;
import pl.gruszm.carts_service.exceptions.CartNotFoundException;
import pl.gruszm.carts_service.exceptions.EntryNotFoundException;
import pl.gruszm.carts_service.exceptions.EntryNotLinkedToCartException;
import pl.gruszm.carts_service.repositories.CartEntryRepository;
import pl.gruszm.carts_service.repositories.CartRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest
{
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartEntryRepository cartEntryRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldReturnCartForGivenUserId() throws CartNotFoundException
    {
        // Given
        long userId = 1L;
        Cart mockCart = new Cart();
        mockCart.setUserId(userId);
        when(cartRepository.getCartByUserId(userId)).thenReturn(mockCart);

        // When
        Cart result = cartService.getCartForUser(userId);

        // Then
        assertThat(result).isEqualTo(mockCart);
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }

    @Test
    void shouldThrowWhenUserIdNegativeIsGiven()
    {
        // Given
        long userId = -1L;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.getCartForUser(userId));
    }

    @Test
    void shouldThrowWhenCartNotFound()
    {
        // Given
        long userId = 0L;
        when(cartRepository.getCartByUserId(userId)).thenReturn(null);

        // When
        // Then
        assertThrows(CartNotFoundException.class, () -> cartService.getCartForUser(userId));
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

    @Test
    void shouldFindExistingCartWithOtherProductAndCreateNewEntry()
    {
        // Given
        long userId = 0L;
        long existingProductId = 0L;
        short existingProductQuantity = 1;
        long newProductId = 1L;
        short newProductQuantity = 1;

        Cart existingCart = new Cart();
        CartEntry existingEntry = new CartEntry(existingCart, existingProductId, existingProductQuantity);

        existingCart.setUserId(userId);
        existingCart.addCartEntry(existingEntry);

        when(cartRepository.getCartByUserId(userId)).thenReturn(existingCart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        Cart result = cartService.addProductToCart(userId, newProductId, newProductQuantity);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getCartEntries());
        assertEquals(2, result.getCartEntries().size());

        CartEntry entry1 = result.getCartEntries().get(0);

        assertEquals(existingProductId, entry1.getProductId());
        assertEquals(existingProductQuantity, entry1.getQuantity());
        assertSame(existingCart, entry1.getCart());

        CartEntry entry2 = result.getCartEntries().get(1);

        assertEquals(newProductId, entry2.getProductId());
        assertEquals(newProductQuantity, entry2.getQuantity());
        assertSame(existingCart, entry2.getCart());

        verify(cartRepository, times(1)).getCartByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldFindExistingCartEntryAndIncreaseQuantity()
    {
        // Given
        long userId = 0L;
        long productId = 0L;
        short initialQuantity = 5;
        short extraQuantity = 10;

        Cart existingCart = new Cart();
        CartEntry existingEntry = new CartEntry(existingCart, productId, initialQuantity);

        existingCart.setUserId(userId);
        existingCart.addCartEntry(existingEntry);

        when(cartRepository.getCartByUserId(userId)).thenReturn(existingCart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        Cart result = cartService.addProductToCart(userId, productId, extraQuantity);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getCartEntries());
        assertEquals(1, result.getCartEntries().size());

        CartEntry entry = result.getCartEntries().get(0);

        assertEquals(productId, entry.getProductId());
        assertEquals((initialQuantity + extraQuantity), entry.getQuantity());
        assertSame(result, entry.getCart());

        verify(cartRepository, times(1)).getCartByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGivenNegativeUserId()
    {
        // Given
        long userId = -1L;
        long productId = 0L;
        short quantity = 5;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart(userId, productId, quantity));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGivenNegativeProductId()
    {
        // Given
        long userId = 0L;
        long productId = -1L;
        short quantity = 5;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart(userId, productId, quantity));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGivenNonPositiveQuantity()
    {
        // Given
        long userId = 0L;
        long productId = 0L;
        short quantity = 0;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart(userId, productId, quantity));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnNegativeUserId()
    {
        // Given
        long userId = -1L;
        long cartEntryId = 0L;
        short quantity = 0;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.setProductQuantityInCart(userId, cartEntryId, quantity));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnNegativeCartEntryId()
    {
        // Given
        long userId = 0L;
        long cartEntryId = -1L;
        short quantity = 0;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.setProductQuantityInCart(userId, cartEntryId, quantity));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnNegativeQuantity()
    {
        // Given
        long userId = 0L;
        long cartEntryId = 0L;
        short quantity = -1;

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> cartService.setProductQuantityInCart(userId, cartEntryId, quantity));
    }

    @Test
    void shouldThrowWhenCartDoesNotExist()
    {
        // Given
        long userId = 0L;
        long cartEntryId = 0L;
        short quantity = 1;

        when(cartRepository.getCartByUserId(userId)).thenReturn(null);

        // When
        // Then
        assertThrows(CartNotFoundException.class, () -> cartService.setProductQuantityInCart(userId, cartEntryId, quantity));
    }

    @Test
    void shouldThrowWhenCartEntryIsNull()
    {
        // Given
        long userId = 0L;
        long cartEntryId = 0L;
        short quantity = 1;
        Cart cart = new Cart();

        cart.setUserId(userId);

        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);
        when(cartEntryRepository.getCartEntryById(cartEntryId)).thenReturn(null);

        // When
        // Then
        assertThrows(EntryNotFoundException.class, () -> cartService.setProductQuantityInCart(userId, cartEntryId, quantity));
    }

    @Test
    void shouldThrowWhenEntryNotLinkedToCart()
    {
        // Given
        long userId = 0L;
        long linkedCartEntryId = 0L;
        long unlinkedCartEntryId = 1L;
        short quantity = 1;
        Cart cart = new Cart();
        CartEntry linkedCartEntry = new CartEntry();
        CartEntry unlinkedCartEntry = new CartEntry();

        cart.addCartEntry(linkedCartEntry);
        linkedCartEntry.setId(linkedCartEntryId);
        linkedCartEntry.setCart(cart);
        unlinkedCartEntry.setId(unlinkedCartEntryId);

        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);
        when(cartEntryRepository.getCartEntryById(unlinkedCartEntryId)).thenReturn(unlinkedCartEntry);

        // When
        // Then
        assertThrows(EntryNotLinkedToCartException.class, () -> cartService.setProductQuantityInCart(userId, unlinkedCartEntryId, quantity));
    }

    @Test
    void shouldSetTheQuantityToDesiredValue() throws EntryNotFoundException, EntryNotLinkedToCartException, CartNotFoundException
    {
        // Given
        long userId = 0L;
        long cartEntryId = 0L;
        short quantity = 1;
        Cart cart = new Cart();
        CartEntry cartEntry = new CartEntry();

        cart.addCartEntry(cartEntry);
        cartEntry.setId(cartEntryId);
        cartEntry.setCart(cart);

        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);
        when(cartEntryRepository.getCartEntryById(cartEntryId)).thenReturn(cartEntry);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        Cart returnedCart = cartService.setProductQuantityInCart(userId, cartEntryId, quantity);

        // Then
        assertEquals(quantity, cartEntry.getQuantity());
        assertEquals(cart, returnedCart);
    }

    @Test
    void shouldDeleteTheCartEntryFromCart() throws EntryNotFoundException, EntryNotLinkedToCartException, CartNotFoundException
    {
        // Given
        long userId = 0L;
        long cartEntryId = 0L;
        short quantity = 0;
        Cart cart = new Cart();
        CartEntry cartEntry = new CartEntry();

        cart.addCartEntry(cartEntry);
        cartEntry.setId(cartEntryId);
        cartEntry.setCart(cart);

        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);
        when(cartEntryRepository.getCartEntryById(cartEntryId)).thenReturn(cartEntry);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        Cart returnedCart = cartService.setProductQuantityInCart(userId, cartEntryId, quantity);

        // Then
        assertEquals(cart, returnedCart);
        assertEquals(0, cart.getCartEntries().size());
    }
}