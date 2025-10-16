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
            // 1) Use service to fetch address list by username
            // Call getLocationsByUsername in LocationServiceImpl
            List<Location> locations = locationService.getLocationsByUsername(username);

            // 2) Convert to frontend format (include defaultAddress; convert to boolean)
            List<Map<String, Object>> data = locations.stream()
                    .map(loc -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("locationId", loc.getLocationId());
                        m.put("locationText", loc.getLocationText());
                        m.put("postal", loc.getPostal());
                        // Important: convert String "1" to true; others to false
                        m.put("defaultAddress", "1".equals(loc.getDefaultAddress()));
                        return m;
                    })
                    .collect(Collectors.toList());

            return new DataResult(Code.SUCCESS, data, "Fetched address list successfully");
        } catch (RuntimeException e) {
            // Catch business exceptions thrown by service (e.g., user not found)
            return new DataResult(Code.FAILED, null, "Failed to fetch address list: " + e.getMessage());
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to fetch address list: internal error");
        }
    }

    @PostMapping("/addLocation")
    public DataResult addLocation(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String locationText = (String) request.get("locationText");
            Integer postal = request.get("postal") == null ? null : (Integer) request.get("postal");

            if (userId == null || locationText == null || locationText.trim().isEmpty() || postal == null) {
                return new DataResult(Code.FAILED, null, "User ID, location text and postal must not be null");
            }

            return locationService.addLocation(userId, locationText, postal);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to add address: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteLocation/{locationId}")
    public DataResult deleteLocation(@PathVariable Integer locationId) {
        try {
            if (locationId == null) {
                return new DataResult(Code.FAILED, null, "Location ID must not be null");
            }

            return locationService.deleteLocation(locationId);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to delete address: " + e.getMessage());
        }
    }

    @PutMapping("/set-default/{locationId}")
    public DataResult setDefaultAddress(@PathVariable Integer locationId,
            @RequestParam String username) {
        try {
            if (locationId == null || username == null || username.trim().isEmpty()) {
                return new DataResult(Code.FAILED, null, "Location ID and username must not be null");
            }

            // Assume LocationService implements default address logic as:
            // 1. Verify the address belongs to the user
            // 2. Clear default flag on other addresses
            // 3. Set default flag on the specified address
            return locationService.setLocationAsDefault(locationId, username);

        } catch (RuntimeException e) {
            // Handle business errors (e.g., address not found, user mismatch)
            return new DataResult(Code.FAILED, null, "Failed to set default address: " + e.getMessage());
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to set default address: internal error");
        }
    }
}
