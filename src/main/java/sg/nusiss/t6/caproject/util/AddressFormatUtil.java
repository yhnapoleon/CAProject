package sg.nusiss.t6.caproject.util;

/**
 * 地址格式标准化工具类
 * 用于统一前后端地址数据格式
 */
public class AddressFormatUtil {
    
    /**
     * 标准地址格式: "街道, 建筑, 邮编, 城市"
     */
    public static final String ADDRESS_FORMAT = "%s, %s, %s, %s";
    
    /**
     * 格式化地址文本（将分离的地址字段合并为标准格式）
     * @param street 街道
     * @param building 建筑
     * @param postal 邮编
     * @param city 城市
     * @return 标准格式的地址文本
     */
    public static String formatAddressText(String street, String building, String postal, String city) {
        if (street == null) street = "";
        if (building == null) building = "";
        if (postal == null) postal = "";
        if (city == null) city = "";
        
        return String.format(ADDRESS_FORMAT, street.trim(), building.trim(), postal.trim(), city.trim());
    }
    
    /**
     * 解析地址文本（将标准格式的地址文本分离为各个字段）
     * @param locationText 标准格式的地址文本
     * @return AddressComponents 包含各个地址组件的对象
     */
    public static AddressComponents parseAddressText(String locationText) {
        if (locationText == null || locationText.trim().isEmpty()) {
            return new AddressComponents("", "", "", "");
        }
        
        String[] parts = locationText.split(",");
        
        // 确保至少有4个部分，不足的用空字符串填充
        String[] normalizedParts = new String[4];
        for (int i = 0; i < 4; i++) {
            normalizedParts[i] = (i < parts.length) ? parts[i].trim() : "";
        }
        
        return new AddressComponents(
            normalizedParts[0],
            normalizedParts[1], 
            normalizedParts[2],
            normalizedParts[3]
        );
    }
    
    /**
     * 验证邮编格式
     * @param postal 邮编字符串
     * @return 是否为有效的6位数字邮编
     */
    public static boolean isValidPostal(String postal) {
        if (postal == null || postal.trim().isEmpty()) {
            return false;
        }
        return postal.trim().matches("\\d{6}");
    }
    
    /**
     * 验证邮编格式（Integer版本）
     * @param postal 邮编数字
     * @return 是否为有效的6位数字邮编
     */
    public static boolean isValidPostal(Integer postal) {
        if (postal == null) {
            return false;
        }
        return postal.toString().matches("\\d{6}");
    }
    
    /**
     * 验证地址文本长度
     * @param locationText 地址文本
     * @return 是否在有效长度范围内
     */
    public static boolean isValidAddressLength(String locationText) {
        if (locationText == null) {
            return false;
        }
        return locationText.length() <= 255;
    }
    
    /**
     * 地址组件类
     */
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
        
        public String getStreet() { return street; }
        public String getBuilding() { return building; }
        public String getPostal() { return postal; }
        public String getCity() { return city; }
        
        @Override
        public String toString() {
            return String.format("AddressComponents{street='%s', building='%s', postal='%s', city='%s'}", 
                street, building, postal, city);
        }
    }
}



