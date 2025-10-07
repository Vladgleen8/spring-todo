package org.todo.todo.exceptions;

public class InvalidInputException extends RuntimeException  {
    public InvalidInputException(String message) {
        super(message);
    }
}