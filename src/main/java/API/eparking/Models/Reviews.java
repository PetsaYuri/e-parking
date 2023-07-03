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
public class Reviews {

    public Reviews(String body, int grade, Users user)    {
        this.body = body;
        this.grade = grade;
        this.date = Timestamp.from(Calendar.getInstance().toInstant());
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false)
    private Timestamp date;

    @ManyToOne
    private Users user;
}
