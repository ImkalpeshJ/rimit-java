package core;

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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utilities.Request;
import static utilities.Configs.*;

@Path("/transaction")
public class TransactionStatus {

	Request requestModule;

	public TransactionStatus() {
		requestModule = new Request();
	}

	@POST
	@Path("/status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> txnStatus(Map<String, Object> request) {
		return txnStatusId(request, null);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@POST
	@Path("/status/{tenant_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> txnStatusId(Map<String, Object> request, @PathParam("tenant_id") String tenant_id) {

		System.out.println("------------------");
		System.out.println("REQUEST : txnStatus");
		System.out.println("------------------");

		Gson gson = new Gson();
		Date date = new Date();

		try {

			/*  */
			/* ASSIGN ENCRYPTION_KEY, API_KEY & API_ID OF ENTITY */
			String ENCRYPTION_KEY = "";
			String AUTH_API_ID = "";
			String AUTH_API_KEY = "";
			/*  */

			/*  */
			/* ASSIGNING DATA RECIVED IN THE REQUEST */
			JsonObject REQUEST_DATA = gson.fromJson(gson.toJson(request), JsonElement.class).getAsJsonObject();
			/*  */

			/*  */
			/* ASSIGNING DATA RECIVED IN THE REQUEST */
			String TRANSACTION_TYPE = REQUEST_DATA.get("type").getAsString();
			String TRANSACTION_ID = REQUEST_DATA.get("id").getAsString();
			String TRANSACTION_AMOUNT = REQUEST_DATA.get("amount").getAsString();
			String TRANSACTION_REF = REQUEST_DATA.get("reference").getAsString();
			/*  */

			// TXN_STATUS REQUEST URL
			String TXN_STATUS_URL = BASE_URL + "/transaction/status";

			Map<String, Object> TXN_STATUS_HEAD = new HashMap<String, Object>();
			TXN_STATUS_HEAD.put("api", "status");
			TXN_STATUS_HEAD.put("apiVersion", "V1");
			TXN_STATUS_HEAD.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date));

			Map<String, Object> AUTH_MAP = new HashMap<String, Object>();
			AUTH_MAP.put("API_ID", AUTH_API_ID);
			AUTH_MAP.put("API_KEY", AUTH_API_KEY);
			TXN_STATUS_HEAD.put("auth", AUTH_MAP);

			Map<String, Object> TXN_STATUS_DATA = new HashMap<String, Object>();
			TXN_STATUS_DATA.put("txn_id", TRANSACTION_ID);
			TXN_STATUS_DATA.put("txn_reference", TRANSACTION_REF);
			TXN_STATUS_DATA.put("txn_amount", TRANSACTION_AMOUNT);
			TXN_STATUS_DATA.put("txn_type", TRANSACTION_TYPE);

			// TXN_STATUS_RESULT MUST BE EMPTY
			Map<String, Object> TXN_STATUS_RESULT = new HashMap<String, Object>();

			JsonObject TXN_STATUS = requestModule.confirmTransaction(gson.toJson(TXN_STATUS_HEAD),
					gson.toJson(TXN_STATUS_RESULT), gson.toJson(TXN_STATUS_DATA), TXN_STATUS_URL, ENCRYPTION_KEY);

			if (TXN_STATUS == null) {
				System.out.println(
						"TXN_STATUS - REQUEST STATUS");
				System.out.println(gson.toJson(TXN_STATUS));
			}

			System.out.println("*****************");
			System.out.println("TXN_STATUS - RESPONSE");
			System.out.println(gson.toJson(TXN_STATUS));
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
