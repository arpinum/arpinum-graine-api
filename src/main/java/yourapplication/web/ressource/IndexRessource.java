package yourapplication.web.ressource;

import fr.arpinum.seed.web.restlet.BaseResource;
import org.restlet.resource.Get;

public class IndexRessource extends BaseResource {

    @Get
    public String représente() {
        return "Hello world";
    }
}
