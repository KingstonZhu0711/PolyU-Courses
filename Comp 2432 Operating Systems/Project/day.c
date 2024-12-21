/**
 * @file day.c
 * @brief This file contains the definition of the day struct and its functions.
 * @author Wang Ruijie
 * @date 12 April 2024
*/

#include "day.h"
#include "factory.h"
#include <stdio.h>

/**
 * Initialize day 
 * Assigns the x pointer to the x member variable of the day object.
 * Assigns the y pointer to the y member variable of the day object.
 * Assigns the z pointer to the z member variable of the day object.
 * Sets the next member variable of the day object to NULL.
*/
void init_day(day *this, factory *x, factory *y, factory *z) {
    this->x = x;
    this->y = y;
    this->z = z;
    this->next = NULL;
}

/**
 * Provides a way to retrieve the pointer to the factory object associated with x in a day object
*/
factory* get_factory_x(day *this) {
    return this->x;
}

/**
 * Provides a way to retrieve the pointer to the factory object associated with y in a day object
*/
factory* get_factory_y(day *this) {
    return this->y;
}

/**
 * Provides a way to retrieve the pointer to the factory object associated with z in a day object
*/
factory* get_factory_z(day *this) {
    return this->z;
}