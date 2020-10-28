package com.unimech.UVIOT


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.unimech.UVIOT.QrActivity.Companion.Url


class MainActivity : AppCompatActivity() {
    lateinit var mWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = intent.getStringExtra("url").toString()
        supportActionBar?.hide()
        mWebView=findViewById(R.id.webView)
        mWebView.loadUrl(Url)
        //mWebView.loadUrl("http://192.168.4.1")
        val webSetting=mWebView.settings
        webSetting.javaScriptEnabled = true
        mWebView.webViewClient= WebViewClient()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val mGenericWebClient = GenericWebClient()
        mWebView.webViewClient = mGenericWebClient

    }
    override fun onBackPressed(){
        if(mWebView.canGoBack())
        {
            mWebView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }

    class GenericWebClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            val statusCode = errorResponse?.statusCode.toString()
            Toast.makeText(view?.context,"The error is : $statusCode",Toast.LENGTH_SHORT).show()
            if(statusCode == "404"){
                Toast.makeText(view?.context,"The 404 error is found hence going back",Toast.LENGTH_SHORT).show()
                view?.goBack()

            }
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            val errorName = description.toString()
            Log.d("TAG", errorName)
            if(errorName == "net::ERR_NAME_NOT_RESOLVED"){
                Toast.makeText(view?.context,"The error is : $errorName, please check your connection!.",Toast.LENGTH_SHORT).show()
            }
        }


    }


}