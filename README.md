# Tombola

I numeri a disposizione per formare le cartelle sono 90 (da 1 a 90)

<ul style="list-style-type:none;padding-left:0px">
<li>Con i 90 numeri vanno costruite 6 cartelle
	<ul style="list-style-type:none">
		<li>ogni cartella con 15 numeri	disposti in 3 righe e 9 colonne.
		<ul style="list-style-type:none">
			<li> ogni riga deve contenere 5 numeri (di decine differenti) e 4 spazi
			<li> ogni colonna deve contenere da 1 a 3 numeri (per decina)
			<ul style="list-style-type:none">
				<li>la 1� tra 1 a 9
				<li>la 2� tra 10 a 19
				<li>...
				<li>l'8�  tra 70 a 79
				<li>la 9� tra 80 a 90
			</ul>
		</ul>
	</ul>
</ul>

<ul style="list-style-type:none;padding-left:0px">
<li>Complessivamente per le 6 cartelle
	<ul style="list-style-type:none">
		<li>la 1� colonna deve contenere i numeri da 1 a 9, 9 numeri e 9 spazi
 		<li>la 2� colonna deve contenere i numeri da 10 a 19, 10 numeri ed 8 spazi
 		<li>...
		<li>l'8� colonna deve contenere i numeri da 70 a 79, 10 numeri ed 8 spazi
		<li>la 9� colonna deve contenere i numeri da 80 a 90, 11 numeri e 7 spazi
	</ul>
</ul>

Per quantita di numeri nelle colonne
le cartelle sono raggruppabili in 4 tipologie

    a:  1 1 1 1 1 1 3 3 3 = 15
    b:  1 1 1 1 1 2 2 3 3 = 15
    c:  1 1 1 1 2 2 2 2 3 = 15
    d:  1 1 1 2 2 2 2 2 2 = 15

<ul style="list-style-type:none;padding-left:0px">
<li>Verticalmente abbiamo invece
	<ul style="list-style-type:none">
		<li>2 tipologie per la 1� colonna
		<li>3 tipologie per le colonne dalla 2� all'8�
		<li>3 tipologie per la 9� colonna
	</ul>
</ul>
		
		1 1  2 3 3  2 3 3
		1 1  2 2 3  2 2 3
		1 1  2 2 1  2 2 2
		1 2  2 1 1  2 2 1
		2 2  1 1 1  2 1 1
		3 2  1 1 1  1 1 1
		= =  = = =  = = =
		9 9  0 0 0  1 1 1

Dato che i numeri in ogni decina sono limitati
le combinazioni delle tipologie per le 6 cartelle
sono a loro volta limitate

	1� d: 1 1 1 2 2 2 2 2 2 = 15
	2� d: 1 1 1 2 2 2 2 2 2 = 15
	3� d: 1 1 1 2 2 2 2 2 2 = 15
	4� d: 2 1 1 1 2 2 2 2 2 = 15
	5� b: 2 3 3 1 1 1 1 1 2 = 15
	6� b: 2 3 3 2 1 1 1 1 1 = 15
	      = = = = = = = = =
	      9 0 0 0 0 0 0 0 1  

Esiste un modo per generare tutte le tipologie delle 6 cartelle?<br>
E da queste tutte le possibili schede?
