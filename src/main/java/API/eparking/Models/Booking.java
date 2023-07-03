package API.eparking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table
@Data
@NoArgsConstructor
public class Booking {

    public Booking(Timestamp start, Timestamp end, int days, int hours, int price, Users user, Parking parkingLot)    {
        this.start_booking = start;
        this.end_booking = end;
        this.days = days;
        this.hours = hours;
        this.user = user;
        this.price = price;
        this.parkingLot = parkingLot;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int days, hours;

    @Column
    private int price;

    @Column(nullable = false)
    private Timestamp start_booking, end_booking;

    @ManyToOne
    private Users user;

    @ManyToOne
    @JsonIgnore
    private Parking parkingLot;
}

