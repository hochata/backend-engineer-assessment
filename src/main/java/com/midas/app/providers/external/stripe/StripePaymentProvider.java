package com.midas.app.providers.external.stripe;

import com.midas.app.exceptions.ApiException;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    System.out.println(this.configuration.getApiKey());
    Stripe.apiKey = this.configuration.getApiKey();
    var params =
        CustomerCreateParams.builder()
            .setEmail(details.getEmail())
            .setName(String.format("%s %s", details.getFirstName(), details.getLastName()))
            .build();
    try {
      var customer = this.stripeClient.createCustomer(params);
      var createdAt =
          OffsetDateTime.ofInstant(Instant.ofEpochMilli(customer.getCreated()), ZoneOffset.UTC);
      var createdAccount =
          Account.builder()
              .email(customer.getEmail())
              .providerId(customer.getId())
              .provider(Account.Provider.STRIPE)
              .createdAt(createdAt)
              .firstName(details.getFirstName())
              .lastName(details.getLastName())
              .updatedAt(createdAt)
              .build();

      return createdAccount;
    } catch (StripeException e) {
      this.logger.error(e.getMessage());
      throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
