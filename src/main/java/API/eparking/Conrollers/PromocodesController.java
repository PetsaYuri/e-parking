package API.eparking.Conrollers;

import API.eparking.DTO.PromocodeDTO;
import API.eparking.Exceptions.PromoCodes.PromoCodeWithoutRequiredFieldInTheBodyException;
import API.eparking.Models.PromoCodes;
import API.eparking.Services.PromocodesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promocodes")
public class PromocodesController {

    private final PromocodesService promocodesService;

    @Autowired
    public PromocodesController(PromocodesService promocodesService)    {
        this.promocodesService = promocodesService;
    }

    @GetMapping
    public ResponseEntity<List<PromocodeDTO>> getAllPromoCodes(@RequestParam(required = false) String available)  {
        return ResponseEntity.ok(promocodesService.getAll(available).stream().map(promoCode -> new PromocodeDTO(promoCode.getTitle(), promoCode.getCount(),
                promoCode.getAmount(), promoCode.getAmount(), promoCode.getDays())).toList());
    }

    @GetMapping("/search")
    public ResponseEntity<PromocodeDTO> getByTitle(@RequestParam String title) {
        try {
            PromoCodes promoCode = promocodesService.getByTitle(title);
            return ResponseEntity.ok(new PromocodeDTO(promoCode.getTitle(), promoCode.getCount(), promoCode.getAmount(), promoCode.getAmount(), promoCode.getDays()));
        }   catch (EntityNotFoundException | NullPointerException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PromocodeDTO> getOne(@PathVariable Long id)  {
        try {
            PromoCodes promoCode = promocodesService.getById(id);
            return ResponseEntity.ok(new PromocodeDTO(promoCode.getTitle(), promoCode.getCount(), promoCode.getAmount(), promoCode.getAmount(), promoCode.getDays()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity createPromoCode(@RequestBody PromocodeDTO promocodeDTO)    {
        try {
            PromoCodes promoCode = promocodesService.create(promocodeDTO);
            return ResponseEntity.ok(new PromocodeDTO(promocodeDTO.title(), promoCode.getCount(), promocodeDTO.amount(), promocodeDTO.percent(), promocodeDTO.days()));
        }   catch (PromoCodeWithoutRequiredFieldInTheBodyException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/updateCode/{id}")
    public ResponseEntity<PromocodeDTO> updatePromoCode(@PathVariable("id") Long id) {
        try {
            PromoCodes promoCode = promocodesService.updateCode(id);
            return ResponseEntity.ok(new PromocodeDTO(promoCode.getTitle(), promoCode.getCount(), promoCode.getAmount(), promoCode.getAmount(), promoCode.getDays()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<PromocodeDTO> editPromoCode(@PathVariable("id") Long id, @RequestBody PromocodeDTO promocodeDTO)  {
        try {
            PromoCodes promoCode = promocodesService.edit(id, promocodeDTO);
            return ResponseEntity.ok(new PromocodeDTO(promoCode.getTitle(), promoCode.getCount(), promoCode.getAmount(), promocodeDTO.percent(), promocodeDTO.days()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePromoCode(@PathVariable("id") Long id)  {
        try {
            boolean result = promocodesService.delete(id);
            return result ? ResponseEntity.ok("Successfully deleted") : ResponseEntity.internalServerError().body("An error occurred while deleting");
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

}
