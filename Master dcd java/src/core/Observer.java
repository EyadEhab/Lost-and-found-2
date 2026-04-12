package core;

import entity.Item;

/**
 * Observer Pattern — Observer Interface
 */
public interface Observer {
    void update(Item postedItem);
}
