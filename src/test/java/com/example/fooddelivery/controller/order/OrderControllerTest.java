package com.example.fooddelivery.controller.order;

import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.order.OrderStatusUpdateDto;
import com.example.fooddelivery.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void testCreateOrder_ShouldReturnOrderDto() throws Exception {
        OrderCreateDto createDto = new OrderCreateDto();
        Long clientId = 1L;
        OrderDto mockOrderDto = new OrderDto();

        when(orderService.createOrder(any(OrderCreateDto.class), eq(clientId)))
                .thenReturn(mockOrderDto);

        mockMvc.perform(post("/api/orders/create/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).createOrder(any(OrderCreateDto.class), eq(clientId));
    }
    @Test
    void testAssignOrder_ShouldReturnOrderDto() throws Exception {
        Long orderId = 1L;
        Long supplierId = 2L;
        OrderDto mockOrderDto = new OrderDto();

        when(orderService.assignOrderToSupplier(orderId, supplierId))
                .thenReturn(mockOrderDto);

        mockMvc.perform(put("/api/orders/{orderId}/assign/{supplierId}", orderId, supplierId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).assignOrderToSupplier(orderId, supplierId);
    }
    @Test
    void testGetRevenue_ShouldReturnBigDecimal() throws Exception {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        Long adminId = 1L;
        BigDecimal revenue = new BigDecimal("123.45");

        when(orderService.getTotalRevenueBetween(from, to, adminId)).thenReturn(revenue);

        mockMvc.perform(get("/api/orders/revenue")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .param("adminId", adminId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("123.45"));

        verify(orderService).getTotalRevenueBetween(from, to, adminId);
    }
    @Test
    void testAcceptOrder_ShouldReturnSuccessMessage() throws Exception {
        Long orderId = 1L;
        Long employeeId = 2L;

        doNothing().when(orderService).acceptOrder(orderId, employeeId);

        mockMvc.perform(put("/api/orders/{orderId}/accept", orderId)
                        .param("employeeId", employeeId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order accepted successfully"));

        verify(orderService).acceptOrder(orderId, employeeId);
    }
    @Test
    void testUpdateStatus_ShouldReturnOrderStatusUpdateDto() throws Exception {
        Long orderId = 1L;
        Long employeeId = 2L;
        OrderStatusUpdateDto mockDto = new OrderStatusUpdateDto();

        when(orderService.updateOrderStatus(orderId, employeeId)).thenReturn(mockDto);

        mockMvc.perform(put("/api/orders/{orderId}/status", orderId)
                        .param("employeeId", employeeId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).updateOrderStatus(orderId, employeeId);
    }
    @Test
    void testTakeOrder_ShouldReturnSuccessMessage() throws Exception {
        Long orderId = 1L;
        Long supplierId = 3L;

        doNothing().when(orderService).takeOrder(orderId, supplierId);

        mockMvc.perform(put("/api/orders/{orderId}/take", orderId)
                        .param("supplierId", supplierId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order taken successfully and is now in delivery"));

        verify(orderService).takeOrder(orderId, supplierId);
    }
    @Test
    void testFinishOrder_ShouldReturnOrderDto() throws Exception {
        Long orderId = 1L;
        Long supplierId = 3L;
        OrderDto mockDto = new OrderDto();

        when(orderService.finishOrder(orderId, supplierId)).thenReturn(mockDto);

        mockMvc.perform(put("/api/orders/{orderId}/finish/{supplierId}", orderId, supplierId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).finishOrder(orderId, supplierId);
    }
    @Test
    void testCancelOrder_ShouldReturnOrderDto() throws Exception {
        Long orderId = 1L;
        Long clientId = 2L;
        OrderDto mockDto = new OrderDto();

        when(orderService.cancelOrderByClient(orderId, clientId)).thenReturn(mockDto);

        mockMvc.perform(put("/api/orders/{orderId}/cancel/{clientId}", orderId, clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).cancelOrderByClient(orderId, clientId);
    }
    @Test
    void testGetOrdersByClient_ShouldReturnList() throws Exception {
        Long clientId = 1L;

        when(orderService.getOrdersByClient(clientId)).thenReturn(List.of());

        mockMvc.perform(get("/api/orders/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).getOrdersByClient(clientId);
    }

    @Test
    void testGetOrderInfoById_ShouldReturnOrderResponseDto() throws Exception {
        Long orderId = 1L;
        Long clientId = 2L;
        OrderResponseDto mockDto = new OrderResponseDto();

        when(orderService.getOrderInfoById(orderId, clientId)).thenReturn(mockDto);

        mockMvc.perform(get("/api/orders/get-order-info/{orderId}", orderId)
                        .param("clientId", clientId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).getOrderInfoById(orderId, clientId);
    }

    @Test
    void testGetOrdersBySupplier_ShouldReturnList() throws Exception {
        Long supplierId = 3L;

        when(orderService.getOrdersBySupplier(supplierId)).thenReturn(List.of());

        mockMvc.perform(get("/api/orders/supplier/{supplierId}", supplierId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).getOrdersBySupplier(supplierId);
    }
}
