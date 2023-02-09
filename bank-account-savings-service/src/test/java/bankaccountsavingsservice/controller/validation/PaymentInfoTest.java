package bankaccountsavingsservice.controller.validation;

import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentInfoTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void serviceToPayMustNotBeNull() {
        PaymentInfoDto request = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(0L))
                .build();

        Set<ConstraintViolation<PaymentInfoDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void initialAmountMustNotBeNullAndGreaterThanZero() {
        PaymentInfoDto request = PaymentInfoDto
                .builder()
                .serviceToPay("TEST")
                .amountToPay(BigDecimal.valueOf(-2.32D))
                .build();

        Set<ConstraintViolation<PaymentInfoDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
