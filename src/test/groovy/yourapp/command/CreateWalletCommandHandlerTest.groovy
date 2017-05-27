package yourapp.command

import org.junit.Rule
import spock.lang.Specification
import yourapp.infrastructure.repository.WithRepositories
import yourapp.model.Repositories


class CreateWalletCommandHandlerTest extends Specification {

    @Rule
    WithRepositories repositories = new WithRepositories()

    def "creates wallet and returns UUID"() {
        given:
        def command = new CreateWalletCommand(name: "name")

        when:
        def result = new CreateWalletCommandHandler().execute(command)

        then:
        result != null
        Repositories.wallets().get(result) != null
    }
}
