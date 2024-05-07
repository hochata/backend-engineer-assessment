package com.midas.app.services;

import com.midas.app.exceptions.ResourceNotFoundException;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.UpdateAccount;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.UpdateAccountWorflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  @Autowired private final WorkflowClient workflowClient;

  @Autowired private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(String.format("create-account-%s", details.getEmail()))
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);

    var account = workflow.createAccount(details);
    return this.accountRepository.save(account);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  private Account buildUpdate(Account account, UpdateAccount update) {
    if (update.getEmail() != null) {
      account.setEmail(update.getEmail());
    }
    if (update.getFirstName() != null) {
      account.setFirstName(update.getFirstName());
    }
    if (update.getLastName() != null) {
      account.setLastName(update.getLastName());
    }
    return account;
  }

  @Override
  public Account updateAccount(UUID id, UpdateAccount update) {
    var account =
        this.buildUpdate(
            this.accountRepository.findById(id).orElseThrow(ResourceNotFoundException::new),
            update);

    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(UpdateAccountWorflow.QUEUE_NAME)
            .setWorkflowId(String.format("update-account-%s", account.getEmail()))
            .build();
    logger.info("initiating workflow to update account for email: {}", account.getEmail());
    var workflow = workflowClient.newWorkflowStub(UpdateAccountWorflow.class, options);

    var updatedAccount = workflow.upateAccount(account);
    return this.accountRepository.save(updatedAccount);
  }
}
