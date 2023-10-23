Algoritmo di Ricerca di Percorso Ottimo MOA*

Questo algoritmo è progettato per trovare il percorso più efficiente da un nodo iniziale a uno o più nodi finali in un grafo pesato.
Di seguito, le istruzioni su come utilizzare l'algoritmo modificando il file settings.txt.

------------------------------------------------------------------------------------------------------------------------------------
Funzionamento:

Il programma legge le impostazioni da un file di testo chiamato "settings.txt", contenente le seguenti informazioni:
File: Indica il nome del file contenente l'elenco degli archi nel formato NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE.
Nodo iniziale: Indica da quale nodo iniziare il percorso.
Nodi finali: Indica quali nodi si desidera raggiungere.
Direzionale: Specifica se gli archi del grafo sono direzionati (S per sì, N per no).
Spiegazione: Specifica se si desidera la stampa della spiegazione (S per sì, N per no).

------------------------------------------------------------------------------------------------------------------------------------
Utilizzo:

Modificare il file settings.txt seguendo le istruzioni sottostanti.
Eseguire il programma App.java. --> "mvn exec:java -q"

------------------------------------------------------------------------------------------------------------------------------------
Formato del file settings.txt:

File: input.txt
Nodo iniziale: START
Nodi finali: END1, END2, ...
Direzionale (S/N): S
Spiegazione (S/N): N

Sostituire START con il nome del nodo iniziale.
Sostituire END1, END2, ... con una lista separata da virgole dei nodi finali.
Sostituire S o N nel terzo campo per specificare se gli archi sono direzionati (Default S).
Sostituire S o N nel quarto campo per specificare se si desidera la stampa della spiegazione (Default N).

------------------------------------------------------------------------------------------------------------------------------------
Esempio di settings.txt:

File: input.txt
Nodo Iniziale: A
Nodi Finali: D, E
Direzionale (S/N): S
Spiegazione (S/N): N

------------------------------------------------------------------------------------------------------------------------------------
Formato del file input.txt:

NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE
...

Inserire gli archi e i costi nel formato NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE per definire la struttura del grafo.

------------------------------------------------------------------------------------------------------------------------------------
Esempio di input.txt:

A | (2.5 3.0) | B
B | (1.0 2.0) | C
B | (2.0 2.5) | D
C | (1.5 2.0) | D
C | (2.0 1.5) | E

------------------------------------------------------------------------------------------------------------------------------------
Contenuto spiegazione:

Nel caso in cui venga richiesta la stampa della spiegazione, verrà stampata una tabella che conterrà le seguenti colonne:

k: Indica il numero dell'iterazione.
n: Rappresenta l'insieme dei nodi in OPEN. I nodi contrassegnati con un asterisco (*) sono quelli che fanno parte dell'insieme ND
   (non dominati), mentre il nodo contrassegnato con due asterischi (**) è il nodo scelto per l'espansione.
New G(n): Questo insieme contiene i costi dei percorsi non dominati dal nodo iniziale al nodo n.
New F(n): Rappresenta l'insieme dei valori di selezione del nodo n. Questi valori combinano i costi accumulati da G(n) e i valori
euristici associati a n.
SOL_COSTS: Contiene i costi dei percorsi di soluzione non dominati.
GOALS: Rappresenta l'insieme dei nodi finali raggiunti.

------------------------------------------------------------------------------------------------------------------------------------
Note Importanti:

I costi possono essere uno o piú (ogni arco deve avere lo stesso numero di costi),
devono essere separati da uno spazio e devono essere numeri decimali positivi.
Ogni arco può avere un solo nodo sorgente e un solo nodo destinazione.
Assicurarsi che i nodi iniziali e finali siano presenti negli archi e siano scritti correttamente.
È possibile aggiungere nuove linee per gli archi.
Non è possibile modificare la posizione delle righe.
Non richiedere la spiegazione in caso di grafi complessi.