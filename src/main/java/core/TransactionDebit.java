package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.Crypto;
import utilities.Request;
import utilities.Response;
import static utilities.Configs.*;
import static utilities.CommonCodes.*;

@Path("/transaction")
public class TransactionDebit {

	Request requestModule;

	public TransactionDebit() {
		requestModule = new Request();
	}

	@POST
	@Path("/debit-amount")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> debitAmount(Map<String, Object> request) {

		return debitAmountId(request, null);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@POST
	@Path("/debit-amount/{tenant_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> debitAmountId(Map<String, Object> request, @PathParam("tenant_id") String tenant_id) {

		System.out.println("------------------");
		System.out.println("REQUEST : debitAmount");
		System.out.println("------------------");

		Gson gson = new Gson();
		Date date = new Date();

		Map<String, Object> head = new HashMap<String, Object>();
		head.put("api", "debitAmount");
		head.put("apiVersion", "V1");
		head.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date));

		Response response = new Response();

		// JsonObject commonCodes = null;

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		try {

			// ASSIGNING TENANT_ID IF THE PLATFORM IS MULTY TENANT
			String TENANT_ID = "";
			if (IS_MULTY_TENANT_PLATFORM.equals("YES")) {
				if (MULTY_TENANT_MODE.equals("QUERY")) {
					TENANT_ID = tenant_id;
				} else if (MULTY_TENANT_MODE.equals("PARAMS")) {
					TENANT_ID = tenant_id;
				}
			}

			/*  */
			/* ASSIGN ENCRYPTION_KEY, API_KEY & API_ID OF ENTITY */
			String ENCRYPTION_KEY = "";
			String AUTH_API_ID = "";
			String AUTH_API_KEY = "";
			/*  */

			// DEBIT_CONFIRM REQUEST URL
			String DEBIT_CONFIRM_URL = BASE_URL + "/transaction/confirmDebit";

			Map<String, Object> DEBIT_CONFIRM_HEAD = new HashMap<String, Object>();
			DEBIT_CONFIRM_HEAD.put("api", "confirmDebit");
			DEBIT_CONFIRM_HEAD.put("apiVersion", "V1");
			DEBIT_CONFIRM_HEAD.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date));

			Map<String, Object> AUTH_MAP = new HashMap<String, Object>();
			AUTH_MAP.put("API_ID", AUTH_API_ID);
			AUTH_MAP.put("API_KEY", AUTH_API_KEY);
			DEBIT_CONFIRM_HEAD.put("auth", AUTH_MAP);

			// ASSIGNING DATA RECIVED IN THE REQUEST
			JsonObject REQUEST_DATA = gson.fromJson(gson.toJson(request), JsonElement.class).getAsJsonObject()
					.getAsJsonObject("encrypted_data");

			// DECRYPTING DATA RECEIVED
			JsonObject DECRYPTED_DATA = Crypto.decryptRimitData(REQUEST_DATA, ENCRYPTION_KEY);

			// ERROR RESPONSE IF DECRYPTION FAILED
			if (DECRYPTED_DATA == null) {
				result.put("code", RESULT_CODE_DECRYPTION_FAILED);
				result.put("status", STATUS_ERROR);
				result.put("message", RESULT_MESSAGE_E2008);

				head.put("HTTP_CODE", HTTP_CODE_BAD_REQUEST);
				data = null;
				return new ObjectMapper().readValue(
						response.error(gson.toJson(head), gson.toJson(result), gson.toJson(data)), HashMap.class);
			}

			JsonObject USER = null, TRANSACTION = null, REFUND_REFERENCE = null;
			//
			try {
				USER = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data").getAsJsonObject("remitter");
				TRANSACTION = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data")
						.getAsJsonObject("transaction");
				REFUND_REFERENCE = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data")
						.getAsJsonObject("refund_reference");

			} catch (Exception e) {
				e.printStackTrace();
			}

			String USER_MOBILE = USER.get("mobile").getAsString();
			String USER_COUNTRY_CODE = USER.get("country_code").getAsString();
			String USER_ACCOUNT_NUMBER = USER.get("account_number").getAsString();
			String USER_BRANCH_CODE = USER.get("branch_code").getAsString();

			String TRANSACTION_TYPE = TRANSACTION.get("type").getAsString();
			String TRANSACTION_ID = TRANSACTION.get("txn_id").getAsString();
			String TRANSACTION_AMOUNT = TRANSACTION.get("amount").getAsString();
			String TRANSACTION_DATE = TRANSACTION.get("date").getAsString();
			String TRANSACTION_TIME = TRANSACTION.get("time").getAsString();

			String REFUND_ID, REFUND_AMOUNT, REFUND_DATE, REFUND_TIME;
			if (TRANSACTION_TYPE.equals("REFUND_DEBIT")) {
				REFUND_ID = REFUND_REFERENCE.get("txn_id").getAsString();
				REFUND_AMOUNT = REFUND_REFERENCE.get("amount").getAsString();
				REFUND_DATE = REFUND_REFERENCE.get("date").getAsString();
				REFUND_TIME = REFUND_REFERENCE.get("time").getAsString();
			}

			/*  */
			/*  */
			/* VERIFY THE USER */
			/*
			 * MANAGE SCOPE FOR FAILED TRANSACTIONS (Refer -
			 * https://doc.rimit.co/transaction-debit/confirm-debit#result-code)
			 */
			/* VERIFY THE USER ACCOUNT */
			/* VERIFY THE USER ACCOUNT BALANCE AVAILABILITY */
			/* DEBIT USER ACCOUNT WITH txn_amount */
			/*  */
			/*  */

			/*  */
			/* GENERATE A UNIQUE TRANSACTION_REF */
			String TRANSACTION_REF = "";
			/*  */

			/*  */
			/* ASSIGN LATEST ACCOUNT_BALANCE AFTER CREDITING THE TRANSACTION_AMOUNT */
			String ACCOUNT_BALANCE = "";
			/*  */

			Map<String, Object> DEBIT_CONFIRM_DATA = new HashMap<String, Object>();
			DEBIT_CONFIRM_DATA.put("txn_id", TRANSACTION_ID);
			DEBIT_CONFIRM_DATA.put("txn_reference", TRANSACTION_REF);
			DEBIT_CONFIRM_DATA.put("txn_amount", TRANSACTION_AMOUNT);
			DEBIT_CONFIRM_DATA.put("txn_type", TRANSACTION_TYPE);
			DEBIT_CONFIRM_DATA.put("account_balance", ACCOUNT_BALANCE);

			/*  */
			/*
			 * EG FOR FAILED REQUEST : FIND LATEST ACCOUNT BALANCE, IF FOUND INSUFFICIENT,
			 * SEND REQUEST AS FAILED
			 */
			boolean CHECK_LATEST_BALANCE = true;
			if (!CHECK_LATEST_BALANCE) {

				Map<String, Object> DEBIT_CONFIRM_RESULT = new HashMap<String, Object>();
				DEBIT_CONFIRM_RESULT.put("code", RESULT_CODE_INSUFFICIENT_ACCOUNT_BALANCE);
				DEBIT_CONFIRM_RESULT.put("status", STATUS_FAILED);
				DEBIT_CONFIRM_RESULT.put("message", RESULT_MESSAGE_E8899);

				JsonObject DEBIT_CONFIRM = requestModule.confirmTransaction(gson.toJson(DEBIT_CONFIRM_HEAD),
						gson.toJson(DEBIT_CONFIRM_RESULT), gson.toJson(DEBIT_CONFIRM_DATA), DEBIT_CONFIRM_URL,
						ENCRYPTION_KEY);

				if (DEBIT_CONFIRM == null) {
					System.out.println(
							"DEBIT_CONFIRM - CHECK_LATEST_BALANCE - REQUEST STATUS");
					System.out.println(gson.toJson(DEBIT_CONFIRM));
				}

				return null;
			}
			/*  */

			// IF THE DEBIT AMOUNT IS SUCCESSFUL
			Map<String, Object> DEBIT_CONFIRM_RESULT = new HashMap<String, Object>();
			DEBIT_CONFIRM_RESULT.put("code", RESULT_CODE_SUCCESS);
			DEBIT_CONFIRM_RESULT.put("status", STATUS_SUCCESS);
			DEBIT_CONFIRM_RESULT.put("message", RESULT_MESSAGE_E1001);

			JsonObject DEBIT_CONFIRM = requestModule.confirmTransaction(gson.toJson(DEBIT_CONFIRM_HEAD),
					gson.toJson(DEBIT_CONFIRM_RESULT), gson.toJson(DEBIT_CONFIRM_DATA), DEBIT_CONFIRM_URL,
					ENCRYPTION_KEY);

			if (DEBIT_CONFIRM == null) {
				System.out.println(
						"CREDIT_CONFIRM - CHECK_ACCOUNT_STATUS - REQUEST STATUS");
				System.out.println(gson.toJson(DEBIT_CONFIRM));
			}

			System.out.println("*****************");
			System.out.println("DEBIT_CONFIRM - RESPONSE");
			System.out.println(gson.toJson(DEBIT_CONFIRM));
			System.out.println("*****************");

			/*  */
			/*  */

			/* MANAGE RECEIVED RESPONSE */
			/*  */

			/*  */
			/*  */

			return null;

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}

	}

}
