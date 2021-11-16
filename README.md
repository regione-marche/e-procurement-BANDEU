# e-procurement-BANDEU
Modulo per l'invio al SIMAP dei dati per la pubblicazione dei formulari GUUE.

La stazione appaltante può pubblicare i bandi di gara e gli avvisi nel sito SIMAP utilizzando il servizio eSenders.
Per l'utilizzo del servizio eSenders è necessaria la qualificazione secondo le regole descritte nel sito https://simap.ted.europa.eu/ e la conseguente configurazione dell'applicazione.

Il modulo permette l’inserimento e la gestione di tutte le informazioni (obbligatorie e facoltative) previste dai formulari europei, ovvero al termine dell’inserimento permette di:
- controllare i dati inseriti prima dell’invio
- produrre il file PDF del formulario (facoltativo)
- inviare i dati al SIMAP per la pubblicazione sulla GUUE
E' quindi possibile controllare lo stato di pubblicazione del formulario e al termine ottenre automaticamente i riferimenti della pubblicazione (numero e data).

Sono inclusi dei servizi (API) per poter acquisire in automatico dal modulo Appalti i dati utili ad istnziare i formulari e per resituirne gli estremi.

Funzioanlità principali:
- Acquisizione dei dati dei formulari mediante API
- Compilaizone e controllo dei dati da pubblicare
- Generazione anterpima PDF del formulario
- Invio dei dati al SIMAP per la pubblicazione dei formulari GUUE
