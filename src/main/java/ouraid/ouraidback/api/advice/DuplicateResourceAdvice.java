package ouraid.ouraidback.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ouraid.ouraidback.Exception.DuplicateResourceException;

@ControllerAdvice
@Slf4j
public class DuplicateResourceAdvice {

    @ResponseBody // signal that this advice is rendered straight into the response
    @ExceptionHandler(DuplicateResourceException.class) // config advice to only respond if an
    @ResponseStatus(HttpStatus.FORBIDDEN) // exception is thrown : 417 code
    String duplicateMemberAdvice(DuplicateResourceException ex) {
        return ex.getMessage();
    }
}
