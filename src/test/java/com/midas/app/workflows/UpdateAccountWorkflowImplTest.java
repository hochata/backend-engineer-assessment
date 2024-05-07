package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

/** UpdateAccountWorkflowImplTest */
class UpdateAccountWorkflowImplTest {
  @RegisterExtension
  static final TestWorkflowExtension testWorkflowExtension =
      TestWorkflowExtension.newBuilder()
          .setWorkflowTypes(UpdateAccountWorflowImpl.class)
          .setDoNotStart(true)
          .build();

  @Test
  void testSuccessfulCreateAccountWorkflow(
      TestWorkflowEnvironment testEnv, Worker worker, UpdateAccountWorflow workflow) {

    var activities =
        Mockito.mock(AccountActivity.class, Mockito.withSettings().withoutAnnotations());
    var accountUpdate =
        Account.builder()
            .id(UUID.randomUUID())
            .firstName("testName")
            .lastName("testLast")
            .email("test@email.com")
            .build();
    Mockito.when(activities.saveAccount(Mockito.any())).thenReturn(accountUpdate);
    worker.registerActivitiesImplementations(activities);

    testEnv.start();

    var result = workflow.upateAccount(accountUpdate);
    Assertions.assertNotEquals(null, result);
    Assertions.assertEquals(accountUpdate.getFirstName(), result.getFirstName());
    Assertions.assertEquals(accountUpdate.getLastName(), result.getLastName());
    Assertions.assertEquals(accountUpdate.getEmail(), result.getEmail());
  }
}
