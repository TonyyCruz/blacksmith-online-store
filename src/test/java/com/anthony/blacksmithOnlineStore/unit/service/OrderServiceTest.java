package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.exceptions.ItemNotFoundException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import com.anthony.blacksmithOnlineStore.service.UserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private UserService userService;
  @Mock
  private OrderItemFactory orderItemFactory;
  @Mock
  private SaleService saleService;
  @Mock
  private ItemService itemService;
  @InjectMocks
  OrderService orderService;
  private final Item targetItem = MockItem.item();
  private final User user = MockUser.user();

  @Nested
  @DisplayName("Happy Path")
  class SaleServiceHappyPath {

    @Test
    @DisplayName("Should create an order with valid data")
    void create_shouldCreateAnOrderSuccessfully() {
      when(userService.getUserReference()).thenReturn(user);



    }

  }

//  @Nested
//  @DisplayName("Exception Path")
//  class SaleServiceExceptionPath {
//
//    @Test
//    @DisplayName("PerformSale should throw an ItemNotFoundException when item not exists")
//    void performSale_shouldShouldThrowAnException_whenItemNotExists() {
//      doThrow(new ItemNotFoundException(targetItem.getId()))
//          .when(itemService).itemExistesVerifier(any());
//
//      assertThrows(ItemNotFoundException.class,
//          () -> saleService.performSale(targetItem.getId(), 2));
//      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
//    }
//
//    @Test
//    @DisplayName("PerformSale should throw an DataModifyException when item have no stock")
//    void performSale_shouldShouldThrowAnException_whenItemHaveNoStock() {
//      doNothing().when(itemService).itemExistesVerifier(any());
//      when(itemRepository.decrementStockAndIncrementSoldQuantity(targetItem.getId(), 2))
//          .thenReturn(0);
//
//      assertThrows(DataModifyException.class,
//          () -> saleService.performSale(targetItem.getId(), 2));
//      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
//    }
//
//    @Test
//    @DisplayName("CancelSale should throw an ItemNotFoundException when item not exists")
//    void cancelSale_shouldShouldThrowAnException_whenItemNotExists() {
//      doThrow(new ItemNotFoundException(targetItem.getId()))
//          .when(itemService).itemExistesVerifier(any());
//
//      assertThrows(ItemNotFoundException.class,
//          () -> saleService.cancelSale(targetItem.getId(), 2));
//      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
//    }
//
//    @Test
//    @DisplayName("CancelSale should throw an DataModifyException when have no sufficient sold quantity")
//    void cancelSale_shouldShouldThrowAnException_whenItemHaveNoSufficientSold() {
//      doNothing().when(itemService).itemExistesVerifier(any());
//      when(itemRepository.incrementStockAndDecrementSoldQuantity(targetItem.getId(), 2))
//          .thenReturn(0);
//
//      assertThrows(DataModifyException.class,
//          () -> saleService.cancelSale(targetItem.getId(), 2));
//      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
//    }
//
//  }

}
