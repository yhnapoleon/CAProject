//By Xu Wenzhe

package sg.nusiss.t6.caproject.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // newly added import
import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.LocationRepository;
import sg.nusiss.t6.caproject.repository.UserRepository; // Assume this exists to find users
import sg.nusiss.t6.caproject.service.LocationService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.util.AddressFormatUtil;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Location> getLocationsByUserId(Integer userId) {
        return locationRepository.findByUserId(userId);
    }

    // =========================================================
    // Method 1: Add address (with default address logic)
    // =========================================================
    @Override
    @Transactional
    public DataResult addLocation(Integer userId, String locationText, Integer postal) {
        try {
            // 1) Validate input
            if (userId == null || userId <= 0) {
                return new DataResult(Code.FAILED, null, "Invalid user ID");
            }

            if (locationText == null || locationText.trim().isEmpty()) {
                return new DataResult(Code.FAILED, null, "Location text must not be empty");
            }

            if (locationText.length() > 255) {
                return new DataResult(Code.FAILED, null, "Location text length must be <= 255 characters");
            }

            if (postal == null || postal <= 0) {
                return new DataResult(Code.FAILED, null, "Invalid postal code");
            }

            // Validate postal format (6 digits)
            if (!AddressFormatUtil.isValidPostal(postal)) {
                return new DataResult(Code.FAILED, null, "Postal code must be 6 digits");
            }

            // 2) Find user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            // 3) Check whether user already has addresses
            List<Location> existingLocations = locationRepository.findByUserId(userId);

            // 4) Create new address
            Location location = new Location();
            location.setUserId(userId);
            location.setLocationText(locationText.trim());
            location.setUser(user);
            location.setPostal(postal);

            // 5) Set defaultAddress: if user has no addresses, new address is default
            // ("1"); otherwise "0"
            if (existingLocations.isEmpty()) {
                location.setDefaultAddress("1");
            } else {
                location.setDefaultAddress("0");
            }

            // 6) Save address
            Location savedLocation = locationRepository.save(location);

            // 7) Return result
            return new DataResult(Code.SUCCESS, savedLocation.getLocationId(), "Address added successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to add address: " + e.getMessage());
        }
    }

    @Override
    public DataResult deleteLocation(Integer locationId) {
        try {
            // 1) Find address
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Address not found: " + locationId));

            // 2) Delete address
            locationRepository.delete(location);

            // 3) Return result
            return new DataResult(Code.SUCCESS, locationId, "Address deleted successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to delete address: " + e.getMessage());
        }
    }

    // =========================================================
    // Method 2: Set default address (fully implemented)
    // =========================================================
    @Override
    @Transactional // Ensure atomicity for clearing and setting operations
    public DataResult setLocationAsDefault(Integer locationId, String username) {
        // 1) Find user ID
        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            return new DataResult(Code.FAILED, null, "User does not exist or username is invalid");
        }

        // 2) Find target address
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return new DataResult(Code.FAILED, null, "Target address ID does not exist");
        }
        Location locationToSetDefault = locationOptional.get();

        // 3) Verify the address belongs to the user
        if (!locationToSetDefault.getUserId().equals(userId)) {
            throw new RuntimeException("Insufficient permissions: address does not belong to the current user");
        }

        // 4) Clear default flags for other addresses of this user (using
        // clearDefaultByUserId)
        locationRepository.clearDefaultByUserId(userId, locationId);

        // 5) Set the target address as default (use "1")
        locationToSetDefault.setDefaultAddress("1");
        locationRepository.save(locationToSetDefault);

        return new DataResult(Code.SUCCESS, null, "Default address set successfully");
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUserName(username);
        return userOptional.map(User::getUserId).orElse(null);
    }

    @Override
    public List<Location> getLocationsByUsername(String username) {
        // Step 1: Get userId by username
        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            // If user does not exist, throw; controller will catch and return
            // DataResult(Code.FAILED)
            throw new RuntimeException("User does not exist");
        }

        // Step 2: Use userId to fetch locations
        return getLocationsByUserId(userId);
    }
}