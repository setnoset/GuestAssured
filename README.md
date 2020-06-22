# GuestAssured

### Configuração

Rode um servidor PostgreSQL e crie o banco de dados 'hotel'
nele. Coloque o URL JBDC do servidor em
application.properties e crie as tabelas de acordo com o
esquema ditado em dbinit.sql.

Para compilar o codigo, com o script da Spring, use
```
./mvnw clean package
```

Assim pode rodar com 

```
java -jar target/guestassured-0.0.1-SNAPSHOT.jar
```

### API

Com o servidor rodando em http://localhost:8080/, 
tente criar um novo hospede com POST /guests, enviando o json:

```
{
    "name": "Fulano Teste 1",
    "document": "297884740",
    "phone": "5541948226148"
}
```

Após concluido, poderá alterar dados com PATCH /guests/id,
usando o id adquerido. Poderá consultar o hóspede com
GET /guests/id. Para deletar um hóspede, basta usar
DELETE /guests/id.

Faça o check-in do hospede com POST /checkin:

```
{
    "guestIdentifierType" : "name",
    "guestIdentifier" : "Fulano Teste 1",
    "date_in": "2020-03-17T08:00:00",
    "date_out": "2020-03-20T10:17:00",
    "parking": true
}
```

Se tentar fazer um outro check-in com datas inconsistentes e
com o mesmo hóspede, não vai funcionar. Vamos desta vez
ultilizar POST /guests/id para o checkin:

```
{
    "date_in": "2020-03-19T08:00:00",
    "date_out": "2020-03-21T10:17:00",
    "parking": true
}
```

Agora crie um novo hóspede e faça um GET /guests.
Os dois hóspedes aparecerão. Usando

```
GET /guests?inhotel=yes&pageid=k
```

pegará uma página (numero k) de 10 resultados em que
os hóspedes estão no hotel atualmente. Com

```
GET /guests?inhotel=no&pageid=k
```

terá a página com hóspedes que não estão no hotel.

Qualquer outro parâmetro colocado em inhotel resulta
em uma busca geral, apresentando resultados independente
de o hóspede estar no hotel ou não.