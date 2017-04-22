package com.mobilprog.tadam.mobilprog;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by thoma on 2017. 04. 22..
 */

public class AsyncCallWS extends AsyncTask<Void, Void, Void> {

    private final String TAG = "Response";
    private SoapPrimitive resultString;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "onPreExecute()");
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(TAG, "doInBackground()");
        calculate();
        return null;
    }

    private void calculate() {
        String SOAP_ACTION = "http://tempuri.org/ITyprXService/PartnersList";
        String METHOD_NAME = "PartnersList";
        String NAMESPACE = "http://tempuri.org/ITyprXService/";
        String URL = "http://typr.cloudapp.net:8080/Typrserver";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("UserId" ,"a4525d29e7d7486997511948b9b5a4d4");


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            Log.i(TAG, "Result Celsius: " + resultString);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i(TAG, "onPostExecute()");
    }
}