package com.ecommercepractice.paymentservice.models.CardMessage.body;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTypeInfo {
    @NotEmpty
    private String tccNumber;

    @NotEmpty
    private String securityCode;

    @NotEmpty
    private String expirationData;

    @NotNull
    @Valid
    private Owner owner;
}
