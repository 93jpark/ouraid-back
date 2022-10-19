package ouraid.ouraidback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    @NonNull private int statusCode;
    private String responseMessage;
    @Builder.Default private Object data = null;

    public static final ResponseEntity<ApiResponse> of(final HttpStatus status, final Object data) {
        return ResponseEntity
                .status(status.value())
                .body(ApiResponse.builder()
                            .statusCode(status.value())
                            .data(data)
                            .build()
                );
    }

    public static final ResponseEntity<ApiResponse> of(final HttpStatus status, final String msg, final Object data) {
        return ResponseEntity
                .status(status.value())
                .body(ApiResponse.builder()
                        .statusCode(status.value())
                        .responseMessage(msg)
                        .data(data)
                        .build()
                );
    }

    public static final ResponseEntity<ApiResponse> of(final HttpStatus status, final String msg) {
        return ResponseEntity
                .status(status.value())
                .body(ApiResponse.builder()
                        .statusCode(status.value())
                        .responseMessage(msg)
                        .data(null)
                        .build()
                );
    }

    public static final ResponseEntity<ApiResponse> of(final HttpStatus status) {
        return ResponseEntity
                .status(status.value())
                .body(ApiResponse.builder()
                        .statusCode(status.value())
                        .data(null)
                        .build()
                );
    }
}
