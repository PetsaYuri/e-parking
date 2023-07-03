package API.eparking.Conrollers;

import API.eparking.Models.Transactions;
import API.eparking.Services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping
    public List<Transactions> getAll() {
        return transactionsService.getAll();
    }

    @GetMapping("{id}")
    public Transactions get(@PathVariable("id") Transactions transaction)   {
        return transaction;
    }

    @PostMapping()
    public Transactions add(@RequestBody Transactions transaction, @RequestParam(required = false) String promoCode)  {
        if (promoCode.isEmpty())    {
            return transactionsService.add(transaction);
        }
        return transactionsService.add(transaction, promoCode);
    }

    @PutMapping("{id}")
    public Transactions update(@PathVariable Transactions existingTransaction, @RequestBody Transactions transaction)   {
        return transactionsService.update(existingTransaction, transaction);
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable("id") Transactions transaction) {
        return transactionsService.delete(transaction);
    }
}
