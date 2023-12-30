package API.eparking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cars {

    public Cars(String numbers, String color, char type, Users user)    {
        this.numbers = numbers;
        this.color = color;
        this.type = type;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numbers, color;

    @Column
    private String image;

    @Column(nullable = false)
    private char type;

    @ManyToOne
    @JsonIgnore
    private Users user;

    @OneToOne
    @JsonIgnore
    private Parking parking;

    @OneToOne
    @Transient
    @JsonIgnore
    private Transactions transactions;
}
