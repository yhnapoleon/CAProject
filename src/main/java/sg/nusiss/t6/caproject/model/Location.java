//By Zhao Jiayi

package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location_text", nullable = false)
    private String locationText;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "default_address", length = 20, nullable = false)
    private String defaultAddress;

    @Column(name = "postal", nullable = false)
    private Integer postal;

    // An address belongs to one user (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;
}
