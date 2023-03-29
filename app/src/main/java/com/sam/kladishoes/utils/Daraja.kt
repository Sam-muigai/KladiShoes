package com.sam.kladishoes.utils

import com.github.daraja.driver.DarajaDriver
import com.github.daraja.model.requests.STKPushRequest
import com.github.daraja.utils.*

const val PASS_KEY: String = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

val darajaDriver = DarajaDriver(
    consumerKey = "CAy6oI1RFDRGwSJxT0fMDJXAfySUOveE",
    consumerSecret = "pm1GxKSGEl0bhrMI",
    environment = Environment.SandBox()
)


fun stkPushRequest(mobilePhone:String,amount:String) = STKPushRequest(
    businessShortCode = "174379",
    password = getPassword("174379", PASS_KEY, timestamp),
    timestamp = timestamp,
    mpesaTransactionType = TransactionType.CustomerPayBillOnline(),
    amount = amount,
    partyA = sanitizePhoneNumber("254$mobilePhone"),
    partyB = "174379",
    phoneNumber = sanitizePhoneNumber("254$mobilePhone"),
    callBackURL = "https://mydomain.com/path",
    accountReference = "Dlight", // Account reference
    transactionDesc = "Dlight STK PUSH" // Transaction description
)



