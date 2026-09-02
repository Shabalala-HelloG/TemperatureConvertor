package org.example.console

import exception.ApiResponseException
import exception.ApiUnavailableException
import org.example.service.TempConvertorService
import kotlin.system.exitProcess


class UserConsole{
    val callUp= TempConvertorService()

    fun consolePlay(){

        while(true){
            println("1.Fahrenheit to Celsius\n2.Celcius to Fahrenheit\n3.Quit")
            val userInput = readlnOrNull()
            val tempValue: Int?

          try {


              when {
                  userInput == null -> {
                      println("No input found")
                  }

                  userInput == "1" -> {
                      println("Enter your Temperature:")
                      tempValue = readlnOrNull()?.toIntOrNull()
                      //do f to c by doing a call up
                      if (tempValue == null) println("No temperature found") else callUp.getTemp(
                          true,
                          userInput.toInt(),
                          tempValue = tempValue
                      )

                  }

                  userInput == "2" -> {
                      //do c to f
                      println("Enter your Temperature:")
                      tempValue = readlnOrNull()?.toIntOrNull()
                      if (tempValue == null) println("No temperature found") else callUp.getTemp(
                          false,
                          userInput.toInt(),
                          tempValue = tempValue
                      )
                  }

                  userInput == "3" || userInput.lowercase() == "quit" -> {
                      exitProcess(0)
                  }

                  else -> {
                      println("The input is invalid")
                  }
              }
          }catch (e: ApiUnavailableException) {
              println(e.message)

          } catch (e: ApiResponseException) {

              when (e.statusCode) {
                  //for now, I only cater for the following
                  401 -> println("${e.statusCode}:Authentication failed.")
                  404 -> println("${e.statusCode}:The requested resource was not found.")
                  else -> println("The API returned an error: ${e.statusCode}")
              }
          }
        }
    }
}