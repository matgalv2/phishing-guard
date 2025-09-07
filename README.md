# Phishing guard
Aplikacja symulująca obsługę subkrypcji usługi i detekcji ryzyka w wiadomościach sms.
Wykorzystane technologie:
* Java 21
* Spring Boot 3.5
* WebFlux (wiele operacji I/O - nieblokująco)
* Redis
* PostgreSQL (sterowniki R2DBC)

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
* algorytm sprawdzający, przeciwko któremu zagrożeniu należy przeskanować uri ???
* kody aktywacyjny/deaktywacyjny mogłyby być przechowywane w bazie, wtedy nie muszą być zahardkodowane
* zmiana odpowiedzi /subscription w przypadku błędnego kodu na 400
* regex do urli nie działa w pełni poprawnie - wysypuje się na polskich znakach
* zmiana nazw serwisów na bardziej intuicyjne: RiskDetectionService i UrlEvaluateService