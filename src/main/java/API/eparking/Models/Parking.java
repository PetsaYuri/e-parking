package API.eparking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
public class Parking {

    public Parking(long id, int pricePerHour, int pricePerDay)    {
        this.id = id;
        this.isAvailable = true;
        this.busy_days = 0;
        this.busy_hours = 0;
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
        this.isBooking = false;
    }

    @Id
    private Long id;

    @Column(nullable = false)
    private Boolean isAvailable, isBooking;

    @Column()
    private int pricePerHour;

    @Column(nullable = false)
    private int pricePerDay, busy_hours, busy_days;

    @Column
    private Timestamp busyStart, busyEnd;

    @Column
    private String promocode;

    @OneToOne
    @JsonIgnore
    private Cars car;

    @OneToOne
    @Transient
    @JsonIgnore
    private Transactions transactions;

    @OneToMany
    @JsonIgnore
    private List<Booking> bookingLot;
}
