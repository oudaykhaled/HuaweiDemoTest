package com.ouday.huawei

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.huawei.hms.auth.api.signin.HuaweiIdSignIn
import com.huawei.hms.auth.api.signin.HuaweiIdSignInClient
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.iap.ConsumePurchaseReq
import com.huawei.hms.support.api.entity.iap.GetBuyIntentReq
import com.huawei.hms.support.api.entity.iap.OrderStatusCode
import com.huawei.hms.support.api.entity.iap.SkuDetailReq
import com.huawei.hms.support.api.hwid.HuaweiIdSignInOptions
import com.huawei.hms.support.api.iap.json.Iap
import com.huawei.hms.support.api.iap.json.IapApiException
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.security.SecureRandom
import java.util.*
import com.ouday.huawei.common.*
import com.ouday.huawei.profile.presentation.ui.ProfileActivity


const val REQUEST_SIGN_IN_LOGIN_CODE = 8888
const val REQ_CODE_BUY = 777

class MainActivity : AppCompatActivity() {

    private var signInOptions: HuaweiIdSignInOptions? = null

    val TAG = "MainActivity"
    private var mSignInClient: HuaweiIdSignInClient? = null
    private var mSignInOptions: HuaweiIdSignInOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mSignInOptions = HuaweiIdSignInOptions.Builder(
            HuaweiIdSignInOptions.DEFAULT_SIGN_IN
        ).requestAccessToken().requestIdToken("").build()

        mSignInClient = HuaweiIdSignIn.getClient(this@MainActivity, mSignInOptions)


        btn_idTokenSignIn.setOnClickListener { idTokenSignIn() }
        btn_AuthSingIn.setOnClickListener { authSignIn() }
        btn_Logout.setOnClickListener { hwLogout() }
        btnListItems.setOnClickListener { initProduct() }
        btnBuy.setOnClickListener {
            getBuyIntent(this, "ConsumeProduct1001", 1)
        }

        btnStart.setOnClickListener {
            var intent = Intent(MainActivity@this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN) {
            //login success
            //get user message by getSignedInAccountFromIntent
            val signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data)
            if (signInHuaweiIdTask.isSuccessful) {
                val huaweiAccount = signInHuaweiIdTask.result
                Log.i(TAG, "signIn success " + huaweiAccount.displayName)
                Log.e(TAG, "\n AccessToken:" + huaweiAccount.accessToken)
                var testviewstring = "signIn success " + huaweiAccount.displayName
                testviewstring = testviewstring + "\n AccessToken:" + huaweiAccount.accessToken
                testviewstring =
                    testviewstring + "\n Display Photo url: " + huaweiAccount.photoUriString

                showInfoView.text = testviewstring
            } else {
                Log.i(
                    TAG,
                    "signIn failed: " + (signInHuaweiIdTask.exception as ApiException).statusCode
                )
                showInfoView.setText("signIn failed: " + (signInHuaweiIdTask.exception as ApiException).statusCode)
            }
        }
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            val signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data)
            if (signInHuaweiIdTask.isSuccessful) {
                val huaweiAccount = signInHuaweiIdTask.result
                Log.i(TAG, "signIn get code success.")
                Log.e(TAG, "\n ServerAuthCode:" + huaweiAccount.serverAuthCode)
                var testviewstring = "signIn success " + huaweiAccount.displayName
                testviewstring =
                    testviewstring + "\n ServerAuthCode:" + huaweiAccount.serverAuthCode
                testviewstring =
                    testviewstring + "\n Display Photo url: " + huaweiAccount.photoUriString

                showInfoView.text = testviewstring
                val entropySource = SecureRandom()
                val randomBytes = ByteArray(64)
                entropySource.nextBytes(randomBytes)
                Log.e(
                    TAG,
                    "verifier:" + Base64.encodeToString(
                        randomBytes,
                        Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
                    )
                )


            } else {
                Log.i(
                    TAG,
                    "signIn get code failed: " + (signInHuaweiIdTask.exception as ApiException).statusCode
                )
                showInfoView.text = "signIn get code failed: " + (signInHuaweiIdTask.exception as ApiException).statusCode
            }
        } else if (requestCode === REQ_CODE_BUY) {
            if (data != null) {
                val buyResultInfo = Iap.getIapClient(this).getBuyResultInfoFromIntent(data)
                if (buyResultInfo.returnCode == OrderStatusCode.ORDER_STATE_SUCCESS) {
                    // verify signature of payment results
                    val success = CipherUtil.doCheck(
                        buyResultInfo.inAppPurchaseData,
                        buyResultInfo.inAppDataSignature,
                        Key.getPublicKey()
                    )
                    if (success) {
                        // call the consumption interface to consume it after successfully delivering the product to your user
                        consumePurchase(
                            this,
                            buyResultInfo.inAppPurchaseData
                        )
                    } else {
                        Toast.makeText(this, "Pay successful,sign failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return
                } else {
                    Toast.makeText(this, "Pay failed", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                Log.i(TAG, "data is null")
            }
            return
        }
    }

    private fun idTokenSignIn() {
        mSignInOptions = HuaweiIdSignInOptions.Builder(
            HuaweiIdSignInOptions.DEFAULT_SIGN_IN
        ).requestAccessToken().requestIdToken("").build()

        mSignInClient = HuaweiIdSignIn.getClient(this@MainActivity, mSignInOptions)
        startActivityForResult(mSignInClient?.signInIntent, Constant.REQUEST_SIGN_IN_LOGIN)
    }

    private fun authSignIn() {
        mSignInOptions = HuaweiIdSignInOptions.Builder(HuaweiIdSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode().build()
        mSignInClient = HuaweiIdSignIn.getClient(this@MainActivity, mSignInOptions)
        startActivityForResult(mSignInClient?.signInIntent, Constant.REQUEST_SIGN_IN_LOGIN_CODE)
    }

    private fun hwLogout() {
        val signOutTask = mSignInClient?.signOut()
        signOutTask?.addOnSuccessListener {
            Log.i(TAG, "sign out Success!")
            showInfoView.text = "sign out Success!"
        }?.addOnFailureListener {
            Log.i(TAG, "sign out fail")
            showInfoView.text = "sign out fail!"
        }

    }

    private fun initProduct() {
        // obtain in-app product details configured in AppGallery Connect, and then show the products
        val iapClient = Iap.getIapClient(this)
        val task = iapClient.getSkuDetail(createSkuDetailReq())
        task.addOnSuccessListener { result ->
            if (result != null && result.skuList.isNotEmpty()) {

                showInfoView.text = Gson().toJson(result.skuList)
            }
        }.addOnFailureListener {
            Toast.makeText(
                this@MainActivity,
                "error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createSkuDetailReq(): SkuDetailReq {
        val skuDetailRequest = SkuDetailReq()
        // In-app product type contains:
        // 0: consumable
        // 1: non-consumable
        // 2: auto-renewable subscription
        skuDetailRequest.priceType = 1
        val skuList = ArrayList<String>()
        // Pass in the productId list of products to be queried.
        // The product ID is the same as that set by a developer when configuring product information in AppGallery Connect.
        skuList.add("ConsumeProduct1001")
        skuDetailRequest.skuIds = skuList
        return skuDetailRequest
    }

    private fun getBuyIntent(activity: Activity, skuId: String, type: Int) {
        Log.d(TAG, "call getBuyIntent")
        val mClient = Iap.getIapClient(activity)
        val task = mClient.getBuyIntent(createGetBuyIntentReq(type, skuId))
        task.addOnSuccessListener { result ->
            Log.d(TAG, "getBuyIntent, onSuccess")
            // you should pull up the page to complete the payment process
            val status = result.status
            try {

                status.startResolutionForResult(activity, REQ_CODE_BUY)
            } catch (exp: IntentSender.SendIntentException) {
                Log.e(TAG, exp.message)
            }
        }.addOnFailureListener { e ->
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.d(TAG, "getBuyIntent, returnCode: $returnCode")
                // handle error scenarios
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }
    }



    private fun createConsumePurchaseReq(purchaseData: String): ConsumePurchaseReq {
        val consumePurchaseRequest = ConsumePurchaseReq()
        var purchaseToken = ""
        try {
            val jsonObject = JSONObject(purchaseData)
            purchaseToken = jsonObject.optString("purchaseToken")
        } catch (e: JSONException) {

        }
        consumePurchaseRequest.purchaseToken = purchaseToken
        return consumePurchaseRequest
    }

    /**
     * Create a GetBuyIntentReq request
     * @param type In-app product type.
     * @param skuId ID of the in-app product to be paid.
     * The in-app product ID is the product ID you set during in-app product configuration in AppGallery Connect.
     * @return GetBuyIntentReq
     */
    private fun createGetBuyIntentReq(type: Int, skuId: String): GetBuyIntentReq {
        val request = GetBuyIntentReq()
        request.productId = skuId
        request.priceType = type
        request.developerPayload = "test"
        return request
    }

    /**
     * consume the unconsumed purchase with type 0
     * @param inAppPurchaseData JSON string that contains purchase order details.
     */
    private fun consumePurchase(context: Context, inAppPurchaseData: String) {
        Log.i(TAG, "call consumePurchase")
        val mClient = Iap.getIapClient(context)
        val task = mClient.consumePurchase(createConsumePurchaseReq(inAppPurchaseData))
        task.addOnSuccessListener {
            // Consume success
            Log.i(TAG, "consumePurchase success")
            Toast.makeText(
                context,
                "Pay success, and the product has been delivered",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.i(TAG, "consumePurchase fail,returnCode: $returnCode")
            } else {
                // Other external errors
                Log.e(TAG, e.message)
                Toast.makeText(
                    context,
                    "External Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }








}


