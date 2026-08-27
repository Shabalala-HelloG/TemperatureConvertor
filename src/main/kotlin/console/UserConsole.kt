package org.example.console

import jakarta.xml.soap.MessageFactory
import jakarta.xml.soap.MimeHeaders
import jakarta.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL
import jakarta.xml.soap.SOAPMessage
import okhttp3.Response
import org.example.api.GetResponseService
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess


class UserConsole{

    fun consolePlay(){

        while(true){
            println("1.Fahrenheit to Celsius\n2.Celcius to Fahrenheit\n3.Quit")
            val userInput = readlnOrNull()
            val tempValue: Int?

            if (userInput == null) {
                println("No input found")
            }else if(userInput == "1"){
                println("Enter your Temperature:")
                tempValue = readlnOrNull()?.toIntOrNull()
                //do f to c by doing a call up
                if (tempValue == null)println("No temperature found")else callUp(true,userInput.toInt(), tempValue = tempValue)

            }else if(userInput == "2"){
                //do c to f
                println("Enter your Temperature:")
                tempValue = readlnOrNull()?.toIntOrNull()
                if (tempValue == null)println("No temperature found")else callUp(false,userInput.toInt(), tempValue = tempValue)
            }else if(userInput == "3" || userInput.lowercase() == "quit"){
                exitProcess(0)
            }else{
                println("The input is invalid")
            }
        }
    }

    /**
     * Normal I would create a separate folder for my callUPs functions but my IDE was doing weird this.
     * I will put everything here
     */

    fun stringToSoapMessage(responseString: String?): SOAPMessage {
        /**
         * this reading had a way to convert string to SoapMessage
         * link:https://www.javacodegeeks.com/how-to-convert-a-string-to-soapmessage-in-java.html
         */
        val messageFactory =MessageFactory.newInstance(SOAP_1_1_PROTOCOL)
        val  inputStream = ByteArrayInputStream(responseString?.toByteArray(StandardCharsets.UTF_8) ?: ByteArray(0))
        return messageFactory.createMessage(MimeHeaders(),inputStream)
    }

    fun getValueFromSoapMessage(message: SOAPMessage,decider: Boolean): String {
        /**
         * link to where I got this functions:https://www.baeldung.com/java-soap-msg-specific-part
         */

        val value: String
        val soapEnv = message.soapPart.envelope
        val soapBody= soapEnv.body
        val elements: NodeList
        if(decider) {

            //true is for fahrenheitToCelsiusResult
            elements = soapBody.getElementsByTagName("FahrenheitToCelsiusResult")
        }else{
            // false is for CelsiusToFahrenheitResult
            elements = soapBody.getElementsByTagName("CelsiusToFahrenheitResult")
        }

        value = elements.item(0).textContent
        return value
    }

    fun callUp(boolValue: Boolean,userInput:Int,tempValue: Int) {
        val respondFromSoap: Response =GetResponseService().getResponse(boolValue, tempValue )
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
                val soapmessage: SOAPMessage = stringToSoapMessage(resString)
                val results: String

                // This fetches the results from the SoapMessage
                results = if (userInput == 1) getValueFromSoapMessage(soapmessage, true) else getValueFromSoapMessage(soapmessage, false)

                if(userInput==1)println("New Temperature in Celsius: $results\n") else println("New Temperature in Fahrenheit: $results\n")


            }
        } else {
            println("bool:${respondFromSoap.isSuccessful} code:${respondFromSoap.code}\n${respondFromSoap.headers}")
        }
    }
}