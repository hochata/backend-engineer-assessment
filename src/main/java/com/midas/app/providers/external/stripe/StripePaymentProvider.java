package com.midas.app.providers.external.stripe;

import com.midas.app.exceptions.ApiException;
import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.providers.payment.UpdateAccount;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  private final StripeClient stripeClient;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) throws ApiException {
    Stripe.apiKey = this.configuration.getApiKey();
    var params =
        CustomerCreateParams.builder()
            .setEmail(details.getEmail())
            .setName(String.format("%s %s", details.getFirstName(), details.getLastName()))
            .build();
    try {
      var customer = this.stripeClient.createCustomer(params);

      return Mapper.toAccount(
          customer, details.getFirstName(), details.getLastName(), Account.Provider.STRIPE);
    } catch (StripeException e) {
      this.logger.error(e.getMessage());
      throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  public Account saveAccount(String providerId, UpdateAccount details) {
    var update =
        CustomerUpdateParams.builder()
            .setEmail(details.getEmail())
            .setName(String.format("%s %s", details.getFirstName(), details.getLastName()))
            .build();
    Stripe.apiKey = this.configuration.getApiKey();
    try {
      var customer = this.stripeClient.retrieveCustomer(providerId);
      customer.update(update);

      return Mapper.toAccount(
          customer, details.getFirstName(), details.getLastName(), Account.Provider.STRIPE);
    } catch (StripeException e) {
      this.logger.error(e.getMessage());
      throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
