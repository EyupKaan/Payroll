package org.eyupkaan.restful.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id){
        super("Colud not find order " + id);
    }
}
