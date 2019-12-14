package com.ouday.huawei

import org.json.JSONException
import org.json.JSONObject
import com.huawei.hms.support.api.entity.iap.ConsumePurchaseReq



object Constant {
    val IS_LOG = 1
    //login
    val REQUEST_SIGN_IN_LOGIN = 1002
    //Not logged in to pull up Huawei account login page
    val REQ_CODE_NOT_LOGIN = 1003
    //Pull up the Payment Cashier
    val REQ_CODE_GO_PAY = 1004
    //Social Request
    val REQUEST_SEND_SNS_MSG = 1005
    //Start Huawei Message Service Interface
    val REQUEST_GET_UI_INTENT = 1006
    //User Authorization
    val REQUEST_SIGN_IN_AUTH = 1007
    //Pull up the Achievement List page
    val REQUEST_GAME_ACHIEVEMENTINTENT = 1008
    //Request Location Permission
    val REQUEST_LOCATION_PERMISSION = 1009
    //login by code
    val REQUEST_SIGN_IN_LOGIN_CODE = 1010
}



