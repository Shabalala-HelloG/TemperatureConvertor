package envelope

import jakarta.xml.soap.*
import jakarta.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL




class SoapMessageBuilder{

    /**
     * The class I will use to create my soap message
     *
     * The java-wire-deck.html prescribed to us contains an example of how to create a soap message & the "6 Working with SOAP Messages" webpage
     * link: https://eclipse-ee4j.github.io/openmq/guides/mq-dev-guide-java/soap-messages.html
     */
    private val messageFactory =MessageFactory.newInstance(SOAP_1_1_PROTOCOL)
    private val soapMessage: SOAPMessage = messageFactory.createMessage()
    private val soapEnvelope: SOAPEnvelope= soapMessage.soapPart.envelope
    private val soapBody: SOAPBody = soapEnvelope.body

    // the header element is not there so I will remove it in my build
    private val soapHeader: SOAPHeader = soapEnvelope.header





    fun buildCelsiusToFahrenheitRequestMessage(input: String): SOAPMessage{
        /**
         * <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
         *   <soap:Body>
         *     <CelsiusToFahrenheit xmlns="https://www.w3schools.com/xml/">
         *       <Celsius>string</Celsius>
         *     </CelsiusToFahrenheit>
         *   </soap:Body>
         * </soap:Envelope>
         */
        // this is to explicitly change the prefix from SOAP-ENV to soap and also removing the header
        soapEnvelope.prefix = "soap"
        soapBody.prefix = soapEnvelope.prefix
        soapHeader.detachNode()

        //here I create the CelsiusToFahrenheit and adding it to the body of the envelope
        val celsiusToFahrenheit = soapEnvelope.createName("CelsiusToFahrenheit","","https://www.w3schools.com/xml/")
        val  celsiusToFahrenheitElement: SOAPBodyElement= soapBody.addBodyElement(celsiusToFahrenheit)

        //here I create the Celsius element and adding it under celsiusToFahrenheit Element
        val celsius = soapEnvelope.createName("Celsius","","https://www.w3schools.com/xml/")
        val celsiusElement: SOAPElement= celsiusToFahrenheitElement.addChildElement(celsius)

        celsiusElement.addTextNode(input)

        soapMessage.saveChanges()
        return soapMessage
    }

    fun buildFahrenheitToCelsiusRequestMessage(input: String): SOAPMessage {
        /**
         * <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
         *   <soap:Body>
         *     <FahrenheitToCelsius xmlns="https://www.w3schools.com/xml/">
         *       <Fahrenheit>string</Fahrenheit>
         *     </FahrenheitToCelsius>
         *   </soap:Body>
         * </soap:Envelope
         */


        // this is to explicitly change the prefix from SOAP-ENV to soap and also removing the header
        soapEnvelope.prefix = "soap"
        soapBody.prefix = soapEnvelope.prefix
        soapHeader.detachNode()

        //here I create the CelsiusToFahrenheit and adding it to the body of the envelope
        val fahrenheitToCelsius = soapEnvelope.createName("FahrenheitToCelsius","","https://www.w3schools.com/xml/")
        val  fahrenheitToCelsiusElement: SOAPBodyElement= soapBody.addBodyElement(fahrenheitToCelsius)

        //here I create the Celsius element and adding it under celsiusToFahrenheit Element
        val fahrenheit = soapEnvelope.createName("Fahrenheit","","https://www.w3schools.com/xml/")
        val fahrenheitElement: SOAPElement= fahrenheitToCelsiusElement.addChildElement(fahrenheit)

        fahrenheitElement.addTextNode(input)

        soapMessage.saveChanges()
        return soapMessage


    }



}