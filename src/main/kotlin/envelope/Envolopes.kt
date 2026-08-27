package org.example.envelope

/**
 * the site showed me how to create an envelope
 * link:https://gist.github.com/clonekim/ba4dbaea6a5a8f3a7c296b90ebcd3a13#:~:text=val%20postBody%20%3D,%22%22%22.trimIndent()
 *
 * Even though I don't use it I keep it for reference
 *
 */

class Envolopes {

    fun createCelsiusToFahrenheitEnvolope(input:String):String{
        val CelsiusToFahrenheitEnvolope ="""
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <CelsiusToFahrenheit xmlns="https://www.w3schools.com/xml/">
                  <Celsius>$input</Celsius>
                </CelsiusToFahrenheit>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        return CelsiusToFahrenheitEnvolope
    }

    fun createFahrenheitToCelsiusEnvolope(input:String):String{
        val FahrenheitToCelsius: String= """
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <FahrenheitToCelsius xmlns="https://www.w3schools.com/xml/">
                  <Fahrenheit>$input</Fahrenheit>
                </FahrenheitToCelsius>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        return FahrenheitToCelsius
    }

}