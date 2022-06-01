package woowacourse.shoppingcart.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import woowacourse.shoppingcart.domain.customer.vo.Account;
import woowacourse.shoppingcart.domain.customer.vo.Address;
import woowacourse.shoppingcart.domain.customer.vo.Nickname;
import woowacourse.shoppingcart.domain.customer.vo.Password;
import woowacourse.shoppingcart.domain.customer.vo.PhoneNumber;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CustomerTest {

    @Test
    void 고객_생성() {
        Customer customer = new Customer(null, "yhh1056", "호호", "gusghWkd12!", "호호네", "010", "1234", "5678");

        assertThat(customer).isNotNull();
    }
}
