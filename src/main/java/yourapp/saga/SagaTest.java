package yourapp.saga;

import arpinum.saga.Saga;
import org.hibernate.validator.constraints.NotEmpty;


public class SagaTest implements Saga<String> {

    @NotEmpty
    public String name;
}
