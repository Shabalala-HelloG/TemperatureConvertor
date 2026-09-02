package exception

class ApiResponseException(
    val statusCode: Int
):ApiException()