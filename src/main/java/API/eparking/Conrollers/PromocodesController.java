package API.eparking.Conrollers;

import API.eparking.Models.PromoCodes;
import API.eparking.Services.PromocodesService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PromoCodes> getAllPromoCodes(@RequestParam(required = false) String title, @RequestParam(required = false) String available)  {
        return promocodesService.getAll(title, available);
    }

    @GetMapping("{id}")
    public PromoCodes getOne(@PathVariable PromoCodes promoCode)  {
        return promoCode;
    }

    @PostMapping
    public PromoCodes createPromoCode(@RequestBody PromoCodes promoCodes)    {
        return promocodesService.create(promoCodes);
    }

    @PutMapping("{id}")
    public PromoCodes updatePromoCode(@PathVariable("id") PromoCodes promoCodes) {
        return promocodesService.updateCode(promoCodes);
    }

    @DeleteMapping("{id}")
    public Boolean deletePromoCode(@PathVariable("id") PromoCodes promoCodes)  {
        return promocodesService.delete(promoCodes);
    }

}
