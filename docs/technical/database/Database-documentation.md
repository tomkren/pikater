<!-- --- title: Dokumentace databáze -->

[[_TOC_]]

Pikater v rámci svojej úlohy vykonáva rôzne operácie na vstupných datách a pritom vzniknú dáta rôzneho povahu: napr. pri klasifikácii vznikne nový, už klasifikovaný dataset a štatistika výpočtu. Tieto dáta musia byť niekam uložené a sprístupnené pre prípadné ďalšie experimenty.

## Predmluva

### Použité skratky

* __ARFF__ (Attribute-Relation File Format)  
Formát používaný knižnicou Weka, čo mulitagentný systém Pikateru interne používa.
* __CSV__ Comma-Separated Values)
* __JDBC__ (Java Database Connectivity)
* __SQL__ (Strucutered Query Language)
* __HSQLDB__ (Hyper SQL Database)
* __PgLOB__ (Postgre Large Object)
* [__JPA__](http://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html) (Java Persistence API)

### Použité pojmy

* __Datasety__  
Alias pre ARFF súbory, kde sú vstupné dáta pre experimenty.




## Výber databázového systému

Pôvodná verzia Pikateru používala databázový systém MySQL ako úložisko vytvorených dát. Prístup k serveru prebiehol pomocou JDBC ovládača. Dotazovanie bolo vyriešené pomocou dynamického vytvorenia SQL dotazov. Výsledkom týchto dotazov bol klasický javovský resultset.  
Nevýhodou tohto princípu bola hlavne viazanosť k danému databázovému systému a neflexibilnosť pri zmenách v schémate.
Zmeny v schématu síce mali byť zriedkavé, ale keď sa k tomu dojde musí byť vyriěsená prípadná nekonzistentnosť medzi starou verziou dotazu a súčasnou verziou databáze.

**PostgreSQL**  
Pri výbere nového databázového systému sme brali do úvahy tri systémy: MySQL, PostgreSQL a HyperSQL DB (HSQLDB). Všetky tri majú nejakú verziu dostupné bez poplatkov, čo je docela výhodné z finančného hladiska.
Pre projekt Pikater výsledná databáza musela splňovať kritérie multiplatformovosti, schopnosti tranzakčného spracovávania a - dôsledkem analýzy problému uloženia vstupných datasetov - schopnosti uloženia väčších (až niekolko 100 megabajtových) súborov.
Nasledujúca tabulka obsahuje dáta o tom, že aké vlastnosti majú jednotlivé kandidáty:

|               |   MySQL         |   HSQLDB         | PostgreSQL          |
|:-------------:|:---------------:|:----------------:|:-------------------:|
| tranzakcie    | áno (InnoDB)    | áno              | áno                 |
| súbory (LOBy) | áno             | áno (64TiB spolu)| áno (4TiB per súbor)|
| iné           | povodne použitý, rozšírená | Java  | rozšírená           |

Síce MySQL podporuje tranzakčné spracovávanie, ale to je dostupné iba pre engine InnoDB, čo je použitelné iba za poplatok. Vzhladom na to, že ostatné dva to ponúkajú zadarmo MySQL už mal docela velkú nevýhodu v porovnaní s ostatnými dvomi.

Medzi HSQLDB a PostgreSQL sme sa rozhodli hlavne na základe prístupu k uloženým bytovým objektom. Súčasné databázové systémy už štandardne umožnia nám používať bytové pole v rámci databázového záznamu, kam môžeme uložit obsah súboru. Jediný problém znamená spôsob prístupu k takto uloženým dátam.
PostgreSQL používá na to vlastný prístup, čo umožní kopírovať dáta z/do databáze pomocou streamov. V HSQLDB k prístupu týmto záznamom máme používať setter a getter, čo pracuje s javovským java.lang.Object triedou. Vzhladom na to, že plánujeme pracovať s docela velkými súbormi, rozhodli sme sa pre PostgreSQL.
Trošku subjektívny pohlad, ale PostgreSQL je rozšírenejší ako HSQLDB, čo teoreticky môže znamenať lepšiu dostupnosť informácií a tým pádom aj jednoduchšie riešenie prípadných problémov.




## Prechod na JPA

Ako sme povedali, pôvodná verzia Pikateru používala prístup viazaný na databázový systém MySQL. Rozhodli sme sa to zmeniť na použitie technológie JPA, čo nám umožnila mapovania záznamov z databáze na javovské objekty a späť.
Obrovskou výhodou JPA je, že objekty pripravené na prácu s JPA nemusia byť zmenené pri zmene databázového systému. Takto pripravené objekty nazývame entitami a najjednoduchšie sú rozpoznatelné na základe značky `@Entity` pred definíciou javovskej triedy.

### Výber connectoru

Rozhodnutie pre JPA znamená, že, okrem databáze a technológie pre prístup k nemu potrebujeme niečo, čo spojuje svet Javy so svetom databáze. V praxi to znamená prevod operácií v JPA na rôzne príkazy v SQL a naopak pre výsledok dotazu vytvoriť odpovedajúce entity. Obvykle výber tohto connectoru neovlivní vývoj aplikácie, okrem použitia nejakých špeciálnych funkcí. Dva z najrozšírenejších connectorov sú Hibernate a Eclipselink. My sme sa rozhodli pre Ecipselink z dôvodu, že u Eclipselink je možné zmeniť vlastnosti databázového pripojenia zo zdrojového kódu, čo môžeme v budúcnosti využívať.

### Konfigurácia

<font color="red">TODO</font>





## Databáza

### Schéma

#### Pôvodné

[[original_db_schema.png|alt=Original DB schema]]

#### Aktuálné

[[schemaspy_output/diagrams/summary/relationships.real.large.png|alt=Current DB schema]]

#### Rozdiely

Pri vytvorení novej štruktúry entít - a tým pádom aj súčasnú schématu databáze - sme vela zachovali z pôvodnej schémy. Na druhej strane bolo potrebné vytvoriť chýbajúce vzťahy medzi entitami (napríklad experiment má viac podexperimentov) a podklady pre nové funkcie (napríklad uloženie datasetov).

|               |   Pôvodný Pikater   |   Aktuálný Pikater  |
|:-------------:|:-------------------:|:-------------------:|
| datasety      | lokálne na disku    | v databázi          |
| ostatné dáta  | v databázi          | v databázi          |

### PostgreSQL specifika

#### PgLOBy

Po výbere PostgreSQL ako databázový systém pre Pikater sme museli naimplementovať špeciálný prístup k súborum uloženým v databáze.

Súbory sú uložene ako Postgre Large Objekty (PgLOBy, alebo jednoducho LOBy), čo znamená, že je uložené v systémovej tabulke `pg_largeobject` danej databáze. Pre každý súbor je vytvoreno množstvo záznamov, ktoré obsahujú súbor rozsekaný na niekolko byteových polí. Pri získaní dát alebo zapisovaní do databáze ale vystačíme s ID daného PgLOBu (oid). Napríklad v entite `JPADatasetLO` máme jednu premennú typu `long`, čo obsahuje OID.

Prestože môžeme u premenných používať v JPA entitách anotáciu `@Lob` (naznačí, aby danú premennú JPA uložil do bytoveho pola), nemáme istotu, že velké súbory budú optimalne vyriešené. V najhoršom prípade môže sa nám stať, že celý obsah súboru je kopírovaný do pamäte a potom je prenesený do databáze.

Triedy pracujúcie s PgLOBy sa nalézajú v balíčku `org.pikater.shared.database.postgre.largeobject`.

### Práca s DB frameworkom

Framework sa nachádzí v balíčku `org.pikater.shared.database`, rozdelenie na podbalíčky by malo byť vcelku jasne. Je ale potreba trochu popísať balíček `org.pikater.shared.database.jpa` - hlavné sú **entity** a **dao objekty**.

#### Entity

Entity začínajú na "JPA" (napríklad `JPAUser`). Stručne je popíšeme.

V súčasnej verzii pre Pikater máme 16 vytvorených entit, ktoré sa nachádzajú v balíčku `org.pikater.shared.database.jpa`. Každá je odvodená od abstraktnej entity `JPAAbstractEntity`. Tento spoločný predok obsahuje iba ID slúžiace ako primárny klúč v databáze.

##### Týkajúce se uživatelů

Základ systému. Uživatelia sa primárně vytvárajú na webu.

1. **JPAUser**  
Entita užívatela. V entitách, kde sme potrebovali uložiť informáciu o vlastníkovani, je referencovaná inštancia tejto entity. Entita obsahuje sám v sebe obsahuje login, zahashované heslo, e-mailovú adresu, maximálnu prioritu, čas vytvorenia a čas posledného prihlásenia. Status užívatela môže byť aktivný a suspendovaný.

2. **JPAUserPriviledge**  
Entita slúžicia na uloženie rôznych privilégií v systému. Síce privilégia sú pevne dané v enumu `org.pikater.shared.database.jpa.PikaterPriviledge`, ale v budúcnosti môže sa nám hodiť, že privilégie sú uložené aj v databázi.

3. **JPARole**  
Rôzne podmnožiny privilégií sú spojené do rôznych rolí,ktoré sú potom priradené k jednotlivým užívatelom.

##### Týkajúce se datasetů

1. **JPADataSetLO**  
Obsahuje záznamy o datasetoch uložené v databáze. Nejdôležitejší je položka OID, čo obsahuje ID PgLOBu, kde je daný dataset uložený. Položka hash slúži na identifikáciu datasetu pomocou MD5 hashe, čo je vypočítaný pri nahrávaní datasetu. Síce môže existovať viac `JPADataSetLO` entít pre rovnaký hash, ale datasety sú považované za identické pri uploadovaní, takže v databáze sú uložené iba raz a všetky entity obsahujú rovnaký OID.  
Z pohladu rôznych metod strojového učenia metadata datasetov hrajú významnú rolu, pretože na základe nich môžeme odhadnúť podobnosť dvoch datasetov bez jejich načítaní. Z tohto dôvodu tieto metadata sú vypočítané už pri uploadovaní datasetov a sú uložené v odpovedajúcich entitách, ktoré sú spojené s entitou datasetu.

2. **JPAGlobalMetaData**  
Entita pre metadata, ktoré je možné zistiť u každého platného datasetu. Obsahuje dáta o tom, že dataset kolko má dátových riadkov (inštancií) a aké atributy obsahuje

3. **JPAAttributeMetadata**  
Entita pre metadata jednotlivých relací (stĺpcov) v rámci datasetu. Tieto metadata je možné zistiť u každého typu atributu a možné z nich zistiť pomer chýbajúcich hodnot (napr. počet riadkov, bez klasifikácie), triedy entropie, či atribut je cielový a aj pôvodne poradie v datasetu.

4. **JPAAttributeCategoricalMetadata**  
Entita, čo obsahuje dáta špecifické pre kategorické atributy - ktoré obsahujú hodnoty z nejakej množiny, čo je definovaná v hlavičke datasetu - a je odvodená od `JPAAttributeMatadata`. V súčasnej dobe dodefinuje premennú pre počet kategorií daného datasetu.

5. **JPAAttributeNumericalMetadata**  
Táto entita je podobná `JPAAttributeCategoricalMetadata`, odvodená od entity `JPAAttributeMetadata`. Obsahuje metadata pre numerické relácie datasetu. Numerické relácie môžu obsahovať hodnoty s pohyblivou desatinnou čiarkou. Pri vytvorení týchto metadát Weka vypočíta interval týchto hodnot, priemer a rozptyl.

6. **JPATaskType**  
Každý dataset má definovaný typ prednastaveného experimentu. Počítali sme s tým, že množina týchto úloh môže byť v budúcnosti rozšírený, preto sme sa rozhodli pre zavedenie entity, čo môžeme dynamicky vytvárať pre nové neznáme úlohy.

##### Týkajúce se experimentů

Hlavnou úlohou Pikateru je spustenie experimentov strojového učenia. Tieto experimenty sú vytvorené na webovom rozhraní a potom sa dastanú do jadra systému.

1. **JPABatch**  
Celý experiment vytvorený na webovom rozhraní je zakódovaný do XML reťazce, čo je potom spracovaný jadrom. Tento reťazec tvorí obsah jednej premennej v tejto entite. Ďalej entita obsahuje premenná na monitorovanie stavu experiment, dáta o užívatelovi, kto vytvoril daný experiment, časové záznamy o vytvorení, spustení a ukončení experimentu.
 
2. **JPAExperiment**  
Experiment sa pri spracovávaní rozpadne na niekolko menších časti (podexperimenty). Tieto menšie časti sú spracované nezávisle a na monitorovanie časových údajov štartu a ukončenia týchto častí táto entita obsahuje premenná, podobne ako `JPABatch`. Entita má pripojené zoznam štatistik `JPAResult`, ako prebiehal experiment. U jednoduchších podexperimentov tento zoznam obsahuje obvykle iba jednu položku.

3. **JPAModel**  
Táto entita slúži na uloženie natrénovaných modelov experimentu. Na jednoznačnú identifikáciu modelu slúži meno agenta, čo je skrývané v modeli. Staršie modely sú vymazané zo systému. Aby užívatel mohol vybrať, že ktoré modely chce zachovať natrvalo, entita obsahuje premennú na označenie.

4. **JPAResult**  
Táto je entita štatistiky menších částí experimentov (podexperimentov). Obsahuje premenná, kde sú uložené hodnoty, ako názov vytvárajúceho agent, rôzne štatistiky (kappa, rôzne chyby, pomer chýb), nastavenia experimentu, časové údaje štartu a ukončenia (kvôli tomu, že podexperimenty môžu mať pripojené viac výsledkových štatistík), vytvorený model (nemusí každý podexperiment vytvoriť nový model)

5. **JPAExternalAgent**  
Entita popíše agent, ktorý bol pridaný do Pikateru užívatelom. Agent je uložený ako bytové pole v tejto entite. Spolu s nim sú uložené aj dáta o vlastníkovi, názvu, názvu triedy agentu a o povolení agentu v systému.

6. **JPAAgentInfo**   
Obsahuje záznamy o agentoch, ktoré sú k dispozicii v systému Pikater. Hlavným účelom týchto entit, je prevod informácii z jadra Pikateru na webové rozhranie, čo získa dáta z databáze.

##### Obecné/týkajúce sa jádra

1. **JPAFilemapping**  
Jednotlivé záznamy predstavujú, pre každý dataset párovanie jeho povodného názvu na MD5 hash súboru. Tento prístup možno nie je najlepší, preto vytvári akoby duplicitný záznam pre datasetov, ale zachová prevodnú tabulku zo starého Pikateru. Hlavným dovodom možnosti použitia povodných názvov v rámci jednotlivých experimentov je zaistenia povodného prístupu, čo zaistil pridanie experimentu z príkazového riadka z XML súboru, čo obsahuje iba klasické názvy datasetov.

#### DAO objekty

DAO objekty končí na "DAO". Pristupuje sa k nim cez triedu `org.pikater.shared.database.jpa.daos.DAOs.java`.  
Cez DAO objekty získáváme, ukládáme, updatujeme a mažeme entity. Príklad:

```java:/src/org/pikater/shared/database/jpa/Showcase.java```

#### Dotazovanie

<font color="red">TODO</font>

##### JPQL dotazy
##### Pomenované dotazy

Vela dotazov je vyriešená pomocou pomenovaných dotazov, ktoré sú zapísané v entitách pomocou značiek `@NameQueries` a `@NamedQuery`.

##### Criteria dotazy







### Poznámky k JPA

<font color="red">Dokumentaci k JPA a použitým anotacím lze zobrazit v odkazu výše</font>.  

Pro úplnost ještě ovšem doplníme:
* `@Lob` - síce uloženie velkých súborov máme vyriešené pomocou PgLOBov, ale menšie môžeme uložiť aj pomocou JPA anotácie. V tomto prípade premenná sa serializuje a uloží sa do bytového pola - alebo iného kompatibilného typu - v databázovom záznamu. Tento prístup využívame napr. u uložení modelov výpočtov, alebo u dlhých reťazcov XMLov.
* `@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS` alebo `InheritanceType.JOINED`) - značka slúží na zadefinovanie, že ako chceme vytvoriť tabulky relácií pre jednotlivé entity. Nastavenie `TABLE_PER_CLASS` vytvorí vlastnú tabulku so všetkými hodnotami, ktoré v danej entite vyskytujú (vlastné aj zdedené). Nastavenie JOINED vytvorí iba jednu tabulu, pre danú entitu a všetky z nej odvodené. Znamená to, že niektoré položky sú prázdne, ale v niektorých prípadoch je to viac prehladný.
* `@Enumerated(EnumType.STRING)` - u premenných odvodených od java.lang.Enum môžeme zadefinovať, či chceme aby sa do databáze uložil názov hodnoty, alebo jeho poradové číslo (`EnumType.ORDINAL`). Uložiť Enum ako String je výhodnejšie z tohto dôvodu, že pri pridaní novej položky nemusíme brať do úvahy poradie hodnôt. Vymazanie nejakej položky zo zdrojového kódu môže spôsobiť výnimku pri získaní hodnôt z databáze. Na druhej strane pri uložení ako `EnumType.ORDINAL` nemusíme získať výnimku, čo môže sposobiť zákerné chyby, pretože hodnoty enumov sú zle namapované.

