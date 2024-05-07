package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.UpdateAccount;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.AccountDto.ProviderTypeEnum;
import com.midas.generated.model.UpdateAccountDto;
import com.stripe.model.Customer;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .providerId(account.getProviderId())
        .providerType(ProviderTypeEnum.valueOf(account.getProvider().toString()))
        .email(account.getEmail())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt());

    return accountDto;
  }

  public static Account toAccount(
      @NonNull Customer customer, String firstName, String lastName, Account.Provider provider) {
    var createdAt =
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(customer.getCreated()), ZoneOffset.UTC);
    var updatedAt = OffsetDateTime.now();

    return Account.builder()
        .email(customer.getEmail())
        .providerId(customer.getId())
        .provider(provider)
        .createdAt(createdAt)
        .firstName(firstName)
        .lastName(lastName)
        .updatedAt(updatedAt)
        .build();
  }

  public static UpdateAccount toUpdateAccount(@NonNull UpdateAccountDto update) {
    return UpdateAccount.builder()
        .email(update.getEmail())
        .firstName(update.getFirstName())
        .lastName(update.getLastName())
        .build();
  }
}
