package one.avdeev.controller

import avdeev.one.beans.PError
import avdeev.one.beans.WordNotFoundError
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import one.avdeev.error.InvalidInput
import one.avdeev.error.NoMatchedWords
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

@ApplicationScoped
class ExceptionHandler {
    @ServerExceptionMapper
    fun mapInvalidInputException(e: InvalidInput): Response {
        val error = PError()
        error.message = e.message
        error.details = e.details

        return Response.status(400)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }

    @ServerExceptionMapper
    fun mapEmptyResultsException(e: NoMatchedWords): Response {
        val error = WordNotFoundError()
        error.message = e.message
        error.noWordDetails = e.details

        return Response.status(404)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}