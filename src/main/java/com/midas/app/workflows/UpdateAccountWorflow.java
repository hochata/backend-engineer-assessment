package com.midas.app.workflows;

import com.midas.app.models.Account;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/** UpdateAccountWorflow */
@WorkflowInterface
public interface UpdateAccountWorflow {

  String QUEUE_NAME = "payment-account";

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @WorkflowMethod
  Account upateAccount(Account details);
}
