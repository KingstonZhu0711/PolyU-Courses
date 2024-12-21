/**
 * @file order.c
 * @brief This file contains the implementation of the order struct and its functions.
 * @note Remember to compile this file with PLS_G07.c
 * @author Wang Ruijie
 * @date 9 April 2024
*/

#include "order.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
/**
 * Initialize an order
 * Allocates memory for the order_number string and assigns a duplicate of the provided order_number string to it using strdup
 * Allocates memory for the due_date string and assigns a duplicate of the provided due_date string to it using strdup
 * Allocates memory for the product_name string and assigns a duplicate of the provided product_name string to it using strdup
 * Assigns the provided quantity value to the quantity member variable of the order object
 * Sets the is_completed member variable to false, indicating that the order is not yet completed
 * Sets the is_accepted member variable to true, indicating that the order is accepted
 * Sets the next member variable to NULL, indicating that there is no next order linked to this order
*/
void create_order(order *this, char *order_number, char *due_date, char *product_name, int quantity) {
    this->order_number = strdup(order_number);
    this->due_date = strdup(due_date);
    this->product_name = strdup(product_name);
    this->quantity = quantity;
    this->is_completed = false;
    this->is_accepted = true;
    this->next = NULL;
}

/**
 * Assigns the due_date pointer to the due_date member variable of an order object
*/
void modify_due_date(order *this, char *due_date) {
    this->due_date = due_date;
}

/**
 * Provides a way to modify the completion status of an order object by updating its is_completed member variable
*/
void modify_order_completion(order *this, bool is_completed) {
    this->is_completed = is_completed;
}

/**
 * Provides a way to modify the acceptance status of an order object by updating its is_accepted member variable
*/
void modify_order_acceptance(order *this, bool is_accepted) {
    this->is_accepted = is_accepted;
}

/**
 * Provides a way to modify the quantity of an order object by updating its quantity member variable
*/
void modify_quantity(order *this, int new_quantity) {
    this->quantity = new_quantity;
}

/**
 * Provides a way to retrieve the order number from an order object by returning its order_number member variable
*/
char* get_order_number(order *this) {
    return this->order_number;
}

/**
 * Provides a way to retrieve the due date from an order object by returning its due_date member variable
*/
char* get_due_date(order *this) {
    return this->due_date;
}

/**
 * Provides a way to retrieve the product name from an order object by returning its product_name member variable
*/
char* get_product_name(order *this) {
    return this->product_name;
}

/**
 * Provides a way to retrieve the quantity from an order object by returning its quantity member variable as an integer value
*/
int get_quantity(order *this) {
    return this->quantity;
}

/**
 * Provides a way to retrieve the completion status of an order object by returning its is_completed member variable as a boolean value
*/
bool get_order_completion(order *this) {
    return this->is_completed;
}

/**
 * Provides a way to retrieve the acceptance status of an order object by returning its is_accepted member variable as a boolean value
*/
bool get_order_acceptance(order *this) {
    return this->is_accepted;
}

/**
 * This function copies the original linked list of orders by creating a new list and 
 * copying the order details from each node of the original list to the corresponding node in the new list
*/
void copy_order_list(order **head, order **new_list_head){
    /*
     * Check if the head of the original list is null
     * If yes, initialize the new list head as null and return
     */
    if (*head == NULL){
        *new_list_head = NULL;
        return;
    }

    /*
     * Initialize the new list head with the head of the original list
     */
    *new_list_head = malloc(sizeof(order));
    create_order(*new_list_head, get_order_number(*head), get_due_date(*head), get_product_name(*head), get_quantity(*head));

    /*
     * Start from the second node in the original list
     * Meanwhile, maintain the last node of the new list to connect new nodes
     */
    order *current = (*head)->next;
    order *new_list_current = *new_list_head;

    while (current != NULL){
        /*
         * Create a new order with the details of the current order
         */
        order *new_order = malloc(sizeof(order));
        create_order(new_order, get_order_number(current), get_due_date(current), get_product_name(current), get_quantity(current));

        /*
         * Connect the new created order to the new list and move the pointers to the next
         */
        new_list_current->next = new_order;
        new_list_current = new_list_current->next;
        current = current->next;
    }

    /*
     * Terminate the new list
     */
    new_list_current->next = NULL;
}


