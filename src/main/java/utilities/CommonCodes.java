package utilities;

public class CommonCodes {
    public static Integer HTTP_CODE_SUCCESS = 200;
    public static Integer HTTP_CODE_BAD_REQUEST = 400;
    public static Integer HTTP_CODE_UNAUTHORIZED = 401;
    public static Integer HTTP_CODE_NOT_ACCEPTABLE = 406;
    public static Integer HTTP_CODE_CONFLICT = 409;
    public static Integer HTTP_CODE_SERVICE_UNAVAILABLE = 503;

    public static String STATUS_SUCCESS = "SUCCESS";
    public static String STATUS_FAILED = "FAILED";
    public static String STATUS_HOLD = "HOLD";
    public static String STATUS_REFUND = "REFUND";
    public static String STATUS_CANCEL = "CANCEL";
    public static String STATUS_PROCESSING = "PROCESSING";
    public static String STATUS_PENDING = "PENDING";
    public static String STATUS_UNDEFINED = "UNDEFINED";
    public static String STATUS_ERROR = "ERROR";

    public static Integer RESULT_CODE_SUCCESS = 1001;
    public static Integer RESULT_CODE_HOLD = 1002;
    public static Integer RESULT_CODE_FAILED = 9999;
    public static Integer RESULT_CODE_TECHNICAL_ERROR = 2001;
    public static Integer RESULT_CODE_ERROR_WITH_SERVICE_PROVIDER = 2002;
    public static Integer RESULT_CODE_SERVICE_NOT_AVAILABLE = 2003;
    public static Integer RESULT_CODE_AUTHENTICATION_FAILED = 2004;
    public static Integer RESULT_CODE_SERVER_DOWN = 2005;
    public static Integer RESULT_CODE_CANNOT_PROCESS_THIS_REQUEST = 2006;
    public static Integer RESULT_CODE_ENCRYPTION_FAILED = 2007;
    public static Integer RESULT_CODE_DECRYPTION_FAILED = 2008;
    public static Integer RESULT_CODE_NO_ACTIVE_ACCOUNT_FOUND = 2011;
    public static Integer RESULT_CODE_ACCOUNT_NOT_FOUND_FOR_USER = 2012;
    public static Integer RESULT_CODE_DOB_MISMATCH = 2013;
    public static Integer RESULT_CODE_MOBILE_NUMBER_NOT_FOUND = 2014;
    public static Integer RESULT_CODE_INVALID_ACCOUNT = 2021;
    public static Integer RESULT_CODE_ACCOUNT_NUMBER_AND_MOBILE_NUMBER_DO_NOT_MATCH = 2022;
    public static Integer RESULT_CODE_INSUFFICIENT_ACCOUNT_BALANCE = 8899;
    public static Integer RESULT_CODE_ACCOUNT_NOT_FOUND = 8898;
    public static Integer RESULT_CODE_ACCOUNT_IS_INACTIVE_BLOCKED_CLOSED = 8897;
    public static Integer RESULT_CODE_DEBIT_IS_NOT_ALLOWED = 8896;
    public static Integer RESULT_CODE_CREDIT_IS_NOT_ALLOWED = 8895;
    public static Integer RESULT_CODE_USER_DECLINED = 8894;
    public static Integer RESULT_CODE_TIME_EXPIRED = 8893;
    public static Integer RESULT_CODE_ATTEMPTS_TO_RETRY_EXCEEDED_MAXIMUM = 8892;
    public static Integer RESULT_CODE_SETTLEMENT_ACCOUNT_NOT_FOUND = 8891;
    public static Integer RESULT_CODE_ENTITY_IS_INACTIVE = 9988;
    public static Integer RESULT_CODE_INSUFFICIENT_VIRTUAL_ACCOUNT_BALANCE = 9989;
    public static Integer RESULT_CODE_TRANSACTION_DETAILS_DO_NOT_MATCH = 2091;
    public static Integer RESULT_CODE_TRANSACTION_REFERENCE_IS_NOT_UNIQUE = 2092;
    public static Integer RESULT_CODE_TRANSACTION_REFERENCE_MUST_BE_SAME = 2093;
    
    public static String RESULT_MESSAGE_E1001 = "The request is successfully processed";
    public static String RESULT_MESSAGE_E1002 = "The status updated as hold";
    public static String RESULT_MESSAGE_E9999 = "Processed request failed";
    public static String RESULT_MESSAGE_E2001 = "Failed due to technical error";
    public static String RESULT_MESSAGE_E2002 = "Error with Service Provider";
    public static String RESULT_MESSAGE_E2003 = "Service not available";
    public static String RESULT_MESSAGE_E2004 = "Authentication Failed";
    public static String RESULT_MESSAGE_E2005 = "Server Down";
    public static String RESULT_MESSAGE_E2006 = "Cannot process this request";
    public static String RESULT_MESSAGE_E2007 = "Encryption failed";
    public static String RESULT_MESSAGE_E2008 = "Decryption failed";
    public static String RESULT_MESSAGE_E2011 = "No active account found";
    public static String RESULT_MESSAGE_E2012 = "Account not found for user";
    public static String RESULT_MESSAGE_E2013 = "DOB mismatch";
    public static String RESULT_MESSAGE_E2014 = "Mobile number not found";
    public static String RESULT_MESSAGE_E2021 = "Invalid account";
    public static String RESULT_MESSAGE_E2022 = "Account number and mobile number do not match";
    public static String RESULT_MESSAGE_E2091 = "Transaction details do not match";
    public static String RESULT_MESSAGE_E2092 = "Transaction reference is not unique";
    public static String RESULT_MESSAGE_E2093 = "The transaction reference must be the same as the HOLD";
    public static String RESULT_MESSAGE_E8899 = "Insufficient account balance";
    public static String RESULT_MESSAGE_E8898 = "Account not found";
    public static String RESULT_MESSAGE_E8897 = "Account is inactive / blocked / closed";
    public static String RESULT_MESSAGE_E8896 = "Debit is not allowed";
    public static String RESULT_MESSAGE_E8895 = "Credit is not allowed";
    public static String RESULT_MESSAGE_E8894 = "User declined";
    public static String RESULT_MESSAGE_E8893 = "Time expired";
    public static String RESULT_MESSAGE_E8892 = "Attempts to retry exceeded maximum";
    public static String RESULT_MESSAGE_E8891 = "Settlement account not found";
    public static String RESULT_MESSAGE_E9988 = "Entity is inactive";
    public static String RESULT_MESSAGE_E9989 = "Insufficient virtual account balance";
}
