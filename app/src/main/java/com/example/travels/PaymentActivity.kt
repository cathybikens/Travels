package com.example.travels

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.text.SimpleDateFormat
import org.json.JSONObject
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private val consumerKey = "06MoavpxUqAPSrgc3q5KFcl7ojroctxsWC8VsDlFb9kfzFG8"
    private val consumerSecret = "aLlgl7TuDJdH3n8xnUGNbQ41e2Qy7EAhGGdiAKksviWFzFmJQanG2Z3Vn2oAjzPU"
    private val shortcode = "YOUR_SHORTCODE"  // Replace with your actual shortcode
    private val lipaNaMpesaOnlinePassword = "YOUR_LIPA_NA_MPESA_ONLINE_PASSWORD" // Replace with your password
    private val callbackUrl = "https://example.com/callback" // Replace with your actual callback URL

    private lateinit var destination: String // Declare as a member variable
    private var amount: Double = 0.0 // To hold the amount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        destination = intent.getStringExtra("destination") ?: "Default Destination"
        amount = intent.getIntExtra("price", 0).toDouble() // Get the price as a double

        // Initiate M-Pesa STK push
        initiateSTKPush(amount, "YOUR_PHONE_NUMBER") // Replace with the customer's phone number
    }

    private fun initiateSTKPush(amount: Double, phoneNumber: String) {
        val accessToken = getAccessToken() // Retrieve the access token

        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val password = Base64.encodeToString("$shortcode:$lipaNaMpesaOnlinePassword:$timestamp".toByteArray(), Base64.NO_WRAP)

        val request = STKPushRequest(
            BusinessShortCode = shortcode,
            Password = password,
            Timestamp = timestamp,
            TransactionType = "CustomerPayBillOnline",
            Amount = amount,
            PartyA = phoneNumber, // The customer’s phone number
            PartyB = shortcode,
            PhoneNumber = phoneNumber,
            CallBackURL = callbackUrl, // Your callback URL
            AccountReference = "YourAccountReference", // Reference for the transaction
            TransactionDesc = "Payment for $destination" // This will now work correctly
        )

        // Create Retrofit instance and call the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sandbox.safaricom.co.ke/") // Use sandbox for testing
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(M_PesaApi::class.java)
        api.initiateSTKPush(request).enqueue(object : Callback<STKPushResponse> {
            override fun onResponse(call: Call<STKPushResponse>, response: retrofit2.Response<STKPushResponse>) {
                if (response.isSuccessful) {
                    // Handle success response
                    Log.d("M-Pesa", "STK Push initiated: ${response.body()}")
                    // Assuming payment is successful, navigate to SuccessActivity
                    val intent = Intent(this@PaymentActivity, SuccessActivity::class.java)
                    intent.putExtra("ticketNumber", generateTicketNumber())
                    startActivity(intent)
                    finish()
                } else {
                    // Handle error response
                    Log.e("M-Pesa", "Error initiating STK Push: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<STKPushResponse>, t: Throwable) {
                Log.e("M-Pesa", "STK Push failed: ${t.message}")
            }
        })
    }

    private fun getAccessToken(): String {
        val client = OkHttpClient()
        val credentials = Base64.encodeToString("$consumerKey:$consumerSecret".toByteArray(), Base64.NO_WRAP)

        val request = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
            .addHeader("Authorization", "Basic $credentials")
            .build()

        return try {
            val response = client.newCall(request).execute()
            val jsonResponse = response.body?.string() ?: throw Exception("Failed to retrieve access token")

            // Extract access token from JSON response
            val jsonObject = JSONObject(jsonResponse)
            jsonObject.getString("access_token")
        } catch (e: Exception) {
            Log.e("M-Pesa", "Error retrieving access token: ${e.message}")
            throw e // Re-throw exception after logging
        }
    }

    private fun generateTicketNumber(): String {
        return "TICKET-" + System.currentTimeMillis()
    }

    interface M_PesaApi {
        @Headers("Authorization: Bearer {token}", "Content-Type: application/json")
        @POST("mpesa/stkpush/v1/processrequest") // Updated the endpoint
        fun initiateSTKPush(@Body request: STKPushRequest): Call<STKPushResponse>
    }

    data class STKPushRequest(
        val BusinessShortCode: String,
        val Password: String,
        val Timestamp: String,
        val TransactionType: String,
        val Amount: Double,
        val PartyA: String,
        val PartyB: String,
        val PhoneNumber: String,
        val CallBackURL: String,
        val AccountReference: String,
        val TransactionDesc: String
    )

    data class STKPushResponse(
        val MerchantRequestID: String,
        val CheckoutRequestID: String,
        val ResponseCode: String,
        val ResponseDescription: String,
        val CustomerMessage: String
    )
}
