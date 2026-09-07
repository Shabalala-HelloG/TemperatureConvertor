package org.example.api

import envelope.SoapMessageBuilder
import jakarta.xml.soap.SOAPMessage
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream

class SoapApiClient{


    private val client = OkHttpClient.Builder().build()

    private val mediaType: MediaType = "text/xml; charset=utf-8".toMediaType()

    //these are my parameter
    private val endPoint="https://www.w3schools.com/xml/tempconvert.asmx"


    private val soapActionURlForC2F="https://www.w3schools.com/xml/CelsiusToFahrenheit"
    private val soapActionURlForF2C="https://www.w3schools.com/xml/FahrenheitToCelsius"




    fun getResponse(chooser: Boolean, input:Int): Response {
        var envelopeValue: SOAPMessage
        var soapActionURl:String

        /**
         * The aim here is convert my SOAPMessage into a string so I can, convert again to a RequestBody
         *
         * SOAPMessage -> String -> RequestBody
         * Since I used a MEssageFactory to create my SOAPMessage this webpage helped me with the essential code to do so
         * link:https://www.javathinking.com/blog/convert-soap-response-to-string-in-java/
         */
        val outputStream = ByteArrayOutputStream()


        when {
            chooser -> {
                //true for F to C
                envelopeValue = SoapMessageBuilder().buildFahrenheitToCelsiusRequestMessage(input.toString())
                envelopeValue.writeTo(outputStream)
                soapActionURl = soapActionURlForF2C
            }
            else -> {
                // false for C to F
                envelopeValue = SoapMessageBuilder().buildCelsiusToFahrenheitRequestMessage(input.toString())
                envelopeValue.writeTo(outputStream)
                soapActionURl = soapActionURlForC2F
            }
        }


        val envelopeString = outputStream.toString("UTF-8")

        val requestBody =
            envelopeString.toRequestBody(mediaType)

        /**
         * The whole concept of passing in my SOAPMessage in my requestBody comes from this reading, provided my Tumishang
         *
         * link:https://telestreamcommunications-my.sharepoint.com/:u:/g/personal/tumishang_mauoane_hellogroup_co_za/IQBl57JplJY3RpyrN4-5ue26AS87mKqdcdDZ0qi49kRR9Fs?e=hdc9Wi
         */

        val request : Request= Request.Builder()
            .url(endPoint)
            .post(requestBody)
            .addHeader("Content-Type","text/xml; charset=utf-8")
            .addHeader("SOAPAction", "\"$soapActionURl\"")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36")
            .build()
        val response = client.newCall(request).execute()
        return response
    }
}