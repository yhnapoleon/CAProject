package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.service.LocationService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    
    @Autowired
    private LocationService locationService;

    @GetMapping("/getLocation")
    public DataResult getLocations(@RequestParam String username) {
        try {
            // 1. 使用 Service 层通过用户名获取地址列表
            // 这里调用 LocationServiceImpl 中新增的 getLocationsByUsername 方法
            List<Location> locations = locationService.getLocationsByUsername(username);

            // 2. 转换为前端所需格式 (包含 defaultAddress 字段，并将其转换为布尔值)
            List<Map<String, Object>> data = locations.stream()
                    .map(loc -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("locationId", loc.getLocationId());
                        m.put("locationText", loc.getLocationText());
                        m.put("postal", loc.getPostal());
                        // 关键修改：将 String 类型的 "1" 转换为 true，其他转换为 false
                        m.put("defaultAddress", "1".equals(loc.getDefaultAddress()));
                        return m;
                    })
                    .collect(Collectors.toList());

            return new DataResult(Code.SUCCESS, data, "获取地址列表成功");
        } catch (RuntimeException e) {
            // 捕获 Service 层抛出的如“用户不存在”等业务异常
            return new DataResult(Code.FAILED, null, "获取地址列表失败: " + e.getMessage());
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "获取地址列表失败: 内部错误");
        }
    }

    @PostMapping("/addLocation")
    public DataResult addLocation(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String locationText = (String) request.get("locationText");
            Integer postal = request.get("postal") == null ? null : (Integer) request.get("postal");
            
            if (userId == null || locationText == null || locationText.trim().isEmpty() || postal == null) {
                return new DataResult(Code.FAILED, null, "用户ID、地址内容和邮编不能为空");
            }
            
            return locationService.addLocation(userId, locationText, postal);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "添加地址失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteLocation/{locationId}")
    public DataResult deleteLocation(@PathVariable Integer locationId) {
        try {
            if (locationId == null) {
                return new DataResult(Code.FAILED, null, "地址ID不能为空");
            }
            
            return locationService.deleteLocation(locationId);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "删除地址失败: " + e.getMessage());
        }
    }

    @PutMapping("/set-default/{locationId}")
    public DataResult setDefaultAddress(@PathVariable Integer locationId,
                                        @RequestParam String username) {
        try {
            if (locationId == null || username == null || username.trim().isEmpty()) {
                return new DataResult(Code.FAILED, null, "地址ID和用户名不能为空");
            }

            // 假设 LocationService 中有一个方法来处理设置默认地址的逻辑
            // 这个逻辑通常包括：
            // 1. 验证该地址ID是否属于该用户名/用户ID
            // 2. 将该用户的其他地址的“默认”标记取消
            // 3. 将指定地址的“默认”标记设为真

            // 注意：您需要在 LocationService 中实现 setLocationAsDefault 方法
            return locationService.setLocationAsDefault(locationId, username);

        } catch (RuntimeException e) {
            // 处理业务逻辑错误（例如：地址不存在，用户不匹配）
            return new DataResult(Code.FAILED, null, "设置默认地址失败: " + e.getMessage());
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "设置默认地址失败: 内部错误");
        }
    }
}
