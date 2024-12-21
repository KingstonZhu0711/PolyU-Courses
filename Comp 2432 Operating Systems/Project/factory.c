/**
 * @file factory.c
 * @brief This file contains the definition of the factory struct and its functions.
 * @note The factory struct is used in the day struct.
 * @see day.h
 * @author Wang Ruijie
 * @date 10 April 2024
*/

#include "order.h"
#include "factory.h"
#include <stdio.h>
#include <stdbool.h>

/**
 * Initialize a new factory
 * Sets the factory name to 'x'.
 * Sets the capacity per day to 300.
 * Sets the availability of the factory to true, indicating it is available for production.
 * Initializes the order_produced variable to 0.
 * Sets the order_produced pointer to NULL.
*/
void init_factory_x(factory *this) {
    this->factory_name = 'x';
    this->capacity_per_day = 300;
    this->is_available = true;
    this->order_produced = 0;
    this->order_produced = NULL;

}

/**
 * Initialize a new factory
 * Sets the factory name to 'y'.
 * Sets the capacity per day to 400.
 * Sets the availability of the factory to true, indicating it is available for production.
 * Initializes the order_produced variable to 0.
 * Sets the order_produced pointer to NULL.
*/
void init_factory_y(factory *this) {
    this->factory_name = 'y';
    this->capacity_per_day = 400;
    this->is_available = true;
    this->quantity_produced = 0;
    this->order_produced = NULL;
}

/**
 * Initialize a new factory
 * Sets the factory name to 'z'.
 * Sets the capacity per day to 500.
 * Sets the availability of the factory to true, indicating it is available for production.
 * Initializes the order_produced variable to 0.
 * Sets the order_produced pointer to NULL.
*/
void init_factory_z(factory *this) {
    this->factory_name = 'z';
    this->capacity_per_day = 500;
    this->is_available = true;
    this->quantity_produced = 0;
    this->order_produced = NULL;
}
/**
 * Links an order object to a factory object, indicating that the factory is now responsible for producing that specific order
*/
void allocate_order(factory *this, order *order) {
    this->order_produced = order;
}

/**
 * Provides a way to change the availability status of a factory object, Ex: when an order complete change factory status to true
*/
void modify_factory_status(factory *this, bool status) {
    this->is_available = status;
}

/**
 * Provides a way to update the quantity of items produced by a factory object
*/
void modify_quantity_produced(factory *this, int quantity_produced) {
    this->quantity_produced = quantity_produced;
}

/**
 * Provides a way to return the factory name for usage
*/
char get_factory_name(factory *this) {
    return this->factory_name;
}

/**
 * Provides a way to retrieve the capacity per day of a factory object
*/
int get_capacity_per_day(factory *this) {
    return this->capacity_per_day;
}

/**
 * Provides a way to check the status or availability of a factory
*/
bool get_factory_status(factory *this) {
    return this->is_available;
}

/**
 * Provides a way to retrieve the pointer to the order object produced by a factory object
*/
order* get_order_produced(factory *this) {
    return this->order_produced;
}

/**
 * Provides a way to retrieve the quantity of items produced by a factory object
*/
int get_quantity_produced(factory *this) {
    return this->quantity_produced;
}