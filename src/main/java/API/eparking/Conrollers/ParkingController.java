package API.eparking.Conrollers;

import API.eparking.DTO.ParkingDTO;
import API.eparking.Exceptions.PromoCodes.PromoCodeExpiredException;
import API.eparking.Exceptions.PromoCodes.PromoCodeNotExistsException;
import API.eparking.Exceptions.PromoCodes.PromoCodeWasUsedException;
import API.eparking.Models.Cars;
import API.eparking.Models.Parking;
import API.eparking.Services.ParkingService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<ParkingDTO> getById(@PathVariable("id") Long id)   {
        try {
            parkingService.checkBusyParking();
            return ResponseEntity.ok(parkingService.getById(id));
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<ParkingDTO> getAll(@RequestParam(name = "available", required = false) String sort)   {
        parkingService.checkBusyParking();
        return parkingService.getAllParkingLots(sort);
    }

    @GetMapping("/create")
    public Boolean createParkingLots() {
        return parkingService.create();
    }

    @PutMapping("{id}")
    public ResponseEntity<ParkingDTO> editPrice(@PathVariable("id") Long id, @RequestBody ParkingDTO updatedParking) {
        try {
            return ResponseEntity.ok(parkingService.editPrice(id, updatedParking));
        } catch (EntityNotFoundException ex)    {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/rent/{id}")
    public ResponseEntity rentParkingLot(@PathVariable("id") Long parkingLotId, @RequestParam("id_car") Long carId,
                                        @RequestParam(value = "promocode", required = false)String promocode, @RequestBody ParkingDTO busyParkingLot)  {
        try {
            if (promocode != null){
                return ResponseEntity.ok().body(parkingService.rentParkingLot(parkingLotId, carId, busyParkingLot, promocode));
            }
            return ResponseEntity.ok().body(parkingService.rentParkingLot(parkingLotId, carId, busyParkingLot));
        } catch (PromoCodeNotExistsException ex)    {
            return ResponseEntity.badRequest().body("Promo code does not exists");
        } catch (PromoCodeWasUsedException ex)    {
            return ResponseEntity.badRequest().body("This promo code was used by the maximum number of people");
        } catch (PromoCodeExpiredException ex)  {
            return ResponseEntity.badRequest().body("Promo code has expired");
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ParkingDTO> removeCarFromParking(@PathVariable("id") Long id)    {
        try {
            return ResponseEntity.ok(parkingService.clearParkingLot(id));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
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
