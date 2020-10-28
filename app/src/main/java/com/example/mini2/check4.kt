package com.example.mini2

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity


class check4 : AppCompatActivity() {
    lateinit var webv: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check4)
        webv = findViewById(R.id.webv)
        webv.loadUrl("https://forms.gle/HDeXd3f4iTjNuGnn9")
        webv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val settings: WebSettings = webv.getSettings()
                settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
                settings.mediaPlaybackRequiresUserGesture=true
                webv.setWebChromeClient(object : WebChromeClient() {
                    override fun onPermissionRequest(request: PermissionRequest) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            request.grant(request.resources)
                        }
                    }
                })

                /*    view?.loadUrl(url)


*/
                webv.settings.javaScriptEnabled
                return true
            }
        }
    }
}