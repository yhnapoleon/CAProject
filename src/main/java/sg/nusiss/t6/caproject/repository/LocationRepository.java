package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    // Find all locations by user ID
    List<Location> findByUserId(Integer userId);

    @Modifying
    @Query("UPDATE Location l SET l.defaultAddress = '0' WHERE l.userId = :userId AND l.locationId != :locationId")
    void clearDefaultByUserId(@Param("userId") Integer userId, @Param("locationId") Integer locationId);
}
