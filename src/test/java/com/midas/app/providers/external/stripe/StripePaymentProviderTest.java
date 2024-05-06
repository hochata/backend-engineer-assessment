package com.midas.app.providers.external.stripe;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** StripePaymentProviderTest */
class StripePaymentProviderTest {
  private PaymentProvider stripeProvider;

  private Customer mockCustomer;

  @BeforeEach
  void setup() throws StripeException {
    mockCustomer = mock(Customer.class);
    when(mockCustomer.getEmail()).thenReturn("test@email.com");
    when(mockCustomer.getCreated()).thenReturn(Instant.now().toEpochMilli());

    var stripeClient = mock(StripeClient.class);
    when(stripeClient.createCustomer(any(CustomerCreateParams.class))).thenReturn(mockCustomer);
    var conf = new StripeConfiguration();
    conf.setApiKey("testKey");
    this.stripeProvider = new StripePaymentProvider(conf, stripeClient);
  }

  @Test
  void successfullyCreateAccount() throws StripeException {
    this.setup();
    var params =
        CreateAccount.builder()
            .firstName("testName")
            .lastName("testLastName")
            .email("test@email.com")
            .build();

    this.stripeProvider.createAccount(params);
    assertNotEquals(null, this.stripeProvider.createAccount(params));
  }
}
