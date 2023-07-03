package API.eparking.Services;

import API.eparking.Models.PromoCodes;
import API.eparking.Repositories.PromocodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    public List<PromoCodes> getAll(String title, String available)    {
        if (title != null)  {
            List<PromoCodes> promoCodes = new ArrayList<>();
            promoCodes.add(promocodesRepository.findByTitle(title)) ;
            return promoCodes;
        }   else if (available != null)  {
            return Boolean.parseBoolean(available)
                    ? promocodesRepository.findByEndsAfter(Timestamp.from(Calendar.getInstance().toInstant()))
                    : promocodesRepository.findByEndsBefore(Timestamp.from(Calendar.getInstance().toInstant()));
        }
        return promocodesRepository.findAll();
    }

    public PromoCodes create(PromoCodes promoCode)  {
        PromoCodes newPromocode;
        if (promoCode.getPercent() != 0) {
            newPromocode = new PromoCodes(promoCode.getCount(), promoCode.getPercent(), promoCode.getDays());
            return promocodesRepository.save(newPromocode);
        }   else if (promoCode.getAmount() != 0)  {
            newPromocode = new PromoCodes(promoCode.getCount(), promoCode.getAmount(), promoCode.getDays());
            return promocodesRepository.save(newPromocode);
        }
        return null;
    }

    public PromoCodes updateCode(PromoCodes promoCode)  {
        if (promocodesRepository.existsById(promoCode.getId()))    {
            String newCode = UUID.randomUUID().toString().substring(0, 6);
            promoCode.setTitle(newCode);
            return promocodesRepository.save(promoCode);
        }
        return null;
    }

    public Boolean delete(PromoCodes promoCode) {
        if (promocodesRepository.existsById(promoCode.getId()))    {
            transactionsService.deletePromoCode(promoCode);
            promocodesRepository.delete(promoCode);
            return true;
        }
        return false;
    }

    public PromoCodes getPromocode(String title)  {
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
