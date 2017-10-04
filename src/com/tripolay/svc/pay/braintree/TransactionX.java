package com.tripolay.svc.pay.braintree;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

public class TransactionX 
{
	private static BraintreeGateway gateway = new BraintreeGateway(
			  Environment.SANDBOX,
			  "tfvht3gs2wnt5y95",
			  "pj899txj8qbm5rnc",
			  "7018d331e087012c63c88b4035d858f1"
			);
	
	
	public static Result<Transaction> Apply(String amount, String nonceFromTheClient)
	{
		TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .paymentMethodNonce(nonceFromTheClient)
			    .options()
	        		.submitForSettlement(true)
	        		.done();

		Result<Transaction> result = gateway.transaction().sale(request);
		return result;
	}
	public static Result<Transaction> Apply2(String amount, String aCustomerId)
	{
		TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .customerId(aCustomerId)
			    .options()
	        		.submitForSettlement(true)
	        		.done();

		Result<Transaction> result = gateway.transaction().sale(request);
		return result;
	}
	public static Result<Transaction> Apply3(String amount, String token)
	{
		TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .paymentMethodToken(token)
			    .options()
		        	.submitForSettlement(true)
		        	.done();

		Result<Transaction> result = gateway.transaction().sale(request);
		return result;
	}

}
