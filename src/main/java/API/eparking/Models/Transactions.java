package API.eparking.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table
@Data
@NoArgsConstructor
public class Transactions {

    public Transactions(String userName, String userPhoneNumber, String carNumbers, String carColor, int price,
                        long parkingId, Timestamp parkingEnds, Users user, Cars car)    {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.carNumbers = carNumbers;
        this.carColor = carColor;
        this.price = price;
        this.parkingId = parkingId;
        this.parkingEnds = parkingEnds;
        this.user = user;
        this.car = car;
        this.date = Timestamp.from(Calendar.getInstance().toInstant());
    }

    public Transactions(String userName, String userPhoneNumber, String carNumbers, String carColor, int price,
                        long parkingId, Timestamp parkingEnds, Users user, Cars car,
                        String promoCodeTitle, int priceWithPromoCode, PromoCodes promoCode)    {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.carNumbers = carNumbers;
        this.carColor = carColor;
        this.price = price;
        this.parkingId = parkingId;
        this.parkingEnds = parkingEnds;
        this.user = user;
        this.car = car;
        this.promoCodeTitle = promoCodeTitle;
        this.priceWithPromoCode = priceWithPromoCode;
        this.promoCode = promoCode;
        this.date = Timestamp.from(Calendar.getInstance().toInstant());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName, userPhoneNumber, carNumbers, carColor;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Long parkingId;

    @Column(nullable = false)
    private Timestamp parkingEnds;

    @Column
    private String promoCodeTitle;

    @Column
    private int priceWithPromoCode;

    @Column(nullable = false)
    private Timestamp date;

    @OneToOne
    private Users user;

    @OneToOne
    private Cars car;

    @OneToOne
    private PromoCodes promoCode;
}
