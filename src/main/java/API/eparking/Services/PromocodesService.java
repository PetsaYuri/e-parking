package API.eparking.Services;

import API.eparking.DTO.PromocodeDTO;
import API.eparking.Exceptions.PromoCodes.PromoCodeWithoutRequiredFieldInTheBodyException;
import API.eparking.Models.PromoCodes;
import API.eparking.Repositories.PromocodesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Component
public class PromocodesService {

    private final PromocodesRepository promocodesRepository;
    private final TransactionsService transactionsService;

    @Autowired
    @Lazy
    public PromocodesService(PromocodesRepository promocodesRepository, TransactionsService transactionsService)  {
        this.promocodesRepository = promocodesRepository;
        this.transactionsService = transactionsService;
    }

    public PromoCodes getById(Long id)  {
        return promocodesRepository.getReferenceById(id);
    }

    public List<PromoCodes> getAll(String available)    {
        if (available != null)  {
            return Boolean.parseBoolean(available)
                    ? promocodesRepository.findByEndsAfter(Timestamp.from(Calendar.getInstance().toInstant()))
                    : promocodesRepository.findByEndsBefore(Timestamp.from(Calendar.getInstance().toInstant()));
        }
        return promocodesRepository.findAll();
    }

    public PromoCodes create(PromocodeDTO promoCode) throws PromoCodeWithoutRequiredFieldInTheBodyException {
        PromoCodes newPromocode;
        if (promoCode.percent() != 0) {
            double percent = promoCode.percent();
            newPromocode = new PromoCodes(promoCode.count(), percent, promoCode.days());
            return promocodesRepository.save(newPromocode);
        }   else if (promoCode.amount() != 0)  {
            newPromocode = new PromoCodes(promoCode.count(), promoCode.amount(), promoCode.days());
            return promocodesRepository.save(newPromocode);
        }
        throw new PromoCodeWithoutRequiredFieldInTheBodyException();
    }

    public PromoCodes updateCode(Long id)  {
        PromoCodes promoCode = promocodesRepository.getReferenceById(id);
        if (promocodesRepository.existsById(promoCode.getId()))    {
            String newCode = UUID.randomUUID().toString().substring(0, 6);
            promoCode.setTitle(newCode);
            return promocodesRepository.save(promoCode);
        }
        throw new EntityNotFoundException();
    }

    public PromoCodes edit(Long id, PromocodeDTO promocodeDTO) {
        PromoCodes promocode = promocodesRepository.getReferenceById(id);
        if (promocodeDTO.count() > 0) {
            promocode.setCount(promocodeDTO.count());
        }

        if (promocodeDTO.amount() > 0) {
            promocode.setAmount(promocodeDTO.amount());
            promocode.setPercent(0);
        }   else if (promocodeDTO.percent() > 0) {
            promocode.setPercent(promocodeDTO.percent());
            promocode.setAmount(0);
        }

        if (promocodeDTO.days() > 0 && Calendar.getInstance().toInstant().isBefore(promocode.getEnds().toInstant())) {
            int seconds = promocodeDTO.days() * 24 * 60 * 60;
            promocode.setEnds(Timestamp.from(promocode.getEnds().toInstant().plusSeconds(seconds)));
        }
        return promocodesRepository.save(promocode);
    }

    public Boolean delete(Long id) {
        PromoCodes promoCode = promocodesRepository.getReferenceById(id);
        if (promocodesRepository.existsById(promoCode.getId()))    {
            transactionsService.deletePromoCode(promoCode);
            promocodesRepository.delete(promoCode);
            return true;
        }
        throw new EntityNotFoundException();
    }

    public PromoCodes getByTitle(String title)  {
        return promocodesRepository.findByTitle(title);
    }

    public int use(PromoCodes promoCode, int price)   {
            promoCode.setCount(promoCode.getCount() - 1);
            promocodesRepository.save(promoCode);
            if (promoCode.getPercent() != 0)    {
                int discount = (int) Math.round(price * promoCode.getPercent() / 100);
                return Integer.parseInt(price - discount + "");
            }   else if (promoCode.getAmount() != 0)    {
                return promoCode.getAmount() < price ? price - promoCode.getAmount() : price;
            }
        return price;
    }
}
