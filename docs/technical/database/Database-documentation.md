<!-- --- title: Technická dokumentace část první: Dokumentace databáze -->

[[_TOC_]]

Pikater v rámci svojej úlohy vykonáva rôzne operácie na vstupných datách a pritom vzniknú dáta rôzneho povahu: napr. pri klasifikácii vznikne nový, už klasifikovaný dataset a štatistika výpočtu. Tieto dáta musia byť niekam uložené a sprístupnené pre prípadné ďalšie experimenty.

## Predslovo

### Použité skratky

* __ARFF__ (Attribute-Relation File Format)  
Formát používaný knižnicou Weka, čo mulitagentný systém Pikateru interne používa.
* __CSV__ Comma-Separated Values)
* __JDBC__ (Java Database Connectivity)
* __SQL__ (Strucutered Query Language)
* __HSQLDB__ (Hyper SQL Database)
* __PgLOB__ (Postgre Large Object)
* [__JPA__](http://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html) (Java Persistence API)
* __DAO__ - Data Access Object
* __ACID__ - Atomicity, Consistency, Isolation, Durability
* __JPQL__ - Java Persistence Query Language

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
| iné           | pôvodne použitý, rozšírená | Java  | rozšírená           |

Síce MySQL podporuje tranzakčné spracovávanie, ale to je dostupné iba pre engine InnoDB, čo je použitelné iba za poplatok. Vzhladom na to, že ostatné dva to ponúkajú zadarmo MySQL už mal docela velkú nevýhodu v porovnaní s ostatnými dvomi.

Medzi HSQLDB a PostgreSQL sme sa rozhodli hlavne na základe prístupu k uloženým bytovým objektom. Súčasné databázové systémy už štandardne umožnia nám používať bytové pole v rámci databázového záznamu, kam môžeme uložit obsah súboru. Jediný problém znamená spôsob prístupu k takto uloženým dátam.
PostgreSQL používá na to vlastný prístup, čo umožní kopírovať dáta z/do databáze pomocou streamov. V HSQLDB k prístupu týmto záznamom máme používať setter a getter, čo pracuje s javovským java.lang.Object triedou. Vzhladom na to, že plánujeme pracovať s docela velkými súbormi, rozhodli sme sa pre PostgreSQL.
Trošku subjektívny pohlad, ale PostgreSQL je rozšírenejší ako HSQLDB, čo teoreticky môže znamenať lepšiu dostupnosť informácií a tým pádom aj jednoduchšie riešenie prípadných problémov.




## Prechod na JPA

Ako sme povedali, pôvodná verzia Pikateru používala prístup viazaný na databázový systém MySQL. Rozhodli sme sa to zmeniť na použitie technológie JPA, čo nám umožnila mapovania záznamov z databáze na javovské objekty a späť.
Obrovskou výhodou JPA je, že objekty pripravené na prácu s JPA nemusia byť zmenené pri zmene databázového systému. Takto pripravené objekty nazývame entitami a najjednoduchšie sú rozpoznatelné na základe značky `@Entity` pred definíciou javovskej triedy.

### Výber connectoru

Rozhodnutie pre JPA znamená, že, okrem databáze a technológie pre prístup k nemu potrebujeme niečo, čo spojuje svet Javy so svetom databáze. V praxi to znamená prevod operácií v JPA na rôzne príkazy v SQL a naopak pre výsledok dotazu vytvoriť odpovedajúce entity. Obvykle výber tohto connectoru neoplyvní vývoj aplikácie, okrem použitia nejakých špeciálnych funkcí. Dva z najrozšírenejších connectorov sú Hibernate a Eclipselink. My sme sa rozhodli pre Eclipselink z dôvodu, že u Eclipselink je možné zmeniť vlastnosti databázového pripojenia zo zdrojového kódu, čo môžeme v budúcnosti využívať.

### Konfigurácia

V starej verzii Pikateru na konfiguráciu databázového pripojenia slúžil iba súbor `Beans.xml`. Pomocu knižnice Spring na základe tohto súboru bol sprístupnený objekt databázového pripojenia. Tento objekt bol získaný pomocou funkcie `ApplicationContext.getBean` .

Pikater stále potrebuje nativný prístup do databáze, kvôli PgLOBom, preto sme sa rozhodli, že zachováme `Beans.xml` súbor a pre plynulejší prechod z hladiska konfigurácie necháme na pôvodnom mieste a to v koreňovom adresári zdrojového kódu.

Časť v súboru `Beans.xml` zodpovedná za vytvorenie databázového pripojenia:
```xml
...
<bean
id="defaultConnection"
class="org.pikater.shared.database.connection.PostgreSQLConnectionProvider"
scope="singleton">
        <constructor-arg index="0">
                  <value><!-- url --></value>
        </constructor-arg>
        <constructor-arg index="1">
                  <value><!-- database username --></value>
        </constructor-arg>
        <constructor-arg index="2">
                  <value><!-- password for database user--></value>
        </constructor-arg>
</bean>
...
```

Okrem natívneho databázového pripojenia potrebujeme, aby správca JPA entít mal takisto prístup k databázi. Okrem toho, musí poznať, že ktoré triedy má považovať za entity. Na túto konfiguráciu slúži súbor `persistence.xml` , čo musí byť uložený v zložke `META-INF`. Tento súbor musí obsahovať zoznam entít v našom programu vo forme, ako to ukáže nasledovný príklad:
```xml
<persistence-unit name="pikaterDataModel" transaction-type="RESOURCE_LOCAL">
<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
…
<class>org.pikater.shared.database.jpa.JPADataSetLO</class>
...
```
Na definovanie databázového pripojenia máme viac možností. Jednak možeme pridať vlastnosti pripojenia do súboru `persistence.xml`. Druhá možnosť je využívať Spring na injektovania správce entít (trieda `EntityManagerFactory`), kde definujeme pripojenie čo sa má používať. 
My sme sa rozhodli využívať iba súbor `persistence.xml` a to z viacerých dôvodov:

* pri vytvorení EntityManagerFactory môžeme v zdrojovom kódu nastaviť vlastnosti pripojenia
* väčšiou výhodou bola sloboda pri vytvorení tried na spravovanie entít.

Pre nás znamenal problém vytvoriť si `EntityManagerFactory` pomocou `Beans.xml` z dôvodu, že Spring interne používá javovské `Proxy` triedy pri vytvorení inštancií jednotlivých beanu. To znamená, že pre každý interface, čo dedí daná trieda je vytvorený jeden `Proxy` objekt, čo musíme pretypovať na typ interfaceu. `EntityManagerFactory` má vela interfaceov od ktorých sa dedí a ani jeden nepokrýva celú funkčnosť triedy a z tohto dôvodu nefunguje prístup pomocou funkcie `ApplicationContext.getBean`. Z podobného dôvodu sme nechceli injektovať `EntityManager`y (získané pomocou `EntityManagerFactory`) do objektov slúžiace na spravovanie entít, lebo v tom prípade tie objekty sú vytvorené takisto pomocou Springu a tým pádom pre každý takýto objekt potrebujeme jeden interface.

Na zjednodušenie vytvorenia konfiguračných súborov je možné používať program `org.pikater.shared.database.util.initialisation.DatabaseInitialisation`, do ktorého môžeme pomocou príkazového riadka zadať potrebné údaje a vygeneruje nám obidva konfiguračné súbory.




## Databáza

### Schéma

#### Pôvodné

[[original_db_schema.png|alt=Original DB schema]]

#### Aktuálné

[[schemaspy_output/diagrams/summary/relationships.real.large.png|alt=Current DB schema]]

#### Rozdiely

Pri vytvorení novej štruktúry entít - a tým pádom aj súčasnú schématu databáze - sme vela zachovali z pôvodnej schémy. Na druhej strane bolo potrebné vytvoriť chýbajúce vzťahy medzi entitami (napríklad experiment má viac podexperimentov) a podklady pre nové funkcie (napríklad uloženie datasetov).

|               |   Pôvodný Pikater   |   Aktuálny Pikater  |
|:-------------:|:-------------------:|:-------------------:|
| datasety      | lokálne na disku    | v databázi          |
| ostatné dáta  | v databázi          | v databázi          |

### PostgreSQL specifika

#### PgLOBy

Po výbere PostgreSQL ako databázový systém pre Pikater sme museli naimplementovať špeciálny prístup k súborom uloženým v databáze.

Súbory sú uložené ako Postgre Large Objekty (PgLOBy, alebo jednoducho LOBy), čo znamená, že je uložený v systémovej tabulke `pg_largeobject` danej databáze. Pre každý súbor je vytvorené množstvo záznamov, ktoré obsahujú súbor rozsekaný na niekolko byteových polí. Pri získaní dát alebo zapisovaní do databáze ale vystačíme s ID daného PgLOBu (oid). Napríklad v entite `JPADatasetLO` máme jednu premennú typu `long`, čo obsahuje OID.

Síce môžeme u premenných používať v JPA entitách anotáciu `@Lob` (naznačí, aby danú premennú JPA uložil do bytového pola), nemáme istotu, že velké súbory budú optimálne vyriešené. V najhoršom prípade môže sa nám stať, že celý obsah súboru je kopírovaný do pamäte a potom je prenesený do databáze.

Triedy pracujúcie s PgLOBy sa nachádzajú v balíčku `org.pikater.shared.database.postgre.largeobject`.

### Práca s DB frameworkom

Framework sa nachádzí v balíčku `org.pikater.shared.database`, rozdelenie na podbalíčky by malo byť vcelku jasne. Je ale potreba trochu popísať balíček `org.pikater.shared.database.jpa` - hlavné sú **entity** a **dao objekty**.

#### Entity

Entity začínajú na "JPA" (napríklad `JPAUser`). Stručne ich popíšeme.

V súčasnej verzii pre Pikater máme 16 vytvorených entít, ktoré sa nachádzajú v balíčku `org.pikater.shared.database.jpa`. Každá je odvodená od abstraktnej entity `JPAAbstractEntity`. Tento spoločný predok obsahuje iba ID slúžiace ako primárny klúč v databáze.

##### Týkajúce se uživatelů

Základ systému. Uživatelia sa primárně vytvárajú na webu.

1. **JPAUser**  
Entita užívatela. V entitách, kde sme potrebovali uložiť informáciu o vlastníkovani, je referencovaná inštancia tejto entity. Entita obsahuje sám v sebe obsahuje login, zahashované heslo, e-mailovú adresu, maximálnu prioritu, čas vytvorenia a čas posledného prihlásenia. Status užívatela môže byť aktivný a suspendovaný.

2. **JPAUserPriviledge**  
Entita slúžicia na uloženie rôznych privilégií v systému. Síce privilégia sú pevne dané v enumu `org.pikater.shared.database.jpa.PikaterPriviledge`, ale v budúcnosti môže sa nám hodiť, že privilégie sú uložené aj v databázi.

3. **JPARole**  
Rôzne podmnožiny privilégií sú spojené do rôznych rolí,ktoré sú potom priradené k jednotlivým užívatelom.

##### Týkajúce sa datasetov

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

##### Týkajúce sa experimentov

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

##### Obecné/týkajúce sa jadra

1. **JPAFilemapping**  
Jednotlivé záznamy predstavujú, pre každý dataset párovanie jeho povodného názvu na MD5 hash súboru. Tento prístup možno nie je najlepší, preto vytvári akoby duplicitný záznam pre datasetov, ale zachová prevodnú tabulku zo starého Pikateru. Hlavným dovodom možnosti použitia povodných názvov v rámci jednotlivých experimentov je zaistenia povodného prístupu, čo zaistil pridanie experimentu z príkazového riadka z XML súboru, čo obsahuje iba klasické názvy datasetov.

#### DAO objekty

DAO objekty končí na "DAO". Pristupuje sa k nim cez triedu `org.pikater.shared.database.jpa.daos.DAOs.java`.  
Cez DAO objekty získáváme, ukládáme, updatujeme a mažeme entity. Príklad:

```java:/src/org/pikater/shared/database/jpa/Showcase.java```

Dôvodom využitia tohto prístupu je spôsob, akým JPA pracuje s entitami. S každou JPA entitou môžeme pracovať ako klasickým java objektom. Môžeme volať ich funkcie, zmeniť premenná. Po tom, ako entitu uložíme do databáze - v JPA terminológii persistujeme - zmeny prevedené na entite sú odzrkadlené do databáze. Jedinou podmienkou je, aby entita bola ešte stále v persistovanom kontextu. Na druhej strane je lešie mať tento kontext čo najmenší, aby sme nemali konflikty medzi entitami.

Riešením je vytvorenie špeciálnych objektov, ktoré ponúkajú rôzne funkcie, ktoré sa dá používať na entity. Obvykle pre každý typ entity máme jeden takýto objekt, ale v niektorých prípadoch funkcia vykoná zmeny na viacerých objektoch. Díky podpory tranzakcí tieto zmeny splňajú podmienku ACID. Tieto objekty obvykle nazývame Data Access Objecty (skrátene DAO objekty) . Každú zmenu na entitách vykonávame pomocou týchto DAO objektov a pritom nemusíme riešiť problematiku persistence contextu.

#### Dotazovanie

Jeden z hlavných dôvodov využitia databáze ako úložísko dát, je možnosť efektívne vyhladávať na základe nejakých podmienok medzi záznamami. DAO objekty, okrem základných operácií, ponúkajú funkcie, ktoré umožnia jednoducho previesť zložitejšie dotazy.

##### JPQL dotazy

JPQL majú syntax velmi podobný klasickým dotazom v SQL. V programu možeme dynamicky vytvoriť takýto dotaz pomocou jeho vygenerovania do reťazce.

##### Pomenované dotazy

Pomenované dotazy sú predpripravené dotazy v jazyku JPQL, ktoré sú uložené buď v zdrojovom kódu enity, alebo v externom konfiguračnom súboru. Toto riešenie má velkú silu kvoli tomu, že dotaz v sebe može obsahovať parametry, ktoré v zdrojovom kódu možeme nastaviť podla potreby.

Vela dotazov Pikater je vyriešený práve pomocou pomenovaných dotazov, ktoré sú zapísané v entitách pomocou značiek `@NameQueries` a `@NamedQuery`.

Príkladom takéto dotazu môže byť nasledovný, čo slúží na získanie datasetov, ktoré boli pridané užívatelom `:owner`, čo je parametrom dotazu.
```java
@NamedQuery(name="DataSetLO.getByOwner",query="select dslo from JPADataSetLO dslo where dslo.owner=:owner")
```
Objekt `DataSetDAO` pomocou volania funkcie `AbstractDAO.getByTypedNamedQuery("DataSetLO.getByOwner", "owner", user)` doplní na miesto parametra odpovedajúci objekt a získa odpoveď.

##### Criteria dotazy

Criteria API je rozhranie pre vytvorenie dotazov na JPA entitách. Pomocou neho môžeme vytvoriť dotazy, ktoré majú viac parametrov a dokonca počet parametrov závisí na nejakej premennej. Príkladom može byť nasledujúci dotaz, čo vráti všetky datasety, ktoré majú hash nevyskytujúci sa v zoznamu.

```java
     	CriteriaBuilder cb= em.getCriteriaBuilder();
 		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
        Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
        
        Predicate p=cb.conjunction();
        for(String exHash:hashesToBeExcluded){
            p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
        }
        p=cb.and(p,cb.isNotNull(r.get("globalMetaData")));
        cq=cq.where(p);
```

Dotazy pomocou Criteria API sme vytvorili hlavne pre webové rozhranie, kde je potrebné používať viac parametrov kvôli možnsti zoradenia podla jednotlivých stĺpcov.



### Poznámky k JPA

Dokumentaci k JPA a použitým anotacím lze zobrazit v odkazu výše. Pre úplnosť samozrejme ešte doplníme:
* `@Lob` - síce uloženie velkých súborov máme vyriešené pomocou PgLOBov, ale menšie môžeme uložiť aj pomocou JPA anotácie. V tomto prípade premenná sa serializuje a uloží sa do bytového pola - alebo iného kompatibilného typu - v databázovom záznamu. Tento prístup využívame napr. u uložení modelov výpočtov, alebo u dlhých reťazcov XMLov.
* `@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS` alebo `InheritanceType.JOINED`) - značka slúží na zadefinovanie, že ako chceme vytvoriť tabulky relácií pre jednotlivé entity. Nastavenie `TABLE_PER_CLASS` vytvorí vlastnú tabulku so všetkými hodnotami, ktoré v danej entite vyskytujú (vlastné aj zdedené). Nastavenie JOINED vytvorí iba jednu tabulu, pre danú entitu a všetky z nej odvodené. Znamená to, že niektoré položky sú prázdne, ale v niektorých prípadoch je to viac prehladný.
* `@Enumerated(EnumType.STRING)` - u premenných odvodených od java.lang.Enum môžeme zadefinovať, či chceme aby sa do databáze uložil názov hodnoty, alebo jeho poradové číslo (`EnumType.ORDINAL`). Uložiť Enum ako String je výhodnejšie z tohto dôvodu, že pri pridaní novej položky nemusíme brať do úvahy poradie hodnôt. Vymazanie nejakej položky zo zdrojového kódu môže spôsobiť výnimku pri získaní hodnôt z databáze. Na druhej strane pri uložení ako `EnumType.ORDINAL` nemusíme získať výnimku, čo môže sposobiť zákerné chyby, pretože hodnoty enumov sú zle namapované.

