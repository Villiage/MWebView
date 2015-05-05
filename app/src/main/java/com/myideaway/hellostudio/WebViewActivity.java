package com.myideaway.hellostudio;


import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;


import android.webkit.WebChromeClient;
import android.webkit.WebView;

import android.widget.Button;
import android.widget.Toast;


public class WebViewActivity extends ActionBarActivity {
    WebView webView;
    Context context;
    Button helloBt, alertBt, chatBt;
    Briage briage;
    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            helloBt.setVisibility(msg.what);

        }

        ;

    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        context = this;


        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavaScriptInterface(), "ncp");
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("file:///android_asset/index.html");

        helloBt = (Button) findViewById(R.id.hello);

        helloBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = helloBt.getText().toString();
                if (text.equals("hide")) {
                    helloBt.setText("show");
                    webView.loadUrl("javascript:hide()");
                }
                if (text.equals("show")) {
                    helloBt.setText("hide");
                    webView.loadUrl("javascript:show()");

                }
            }
        });
        alertBt = (Button) findViewById(R.id.alert);
        alertBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                webView.loadUrl("javascript:tip('i am a alert msg')");
            }
        });

        chatBt = (Button) findViewById(R.id.chat);
        chatBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:chat('nice to meet you!')");
            }
        });
        setBriage(new Briage() {
            @Override
            public void onResponse(String resp) {
                Toast.makeText(WebViewActivity.this, resp, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void show() {
            handler.sendEmptyMessage(View.VISIBLE);
        }

        @JavascriptInterface
        public void hide() {

            handler.sendEmptyMessage(View.GONE);
        }

        @JavascriptInterface
        public void toast(String msg) {
            Toast.makeText(WebViewActivity.this, msg, Toast.LENGTH_SHORT).show();

        }

        @JavascriptInterface
        public void onResponse(String resp) {
            briage.onResponse(resp);
        }

        ;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface Briage {

        void onResponse(String resp);

    }

    public void setBriage(Briage briage) {
        this.briage = briage;
    }
}
