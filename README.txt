Algoritmo di Ricerca di Percorso Ottimo MOA*

Questo algoritmo è progettato per trovare il percorso più efficiente da un nodo iniziale a uno o più nodi finali in un grafo pesato.
Di seguito, le istruzioni su come utilizzare l'algoritmo modificando il file input.txt.

------------------------------------------------------------------------------------------------------------------------------------
Funzionamento:

Input: Il programma legge i dati di input da un file di testo chiamato "input.txt".
Questo file contiene le informazioni sul grafo e sui nodi di partenza e arrivo.
Il file input.txt contiene le seguenti informazioni:
Nodo iniziale: Indica da quale nodo iniziare il percorso.
Nodi finali: Indica quali nodi si desidera raggiungere.
Direzionale: Specifica se gli archi del grafo sono direzionati (S per sì, N per no).
Spiegazione: Specifica se si desidera la stampa della spiegazione (S per sì, N per no).
Archi e Costi: Ogni riga successiva rappresenta un arco nel formato NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE.

------------------------------------------------------------------------------------------------------------------------------------
Utilizzo:

Modificare il file input.txt seguendo le istruzioni sottostanti.
Eseguire il programma App.java. --> "mvn exec:java -q"

------------------------------------------------------------------------------------------------------------------------------------
Formato del File input.txt:

Nodo iniziale: START
Nodi finali: END1, END2, ...
Direzionale (S/N): S
Spiegazione (S/N): N

NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE
...

Sostituire START con il nome del nodo iniziale.
Sostituire END1, END2, ... con una lista separata da virgole dei nodi finali.
Sostituire S o N nel terzo campo per specificare se gli archi sono direzionati (Default S).
Sostituire S o N nel quarto campo per specificare se si desidera la stampa della spiegazione (Default N).
Inserire gli archi e i costi nel formato NODO_SORGENTE | (COSTO1 COSTO2) | NODO_DESTINAZIONE per definire la struttura del grafo.

------------------------------------------------------------------------------------------------------------------------------------
Esempio di File input.txt:

Nodo Iniziale: A
Nodi Finali: D, E
Direzionale (S/N): S
Spiegazione (S/N): N

A | (2.5 3.0) | B
B | (1.0 2.0) | C
B | (2.0 2.5) | D
C | (1.5 2.0) | D
C | (2.0 1.5) | E

------------------------------------------------------------------------------------------------------------------------------------
Note Importanti:

I costi possono essere uno o piú (ogni arco deve avere lo stesso numero di nodi), devono essere separati da uno spazio e devono essere numeri decimali positivi.
Ogni arco può avere un solo nodo sorgente e un solo nodo destinazione.
Assicurarsi che i nodi iniziali e finali siano presenti negli archi e siano scritti correttamente.
È possibile aggiungere nuove linee per gli archi.
Non è possibile modificare la posizione delle prime quattro righe.
Non richiedere la spiegazione in caso di grafi complessi.