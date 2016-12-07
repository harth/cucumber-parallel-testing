package uk.co.hmtt.cucumber.parallel.exceptions;

public class ParallelException extends RuntimeException {

    public ParallelException(final Throwable throwable) {super(throwable);}

    public ParallelException(final String description) {super(description);}

}
