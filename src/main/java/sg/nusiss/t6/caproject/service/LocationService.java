package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.Location;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.List;

public interface LocationService {
    List<Location> getLocationsByUserId(Integer userId);
    
    DataResult addLocation(Integer userId, String locationText);
    
    DataResult deleteLocation(Integer locationId);
}
