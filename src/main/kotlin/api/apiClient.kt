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

class GetResponseService{


    private val client = OkHttpClient.Builder().build()

    val mediaType: MediaType = "text/xml; charset=utf-8".toMediaType()

    //these are my parameter
    val endPoint="https://www.w3schools.com/xml/tempconvert.asmx"


    val soapActionURlForC2F="https://www.w3schools.com/xml/CelsiusToFahrenheit"
    val soapActionURlForF2C="https://www.w3schools.com/xml/FahrenheitToCelsius"




    fun getResponse(chooser: Boolean, input:Int): Response {
        var envelopeValue: SOAPMessage
        var soapActionURl:String
        val outputStream = ByteArrayOutputStream()


        if(chooser) {
            //true for F to C
            envelopeValue=SoapMessageBuilder().buildFahrenheitToCelsiusRequestMessage(input.toString())
            envelopeValue.writeTo(outputStream)
            soapActionURl=soapActionURlForF2C
        } else {
            // false for C to F
            envelopeValue= SoapMessageBuilder().buildCelsiusToFahrenheitRequestMessage(input.toString())
            envelopeValue.writeTo(outputStream)
            soapActionURl=soapActionURlForC2F
        }


        val envelopeBytes = outputStream.toByteArray()

        val requestBody =
            envelopeBytes.toRequestBody(mediaType)

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