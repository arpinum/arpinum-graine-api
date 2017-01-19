package arpinum.infrastructure.bus;

public class ExecutionResult<TReponse> {

    public static <TReponse> ExecutionResult<TReponse> success(TReponse reponse) {
        return new ExecutionResult<>(reponse, true);
    }

    public static <TReponse> ExecutionResult<TReponse> error(Throwable erreur) {
        return new ExecutionResult<>(erreur);
    }


    private ExecutionResult(TReponse reponse, boolean success) {
        this.reponse = reponse;
        this.success = success;
        error = null;
    }

    public ExecutionResult(Throwable error) {
        this.error = error;
        success = false;
        reponse = null;
    }

    public TReponse data() {
        return reponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return !success;
    }

    public Throwable error() {
        return error;
    }

    private final TReponse reponse;
    private final boolean success;
    private final Throwable error;
}
