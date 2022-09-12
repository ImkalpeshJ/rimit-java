package core;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import utilities.Crypto;
import utilities.Response;
import static utilities.Configs.*;
import static utilities.CommonCodes.*;

@Path("/account")
public class AccountFetch {

	@POST
	@Path("/fetch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> accountFetch(Map<String, Object> request)
			throws JsonMappingException, JsonProcessingException {

		return accountFetchId(request, null);
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/fetch/{tenant_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> accountFetchId(Map<String, Object> request, @PathParam("tenant_id") String tenant_id)
			throws JsonMappingException, JsonProcessingException {

		System.out.println("------------------");
		System.out.println("REQUEST : accountFetch");
		System.out.println("------------------");

		Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

			@Override
			public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
				if (src == src.longValue())
					return new JsonPrimitive(src.longValue());
				return new JsonPrimitive(src);
			}
		}).create();

		Date date = new Date();

		Map<String, Object> head = new HashMap<String, Object>();
		head.put("api", "accountFetch");
		head.put("apiVersion", "V1");
		head.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date));

		Response response = new Response();

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		List<Map<String, Object>> USER_ACCOUNTS = new ArrayList<Map<String, Object>>();

		try {
			String TENANT_ID = "";
			if (IS_MULTY_TENANT_PLATFORM.equals("YES")) {
				if (MULTY_TENANT_MODE.equals("QUERY")) {
					if (tenant_id != null)
						TENANT_ID = tenant_id;
				} else if (MULTY_TENANT_MODE.equals("PARAMS")) {
					if (tenant_id != null)
						TENANT_ID = tenant_id;
				}
			}

			/*  */
			/* ASSIGN ENCRYPTION_KEY OF ENTITY */
			String ENCRYPTION_KEY = "";
			/*  */

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

			try {
				String USER_MOBILE = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data").get("mobile")
						.getAsString();
				String USER_CC = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data").get("country_code")
						.getAsString();
				String DOB = DECRYPTED_DATA.getAsJsonObject("content").getAsJsonObject("data").get("dob").getAsString();

			} catch (Exception e) {
				e.printStackTrace();
			}

			/*  */
			/*  */
			/* VERIFY THE USER */
			/*
			 * MANAGE SCOPE FOR ERRORS (Refer -
			 * https://doc.rimit.co/account/account-fetch#response-code)
			 */
			/*  */
			/*  */

			/*  */
			/* EG FOR FAILED RESPONSE :FIND USER, IF NOT FOUND, SEND RESPONSE AS FAILED */
			boolean FIND_USER = true;
			if (!FIND_USER) {
				result.put("code", RESULT_CODE_MOBILE_NUMBER_NOT_FOUND);
				result.put("status", STATUS_FAILED);
				result.put("message", RESULT_MESSAGE_E2014);

				head.put("HTTP_CODE", HTTP_CODE_SUCCESS);
				data = null;

				return new ObjectMapper().readValue(
						response.success(gson.toJson(head), gson.toJson(result), gson.toJson(data), ENCRYPTION_KEY),
						HashMap.class);
			}
			/*  */

			List<Map<String, Object>> ACCOUNT_DATA = new ArrayList<Map<String, Object>>();

			/*  */
			/* READ ALL ACCOUNTS OF THE USER IN ACCOUNTS DATA */
			Map<String, Object> account1 = new HashMap<String, Object>();
			account1.put("account_name", "ATHISH BALU");
			account1.put("account_number", "11223333");
			account1.put("branch_code", "BR001");
			account1.put("branch_name", "KANNUR");
			account1.put("account_type", "SAVING_ACCOUNT");
			account1.put("account_class", "SAVING");
			account1.put("txn_amount_limit", "5000");
			account1.put("account_status", "ACTIVE");
			account1.put("account_opening_date", "2020-09-01");

			account1.put("is_debit_allowed", true);
			account1.put("is_credit_allowed", true);
			account1.put("is_cash_debit_allowed", true);
			account1.put("is_cash_credit_allowed", true);

			ACCOUNT_DATA.add(account1);

			Map<String, Object> account2 = new HashMap<String, Object>();
			account2.put("account_name", "ATHISH BALU K");
			account2.put("account_number", "613274841345");
			account2.put("branch_code", "BR002");
			account2.put("branch_name", "ERNAKULAM");
			account2.put("account_type", "GOLD_LOAN");
			account2.put("account_class", "GOLD");
			account2.put("txn_amount_limit", "200000");
			account2.put("account_status", "ACTIVE");
			account2.put("account_opening_date", "2020-12-10");

			account2.put("is_debit_allowed", true);
			account2.put("is_credit_allowed", true);
			account2.put("is_cash_debit_allowed", false);
			account2.put("is_cash_credit_allowed", false);

			ACCOUNT_DATA.add(account2);

			/*  */

			/*  */
			/* ASSIGN DATA RECEIVED FROM ACCOUNTS_DATA ARRAY */

			if (ACCOUNT_DATA.size() > 0) {
				for (Map<String, Object> account : ACCOUNT_DATA) {
					Map<String, Object> user_account = new HashMap<String, Object>();
					user_account.put("account_name", account.get("account_name"));
					user_account.put("account_number", account.get("account_number"));
					user_account.put("branch_code", account.get("branch_code"));
					user_account.put("branch_name", account.get("branch_name"));
					user_account.put("account_type", account.get("account_type"));
					user_account.put("account_class", account.get("account_class"));
					user_account.put("txn_amount_limit", account.get("txn_amount_limit"));
					user_account.put("account_status", account.get("account_status"));
					user_account.put("account_opening_date", account.get("account_opening_date"));

					user_account.put("is_debit_allowed", account.get("is_debit_allowed"));
					user_account.put("is_credit_allowed", account.get("is_credit_allowed"));
					user_account.put("is_cash_debit_allowed", account.get("is_cash_debit_allowed"));
					user_account.put("is_cash_credit_allowed", account.get("is_cash_credit_allowed"));

					USER_ACCOUNTS.add(user_account);
				}
			}
			/*  */

			result.put("code", RESULT_CODE_SUCCESS);
			result.put("status", STATUS_SUCCESS);
			result.put("message", RESULT_MESSAGE_E1001);

			head.put("HTTP_CODE", HTTP_CODE_SUCCESS);
			data.put("accounts", USER_ACCOUNTS);
			return new ObjectMapper().readValue(
					response.success(gson.toJson(head), gson.toJson(result), gson.toJson(data), ENCRYPTION_KEY),
					HashMap.class);

		} catch (Exception e) {
			result.put("code", RESULT_CODE_SERVICE_NOT_AVAILABLE);
			result.put("status", STATUS_ERROR);
			result.put("message", RESULT_MESSAGE_E2003);

			head.put("HTTP_CODE", HTTP_CODE_SERVICE_UNAVAILABLE);
			data = null;
			return new ObjectMapper().readValue(
					response.error(gson.toJson(result), gson.toJson(head), gson.toJson(data)), HashMap.class);
		}
	}
}
