# admin-do-catalogo
## Migrações do banco de dados com Flyway

## Executar migrations
./gradlew flywayMigrate

## limpar migrations
./gradlew flywayClean

## reparando migration
./gradlew flywayRepair

## comandos uteis
./gradlew flywayInfo
./gradlew flywayValidade

## Para saber todos os comandos disponíveis: [Documentation click here]( https://documentation.red-gate.com/fd/ )

## usar docker compose com profile command
docker compose --profile nameProfile up -d
## onde o nome do profile deve ter sido criado no docker-compose.yml
