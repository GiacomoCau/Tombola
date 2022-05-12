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

Si può però generare un foglio scegliendo a caso la prima scheda fra tutte le 735.210 strutture (vedi Schede.{js|html}|Core.java <code>S(3,5,[3,3,3,3,3,3,3,3,3])</code>) e le successive cinque fra le strutture di schede che siano numericamente compatibli con i vincoli verticali di numericita minima e massima di ogni colonna a loro volta dettati dalle precedenti strutture di schede scelte. 
Avendo ragguppato le schede per numericità verticale l'algoritmo si risolve rapidamente, 2ms per foglio, con selezioni casuali nell'insimeme delle strutture compatibili ottentue filtrando i 1.554 raggruppamenti delle 735.210 strutture.

Una volta definita la composizione delle strutture di schede per il foglio è possibile disporre i numeri sulle schede prendendoli da una disposizione casuale per ogni decina, tale disposizione casuale può essere mantenuta per tutti i fogli o cambiata per ogni foglio generato.

Così con:

	d:\git\tombola\bin> java -cp .;bin --enable-preview Tombola fogli compact

	  |  |25|36|  |54|62|  |82
	 7|  |  |  |  |56|67|70|89
	  |12|26|  |41|  |68|76|
	
	 5|10|  |32|  |50|63|  |
	  |13|  |  |  |53|64|75|87
	  |16|27|  |43|  |69|78|
	
	  |  |29|31|44|57|65|  |
	 1|14|  |35|45|  |  |74|
	  |19|  |39|49|  |  |79|83
	
	  |  |  |33|40|51|  |72|81
	 6|11|  |37|48|55|  |  |
	 9|15|20|  |  |  |61|  |85
	
	 2|18|21|  |  |52|  |  |86
	 4|  |  |30|  |58|  |77|88
	 8|  |22|  |47|  |66|  |90
	
	 3|  |23|34|42|  |  |71|
	  |17|24|  |  |59|60|  |80
	  |  |28|38|46|  |  |73|84

mentre con:

	d:\git\tombola\bin> java -cp .;bin --enable-preview Tombola fogli -m 3 compact

	 6|13|  |  |45|51|  |  |80        1|10|23|  |40|  |  |75|          1|  |  |37|  |57|  |71|84
	 8|15|28|  |  |  |  |73|82        6|  |  |30|  |50|  |76|80        9|  |20|39|47|  |  |73|
	  |16|  |33|  |53|62|  |85        9|14|  |  |  |54|60|  |87         |16|29|  |49|  |63|  |88
	
	 1|  |24|34|43|  |  |  |87         |  |  |32|48|59|  |74|84        3|10|23|34|  |  |  |75|
	 5|  |26|39|44|52|  |  |           |19|  |35|  |  |68|78|86         |  |  |35|  |55|62|76|81
	 7|12|  |  |  |55|67|72|          7|  |21|39|  |  |69|  |88        7|12|  |  |48|  |  |78|90
	
	 2|11|27|  |47|54|  |  |          4|11|  |  |41|53|  |  |90        2|  |24|30|  |  |  |72|80
	  |  |  |30|  |57|65|76|89         |  |27|33|47|  |66|70|          5|17|  |  |41|56|65|  |
	  |19|  |38|48|59|  |77|          5|18|28|37|49|  |  |  |           |18|  |  |43|59|69|79|
	
	  |14|20|36|  |  |  |71|81         |  |20|34|  |  |63|79|81         |11|28|  |40|  |67|  |82
	  |  |22|  |40|  |60|75|83        8|13|  |38|44|51|  |  |           |  |  |33|42|50|  |77|83
	 4|  |23|  |41|58|  |  |90         |17|25|  |45|  |65|  |82        6|13|  |36|46|  |68|  |
	
	  |10|  |31|  |  |64|70|86        3|  |24|  |43|52|61|  |          8|15|21|  |45|  |  |74|
	  |18|29|  |42|  |66|74|           |12|26|  |  |55|  |71|83         |  |22|38|  |53|61|  |86
	 9|  |  |  |  |50|68|79|88         |16|  |36|46|57|  |72|           |19|26|  |  |58|66|  |87
	
	  |  |21|32|46|  |61|  |84        2|  |22|  |  |56|62|73|          4|  |25|31|  |51|60|  |
	  |  |25|35|49|  |63|78|           |15|  |  |  |58|64|77|85         |14|  |32|  |52|64|  |85
	 3|17|  |37|  |56|69|  |           |  |29|31|42|  |67|  |89         |  |27|  |44|54|  |70|89

 più giocabile invece:
 
	 d:\git\tombola\bin> java -cp .;bin --enable-preview Tombola fogli boxed

	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 7│14│  │  │  │55│63│  │83│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │18│  │33│42│  │  │79│86│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │19│27│36│  │  │67│  │89│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 2│  │  │30│40│52│62│  │  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │  │38│44│59│  │72│87│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 8│11│26│  │49│  │  │  │88│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 3│  │  │31│45│  │64│70│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │13│  │35│  │56│  │73│85│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 9│15│23│  │47│  │68│  │  │
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│  │17│  │37│43│  │60│71│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 4│  │25│  │46│51│  │75│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │29│  │  │53│69│78│90│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 6│  │20│  │  │58│66│  │81│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │24│32│48│  │  │74│82│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │16│28│34│  │  │  │77│84│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 1│  │21│39│  │50│  │76│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 5│10│  │  │41│54│61│  │  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │12│22│  │  │57│65│  │80│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘
 
Oppure:

	d:\git\tombola\bin> java -cp .;bin --enable-preview Tombola fogli boxed -r 2 -c 3

	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│  │11│25│33│  │52│  │75│  │   │ 2│17│  │32│  │  │60│71│  │   │  │  │22│30│46│59│  │  │80│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 1│  │  │38│  │  │68│79│85│   │  │  │28│  │42│56│65│76│  │   │ 9│16│  │  │47│  │62│72│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 5│  │26│39│45│  │69│  │  │   │ 4│  │  │36│  │58│67│  │86│   │  │19│29│  │  │  │63│77│81│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 3│14│20│  │  │50│61│  │  │   │  │12│  │31│40│54│  │  │83│   │ 8│  │24│34│  │51│  │  │82│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │21│35│  │55│  │73│89│   │  │  │23│37│43│  │64│  │87│   │  │10│  │  │41│53│66│  │84│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 6│18│  │  │48│57│  │74│  │   │ 7│15│  │  │44│  │  │78│90│   │  │13│27│  │49│  │  │70│88│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘

O ancora

	d:\git\tombola\bin> java -cp .;bin --enable-preview Tombola fogli -n 3 boxed -r 2 -c 3

	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 3│  │20│31│42│  │62│  │  │   │ 7│16│  │33│48│  │  │74│  │   │ 6│  │21│30│40│  │  │77│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │23│  │45│  │69│75│80│   │ 8│18│  │  │49│55│  │  │84│   │  │  │22│  │41│58│61│78│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 5│15│25│39│  │59│  │  │  │   │ 9│  │27│34│  │57│67│  │  │   │  │10│29│  │44│  │  │79│85│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│  │13│  │  │47│50│64│70│  │   │  │12│  │35│  │51│  │71│87│   │  │11│24│32│43│  │  │  │81│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │28│  │  │53│65│73│82│   │  │  │  │37│46│52│63│  │88│   │  │17│  │36│  │  │60│76│83│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 2│14│  │38│  │56│  │  │89│   │ 1│  │26│  │  │  │68│72│90│   │ 4│19│  │  │  │54│66│  │86│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 3│  │27│30│  │54│62│  │  │   │ 2│12│  │32│40│  │  │70│  │   │  │  │21│33│  │57│64│76│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 4│  │  │35│43│  │65│74│  │   │  │15│  │  │  │53│60│77│82│   │ 7│13│25│  │  │  │  │79│89│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 5│14│  │36│  │  │  │78│86│   │ 6│16│29│  │  │56│61│  │  │   │  │19│  │37│45│59│66│  │  │
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│  │  │22│31│  │50│  │72│84│   │ 9│  │24│  │46│  │  │73│80│   │  │18│20│  │41│52│  │  │81│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 8│10│  │  │48│  │  │75│85│   │  │  │26│38│47│  │67│  │90│   │  │  │28│34│42│  │68│  │83│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │11│23│  │  │51│63│  │88│   │  │17│  │39│49│55│69│  │  │   │ 1│  │  │  │44│58│  │71│87│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│  │  │  │  │41│50│60│74│80│   │ 7│10│20│38│  │  │61│  │  │   │  │11│26│  │  │51│62│70│  │
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 2│15│23│31│  │59│  │  │  │   │  │16│  │39│  │  │64│71│81│   │ 6│  │  │30│47│52│  │  │85│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│ 5│  │  │  │44│  │65│78│87│   │ 8│  │  │  │40│53│66│  │88│   │ 9│12│  │36│  │58│63│  │  │
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘
	
	┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐   ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐
	│ 3│14│22│  │46│  │  │75│  │   │  │18│21│34│42│  │  │73│  │   │ 4│  │24│35│43│  │  │  │82│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │  │  │32│48│55│67│76│  │   │  │19│27│37│  │  │68│  │83│   │  │13│25│  │45│  │69│  │84│
	├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤   ├──┼──┼──┼──┼──┼──┼──┼──┼──┤
	│  │17│  │33│  │56│  │79│86│   │ 1│  │28│  │  │57│  │77│90│   │  │  │29│  │49│54│  │72│89│
	└──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘   └──┴──┴──┴──┴──┴──┴──┴──┴──┘

Questa la sintassi completa del comando

	smorfia [compact | boxed]
	 schede [-n n [vs [pb ps]]] [-m m [os]] compact | boxed
	  fogli [-n n [vs [pb ps]]] [-m m [os]] compact | boxed [-r r [vs [pb ps]]] [-c c [os]]

con

	 n: numero di fogli o schede verticali
	vs: separatore verticale, numero di a capo
	pb: fine pagina, numero di separazioni verticali
	ps: separatore di pagina, numero di a capo
	 m: numero di fogli o schede orizzonatali
	os: separatore orizzonatale, numero di spazi 
	 r: numero di righe
	 c: numero di colonne
	
