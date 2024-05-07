package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.UpdateAccount;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  /**
   * updateAccount updates information from an existing account
   *
   * @param accountId identifier of the account to update
   * @param updateAccount contains the updated InformationExtractorJdbcDatabaseMetaDataImpl
   * @return Account
   */
  Account updateAccount(UUID accountId, UpdateAccount updateAccount);

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();
}
