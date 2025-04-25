package com.example.fooddelivery.util;

/**
 * Съобщения при грешки
 */

public class SystemErrors {

    public static final class Auth {
        public static final String INVALID_USERNAME = "Invalid username.";
        public static final String INVALID_PASSWORD = "Invalid password.";
        public static final String USERNAME_ALREADY_TAKEN = "Username is already taken.";
        public static final String EMAIL_ALREADY_TAKEN = "Email is already taken.";
        public static final String PHONE_NUMBER_ALREADY_TAKEN = "Phone number is already taken.";
    }

    public static final class Role {
        public static final String NO_CLIENT_ROLE = "There is no CLIENT role!";
    }

    public static final class Review {
        public static final String INVALID_RATING = "Invalid rating.";
        public static final String REVIEW_ONLY_FOR_SUPPLIER = "Reviews are only possible for suppliers.";
        public static final String REVIEW_UNABLE_TO_YOURSELF = "No permission to review yourself.";
    }

    public static final class Discount {
        public static final String NOT_CLIENT_ROLE_FOR_DISCOUNT = "Only clients are eligible for client discounts.";
    }

    public static final class Order {
        public static final String ORDER_NOT_FOUND = "Order not found.";
        public static final String ONLY_SUPPLIER_TAKE_ORDERS = "Only SUPPLIERS can take orders.";
        public static final String ONLY_SUPPLIER_ASSIGN_ORDERS = "Only SUPPLIERS can assign orders.";
        public static final String ONLY_SUPPLIER_SEE_AVAILABLE_ORDERS = "Only SUPPLIERS can see available orders.";
        public static final String ORDER_NOT_FOR_SUPPLIER = "This order is not for the current supplier.";
        public static final String ORDER_SUPPLIER_FINISH_ORDERS = "Only SUPPLIERS can finish orders.";
        public static final String ONLY_EMPLOYEE_ACCEPT_ORDERS = "Only EMPLOYEES can accept orders.";
        public static final String ONLY_EMPLOYEE_UPDATE_ORDERS = "Only EMPLOYEES can update order status.";
        public static final String ONLY_EMPLOYEE_SEE_ORDER_BY_STATUS = "Only EMPLOYEES can see all orders with given status.";
        public static final String ALREADY_TAKEN_ORDER = "Order already has a supplier.";
        public static final String WRONG_ORDER_STATUS_TO_TAKE_ORDER = "Order cannot be taken because of wrong status.";
        public static final String WRONG_ORDER_STATUS_TO_UPDATE_ORDER = "You cannot update status currently.";
        public static final String WRONG_ORDER_STATUS_TO_FINISH_ORDER = "Order can't be finished unless it's in IN_DELIVERY status. " +
                                                                        "Current status: %s.";
        public static final String WRONG_ORDER_STATUS_TO_CANCEL_ORDER = "Cannot cancel order with status: %s. " +
                                                                        "Only PENDING orders can be cancelled.";
        public static final String ORDER_NOT_FOR_CLIENT = "This order does not belong to the client.";
        public static final String NOT_PENDING_STATUS = "You cannot accept order which are not with PENDING status.";
        public static final String CONCURRENTLY_TAKEN_ORDER = "Order was taken by another supplier.";
        public static final String CONCURRENTLY_MODIFIED_ORDER = "Order was modified by another employee. Please try again.";
        public static final String ONLY_ADMIN_CHECK_TOTAL_REVENUE = "Only ADMINS can see total revenue between dates";
        public static final String UNDERAGE_USER_CANNOT_ORDER_ALCOHOL = "You are underage and cannot order alcohol.";
    }

    public static final class Restaurant {
        public static final String RESTAURANT_NOT_FOUND = "Restaurant not found.";
    }

    public static final class Product {
        public static final String PRODUCT_NOT_FOUND = "Product not found.";
        public static final String PRODUCT_NOT_IN_RESTAURANT = "Product: %s is not in Restaurant: %s.";
        public static final String PRODUCT_QUANTITY_LESS_THAN_0 = "Quantity for product with ID: %d must be greater than 0.";
    }

    public static final class User {
        public static final String USER_NOT_FOUND = "User not found.";
        public static final String SUPPLIER_NOT_FOUND = "Supplier not found.";
        public static final String USER_ADDRESS_NOT_FOUND = "User: %s %s doesn't have this address";
    }

    private SystemErrors() {}
}