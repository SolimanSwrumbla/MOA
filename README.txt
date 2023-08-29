Algoritmo di Ricerca di Percorso Ottimo MOA*

Questo algoritmo è progettato per trovare il percorso più efficiente da un nodo iniziale a uno o più nodi finali in un grafo pesato.
Di seguito, le istruzioni su come utilizzare l'algoritmo modificando il file input.txt.

------------------------------------------------------------------------------------------------------------------------------------
Funzionamento:

Input: Il programma legge i dati di input da un file di testo chiamato "input.txt".
Questo file contiene le informazioni sul grafo e sui nodi di partenza e arrivo.
Il file input.txt contiene le seguenti informazioni:
Nodo Iniziale: Indica da quale nodo iniziare il percorso.
Nodi Finali: Indica quali nodi si desidera raggiungere.
Opzione Direzionale: Specifica se gli archi del grafo sono direzionati (S per sì, N per no).
Archi e Costi: Ogni riga successiva rappresenta un arco nel formato NODO_SORGENTE - (COSTO1 COSTO2) - NODO_DESTINAZIONE.

------------------------------------------------------------------------------------------------------------------------------------
Utilizzo:

Modificare il file input.txt seguendo le istruzioni sottostanti.
Eseguire il programma App.java.

------------------------------------------------------------------------------------------------------------------------------------
Formato del File input.txt:

Nodo Iniziale: START
Nodi Finali: END1, END2, ...
Opzione Direzionale: S o N

NODO_SORGENTE - COSTO1 COSTO2 - NODO_DESTINAZIONE
...

Sostituire START con il nome del nodo iniziale.
Sostituire END1, END2, ... con una lista separata da virgole dei nodi finali.
Sostituire S o N per specificare se gli archi sono direzionati.
Inserire gli archi e i costi nel formato NODO_SORGENTE - (COSTO1 COSTO2) - NODO_DESTINAZIONE per definire la struttura del grafo.

------------------------------------------------------------------------------------------------------------------------------------
Esempio di File input.txt:

Nodo Iniziale: A
Nodi Finali: D, E
Opzione Direzionale: N

A - (2.5 3.0) - B
B - (1.0 2.0) - C
B - (2.0 2.5) - D
C - (1.5 2.0) - D
C - (2.0 1.5) - E

------------------------------------------------------------------------------------------------------------------------------------
Note Importanti:

I costi sono separati da uno spazio e devono essere numeri decimali positivi (Attualmente solo 2 per ogni arco).
Ogni arco può avere un solo nodo sorgente e un solo nodo destinazione.
Assicurarsi che i nodi iniziali e finali siano presenti negli archi e siano scritti correttamente.
È possibile aggiungere nuove linee per gli archi.
Non è possibile modificare la posizione delle prime tre righe.