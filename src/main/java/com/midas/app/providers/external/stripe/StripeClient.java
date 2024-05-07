package com.midas.app.providers.external.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

/**
 * StripeClient
 *
 * <p>The Stripe SDK methods we are using are static, so mocking them is problematic.
 *
 * <p>Wrapping them in this class makes testing easier.
 */
public interface StripeClient {
  Customer createCustomer(CustomerCreateParams params) throws StripeException;

  Customer retrieveCustomer(String customer) throws StripeException;
}
