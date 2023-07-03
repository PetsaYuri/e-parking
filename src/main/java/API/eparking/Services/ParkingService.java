package API.eparking.Services;

import API.eparking.Exceptions.PromoCodes.PromoCodeExpiredException;
import API.eparking.Exceptions.PromoCodes.PromoCodeNotExistsException;
import API.eparking.Exceptions.PromoCodes.PromoCodeWasUsedException;
import API.eparking.Models.*;
import API.eparking.Repositories.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ParkingService {

    @Value(("${parking.count}"))
    private int count;

    @Value("${parking.price.hour}")
    private int pricePerHour;

    @Value("${parking.price.day}")
    private int pricePerDay;

    private final ParkingRepository parkingRepository;
    private final CarsService carsService;
    private final PromocodesService promocodesService;
    private final TransactionsService transactionsService;
    private final BookingService bookingService;

    @Autowired
    public ParkingService(ParkingRepository parkingRepository, CarsService carsService, PromocodesService promocodesService,
                          TransactionsService transactionsService, BookingService bookingService) {
        this.parkingRepository = parkingRepository;
        this.carsService = carsService;
        this.promocodesService = promocodesService;
        this.transactionsService = transactionsService;
        this.bookingService = bookingService;
    }

    public Boolean setPricePerHour(int value) {
        pricePerHour = value;
        return changeApplicationProperties("parking.price.hour", String.valueOf(value));
    }

    public Boolean setPricePerDay(int value) {
        pricePerDay = value;
        return changeApplicationProperties("parking.price.day", String.valueOf(value));
    }

    public Boolean changeApplicationProperties(String name, String value) {
        try {
            File file = new File( "src/main/resources/application.properties");
            String[] content = getContentFromFile(file).split(name, 2);
            String arr[] = content[1].split("\n", 2);
            String fileEnd = "";
            if (arr.length > 1)    {
                fileEnd += arr[1];
            }
            fileEnd = fileEnd.isEmpty() ? "" : "\n" + fileEnd;
            if (file.exists())  {
                FileWriter fw = new FileWriter(file);
                fw.write(content[0] + name + "=" + value + fileEnd);
                fw.flush();
                fw.close();
                return true;
            }
        }   catch (IOException ex)  {
            System.out.println(ex.getStackTrace());
        }
        return false;
    }

    public String getContentFromFile(File file)   {
        String content = "";
        try {
            FileReader fileReader = new FileReader(file);
            int c;
            while ((c= fileReader.read()) != -1)  {
                content += (char) c;
            }
            fileReader.close();
        }   catch (FileNotFoundException ex)    {
            System.out.println("File \"" + file.getName() + "\" not found");
        }   catch (IOException ex)  {
            System.out.println("IOException");
        }
        return content;
    }

    public Parking findParkingById(long id) {
        return parkingRepository.getReferenceById(id);
    }

    public List<Parking> getAllParkingLots(String sort)    {
        return sort == null ? parkingRepository.findAll(Sort.by("id")) : parkingRepository.findByIsAvailable(Boolean.valueOf(sort));
    }

    public Boolean create()    {
        if (parkingRepository.count() < count) {
            for (long i = parkingRepository.count(); i < count; i++) {
                Parking parking = new Parking(i + 1, pricePerHour, pricePerDay);
                parkingRepository.save(parking);
            }
        }   else {
            List<Parking> notAvailableSlots = new ArrayList<>();
            for (long i = parkingRepository.count(); i > count; i--)    {
                Parking parking = parkingRepository.getReferenceById(i);
                if (parking.getIsAvailable())   {
                    parkingRepository.delete(parking);
                }   else {
                    notAvailableSlots.add(parking);
                    if (notAvailableSlots.size() > getAllAvailableParkingSlot().size()) {
                        break;
                    }
                }
            }
            List<Parking> availableSlots = getAllAvailableParkingSlot();
            try {
                for (int i = 0; i < notAvailableSlots.size(); i++)  {
                    Parking avSlot = availableSlots.get(i);
                    Parking notAvSlot = notAvailableSlots.get(i);
                    Cars car = carsService.deleteParkingLot(notAvSlot.getCar());
                    avSlot.setCar(car);
                    carsService.setParkingLotToCar(car, avSlot);
                    avSlot.setPricePerHour(notAvSlot.getPricePerHour());
                    avSlot.setPricePerDay(notAvSlot.getPricePerDay());
                    avSlot.setIsAvailable(false);
                    parkingRepository.delete(notAvSlot);
                    parkingRepository.save(avSlot);
                }
            } catch (IndexOutOfBoundsException ex)  {
                for (Parking notAvSlot : notAvailableSlots) {
                    long i;
                    for (i = 1; i <= parkingRepository.count(); i++)    {
                        if (!parkingRepository.existsById(i))  {
                            break;
                        }
                    }
                    Cars car = notAvSlot.getCar();
                    carsService.deleteParkingLot(car);
                    notAvSlot.setCar(null);
                    parkingRepository.save(notAvSlot);
                    notAvSlot.setId(i);
                    parkingRepository.save(notAvSlot);
                    carsService.setParkingLotToCar(car, notAvSlot);
                    notAvSlot.setCar(car);
                    parkingRepository.save(notAvSlot);
                }
            }
        }
        return true;
    }

    public Parking editPrice(Parking parking, Parking updatedParking) {
        if (!Double.toString(updatedParking.getPricePerHour()).isEmpty() && updatedParking.getPricePerHour() != parking.getPricePerHour()
                && updatedParking.getPricePerHour() != 0)    {
            parking.setPricePerHour(updatedParking.getPricePerHour());
        }

        if (!Double.toString(updatedParking.getPricePerDay()).isEmpty() && updatedParking.getPricePerDay() != parking.getPricePerDay()
                && updatedParking.getPricePerDay() != 0)    {
            parking.setPricePerDay(updatedParking.getPricePerDay());
        }
        return parkingRepository.save(parking);
    }

    public long setEnd(int days, int hours)    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime().getTime();
    }

    public Parking rentParkingLot(Parking parkingLot, Cars car, Parking busyParkingLot)    {

        if(parkingLot.getIsBooking())   {

        }

        checkBusyParking();
        boolean setTimer = false;
        Transactions transaction = null;
        if (parkingLot.getIsAvailable() && car != null
                && (busyParkingLot.getBusy_days() > 0 || busyParkingLot.getBusy_hours() > 0)) {
            parkingLot.setCar(car);
            carsService.setParkingLotToCar(car, parkingLot);
            parkingLot.setIsAvailable(false);
            parkingLot.setBusy_days(busyParkingLot.getBusy_days());
            parkingLot.setBusy_hours(busyParkingLot.getBusy_hours());
            parkingLot.setBusyStart(Timestamp.from(Calendar.getInstance().toInstant()));
            parkingLot.setBusyEnd(new Timestamp(setEnd(busyParkingLot.getBusy_days(), busyParkingLot.getBusy_hours())));
            setTimer = true;

            transaction = transactionsService.writeTransaction(parkingLot, car);
        }
        Parking savedParking = parkingRepository.save(parkingLot);
        if (setTimer)   {
            setTimer(busyParkingLot.getBusy_hours(), busyParkingLot.getBusy_hours(), savedParking);
        }

        boolean transactionSaved = transactionsService.saveTransaction(transaction);
        if (!transactionSaved)  {
            System.out.println("Transaction with parking id \"" + parkingLot.getId() + "\" not written");
        }
        return savedParking;
    }

    public Parking rentParkingLot(Parking parkingLot, Cars car, Parking busyParkingLot, String namePromocode)
            throws PromoCodeNotExistsException, PromoCodeWasUsedException, PromoCodeExpiredException {
        checkBusyParking();
        PromoCodes promoCode = promocodesService.getPromocode(namePromocode);
        boolean setTimer = false;
        Transactions transaction = null;

        if (parkingLot.getIsAvailable() && car != null && (busyParkingLot.getBusy_days() > 0 || busyParkingLot.getBusy_hours() > 0) && car.getParking() == null) {
            parkingLot.setCar(car);
            carsService.setParkingLotToCar(car, parkingLot);
            parkingLot.setIsAvailable(false);
            parkingLot.setBusy_days(busyParkingLot.getBusy_days());
            parkingLot.setBusy_hours(busyParkingLot.getBusy_hours());
            parkingLot.setBusyStart(Timestamp.from(Calendar.getInstance().toInstant()));
            parkingLot.setBusyEnd(new Timestamp(setEnd(busyParkingLot.getBusy_days(), busyParkingLot.getBusy_hours())));
            //promoCode
            if (promoCode != null) {
                if (promoCode.getCount() > 0) {
                    if (Calendar.getInstance().getTime().before(promoCode.getEnds())) {
                        parkingLot.setPromocode(promoCode.getTitle());
                        transaction = transactionsService.writeTransactionWithPromoCode(parkingLot, car, promoCode);
                    }   else {
                        throw new PromoCodeExpiredException();
                    }
                }   else {
                    throw new PromoCodeWasUsedException();
                }
            }   else {
                throw new PromoCodeNotExistsException();
            }
            setTimer = true;
        }
        Parking savedParking = parkingRepository.save(parkingLot);
        if (setTimer)   {
            setTimer(busyParkingLot.getBusy_hours(), busyParkingLot.getBusy_hours(), savedParking);
        }

        boolean transactionSaved = transactionsService.saveTransaction(transaction);
        if (!transactionSaved) {
            System.out.println("Transaction with parking id \"" + parkingLot.getId() + "\" not written");
        }
        return savedParking;
    }

    public Parking clearParkingLot(Parking parking)  {
        if (parking.getCar() != null)   {
            carsService.deleteParkingLot(parking.getCar());
            parking.setCar(null);
            parking.setPricePerHour(pricePerHour);
            parking.setPricePerDay(pricePerDay);
            parking.setIsAvailable(true);
            parking.setBusyStart(null);
            parking.setBusyEnd(null);
            parking.setBusy_hours(0);
            parking.setBusy_days(0);
            parking.setPromocode(null);
            return parkingRepository.save(parking);
        }
        return parking;
    }

    public void checkBusyParking()  {
        List<Parking> parkingList = parkingRepository.findByIsAvailable(false);
        List<Booking> listToDelete = new ArrayList<>();
        for (Parking parking : parkingList) {
            for (Booking booking : parking.getBookingLot()) {
                if (Timestamp.from(Calendar.getInstance().toInstant()).after(booking.getStart_booking()))   {
                    listToDelete.add(booking);
                }
            }
            deleteUselessBookings(parking, listToDelete);

            if (Timestamp.from(Calendar.getInstance().toInstant()).after(parking.getBusyEnd()))  {
                clearParkingLot(parking);
            }
        }
    }

    public void setTimer(int hours, int days, Parking busyParkingLot) {
        long time = 60000;
        time += hours > 0 ? (long) hours * 3600000 : 0;
        time += days > 0 ? (long) days * 86400000 : 0;

        TimerTask timerTask = new ClearingParkingLot(busyParkingLot);
        Timer timer = new Timer();
		timer.schedule(timerTask, time); //1 min = 60 000 mili
    }

    public List<Parking> getAllAvailableParkingSlot()  {
        return parkingRepository.findByIsAvailable(true);
    }

    public Boolean parkingLotsEqualsCount()   {
        return count == parkingRepository.count();
    }

    public class ClearingParkingLot extends TimerTask {

        private final Parking busyParkingLot;

        public ClearingParkingLot(Parking parkingLot)  {
            this.busyParkingLot = parkingLot;
        }

        @Override
        public void run() {
            clearParkingLot(busyParkingLot);
        }
    }

    public void bookingParkingLot(Booking bookingLot, Parking parkingLot)  {
        checkBusyParking();
        parkingLot.setIsBooking(true);
        parkingLot.getBookingLot().add(bookingLot);
        parkingRepository.save(parkingLot);
    }

    public boolean deleteBooking(Parking parkingLot) {
        parkingLot.setIsBooking(false);
        parkingLot.setBookingLot(null);
        parkingRepository.save(parkingLot);
        return true;
    }

    public void deleteUselessBookings(Parking parkingLot, List<Booking> listBooking)    {
        bookingService.deleteParkingLot(listBooking);
        parkingLot.getBookingLot().removeAll(listBooking);
        parkingRepository.save(parkingLot);
    }
}
