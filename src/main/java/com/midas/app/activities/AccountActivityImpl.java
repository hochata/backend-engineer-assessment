package com.midas.app.activities;

import com.midas.app.exceptions.ApiException;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;

/** AccountActivityImpl */
public class AccountActivityImpl implements AccountActivity {
  private PaymentProvider provider;

  public AccountActivityImpl(PaymentProvider provider) {
    this.provider = provider;
  }

  @Override
  public Account saveAccount(Account account) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveAccount'");
  }

  @Override
  public Account createPaymentAccount(Account account) throws ApiException {
    var params =
        CreateAccount.builder()
            .email(account.getEmail())
            .firstName(account.getFirstName())
            .lastName(account.getLastName())
            .build();
    return this.provider.createAccount(params);
  }
}
