package API.eparking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "promocodes")
@Data
@NoArgsConstructor
public class PromoCodes {

  public PromoCodes(int count, int amount, int days) {
      this.title = UUID.randomUUID().toString();
      this.count = count;
      this.amount = amount;
      this.days = days;
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE, days);
      this.ends = Timestamp.from(calendar.toInstant());
    }

    public PromoCodes(int count, double percent, int days) {
    this.title = UUID.randomUUID().toString().substring(0, 6);
    this.count = count;
    this.percent = percent;
    this.days = days;
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, days);
    this.ends = Timestamp.from(calendar.toInstant());
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column
  private int amount;

  @Column
  private double percent;

  @Column(nullable = false)
  private int count, days;

  @Column
  private Timestamp ends;

  @OneToOne
  @Transient
  @JsonIgnore
  private Transactions transactions;
}
