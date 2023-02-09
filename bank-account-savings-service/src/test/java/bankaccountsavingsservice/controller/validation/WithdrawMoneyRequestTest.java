package bankaccountsavingsservice.controller.validation;

import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WithdrawMoneyRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void amountMustNotBeNullAndGreaterThanZero() {
        WithdrawMoneyRequestDto request = WithdrawMoneyRequestDto
                .builder()
                .amount(BigDecimal.valueOf(-2L))
                .build();

        Set<ConstraintViolation<WithdrawMoneyRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
