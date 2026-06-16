import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public interface PaymentProcessor {
    void process(PaymentCreateDto dto);
}
