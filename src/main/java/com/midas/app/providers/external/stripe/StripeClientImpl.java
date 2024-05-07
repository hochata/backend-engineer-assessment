package com.midas.app.providers.external.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * StripeClientImpl
 *
 * <p>Just a wrapper to call the actual Stripe methods
 */
@Service
@RequiredArgsConstructor
public class StripeClientImpl implements StripeClient {
  @Override
  public Customer createCustomer(CustomerCreateParams params) throws StripeException {
    return Customer.create(params);
  }

  @Override
  public Customer retrieveCustomer(String customer) throws StripeException {
    return Customer.retrieve(customer);
  }
}
