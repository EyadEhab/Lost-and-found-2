package entity;

/**
 * Flyweight class for shared item metadata (category and status).
 * This class represents the intrinsic state that can be shared among many items.
 */
public class ItemTypeFlyweight {
    private final String category;
    private final String status;

    public ItemTypeFlyweight(String category, String status) {
        this.category = category;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemTypeFlyweight that = (ItemTypeFlyweight) o;
        return category.equals(that.category) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
