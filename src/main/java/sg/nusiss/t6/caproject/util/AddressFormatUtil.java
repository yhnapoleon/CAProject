//By Xu Wenzhe

package sg.nusiss.t6.caproject.util;

import lombok.Getter;

/**
 * Address formatting utility.
 * Used to standardize address format between frontend and backend.
 */
public class AddressFormatUtil {

    //Standard address format: "street, building, postal, city"
    public static final String ADDRESS_FORMAT = "%s, %s, %s, %s";

    public static String formatAddressText(String street, String building, String postal, String city) {
        if (street == null)
            street = "";
        if (building == null)
            building = "";
        if (postal == null)
            postal = "";
        if (city == null)
            city = "";

        return String.format(ADDRESS_FORMAT, street.trim(), building.trim(), postal.trim(), city.trim());
    }

    //Parse address text (split standardized text into components)
    public static AddressComponents parseAddressText(String locationText) {
        if (locationText == null || locationText.trim().isEmpty()) {
            return new AddressComponents("", "", "", "");
        }

        String[] parts = locationText.split(",");

        // Ensure at least 4 parts; fill missing ones with empty strings
        String[] normalizedParts = new String[4];
        for (int i = 0; i < 4; i++) {
            normalizedParts[i] = (i < parts.length) ? parts[i].trim() : "";
        }

        return new AddressComponents(
                normalizedParts[0],
                normalizedParts[1],
                normalizedParts[2],
                normalizedParts[3]);
    }

    //Validate postal code format
    public static boolean isValidPostal(String postal) {
        if (postal == null || postal.trim().isEmpty()) {
            return false;
        }
        return postal.trim().matches("\\d{6}");
    }

    //Validate postal code format (Integer version)
    public static boolean isValidPostal(Integer postal) {
        if (postal == null) {
            return false;
        }
        return postal.toString().matches("\\d{6}");
    }

    //Validate address text length
    public static boolean isValidAddressLength(String locationText) {
        if (locationText == null) {
            return false;
        }
        return locationText.length() <= 255;
    }

    //Address components wrapper class
    @Getter
    public static class AddressComponents {
        private final String street;
        private final String building;
        private final String postal;
        private final String city;

        public AddressComponents(String street, String building, String postal, String city) {
            this.street = street;
            this.building = building;
            this.postal = postal;
            this.city = city;
        }

        @Override
        public String toString() {
            return String.format("AddressComponents{street='%s', building='%s', postal='%s', city='%s'}",
                    street, building, postal, city);
        }
    }
}
