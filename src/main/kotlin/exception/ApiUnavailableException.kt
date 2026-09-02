package exception

class ApiUnavailableException(
    message: String="The API is unavailable"
):ApiException(message)