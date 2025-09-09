# Phishing guard
Aplikacja symulująca obsługę subkrypcji usługi i detekcji ryzyka w wiadomościach sms.
Wykorzystane technologie:
* Java 21
* Spring Boot 3.5
* WebFlux (wiele operacji I/O - nieblokująco)
* Redis
* PostgreSQL (sterowniki R2DBC)

## Uruchamianie
Najszybszym sposobem uruchomienia aplikacji jest wykorzystanie [docker-compose](docker-compose.yml). Należy pamiętaż, że trzeba dodać plik .env z następującymi zmiennymi:
* DATABASE_URL=postgresql://postgres:5432/phish
* DATABASE_USERNAME=phish
* DATABASE_PASSWORD=phish
* CACHE_HOST=redis
* CACHE_PORT=6379
* GOOGLE_WEBRISK_TOKEN=token

## Architektura
* Modularny monolit - jedna aplikacja podzielona na moduły (subscription i riskdetection).
* Heksagon - wykorzystanie portów i adapterów.
#### Zalety tego podejścia:
* Szybkie wdrżenie
* Wyzanczone granice między modułami - czysta architektura
* Możliwość podmiany technologii - wymiana adapterów
* Możliwość przejścia na mikroserwisy
* Skalowalność horyzontalna

## Flow
### /subscription
#### Założenie: smsy, które tu trafiają, dotyczą jedynie konkretnego numeru odpowiedzialnego za działanie subskrypcji - można by to było zmienić, aby numer, na który trzeba wysłać wiadomość, był parametrem, a jego wartość była w bazie danych - tak samo z kodami
* request trafia do kontrolera
* serwis sprawdza, czy kod w wiadomości jest poprawny
* Tak -> zapis danych do bazy + zwrócenie informacji, Nie -> zwrócenie informacji o błednym kodzie
### /sms
* request trafia do kontrolera
* serwis#1 odpytuje moduł subskrupcji, czy dany numer jest chroniony (jeśli nie to Dyspozycja = ALLOWED)
* serwis#1 wyciąga z wiadomości urle (jeśli brak Dyspozycja = ALLOWED)
* urle są ewaluowane przez serwis#2
* serwis#2 sprawdza, czy url/eTld są zapisane w cache'u (jeśli nie to przechodzi dalej)
* serwis#2 wykorzystuje strategie sprawdzania urli (podszywanie się pod domenę, podejrzany host, podejrzany Tld), jeśli któraś z zasad jest złamana, to przechodzi dalej
* serwis#2 wysyła request do Google WebRisk api, aby sprawdzić, czy dany url jest złośliwy
* po przetworzeniu wszystkich urli serwis#1 decyduje, czy dany sms jest złośliwy


## Perspektywy rozwoju
* Algorytm sprawdzający, przeciwko któremu zagrożeniu należy przeskanować uri ???
* Kody aktywacyjny/deaktywacyjny mogłyby być przechowywane w bazie, wtedy nie muszą być zahardkodowane
* Zmiana odpowiedzi /subscription w przypadku błędnego kodu na 400
* Regex do urli nie działa w pełni poprawnie - wysypuje się na polskich znakach
* Zmiana nazw serwisów na bardziej intuicyjne: RiskDetectionService i UrlEvaluateService
* Przenisienie sekretów do docker secrets/vault
* Aby przyspieszyć komunikację między serwisami, zamiast resta można byłoby użyć gRPC