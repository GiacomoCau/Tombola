# Tombola

I numeri a disposizione per formare le cartelle sono 90 (da 1 a 90)

<ul style="list-style-type:none;padding-left:0px">
<li>Con i 90 numeri va costruito un foglio composto di 6 cartelle
	<ul style="list-style-type:none">
		<li>ogni cartella con 15 numeri	disposti in 3 righe e 9 colonne.
		<ul style="list-style-type:none">
			<li>ogni riga deve contenere 5 numeri (di decine differenti) e 4 spazi
			<li>ogni colonna deve contenere da 1 a 3 numeri (per decina)
			<ul style="list-style-type:none">
				<li>la 1ª tra 1 a 9
				<li>la 2ª tra 10 a 19
				<li>...
				<li>l'8ª  tra 70 a 79
				<li>la 9ª tra 80 a 90
			</ul>
			<li>in ogni colonna i numeri devono essere in ordine crescente
		</ul>
	</ul>
</ul>

<ul style="list-style-type:none;padding-left:0px">
<li>Complessivamente per il foglio ovvero per le 6 cartelle
	<ul style="list-style-type:none">
		<li>la 1ª colonna deve contenere i numeri da 1 a 9, 9 numeri e 9 spazi
 		<li>la 2ª colonna deve contenere i numeri da 10 a 19, 10 numeri ed 8 spazi
 		<li>...
		<li>l'8ª colonna deve contenere i numeri da 70 a 79, 10 numeri ed 8 spazi
		<li>la 9ª colonna deve contenere i numeri da 80 a 90, 11 numeri e 7 spazi
	</ul>
</ul>

<br>
Per quantita di numeri nelle colonne 
le cartelle sono raggruppabili in 4 tipologie (a, b, c, d)

		a: 1 1 1 1 1 1 3 3 3 = 15
		b: 1 1 1 1 1 2 2 3 3 = 15
		c: 1 1 1 1 2 2 2 2 3 = 15
		d: 1 1 1 2 2 2 2 2 2 = 15


<ul style="list-style-type:none;padding-left:0px">
<li>Verticalmente abbiamo invece
	<ul style="list-style-type:none">
		<li>2 tipologie per la 1ª colonna (a1, a2)
		<li>3 tipologie per le colonne dalla 2ª all'8ª (b1, b2, b3)
		<li>3 tipologie per la 9ª colonna (c1, c2, c3)
	</ul>
</ul>

		a1 a2  b1 b2 b3  c1 c2 c3 
		 :  :   :  :  :   :  :  :
		 1  1   2  3  3   2  3  3
		 1  1   2  2  3   2  2  3
		 1  1   2  2  1   2  2  2
		 1  2   2  1  1   2  2  1
		 2  2   1  1  1   2  1  1
		 3  2   1  1  1   1  1  1
		 = ==  == == ==  == == ==
		 9  9  10 10 10  11 11 11

<br>
Dato che i numeri in ogni decina sono limitati 
le combinazioni delle tipologie per le 6 cartelle 
sono a loro volta vincolate

	      1ª 2ª 3ª 4ª 5ª 6ª 7ª 8ª 9ª
	      a2 b3 b3 b1 b1 b1 b1 b1 c1  
	       :  :  :  :  :  :  :  :  :
	1ª d:  1  1  1  2  2  2  2  2  2 = 15
	2ª d:  1  1  1  2  2  2  2  2  2 = 15
	3ª d:  1  1  1  2  2  2  2  2  2 = 15
	4ª d:  2  1  1  1  2  2  2  2  2 = 15
	5ª b:  2  3  3  1  1  1  1  1  2 = 15
	6ª b:  2  3  3  2  1  1  1  1  1 = 15
	      == == == == == == == == ==
	       9 10 10 10 10 10 10 10 11  

<p>
Esiste un modo per generare tutte le tipologie delle 6 cartelle?<br>
E da queste tutte le possibili schede?


<p><br>
La risposta è ovviamente si (vedi Schede.{js|html} <code>F(6,3,5,[9,10,10,10,10,10,10,10,11])</code>) ma una generazione seriale fa in modo che un foglio sia diverso dal successivo per la posizione di un solo numero su di una sola scheda, così che tra un foglio ed il successivo vi siano ben cinque schede identiche, rendendo i fogli inutilizzabili per il gioco.

L'alternativa è una generazione casuale di un foglio fra tutti i possibili ma qui ci si scontra con la difficoltà di trattare una dimensione enorme.
Tenedo conto che esistono 735.210 strutture di schede e dovendo fare un foglio di sei schede ci si trova a dover combattere con qualcosa, visti i vincoli di numericià, poco meno di 735.210<sup>6</sup> alternative.

Si può però generare un foglio scegliendo a caso la prima scheda fra tutte le 735.210 strutture (vedi Schede.{js|html}|Core.java <code>S(3,5,[3,3,3,3,3,3,3,3,3])</code>) e le successive cinque fra le strutture di schede che siano numericamente compatibli con i vincoli di numericita minima e massima di ogni colonna a loro volta dettati dalle precedenti strutture di schede scelte. 
Avendo ragguppato le schede per numericità verticale l'algoritmo si risolve rapidamente, 2ms per foglio, con selezioni casuali fra le strutture compatibili ottentue filtrando i 1.554 raggruppamenti delle 735.210 strutture.

Una volta definita la composizione delle strutture di schede per il foglio è possibile disporre i numeri sulle schede prendendoli da una disposizione casuale per ogni decina, tale disposizione casuale può essere mantenuta per tutti i fogli o cambiata per ogni foglio generato.
