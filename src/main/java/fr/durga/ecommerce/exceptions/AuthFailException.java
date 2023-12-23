package fr.durga.ecommerce.exceptions;

public class AuthFailException extends IllegalArgumentException {
    public AuthFailException(String msg) {
        super(msg);
    }
}
