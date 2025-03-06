package com.mecsbalint.backend.dao;

import java.util.List;

public interface TrackerAppDao<T> {

    /**
     * Add an element to the database
     *
     * @param element: the element to be added
     */
    void add(T element);

    /**
     * Return with an element from the database find by its id
     *
     * @param id: the id number the element is found in the database
     * @return: the element with the corresponding id
     * if there is no element with this id or the connection is failed then it returns null
     */
    T read(int id);

    /**
     * Returns with all elements from a table
     *
     * @return: List of the elements
     */
    List<T> readAll();

    /**
     * Update one element found by its id
     *
     * @param element: the updated version of the element
     */
    void update(T element);

    /**
     * Delete an element found by its id
     *
     * @param id: the id number with which the element is found
     */
    void deleteById(int id);
}
