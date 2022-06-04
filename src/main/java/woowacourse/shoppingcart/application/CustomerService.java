package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import woowacourse.common.exception.UnauthorizedException;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.domain.customer.PasswordEncoder;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;
import woowacourse.shoppingcart.dto.CustomerUpdateRequest;
import woowacourse.shoppingcart.dto.PasswordRequest;
import woowacourse.shoppingcart.dto.PhoneNumberRequest;
import woowacourse.shoppingcart.entity.CustomerEntity;
import woowacourse.shoppingcart.exception.DuplicatedCustomerException;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@Service
public class CustomerService {

    private static final String MISMATCHED_PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";

    private final PasswordEncoder passwordEncoder;
    private final CustomerDao customerDao;

    public CustomerService(PasswordEncoder passwordEncoder, CustomerDao customerDao) {
        this.passwordEncoder = passwordEncoder;
        this.customerDao = customerDao;
    }

    public void create(CustomerRequest customerRequest) {
        if (customerDao.existsByAccount(customerRequest.getAccount())) {
            throw new DuplicatedCustomerException();
        }
        Customer customer = customerRequest.toCustomer();
        customer.encryptPassword(passwordEncoder);

        customerDao.save(CustomerEntity.from(customer));
    }

    public CustomerResponse findById(Long customerId) {
        CustomerEntity customerEntity = getCustomer(customerId);
        return CustomerResponse.from(customerEntity.toCustomer());
    }

    public void delete(Long customerId, PasswordRequest passwordRequest) {
        CustomerEntity customerEntity = getCustomer(customerId);

        String rawPassword = passwordRequest.getPassword();
        String encryptPassword = customerEntity.getPassword();
        if (!passwordEncoder.match(rawPassword, encryptPassword)) {
            throw new UnauthorizedException(MISMATCHED_PASSWORD_ERROR);
        }

        customerDao.deleteById(customerId);
    }

    public void update(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        CustomerEntity customerEntity = getCustomer(customerId);

        Customer customer = customerEntity.toCustomer();
        updateCustomer(customer, customerUpdateRequest);

        customerDao.update(CustomerEntity.from(customer));
    }

    private CustomerEntity getCustomer(Long customerId) {
        return customerDao.findById(customerId)
                .orElseThrow(InvalidCustomerException::new);
    }

    private void updateCustomer(Customer customer, CustomerUpdateRequest customerUpdateRequest) {
        customer.changeNickname(customerUpdateRequest.getNickname());
        customer.changeAddress(customerUpdateRequest.getAddress());

        PhoneNumberRequest phoneNumberRequest = customerUpdateRequest.getPhoneNumber();
        customer.changePhoneNumber(
                phoneNumberRequest.getStart(),
                phoneNumberRequest.getMiddle(),
                phoneNumberRequest.getLast()
        );
    }
}
