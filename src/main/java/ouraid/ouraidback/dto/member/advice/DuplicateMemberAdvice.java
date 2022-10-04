package ouraid.ouraidback.dto.member.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ouraid.ouraidback.Exception.DuplicateMemberException;

@ControllerAdvice
public class DuplicateMemberAdvice {

    @ResponseBody // signal that this advice is rendered straight into the response
    @ExceptionHandler(DuplicateMemberException.class) // config advice to only respond if an
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED) // exception is thrown : 417 code
    String duplicateMemberAdvice(DuplicateMemberException ex) {
        return ex.getMessage();
    }
}
