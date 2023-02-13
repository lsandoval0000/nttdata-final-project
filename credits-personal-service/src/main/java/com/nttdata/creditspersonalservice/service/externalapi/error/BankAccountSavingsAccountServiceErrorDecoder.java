package com.nttdata.creditspersonalservice.service.externalapi.error;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.creditspersonalservice.controller.exception.BankAccountSavingsAccountServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * The Bank account savings account service error decoder.
 */
@Slf4j
public class BankAccountSavingsAccountServiceErrorDecoder implements ErrorDecoder {
    /**
     * @param methodKey {@link feign.Feign#configKey} of the java method that invoked the request. ex.
     *                  {@code IAM#getUser()}
     * @param response  HTTP response where {@link Response#status() status} is greater than or equal
     *                  to {@code 300}.
     * @return the exception
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        String erroMessage = null;
        Reader reader = null;

        try {
            reader = response.body().asReader(StandardCharsets.UTF_8);
            String result = IOUtils.toString(reader);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ErrorResponse exceptionMessage = mapper.readValue(result,
                    ErrorResponse.class);

            erroMessage = exceptionMessage.getMessage();

        } catch (IOException e) {
            log.error("IO Exception on reading exception message feign client" + e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("IO Exception on reading exception message feign client" + e);
            }
        }

        return new BankAccountSavingsAccountServiceException(erroMessage);
    }
}
