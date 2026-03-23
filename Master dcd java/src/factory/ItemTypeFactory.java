package factory;

import entity.ItemTypeFlyweight;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to manage and reuse ItemTypeFlyweight instances.
 * This class implements the Flyweight pattern's factory component.
 */
public class ItemTypeFactory {
    private static final Map<String, ItemTypeFlyweight> itemTypes = new HashMap<>();

    /**
     * Get an existing ItemTypeFlyweight or create a new one if it doesn't exist.
     * 
     * @param category items category (e.g., Electronics, Keys)
     * @param status items status (e.g., Lost, Found, Claimed)
     * @return shared ItemTypeFlyweight instance
     */
    public static synchronized ItemTypeFlyweight getItemType(String category, String status) {
        String key = (category == null ? "Unknown" : category) + ":" + (status == null ? "Unknown" : status);
        
        if (!itemTypes.containsKey(key)) {
            itemTypes.put(key, new ItemTypeFlyweight(category, status));
            System.out.println("[Flyweight] Created new shared metadata instance for: " + key);
        }
        
        return itemTypes.get(key);
    }
    
    /**
     * Returns the number of unique flyweight objects currently in use.
     * @return count of unique metadata objects
     */
    public static int getFlyweightCount() {
        return itemTypes.size();
    }
}
