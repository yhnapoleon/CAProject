package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.List;

public interface LocationService {
    List<Location> getLocationsByUserId(Integer userId);
    
    DataResult addLocation(Integer userId, String locationText, Integer postal);
    
    DataResult deleteLocation(Integer locationId);

    DataResult setLocationAsDefault(Integer locationId, String username);

    Integer getUserIdByUsername(String username);

    List<Location> getLocationsByUsername(String username);
}
