package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.LocationRepository;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.LocationService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Location> getLocationsByUserId(Integer userId) {
        return locationRepository.findByUserId(userId);
    }

    @Override
    public DataResult addLocation(Integer userId, String locationText) {
        try {
            // 1️⃣ 查找用户
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

            // 2️⃣ 创建新地址
            Location location = new Location();
            location.setUserId(userId);
            location.setLocationText(locationText);
            location.setUser(user);

            // 3️⃣ 保存地址
            Location savedLocation = locationRepository.save(location);

            // 4️⃣ 返回结果
            return new DataResult(Code.SUCCESS, savedLocation.getLocationId(), "地址添加成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "地址添加失败: " + e.getMessage());
        }
    }

    @Override
    public DataResult deleteLocation(Integer locationId) {
        try {
            // 1️⃣ 查找地址
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("地址不存在: " + locationId));

            // 2️⃣ 删除地址
            locationRepository.delete(location);

            // 3️⃣ 返回结果
            return new DataResult(Code.SUCCESS, locationId, "地址删除成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "地址删除失败: " + e.getMessage());
        }
    }
}
