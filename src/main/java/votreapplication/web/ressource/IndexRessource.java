package votreapplication.web.ressource;

import fr.arpinum.graine.web.restlet.BaseResource;
import org.restlet.resource.Get;

public class IndexRessource extends BaseResource {

    @Get
    public String représente() {
        return "Hello world";
    }
}
