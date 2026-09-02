package org.example.service

import exception.ApiResponseException
import exception.ApiUnavailableException
import jakarta.xml.soap.MessageFactory
import jakarta.xml.soap.MimeHeaders
import jakarta.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL
import jakarta.xml.soap.SOAPMessage
import okhttp3.Response
import org.example.api.GetResponseService
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class TempConvertorService {

    fun getTemp(boolValue: Boolean,userInput:Int,tempValue: Int) {
        try {
            val respondFromSoap: Response = GetResponseService().getResponse(boolValue, tempValue)
            val responseBody = respondFromSoap.body
            if (respondFromSoap.isSuccessful) {


                /**
                 * when I print by response body I get something like:okhttp3.internal.http.RealResponseBody@16610890
                 * this is a memory address
                 * even when I used println(responseBody.toString()) it al
                 */
                if (responseBody == null) {
                    println("The response body is empty")
                } else {
                    /**
                     * I did a search on how to print what's in the memory address of when the response body brings back an address
                     * and there only way us to convert
                     */
                    val resString: String = responseBody.bytes().toString(Charsets.UTF_8)
                    val soapMessage: SOAPMessage = stringToSoapMessage(resString)

                    // This fetches the results from the SoapMessage
                    val results: String =
                        (if (userInput == 1) getValueFromSoapMessage(soapMessage, true) else getValueFromSoapMessage(
                            soapMessage,
                            false
                        )).toString()

                    if (userInput == 1) println("New Temperature in Celsius: $results\n") else println("New Temperature in Fahrenheit: $results\n")


                }
            } else {
                throw ApiResponseException(respondFromSoap.code)
            }
        }catch (_: IOException){
            throw ApiUnavailableException("Unable to connect to Temperature Convertor API")
        }
    }

    private fun stringToSoapMessage(responseString: String?): SOAPMessage {
        /**
         * this reading had a way to convert string to SoapMessage
         * link:https://www.javacodegeeks.com/how-to-convert-a-string-to-soapmessage-in-java.html
         */
        val messageFactory =MessageFactory.newInstance(SOAP_1_1_PROTOCOL)
        val  inputStream = ByteArrayInputStream(responseString?.toByteArray(StandardCharsets.UTF_8) ?: ByteArray(0))
        return messageFactory.createMessage(MimeHeaders(),inputStream)
    }

    private fun getValueFromSoapMessage(message: SOAPMessage,decider: Boolean): String? {
        /**
         * link to where I got this functions:https://www.baeldung.com/java-soap-msg-specific-part
         */

        var element: NodeList
        if(decider) {

            //true is for fahrenheitToCelsiusResult
            element = message.soapBody.getElementsByTagName("FahrenheitToCelsiusResult")
        }else{
            // false is for CelsiusToFahrenheitResult
            element = message.soapBody.getElementsByTagName("CelsiusToFahrenheitResult")
        }

        return element.item(0).textContent
    }
}