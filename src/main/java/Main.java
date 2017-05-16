import com.spotify.apollo.Environment;
import com.spotify.apollo.core.Service;
import com.spotify.apollo.httpservice.HttpService;
import yourapp.YourBoundedContext;

public class Main {

    public static void main(String[] args) throws Exception {

        Service app = HttpService.usingAppInit(Main::init, "app")
                .build();
        HttpService.boot(app, args);
    }

    static void init(Environment environment) {
        YourBoundedContext.start(environment);
    }
}
