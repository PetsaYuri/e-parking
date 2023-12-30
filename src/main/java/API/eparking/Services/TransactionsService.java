package API.eparking.Services;

import API.eparking.Models.*;
import API.eparking.Repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final PromocodesService promocodesService;

    @Autowired
    @Lazy
    public TransactionsService(TransactionsRepository transactionsRepository, PromocodesService promocodesService)   {
        this.transactionsRepository = transactionsRepository;
        this.promocodesService = promocodesService;
    }

    public List<Transactions> getAll()  {
        return transactionsRepository.findAll();
    }

    public Transactions add(Transactions transaction)   {
        Transactions newTransaction = new Transactions(transaction.getUserName(), transaction.getUserPhoneNumber(), transaction.getCarNumbers(),
                transaction.getCarColor(), transaction.getPrice(), transaction.getParkingId(), transaction.getParkingEnds(), transaction.getUser(), transaction.getCar());
        return transactionsRepository.save(newTransaction);
    }

    public Transactions add(Transactions transaction, String titlePromoCode)   {
        PromoCodes promoCode = promocodesService.getByTitle(titlePromoCode);
        Transactions newTransaction = new Transactions(transaction.getUserName(), transaction.getUserPhoneNumber(), transaction.getCarNumbers(),
                transaction.getCarColor(), transaction.getPrice(), transaction.getParkingId(), transaction.getParkingEnds(), transaction.getUser(), transaction.getCar(),
                titlePromoCode, transaction.getPriceWithPromoCode(), promoCode);
        return transactionsRepository.save(newTransaction);
    }

    public Transactions update(Transactions existingTransaction, Transactions transaction)  {
        transaction.setId(existingTransaction.getId());
        return transactionsRepository.save(transaction);
    }

    public boolean delete(Transactions transactions)    {
        transactionsRepository.delete(transactions);
        return !transactionsRepository.existsById(transactions.getId());
    }

    public Transactions writeTransaction(Parking parkingLot, Cars car)   {
        Users user = car.getUser();
        return new Transactions(user.getFirst_name() + " " + user.getLast_name(), user.getPhoneNumber(),
                car.getNumbers(), car.getColor(),
                (parkingLot.getPricePerDay() * parkingLot.getBusy_days() + parkingLot.getPricePerHour() * parkingLot.getBusy_hours()),
                parkingLot.getId(), parkingLot.getBusyEnd(), user, car);
    }

    public Transactions writeTransactionWithPromoCode(Parking parkingLot, Cars car, PromoCodes promoCode)   {
        Users user = car.getUser();
        int totalPrice = promocodesService.use(promoCode, parkingLot.getPricePerDay() * parkingLot.getBusy_days() + parkingLot.getPricePerHour() * parkingLot.getBusy_hours());
        return new Transactions(user.getFirst_name() + " " + user.getLast_name(), user.getPhoneNumber(), car.getNumbers(), car.getColor(),
                (parkingLot.getBusy_days() * parkingLot.getPricePerDay()) + (parkingLot.getBusy_hours() * parkingLot.getPricePerHour()),
                parkingLot.getId(), parkingLot.getBusyEnd(), user, car, promoCode.getTitle(), totalPrice, promoCode);
    }

    public Boolean saveTransaction(Transactions transactions)   {
        if (transactions == null)   {
            return false;
        }
        transactionsRepository.save(transactions);
        return true;
    }

    public void deletePromoCode(PromoCodes promoCode)   {
        List<Transactions> transactions = transactionsRepository.findByPromoCode(promoCode);
        for (Transactions transaction : transactions)   {
            transaction.setPromoCode(null);
            transactionsRepository.save(transaction);
        }
    }

    public void deleteCar(Cars car) {
        List<Transactions> transactions = transactionsRepository.findByCar(car);
        for (Transactions transaction : transactions)   {
            transaction.setCar(null);
            transactionsRepository.save(transaction);
        }
    }

    public void deleteUser(Users user)   {
        List<Transactions> transactions = transactionsRepository.findByUser(user);
        for (Transactions transaction : transactions)   {
            transaction.setUser(null);
            transactionsRepository.save(transaction);
        }
    }
}
