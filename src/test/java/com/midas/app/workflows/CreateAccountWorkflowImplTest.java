package com.midas.app.workflows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/** CreateAccountWorkflowImplTest */
class CreateAccountWorkflowImplTest {
  @RegisterExtension
  static final TestWorkflowExtension testWorkflowExtension =
      TestWorkflowExtension.newBuilder()
          .setWorkflowTypes(CreateAccountWorkflowImpl.class)
          .setDoNotStart(true)
          .build();

  @Test
  void testSuccessfulCreateAccountWorkflow(
      TestWorkflowEnvironment testEnv, Worker worker, CreateAccountWorkflow workflow) {

    var activities = mock(AccountActivity.class, withSettings().withoutAnnotations());
    var newAccount =
        Account.builder()
            .firstName("testName")
            .lastName("testLast")
            .email("test@email.com")
            .build();
    when(activities.createPaymentAccount(any())).thenReturn(newAccount);
    worker.registerActivitiesImplementations(activities);

    testEnv.start();

    var result = workflow.createAccount(newAccount);
    assertNotEquals(null, result);
    assertEquals(newAccount.getFirstName(), result.getFirstName());
    assertEquals(newAccount.getLastName(), result.getLastName());
    assertEquals(newAccount.getEmail(), result.getEmail());
  }
}
