package io.renren.common.exception;

public class EducationException extends RuntimeException{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EducationException() {
    }

    public EducationException(String message) {
        this.message = message;
    }

    public static void exception(String message){
        throw new EducationException(message);
    }
}
