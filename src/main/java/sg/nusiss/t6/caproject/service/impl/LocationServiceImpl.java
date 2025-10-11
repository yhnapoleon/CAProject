// LocationServiceImpl.java

package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- 新增导入
import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.LocationRepository;
import sg.nusiss.t6.caproject.repository.UserRepository; // 假设您有这个类来查找用户
import sg.nusiss.t6.caproject.service.LocationService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.List;
import java.util.Optional;

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

    // =========================================================
    // 【方法 1】新增地址 (已完善默认地址逻辑)
    // =========================================================
    @Override
    @Transactional
    public DataResult addLocation(Integer userId, String locationText) {
        try {
            // 1️⃣ 查找用户
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

            // 2️⃣ 检查该用户是否已有地址
            List<Location> existingLocations = locationRepository.findByUserId(userId);

            // 3️⃣ 创建新地址
            Location location = new Location();
            location.setUserId(userId);
            location.setLocationText(locationText);
            location.setUser(user);

            // 4️⃣ 设置 defaultAddress: 如果用户没有其他地址，则新地址默认为默认地址 ("1")，否则为非默认 ("0")
            if (existingLocations.isEmpty()) {
                location.setDefaultAddress("1");
            } else {
                location.setDefaultAddress("0");
            }

            // 5️⃣ 保存地址
            Location savedLocation = locationRepository.save(location);

            // 6️⃣ 返回结果
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

    // =========================================================
    // 【方法 2】设置默认地址 (已完整实现)
    // =========================================================
    @Override
    @Transactional // 保证清除和设置操作的原子性
    public DataResult setLocationAsDefault(Integer locationId, String username) {
        // 1. 查找用户ID
        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            return new DataResult(Code.FAILED, null, "用户不存在或用户名无效");
        }

        // 2. 查找目标地址
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return new DataResult(Code.FAILED, null, "目标地址ID不存在");
        }
        Location locationToSetDefault = locationOptional.get();

        // 3. 验证地址是否属于该用户
        if (!locationToSetDefault.getUserId().equals(userId)) {
            throw new RuntimeException("权限不足：该地址不属于当前用户");
        }

        // 4. 清除该用户所有其他地址的默认标记 (使用 LocationRepository 中新增的 clearDefaultByUserId 方法)
        locationRepository.clearDefaultByUserId(userId, locationId);

        // 5. 设置目标地址为默认 (使用 "1")
        locationToSetDefault.setDefaultAddress("1");
        locationRepository.save(locationToSetDefault);

        return new DataResult(Code.SUCCESS, null, "默认地址设置成功");
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUserName(username);
        return userOptional.map(User::getUserId).orElse(null);
    }

    @Override
    public List<Location> getLocationsByUsername(String username) {
        // 步骤 1: 通过用户名获取 userId
        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            // 如果用户不存在，抛出 RuntimeException，Controller 会捕获并返回 DataResult(Code.FAILED)
            throw new RuntimeException("用户不存在");
        }

        // 步骤 2: 调用根据 userId 查找地址的方法
        return getLocationsByUserId(userId);
    }
}