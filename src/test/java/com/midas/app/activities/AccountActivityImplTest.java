package com.midas.app.activities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.PaymentProvider;
import io.temporal.testing.TestActivityExtension;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/** AccountActivityImplTest */
class AccountActivityImplTest {
  static final PaymentProvider provider = mock(PaymentProvider.class);

  @RegisterExtension
  static final TestActivityExtension activityExtension =
      TestActivityExtension.newBuilder()
          .setActivityImplementations(new AccountActivityImpl(provider))
          .build();

  // Test the Activity in isolation from the Workflow
  @Test
  void testCreateAccountActivity(AccountActivity activities) {
    var params =
        Account.builder()
            .firstName("testFirst")
            .lastName("testLast")
            .email("test@email.com")
            .build();
    when(provider.createAccount(any())).thenReturn(params);

    // Run the Activity in the test environment
    var result = activities.createPaymentAccount(params);

    // Check for the expected return value
    assertEquals(params.getFirstName(), result.getFirstName());
    assertEquals(params.getLastName(), result.getLastName());
    assertEquals(params.getEmail(), result.getEmail());
  }

  @Test
  void testSaveAccountActivity(AccountActivity activities) {
    var params =
        Account.builder()
            .id(UUID.randomUUID())
            .providerId("testExternalId")
            .firstName("testFirstName")
            .lastName("testLastName")
            .email("test@email.com")
            .build();

    when(provider.saveAccount(anyString(), any())).thenReturn(params);

    // Run the Activity in the test environment
    var result = activities.saveAccount(params);

    // Check for the expected return value
    assertEquals(params.getFirstName(), result.getFirstName());
    assertEquals(params.getLastName(), result.getLastName());
    assertEquals(params.getEmail(), result.getEmail());
  }
}
