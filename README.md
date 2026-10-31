
# Fascicolo Sanitario 2.0

# _it-fse-srv-semantic-rules-manager_

Il microservizio in oggetto espone API che consentono la creazione, lettura, aggiornamento ed eliminazione degli artefatti per i controlli semantici utilizzati dal Gateway in fase di validazione. Il microservizio, inoltre, espone API necessarie al Gateway per effettuare l'allineamento dei nuovi artefatti.

<br/>

## Repos
- [*it-fse-support*](https://github.com/ministero-salute/it-fse-support) - Pagina di supporto FSE 2.0
- [*it-fse-landing*](https://github.com/ministero-salute/it-fse-landing) - Landing page dei repository FSE 2.0

<br/>

## Isolation tests

With Java 21 and Maven, run the opt-in suite without MongoDB or other services:

```sh
mvn -PISOLATION clean test
```

The profile selects JUnit tests tagged `ISOLATION`, which ordinary Maven test
runs exclude. External dependencies are mocked and Spring contexts are limited
to the components under test. JaCoCo reports are generated in `target/site/jacoco/`.

## License

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
