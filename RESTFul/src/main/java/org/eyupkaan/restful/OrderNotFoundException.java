package org.eyupkaan.restful;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id){
        super("Colud not find order " + id);
    }
}
