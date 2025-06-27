package com.example.thecoffeehouse.constant;

public enum OrderStatus {
    PENDING("Chờ xác nhận", 1),
    CONFIRMED("Đã xác nhận", 2),
    SHIPPING("Đang giao", 3),
    DELIVERED("Đã giao", 4),
    CANCELED("Đã hủy", 5);

    private final String displayName;

    private int value;

    OrderStatus(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getValue(){
        return value;
    }

    public static String getDisplayNameFromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status.getDisplayName();
            }
        }
        return null;
    }
}