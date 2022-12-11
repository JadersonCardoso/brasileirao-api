package br.com.brasileirao.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {

    private Long timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public StandardError(HttpStatus httpStatus, String message, String path) {
        super();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.timestamp = new Date().getTime();
        this.message = message;
        this.path = path;
    }

}
