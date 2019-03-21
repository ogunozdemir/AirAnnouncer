package com.akyasis.havaspikeri.havaspikeri;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class WebServices {


    private static final String TAG = "_WebServices";

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://kou.akyasis.com/HavaDurumu.asmx";
    private static final String METHOD_NAME = "UyariBul";
    private static final String SOAP_ACTION = "http://tempuri.org/UyariBul";

    public String uyariOku(int derece){

        try {
            String veri = "UYARI OKUNAMADI !";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sicaklik", derece);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

            Object response = null;

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                response = envelope.getResponse();
                veri = response.toString();
                Log.i(TAG, " :: " + response.toString());
            } catch (Exception ex) {

                Log.w(TAG, "ERR : " + ex.toString());
            }


            Log.i(TAG, " :: " + response.toString());
            return veri;
        }
        catch (Exception e)
        {
            return "Hata";
        }
    }
}