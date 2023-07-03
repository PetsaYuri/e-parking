package API.eparking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users {

    public Users(String first_name, String last_name, String phoneNumber, String email, String password)  {
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_registered = Date.valueOf(Calendar.getInstance().toInstant().toString().split("T", 2)[0]);
        this.time_registered = Time.valueOf(java.sql.Date.from(Instant.now()).toString().split(" ", 5)[3]);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = "user";
        this.password = password;
        this.isBanned = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String first_name, last_name;

    @Column(nullable = false)
    private String phoneNumber, email;

    @Column(nullable = false)
    private Date date_registered;

    @Column
    private Time time_registered;

    @Column
    private String role, image;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isBanned;

    @OneToMany
    private List<Cars> cars;

    @OneToMany
    private List<Reviews> reviews;

    @OneToOne
    @Transient
    @JsonIgnore
    private Transactions transactions;

    @OneToMany
    private List<Booking> bookingLots;
}
