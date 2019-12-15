package com.ouday.huawei.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.fragment.app.Fragment
import com.huawei.hms.auth.api.signin.HuaweiIdSignIn
import com.huawei.hms.auth.api.signin.HuaweiIdSignInClient
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.iap.ConsumePurchaseReq
import com.huawei.hms.support.api.entity.iap.GetBuyIntentReq
import com.huawei.hms.support.api.entity.iap.OrderStatusCode
import com.huawei.hms.support.api.hwid.HuaweiIdSignInOptions
import com.huawei.hms.support.api.hwid.SignInHuaweiId
import com.huawei.hms.support.api.iap.json.Iap
import com.huawei.hms.support.api.iap.json.IapApiException
import com.ouday.huawei.Constant
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

const val REQ_CODE_BUY = 777
const val TAG = "HuaweiSdkWrapper"

class HuaweiSdkWrapper @Inject constructor(val context: Context){

    private var mSignInClient: HuaweiIdSignInClient? = null
    private var mSignInOptions: HuaweiIdSignInOptions? = null

    var onLoginSuccessfully: ((signInHuaweiId: SignInHuaweiId) -> Unit)? = null
    var onLoginFailed: ((exception: Exception) -> Unit)? = null

    var onLogoutSuccessfully: (() -> Unit)? = null
    var onLogoutFailed: (() -> Unit)? = null

    var onPaymentSuccessfully: (() -> Unit)? = null
    var onPaymentFailed: ((exception: Exception) -> Unit)? = null

    fun init(){
        mSignInOptions = HuaweiIdSignInOptions.Builder(
            HuaweiIdSignInOptions.DEFAULT_SIGN_IN
        ).requestAccessToken().requestIdToken("").build()

        mSignInClient = HuaweiIdSignIn.getClient(context, mSignInOptions)
    }


    fun setOnLoginSuccessfully( onLoginSuccessfully: ((signInHuaweiId: SignInHuaweiId) -> Unit)): HuaweiSdkWrapper{
        this.onLoginSuccessfully = onLoginSuccessfully
        return this
    }

    fun setOnLoginFailed( onLoginFailed: ((exception: Exception) -> Unit)): HuaweiSdkWrapper{
        this.onLoginFailed = onLoginFailed
        return this
    }

    fun setOnLogoutSuccessfully( onLogoutSuccessfully: (() -> Unit)): HuaweiSdkWrapper{
        this.onLogoutSuccessfully = onLogoutSuccessfully
        return this
    }

    fun setOnLogoutFailed( onLogoutFailed: (() -> Unit)): HuaweiSdkWrapper{
        this.onLogoutFailed = onLogoutFailed
        return this
    }

    fun setOnPaymentSuccessfully( onPaymentSuccessfully: (() -> Unit)): HuaweiSdkWrapper{
        this.onPaymentSuccessfully = onPaymentSuccessfully
        return this
    }

    fun setOnPaymentFailed( onPaymentFailed: ((exception: Exception) -> Unit)): HuaweiSdkWrapper{
        this.onPaymentFailed = onPaymentFailed
        return this
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN) {
            onLogin(data)
        }
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN_CODE) {
            onLoginByCode(data)
        } else if (requestCode === REQ_CODE_BUY) {
            onBuy(data)
            return
        }
    }

    private fun onBuy(data: Intent?) {
        if (data != null) {
            val buyResultInfo = Iap.getIapClient(context).getBuyResultInfoFromIntent(data)
            if (buyResultInfo.returnCode == OrderStatusCode.ORDER_STATE_SUCCESS || buyResultInfo.returnCode == OrderStatusCode.ORDER_ITEM_ALREADY_OWNED) {
                onPaymentSuccessfully?.invoke()
            } else {
                onPaymentFailed?.invoke(Exception("Pay failed"))
                return
            }
        } else {
            onPaymentFailed?.invoke(Exception("data is null"))
        }
        return
    }

    private fun onLoginByCode(data: Intent?) {
        val signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data)
        if (signInHuaweiIdTask.isSuccessful) {
            val huaweiAccount = signInHuaweiIdTask.result
            onLoginSuccessfully?.invoke(huaweiAccount)

        } else {
            onLoginFailed?.invoke(signInHuaweiIdTask.exception as ApiException)
        }
    }

    fun onLogin(data: Intent?) {
        val signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data)
        if (signInHuaweiIdTask.isSuccessful) {
            val huaweiAccount = signInHuaweiIdTask.result
            onLoginSuccessfully?.invoke(huaweiAccount)
        } else {
            onLoginFailed?.invoke(signInHuaweiIdTask.exception)
        }
    }
        fun signin(fragment: Fragment) {
        mSignInOptions = HuaweiIdSignInOptions.Builder(
            HuaweiIdSignInOptions.DEFAULT_SIGN_IN
        ).requestAccessToken().requestIdToken("").build()

        mSignInClient = HuaweiIdSignIn.getClient(context, mSignInOptions)
        fragment.startActivityForResult(mSignInClient?.signInIntent, Constant.REQUEST_SIGN_IN_LOGIN)
    }

    private fun authSignIn(fragment: Fragment) {
        mSignInOptions = HuaweiIdSignInOptions.Builder(HuaweiIdSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode().build()
        mSignInClient = HuaweiIdSignIn.getClient(context, mSignInOptions)
        fragment.startActivityForResult(mSignInClient?.signInIntent, Constant.REQUEST_SIGN_IN_LOGIN_CODE)
    }

    private fun logout() {
        val signOutTask = mSignInClient?.signOut()
        signOutTask?.addOnSuccessListener {
            onLogoutSuccessfully?.invoke()
        }?.addOnFailureListener {
            onLogoutFailed?.invoke()
        }

    }

    fun buy(activity: Activity, skuId: String, type: Int) {
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
        }.addOnFailureListener { e ->
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.i(TAG, "consumePurchase fail,returnCode: $returnCode")
            } else {
                // Other external errors
                Log.e(TAG, e.message)

            }
        }
    }

}