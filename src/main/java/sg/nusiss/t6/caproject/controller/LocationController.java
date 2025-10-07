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

    @GetMapping("/getLocation/{userId}")
    public DataResult getLocations(@PathVariable Integer userId) {
        try {
            List<Location> locations = locationService.getLocationsByUserId(userId);
            List<Map<String, Object>> data = locations.stream()
                    .map(loc -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("locationId", loc.getLocationId());
                        m.put("locationText", loc.getLocationText());
                        return m;
                    })
                    .collect(Collectors.toList());
            return new DataResult(Code.SUCCESS, data, "获取地址列表成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "获取地址列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/addLocation")
    public DataResult addLocation(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String locationText = (String) request.get("locationText");
            
            if (userId == null || locationText == null || locationText.trim().isEmpty()) {
                return new DataResult(Code.FAILED, null, "用户ID和地址内容不能为空");
            }
            
            return locationService.addLocation(userId, locationText);
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
}
