package API.eparking.Conrollers;

import API.eparking.Exceptions.PromoCodes.PromoCodeExpiredException;
import API.eparking.Exceptions.PromoCodes.PromoCodeNotExistsException;
import API.eparking.Exceptions.PromoCodes.PromoCodeWasUsedException;
import API.eparking.Models.Cars;
import API.eparking.Models.Parking;
import API.eparking.Services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("{id}")
    public Parking getById(@PathVariable("id") Parking parking)   {
        parkingService.checkBusyParking();
        return parking;
    }

    @GetMapping
    public List<Parking> getAll(@RequestParam(name = "available", required = false) String sort)   {
        parkingService.checkBusyParking();
        return parkingService.getAllParkingLots(sort);
    }

    @GetMapping("/create")
    public Boolean createParkingLots() {
        return parkingService.create();
    }

    @PutMapping("{id}")
    public Parking edit(@PathVariable("id") Parking parking, @RequestBody Parking updatedParking) {
        return parkingService.editPrice(parking, updatedParking);
    }

    @PutMapping("/rent/{id}")
    public ResponseEntity rent(@PathVariable("id") Parking parkingLot, @RequestParam("id_car") Cars car,
                                        @RequestParam(value = "promocode", required = false)String promocode, @RequestBody Parking busyParkingLot)  {
        try {
            if (promocode != null){
                return ResponseEntity.ok().body(parkingService.rentParkingLot(parkingLot, car, busyParkingLot, promocode));
            }
            return ResponseEntity.ok().body(parkingService.rentParkingLot(parkingLot, car, busyParkingLot));
        } catch (PromoCodeNotExistsException ex)    {
            return ResponseEntity.badRequest().body("Promo code does not exists");
        } catch (PromoCodeWasUsedException ex)    {
            return ResponseEntity.badRequest().body("This promo code was used by the maximum number of people");
        } catch (PromoCodeExpiredException ex)  {
            return ResponseEntity.badRequest().body("Promo code has expired");
        }

    }

    @DeleteMapping("{id}")
    public Parking removeCarFromParking(@PathVariable("id") Parking parking)    {
        return parkingService.clearParkingLot(parking);
    }

    @PostMapping("/properties/pricePerHour")
    public Boolean setPricePerHour(@RequestParam int value) {
        return parkingService.setPricePerHour(value);
    }

    @PostMapping("/properties/pricePerDay")
    public Boolean setPricePerDay(@RequestParam int value) {
        return parkingService.setPricePerDay(value);
    }
}
