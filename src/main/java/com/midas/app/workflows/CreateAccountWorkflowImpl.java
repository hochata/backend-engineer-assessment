package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

/** CreateAccountWorkflowImpl */
@WorkflowImpl(taskQueues = "payment-account")
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  private final ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build();

  private final AccountActivity activities =
      Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account createAccount(Account details) {
    return this.activities.createPaymentAccount(details);
  }
}
