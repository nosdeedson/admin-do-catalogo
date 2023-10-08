package e3n.com.admin.catalogo.application;
import io.vavr.control.Either;
public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);
}