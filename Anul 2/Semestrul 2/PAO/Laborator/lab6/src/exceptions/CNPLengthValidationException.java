package exceptions;

public class CNPLengthValidationException extends CNPValidationException{

    private Integer length;

    public CNPLengthValidationException(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }
}
