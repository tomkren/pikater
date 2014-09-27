<!-- --- title: Technická dokumentace část druhá: Dokumentace jádra systému -->

[[_TOC_]]

## Úvod

Pikater je multiagentní systém vyvíjený týmem Mgr. Romana Nerudy, CSc. na Matematicko-fyzikální fakultě Univerzity Karlovy a je úzce spojen s Ústavem informatiky Akademie věd České Republiky. Systém byl vyvíjen za účelem vytvořit výpočetní platformu pro akademický výzkum v oblasti strojového učení. Prošel mnoha podobami. Využíval se jako výpočetní model pro výzkum v oblasti umělé inteligence na Edinburgské univerzitě, rovněž jako výukový program poskytující úvod do problematiky umělé inteligence pro studenty středních škol. Zároveň slouží jako univerzální prostředí pro ověření správnosti nových nápadů členům katedry.

### Motivace

Procesy strojového učení patří mezi výpočetně velmi náročné úlohy. Je mnoho způsobů, jak učit model rozhodovat obecné problémy a není snadné predikovat, který způsob strojového učení je pro daný typ problému optimální. Problém volby optimální metody strojového učení je v systému řešen analýzou vstupních dat. Na základě podobnosti vstupních datových množin je z již vypočtených výsledků v systému vybírána metoda strojového učení, která byla v minulých pokusech nejúspěšnější. Metody strojového učení poskytují mnoho parametrů, jimiž lze přímo ovlivnit proces učení, testování i validace. Pikater se snaží vhodnými způsoby procházení stavového prostoru parametrů:
* ušetřit strojový čas učení,
* zvolit vhodný poměr explorace a exploatace pro zajištění konvergence procesů učení co možná nejblíže k lokálnímu optimu.

### Technologie

**JADE**  
Systém je postaven nad multiagentní middleware platformou JADE vyvíjenou italským Telecomem. Jedná se o vhodnou platformu pro modulární členění jednotlivých metod strojového učení. Hlavní přednosti jsou:
* Již vyřešená infrastruktura a snadná integrace nových agentů (metod strojového učení, komponent a tak dále).
* Jednoduché uložení/načtení naučených modelů - uspávání/probouzení agentů.
* Komunikační mechanismy, které transportují zprávy mezi agenty.

**JADE gateway**  
Brána do systému. Umožňuje poslat agentovi zprávu z prostředí mimo JADE.

**WEKA**  
Sada předprogramovaných algoritmů a datových struktur pro strojové učení. Ve většině případů využívají výpočetní agenti Pikateru nějakým způsobem rozšířené metody z knihovny WEKA.

**JPA**  
JPA framework je použit pro ukládání JAVA objektů pomocí objektově relačního mapování, nezávisle na užitém databázovém systému. Jedinou výjimku z tohoto pravidla tvoří ukládání velkých souborů - projekt využívá databázi PostgreSQL, která soubory ukládá do tzv. "LargeObjektů".

**XStream**  
Knihovna pro serializaci/deserializaci JAVA objektů z/do XML.

### Terminologie

**Jádro**  
Jádro je "výpočetní jednotka" projektu a zároveň celý původní projekt Pikater. Spolupracuje s webovým GUI a udržuje jisté standardy, aby ho bylo možno propojit s jinými platformami multiagentních, případně expertních systémů. Speciálním typem použití je pak nasazení na superpočítač a ke kompilaci nejsou proto potřeba žádné externí knihovny.
Tento dokument se zabývá právě dokumentací jádra.

**Dataset**  
Obecný termín pro datovou množinu.

**Experiment**  
Logická struktura zahrnující vstupní datasety, popis jak se mají využít a co se má vypočítat. Dělí se na "úlohy".

**Úloha**  
Dále nedělitelná podčást experimentu, nezávislá na dalších úlohách.

**Dávka**  
Experiment přetransformovaný do seznamu úloh.

**Hledače**    
Agenti, jejichž účel je generovat a hledat optimální parametry dané metody strojového učení v rámci aktuálního 
experimentu.

**Doporučovače**  
Agenti, jejichž účel je vybírat co nejlepší metodu strojového učení v závislosti na vstupních datech aktuálního problému. Jako metrika vhodnosti je vybrána podobnost vstupních dat aktuálního problému a předchozích problémů.

**Ontologie**    
Obecný termín pro zprávu (JAVA objekt) posílaný mezi agenty prostřednictvím komunikačních mechanismů platformy JADE.

**Krabička**  
Označení pro autonomní část experimentu (z logického úhlu pohledu). Experimenty jsou ve webovém rozhraní definovány pomocí "spojování krabiček".

## Původní jádro

Funkce původního systému jsou:
* Efektivní procházení stavového prostoru parametrů jednotlivých algoritmů.
* Využití doporučovačů.
* Zpracování experimentů představující jednu úlohu (trénování a testování, případně ukládání modelu). Úlohy mohly být spouštěny postupně na několika vstupních datasetech a s různými parametry buďto zadanými uživatelem nebo vygenerovanými nějakým hledačem.
* Bylo možné si vybrat konkrétní metodu strojového učení, evaluační metodu a nastavit parametry ovlivňující řízení běhu výpočtu. Nebyla-li metoda strojového učení uživatelem zadána, musel být vybrán nějaký doporučovač.
 
## Nové jádro

V této kapitole pojednáme o změnách jádra oproti původní verzi.

### Kompletní přestavba

Z podoby připomínající skript, který se spouštěl z příkazové řádky má dnes Pikater podobu interaktivní aplikace podporující jak konzolový, tak i grafický režim (přes webové stránky).

Z hlediska struktury kódu byl pro jádro vytvořen balíček `org.pikater.core`, ve kterém se jednotliví agenti dělí na základě jejich funkce na "systémové" a "experimentové". Systémoví agenti se starají o běh systému a zajišťují infrastrukturu experimentovým agentům.

Došlo k přeuspořádání agentů, jehož vedlejším efektem bylo téměř perfektně bijektivní zobrazení experimentových agentů na zadefinované krabičky. Hlavní změny:
* Spojení agentů `Manager` a `OptionManager`.
* Rozdělení jediného výpočetního agenta obsahujícího všechny algoritmy strojového učení na několik samostatných výpočetních agentů (za použití vhodného polymorfismu).
 
Kompletní přestavbou prošla také většina ontologií. Byly hierarchizovány do jmenných prostorů (se snahou udržet mapování na agenty), a většina i zobecněna nebo rozšířena. Samozřejmě také přibylo mnoho nových, aby systém splnil specifikaci projektu.

Dále došlo k výměně ontologie `Option`. Původní verze využívala k reprezentaci informací výlučně textové řetězce. Nová verze je obecnější (typovaná), rozšiřitelná, validovatelná a lze do ní ukládat omezení na hodnoty pro každý typ.

Všechna sdílená data jsou nyní ukládána do objektově-relační databáze, kterou využívá i webové rozhraní.

**Schéma nové architektury**

[[/docs/technical/architektura-final.png]]

### Dávky

Motivací pro tuto funkci byla podpora paralelního spouštění úloh, zrychlení a zrobustnění výpočtů. Pro reprezentaci dávky byla v jádře systému vytvořena ontologie `ComputationDescription`.

### Univerzální formát

Nová struktura sloužící jako obecný prostředník mezi interními formáty experimentů jádra (dokonce i konzolových GUI agentů) a webové nadstavby. Univerzální formát tedy:
* představuje formát, v němž jsou experimenty ukládány do databáze,
* definuje experimenty (nepřímo) jako grafické krabičky spojené hranami a lze do něj uložit speciální informace webové reprezentace (aktuálně jsou pouze vyžadovány pozice krabiček).
* je navržen, aby byl rozšiřitelný oběma směry (pro jádro i webvé rozhraní) a snaží se o vzájemné odstínění.

Aktuální návrh vychází z myšlenky obalit vnitřní ontologie dávky, přičemž až na jisté výjimky se každý takový obal nepovinně odkazuje na třídu s informacemi pro GUI.

V kódu ho lze nalézt v balíčku `org.pikater.shared.experiment`. Třída reprezentující celou strukturu se nazývá `UniversalComputationDescription`.

### Zrychlení

Původní jádro mělo dva stěžejní výkonnostní problémy:

1. **Sekvenční počítání**  
Vyřešeno pomocí distribuovaného počítání a efektivního plánování, které se snaží zbytečně nevytěžovat síťovou infrastrukturu přesouváním velkých dat.
2. **Přenos dat prostřednictvím ACL zpráv**  
Řešení je vysvětleno dále v dokumentu.

#### Přenos dat

Jedním z požadavků na nový systém bylo zefektivnění datových přenosů mezi agenty. Tato kapitola popisuje způsob reprezentace a distribuce dat a kroky provedené pro optimalizaci přenosů.

**Reprezentace dat**  
Vnitřní reprezentace uložených datasetů má podobu formátu ARFF - nativního formátu knihovny WEKA. ARFF soubor je v podstatě textový CSV soubor obohacený o metadata v podobě anotací. Podrobnou dokumentaci lze nalézt zde:  
<http://weka.wikispaces.com/ARFF>

Využití tohoto formátu bylo upřednostněno před zavedením vlastního nového formátu z několika důvodů:
* Abychom nemuseli měnit existující "parsery".
* Pro jeho kvalitní dokumentaci a přívětivost jak pro lidské, tak i strojové zpracování.

Na druhou stranu, formát se nezabývá kompresí a soubory tedy mají tendenci být poměrně velké. Problém sice lze řešit dodatečnou kompresí, ale na úkor výkonu.

V paměti (a systému) jsou datasety reprezentovány ontologií `DataInstances` (datovými instancemi). Je to obálka pro data převoditelná z a do formátu zpracovatelného knihovnou WEKA (`weka.core.Instances`).

**Řešení přenosu**  

V původním Pikateru se načtená data předávala výpočetním agentům přímo ACL zprávami, což se ukázalo jako nanejvýš neefektivní – potenciálně velká data musela projít drahou serializací a deserializací, dokonce i v rámci jednoho JADE kontejneru.

Nyní jsou data přenášena z databáze přímo do agenta dedikovaného pro přístup k databázi, který se vyskytuje v každém kontejneru. Aktuální systém přepokládá výskyt právě jednoho slave kontejneru na jednom fyzickém stroji.

V rámci daného kontejneru (stroje) jsou pak data přenášena tzv. "object to agent" rozhraním, které platforma JADE poskytuje. K přenosu dat vlastně dojde pouze předáním odkazu do stejného adresního prostoru, bez zbytečné serializace nebo kopírování dat.

Pro zefektivnění přenosů mezi různými stroji pak bylo zavedeno cacheování dat. Data se na každém stroji zachovávají (nemažou) a pokud již jsou na daném stroji k dispozici, nepřenášejí se systémem znovu.

Všimněte si ovšem, že některá data se stále přenášejí ACL zprávami, protože jde typicky o malé soubory (do 5 MB) a jejich přenos nepředstavuje riziko velkého vytížení infrastruktury. Konkrétně jsou to:
* __Serializované modely__  
Typicky mívají do 100 kB, jsou doručovány binárně (bytová pole) v podobě serializované JAVA třídy (agenta). Serializaci/deserializaci zajišťuje WEKA.
* __JAR soubory__  
Externí agenti a metody strojového učení (definované uživateli).

I tato data se cachují.

### Interakce s webovým GUI

Jádro webovému rozhraní poskytuje:
* Seznam aktuálně zadefinovaných krabiček pro grafický editor experimentů, s veškerými doprovodnými informacemi (parametry metod strojového učení a tak dále). Pro tento účel byla vytvořena ontologie `AgentInfo` reprezentující jednu krabičku.
* API přejímající informace o událostech, např. "byl vydán příkaz ke spuštění této dávky".

Od webového rozhraní nevyžaduje jádro naopak nic - musí i nadále pracovat jako nezávislá jednotka.

## Popis agentů

### Systémoví agenti

#### Agent_Initiator

Zajišťuje start celého systému a všech agentů uvedených v konfiguračním souboru `core/Configuration.xml`.

Agent je naprogramován dostatečně obecně a může jádro spouštět jak na master containeru (kde se spravují systém a výpočty), tak i na slave containerech, které by měly převážně být vytěžovány výpočetními agenty. Způsob spouštění systému je stejný ve všech případech, liší se jen iniciační parametr a jméno konfiguračního souboru. V případě slave containeru se přidává ještě druhý parametr označující pořadové číšlo vytvářeného slave containeru.

Postup pro spuštění agenta z konzole:

1. Spustit JADE.
2. <font color="red">Do primárního nebo slave containeru umístit inicializačního agenta s parametrem cesty ke konfiguračnímu souboru (v případě Slave containeru s druhým parametrem udávajícím číslo Slave containeru). Konfigurační soubor obsahuje jméno třídy (včetně celého balíčku spouštěného agenta), jeho jméno a seznam parametrů. Pomocí druhého parametru se zajišťuje pouze jednoznačnost pojmenování agentů v rámci všech containerů. Ulehčuje se tím práce uživateli, jemuž pro spuštění několika Slave containerů stačí pouze jeden konfigurační soubor. Druhý parametr označující číslo containeru si agent Iniciator přidá jako sufix ke všem jménům v konfiguračním souboru. Nejpohodlněji se dá systém ovšem zapnout pomocí bash skriptů. Jeden zajišťuje buildování jádra systému, další spouštění hlavního containeru a poslední spouštění slave containeru s daným číselným parametrem. Tyto skripty volají pouze ANT na souboru buildCore.xml. Z kompilace jsou záměrně vyloučeny Balíčky potřebné pouze pro běh webové nadstavby, protože pro ni jsou potřeba knihovny dostupné přes Apache Ivy. Důvodem, proč se i pro kompilaci jádra nepoužívá Ivy, je přání zadavatelů, aby jádro systému Pikater šlo zkompilovat přímo ze zdrojových kódů bez nutnosti připojení k internetu (z bezpečnostních důvodů nebývá na superpočítačích k dispozici).</font>

#### Agent_ManagerAgent

Tento agent se nachází na každém containeru právě jednou. Na master i slave containeru má funkci vládce.

<font color="red">TODO: co s tímhle? A je to vůbec správně?
"Systém předpokládá, že na jednom počítači běží pouze jeden container"</font>.

Přijímá následující ontologie bez odpovědi:
* `CreateAgent`  
Signál pro vytváření agenta specifikovaného ve zprávě obsahující textový řetězec "type" (plné jméno třídy agenta), textový řetězec "name" (udávající jméno agenta), ontologii `Arguments` (poskytuje argumenty agenta).
* `KillAgent`  
Signál pro ukončení agenta specifikovaného ve zprávě.
* `SaveAgent`  
Signál pro uložení již natrénovaného modelu (agenta).
* `LoadAgent`  
Signál pro oživení již natrénovaného modelu (agenta).

Agenti se ukládají i oživují ze souborů umístěných na filesystému. Přepravu souborů z filesystému do databáze nebo na jiný stroj řeší modul pro přepravu dat.

Přijímá následující ontologie s odpovědí:
* `GetComputerInfo`  
Odpovídá zasláním informací o počítači, na kterém běží. Vrací ontologii `ComputerInfo`, která obsahuje například počet jader procesoru.
* `Ping`  
Odpovídá zasílateli, aby bylo vidět, že container "žije".

**Vytvoření agenta**  

{{{{{{
Manager->+ManagerAgent: CreateAgent (type)
activate Manager
ManagerAgent->ManagerAgent: starts new agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

**Vytvoření agenta s natrénovaným modelem**  

{{{{{{
Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

#### Manager

Nejdůležitější agent celého systému. Přejímá, transformuje, přeposílá a řídí zadané výpočty.

Přijímá následující ontologie bez odpovědi:
* `NewBatch`  
Signál pro spuštění nové dávky. Obsahuje jednoznačný identifikátor dávky a uživatele, který ji zadal.
* `LoadBatch`  
Signál pro načtení dávky. Dávka je získána z agenta `DataManager`, ve formě ontologie `Batch` obsahující všechny relevantní informace.

<font color="red">Rámcový postup pro spuštění nové dávky:</font>

1. Třídou `Parser` je dávka transformována na orientovaný a potenciálně cyklický multigraf.
2. Multigraf je rozčleněn na části a protože jsou tyto části sdíleny mezy agenty, jsou jim následně přiřazeny identifikátory (JADE nezachovává při tomto druhu sdílení JAVA objektů reference).
3. ... a tak dále. Destilovat z textu níže:

**Parsování elementů**  

<font color="red">
WTH? Guys, this is NOT Javadoc... You will only confuse the reader by shoving all this in his face. As for me, I tried to make myself read it several times and each time I just failed horribly!  
Keep it nice, simple and fluent. Use text structuring to help your reader understand what's going on and try to explain in small steps... not one big hell of a mess like this.</font>

Při parsování logického multigrafu obsahující reprezentaci experimentů i ontologie, které mají pouze logický význam pomíjející. Problémy distribuovanosti výpočtu, problémy datových závislostí, skrývající problém vhodného pořadí spouštění tasku a přepravu dat na potřebná místa. V průběhu pársování si Manager vytváří závislostní graf reprezentovaný třídou ComputationGraf. Vrcholy závislostního grafu, reprezentované pomocí tříd ComputationNode, DataProcessingComputationNode, RecommenderComputationNode, SearchComputaionNode a ModelComputationNode.

Mezi uzly vzájemně sdílenou strukturu front, velmi blízce připomínající problém a návrhový vzor Producent Konzument. Do systému front tvořených potomky třídy AbstractComputionBuffer se přidávají informace už v průběhu parsování Ontologie ComputationDescription, například logické jméno vstupního datasetu z Ontologie FileDataInput přeložené na interní jméno neboli hash, Optiony upravující způsob běhu Searche agenta a další Optiony, přičemž jeden z nejdůležitějších Optionů je typ agenta. Touto cestou se postaví závislostní graf včetně sdílených front, nainicializuje se potřebnými informacemi.

Všechny fronty cyklicky prochází, zda jsou k dispozici všechna data pro zpracování dalšího kroku výpočtu. V průběhu parsování Batche se poprvé v systému zjistí, kolik logických experimentů v sobě aktuální ComputationDescription obsahuje, tudíž v parseru dochází k lobování existence nových experimentů. V okamžiku dokončení experimentu se za použití návrhového vzoru Observeru zalogovává (opět použitím zprávy DataManagerovi) změna statusu experimentu na Finished. 

Pro změnu statusu Batche se používá opět odesílání Ontologie DataManagerovi, v tomto případě Ontologie UpdateBatchStatus. Pro změnu statusu experimentu se využívá Ontologie UpdateExperimentStatus. Experimentům se na rozdíl od Batchů Managerovi pouze nemění statusy, ale experimenty se pomocí Ontologie SaveExperiment za pomoci DataManagera v systému i vytváří.

Pro zpracování jednotlivých vrcholů závislostního grafu se využívají tzv. strategie. Strategie jsou třídy, které na základě příchozích dat upravují informace uložené ve struktuře front. Mezi nejintuitivnější patří CAStartComputationStrategy, která řídí strukturu front výpočetního agenta. Pokud jsou k dispozici všechna data a informace, vyprodukuje z nich jeden Task. Mezi další strategie patří RecommenderStartComputation, která zajišťuje komunikaci s Recommendrem, a následně za použití strategie výpočetního agenta vygeneruje opět jeden Task. Strategie SearchStartComputationStrategy zajišťuje komunikaci se Searcherem, který Managerovi vrací parametry, na nichž se postupně spouští strategie výpočetního agenta. Nejobecnější strategie je strategie DataProcessecingStrategy, která pouze čeká na všechna potřebná data pro vytvoření jedné Ontologie Task. Strategiemi vyprodukované Tasky se předávají ke zpracování agentovi Plannerovi.

V okamžiku, kdy se všechny fronty vyprázdní a Manager obdrží od plánovače výsledky všech Tasků příslušících aktuálnímu Batche, jsou již statusy všech experimentů dokončeny, výpočet ComputationDescription končí a jako poslední krok se změní status Batche na Finished.

#### Agent_Planner

Slouží k efektivnímu plánování příchozích úloh (prostřednictvím ontologie `Task`), které představují jeden problém pro jednoho výpočetního agenta nebo data processing.

Důvodem, proč vznikl tento agent, je snaha maximálně odstínit problematiku spouštění úloh od parsování dávek a řízení výpočtů. Distribuované výpočty (paralelní spouštění úloh) přináší do systému poměrně velký zmatek:
* Úlohy jsou spouštěny na několika containerech umístěných na různých strojích.
* V každém containeru může zároveň běžet několik úloh.

Je třeba vyřešit vzniklý problém přepravy dat mezi agenty.
* Běží-li výpočty pouze na jednom stroji, můžeme využít sdíleného filesystému.
* Přeprava dat mezi containery probíhá prostřednictvím `DataManager` agenta - jeden existuje pro každý container. Velká data jsou přepravována přes síťové sockety a tedy mimo JADE.

Přijímá následující ontologie bez odpovědi:
* `ExecuteTask`  
Signál pro zadání jedné úlohy (pro jednoho agenta). Přijaté úlohy se řadí do datové struktury `WaitingTaskQueues` představující skupinu prioritních front pro úlohy, které čekají na zpracování. Po každém doručení nové úlohy zároveň dochází k přeplánování.
* `KillTasks`  
Signál pro "ukončení" úloh, týkajících se jisté dávky. Plánovač vyřadí ze struktury front ty z úloh, které odpovídají ID dávky a které ještě nebyly spuštěny. Nedopočítané úlohy nejsou nijak rušeny a úspěšně doběhnou.

Přijímá následující ontologie s odpovědí:
* `GetSystemLoad`  
Odpovídá zasláním informací o aktuálním vytížení systému - aktuální využití všech CPU jader v rámci celého slave containeru. Informace jsou získány z třídy `CPUCoresStructure`, s jejíž pomocí funguje rozvrhování úloh. 

Rozvrhování se spouští kdykoli agent detekuje změny, které by mohly ovlivnit optimalitu rozvržení zadaných úloh na dostupné zdroje. Typickými zástupci takových podnětů jsou příjem nové úlohy a skončení úlohy.

Proces plánování: <font color="red">(tohle chce speciálně zkontrolovat...)</font>

1. Zjistíme, zda je k dispozici alespoň jedna úloha - pokud ano, vybere se ta z nich, která má nejvyšší prioritu. Nikoliv pouze nejvyšší celkovou prioritu, nýbrž i prioritu zohledňující průchodnost systému pro (podle predikce) krátce trvající dávky. Jako první se tedy vybírají úlohy s nejvyšší prioritou, které zároveň nebudou trvat dlouho.
2. Vyhledají se nové, v plánovači dosud nezaregistrované slave servery. Zároveň s tím musí proběhnout změna struktury `CPUCoreStructure`, kam přibude informace o nových volných CPU jádrech.
3. Všem registrovaným agentům `AgentManager` se pošle ontologie `Ping`, abychom ověřili dostupnost a připravenost containerů pro počítání.
4. V případě, že nějaký container "umřel", musí být všechny jeho nedopočítané úlohy navráceny do struktury front.
5. Pro vybranou úlohu se ze třídy `DataRegistry` (pro každou vstupní datovou závislost) určí seznam strojů, na nichž se soubor nachází, a zjistí se tak, jsou-li k dispozici všechna data. Pokud ano, plánovač se pokusí úloze přiřadit volné procesorové jádro na stroji, kde jsou alespoň nějaká data potřebná pro její výpočet.
6. Nejsou-li vstupní data aktuálně dostupná na žádném stroji, plánovač se pokusí znovu spustit úlohy, které měly již dříve tato data spočítat, vybraná úloha je navrácena zpět do struktury front a znovu dojde k přeplánování.
7. Pokud jsou všechna data k dispozici a pro výpočet existuje volné jádro, požádá plánovač agenta `ManagerAgent` na stroji, kde se nachází dané CPU jádro, aby vytvořil nového výpočetního agenta potřebného typu a zaslal plánovači v odpovědi jeho jméno.
8. Plánovač vytvořenému agentovi pošle vybranou úlohu a označí vybrané výpočetní jádro za obsazené.
9. Po dokončení úlohy se plánovači vrátí informace o spočtených souborech. Plánovač si je uloží do třídy `DataRegistry` a odešle vypočtenou úlohu zpět agentovi `Manager`.

Agenti po dokončení výpočtu jim přiřazené úlohy a navrácení výsledku sami "zemřou".

#### AgentInfoManager

Slouží ke sbírání informací (instancí třídy `AgentInfo`) o experimentových agentech. Využívá je jak webové GUI, tak i doporučovači. Až na výjimky se dá říci, že jeden agent vždy produkuje jednu instanci `AgentInfo`, která odpovídá jedné grafické krabičce.

Přijímá následující ontologie s odpovědí:
* `GetAgentInfo`  
Obsahuje ID uživatele, pro něhož se má sestavit seznam přípustných krabiček (instancí třídy `AgentInfo`) - ty jsou navráceny v odpovědi. Odpověď obsahuje jak defaultní agenty nahrané na GitHubu, tak i externí agenty nahrané do aplikace samotným uživatelem.

Přijímá následující ontologie bez odpovědi:
* `NewAgent`  
Signál pro zaregistrování nově uploadnutého externího agenta do systému. Z příchozí zprávy je vzato plné jméno nového agenta, agent se vytvoří a do databáze je pro něj uložena struktura `AgentInfo` (prostřednictvím agenta `DataManager` a ontologie `SaveAgentInfo`). Ihned poté je agent ukončen.

Tímto způsobem si agent vždy při zapnutí ověřuje, že jsou v databázi uloženy informace o všech experimentových agentech. Pakliže ne, stejným postupem je struktura `AgentInfo` reinicializována pro každého takového agenta.

#### Agent_KlaraGUIAgent

Agent je pojmenovaný po Mgr. Kláře Peškové, autorce původního Pikateru. Vznikl jako další "entry-point" do aplikace, nezávislý na webové nadstavbě, ze kterého by byla dostupná veškerá funkcionalita jádra.

Po zapnutí agenta se spustí jednoduchý komunikační automat, který umožňuje v konzolovém režimu zadat novou dávku, přidat nový dataset a vygenerovat pro něj metadata. Nová dávka se načítá v podobě instance třídy `ComputationDescription`, serializované do XML souboru na filesystému. Konverze formátu z/do JAVA objektů zajišťuje knihovna XStream.

Agent komunikuje s agentem `GUIComunicator`, jehož snahou je vytvořit jednotné přístupové rozhraní pro všechny takové agenty. V praxi si tedy může každý vytvořit vlastního "GUI agenta" a vlastní formát pro serializaci třídy `ComputationDescription`.

#### Agent_Mailing

Nový agent. Zprostředkovává zbytku systému "emailovací službu". Komunikuje s lokálním SMTP serverem.

Přijímá následující ontologie bez odpovědi:
* `SendEmail`  
Signál pro zaslání emailu specifikovaného zprávou na jednu emailovou adresu. 

Aktuálně je agent je v systému využíván pouze při dopočtení dávky.

**Odesílání notifikace o dokončeném výpočtu**  

{{{{{{
Agent\nManager->+Agent\nDataManager: updateExperimentStatus
opt new experiment status = FINISHED
activate Agent\nManager
Agent\nDataManager->+Agent\nMailing: SendEmail
  Agent\nMailing->Agent\nMailing: sends e-mail notification\nvia local SMTP server
  Agent\nMailing-->>-Agent\nDataManager: 
end
Agent\nDataManager-->>-Agent\nManager:
}}}}}}

#### Agent_ARFFReader

Agenta je využíván k načítání ARFF souborů z filesystému a transformaci načtených dat na ontologie.

Přijímá ontologii `GetData` obsahující jméno souboru a jako odpověď zasílá ontologii `DataInstances`.

**Načítání dat**  

{{{{{{
ComputingAgent->+ARFFReader: GetData
activate ComputingAgent
opt data not cached
  ARFFReader->+DataManager:
  DataManager->DataManager: gets data from DB,\nstores them locally
  DataManager-->>-ARFFReader:
end
ARFFReader->ARFFReader: loads data
ARFFReader-->>-ComputingAgent: (data - O2A)
}}}}}}

### Experimentoví agenti

Tvoří základní stavební kameny pro dávky. Nezabývají se technickými problémy ani zajištěním běhu Pikateru, nýbrž pouze a výlučně implementací jednotlivých metod strojového učení. Nacházejí se v samostatné package `org.pikater.core.agents.experiment`, dědí od agenta `AbstractExperiment` a dělí se podle kategorií na:
* výpočetní agenty,
* doporučovače,
* hledače,
* data processingy.

Tito agenti nejsou jednoznačně přiřazeni krabičkám. Abychom tohoto stavu docílili, vytvořili jsme speciální podbalíček `virtual` a v něm tzv. "virtuální agenty", jejichž jediným účelem je deklarovat a vracet instance třídy `AgentInfo`.

Seznam krabiček si za běhu systému nepřímo žádá jak webové rozhraní (viz výše), tak i samotné jádro. Jsou potřeba jak pro zadefinování experimentu z webového rozhraní do "univerzálního formátu" (viz výše), tak i pro rozparsování tohoto formátu do struktury více vyhovující jádru, až bude zadán příkaz ke spuštění experimentu.

#### Data processing agenti

Představují "datové transformace", dědí od agenta `DataProcessing`. Na vstupu vyžadují data a na výstupu vydávají nějakým způsobem transformovaná data. Motivací pro jejich vznik byla potřeba preprocessingu vstupních dat a postprocessingu dat vzniklých strojovým učení.

Data mají v celém systému formát ARFF a tento agent tedy využívá pro čtení i zapisování dat agenta `ARFFReader`.

V dávkách/experimentech jsou data processing agenti identifikování ontologií `DataProcessing`.

Existence data processingu umožňuje učit modely postupně, případně vytvářet dva modely, kde jeden řeší lépe problém pro data jednoho typu a druhý pro data jiného typu. Jako příklad uvedeme agenta `WeatherSplitter`, který obdrží na vstupu dva vstupní datasety se stejnou strukturou a na základě hodnoty v jistém sloupečku je rozdělí na tři soubory.

#### Výpočetní agenti

Specifičtější forma data processingu. Nejedná se o pouhé datové transformace, nýbrž přímo o proces strojového učení. Většinou je k tomu využita knihovna Weka, ale nic nebrání vlastní implementaci jakékoli metody. Tyto agenty nazýváme výpočetní, protože provádějí časově nejnáročnější operace v systému. Ze stejného důvodu se výpočty distribuují na slave servery. Pro reprezentaci výpočetního agenta se v dávkách používá ontologie `ComputingAgent`.

Přijímají následující ontologie bez odpovědi:
* `Task`  
Signál pro provedení procesu trénování, testování a validace. Vše určuje úloha obsažená ve zprávě.

**Natrénované modely**  
Dle specifikace serializují výpočetní agenti (v závislosti na úloze) svůj model a posílají ho jako součást výsledku výpočtu v ontologii `Task`. Agent `Manager` tento model přepošle k uložení agentovi `DataManager`, který ho uloží do databáze s vazbou na daný výsledek a experiment.

Serializaci modelu zajišťuje metoda (zděděná od předka) `org.pikater.core.agents.experiment.computing.Agent_ComputingAgent.getAgentObject()`.

Dříve uložený model lze oživit prostřednictvím meotdy `org.pikater.core.ontology.subtrees.management.LoadAgent()`.

**Uložení natrénovaného modelu**  

{{{{{{
ComputingAgent-->>+Manager: (result for Task)
destroy ComputingAgent
Manager->+DataManager: SaveResults (Task)
DataManager->DataManager: saves result to DB
opt model included in Task result
DataManager->DataManager: saves model for the result
end
DataManager-->>-Manager: 
}}}}}}

**Vytvoření agenta s natrénovaným modelem**  

{{{{{{
Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

<font color="red">TODO: začlenit obrázek</font>

{{{{{{
Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

<font color="red">TODO: začlenit obrázek</font>

{{{{{{
ComputingAgent->+ARFFReader: GetData
activate ComputingAgent
opt data not cached
  ARFFReader->+DataManager:
  DataManager->DataManager: gets data from DB,\nstores them locally
  DataManager-->>-ARFFReader:
end
ARFFReader->ARFFReader: loads data
ARFFReader-->>-ComputingAgent: (data - O2A)
}}}}}}

#### Hledači

Jsou využíváni pro procházení množiny parametrů, dědí od abstraktního agenta `Searcher` a v dávkách jsou reprezentováni ontologií `Searcher`.

K efektivnímu procházení stavového prostoru se používá mnoho přístupů. Od nejjednodušších agentů využívajících náhodný výběr hodnoty parametru (např. `RandomSearch`), přes složitější (`ChooseXValues`), po složité hledače využívající genetických a evolučních algoritmů. Složitější agenti obsahují více nastavení a nabízí širší možnosti prohledávání.

Přijímají následující ontologie s odpovědí:
* `GetParameters`  
Odpovědí je ontologie `ExecuteParameters` obsahující vygenerované/vypočtené parametry podle omezení na rozsah nebo množinu povolených hodnot získaných z přijaté zprávy.

Přijímají následující ontologie bez odpovědi:
* `Eval`  
Zpětná vazba úspěšnosti vygenerovaných parametrů. Jako metrika jsou použity míry chyb naučeného a otestovaného modelu.

#### Doporučovači

Jejich úkolem je vybírat pro daná vstupní data vhodnou metodu strojového učení. V praxi dostanou hash vstupního souboru trénovací množiny dat a vydávají plné jméno třídy agenta, který by měl pro daná vstupní data vytvořit nejkvalitnější/nejúspěšnější model. Dokáží poradit nejen metodu strojového učení, ale i nastavení jejích parametrů - k tomu využívají primárně podobnost vstupních datových množin. Pro porovnávání dat se nepoužívají přímo velké vstupní soubory, nýbrž vygenerovaná metadata (v zájmu efektivity a snadnějšího zpracování). Metadata byla navržena tak, aby dokázala pojmout informace o každém sloupci datového ARFF souboru a aby obsahovala k jednotlivým sloupcům i jisté typované statistiky.

Všimněte si, že vstupní data nemusí nutně být vstupní data celé dávky nebo experimentu - mohou to například být i data spočítané předchozí dílčí úlohou. Jedinou podmínkou je, aby měla vygenerovaná metadata.

**Proces doporučení:**  
Jakmile je nalezena povahově nejbližší datová množinu (ke vstupním datům), projdou se prostřednictvím agenta `DataManager` výsledky všech předchozích výpočtů s ní souvisejících a vybere se ten "nejúspěšnější". Ke každému výpočtu (úloze) lze dohledat agenta, který tohoto výsledku dosáhl a tento údaj se vrátí.

#### Agent_DataManager

Původně byl Pikater vytvořen jako multiagentní a maximálně adaptibilní systém, jehož architektura si kladla za cíl co nejvíce odstínit funkcionalitu od použité technologie a správy systému. V našem projektu bylo dalším úkolem odstínit systém od specifik původního databázového systému a proto vznikl speciální agent zajišťující uchovávání dat. Jako vedlejší produkt je tak odstíněna i samotná existence databáze. Agent komunikuje opět pouze pomocí ontologií a má jako jediný v jádře systému možnost přímo číst a měnit stav databáze.

Webové GUI využívá na rozdíl od jednotlivých agentů přímý přístup do databáze. Teoreticky by šlo i z webového GUI využívat pro manipulaci s databází tohoto agenta, ale z mnoha důvodů se jedná o naprosto nevhodnou variantu:
* Velice podstatně by se zvýšil počet definovaných ontologií a komplexnost systému.
* Došlo by k razantnímu zpomalení aplikace.
* Webové GUI by se stalo závislé na jádru.

**Použité ontologie**  
Můžeme je rozdělit do několika logických oblastí:
* Pro získání základních informací o uživateli a překlad přihlašovacího jména na ID se používají ontologie `GetUser` a `GetUserID`.
* Pro překlad jména souboru na hash se využívá ontologie `TranslateFilename`.
* Pro ukládání a načítání struktury `AgentInfo` se používají ontologie `SaveAgentInfo` a `GetAgentInfo`. Existují i ontologie pro získání všech `AgentInfo` interních i externích krabiček uložených v databázi.
* Pro práci s dávkami se používají ontologie `SaveBatch`, `LoadBatch`, `UpdateBatchStatus`, `GetBatchPriority`.
* Podobně koncipované jsou ontologie pracující s experimenty: `SaveExperiment` a `UpdateExperimentStatus`.
* Analogicky se ukládají i výsledky: `SaveResults`, `LoadResult`. Ontologie `Result` ovšem slouží puze pro reprezentaci chyb natrénovaného modelu a neobsahuje tudíž žádné datové množiny.
* Pro ukládání datových množin slouží ontologie `SaveDataset`. Načítání provádí ontologie `GetFile`. Speciálním případem jsou pak externí agenti - pro ně je k dispozici ontologie `GetExternalAgentJar`. Ukládání agentů provádí pouze webové GUI, tudíž ontologie "SaveExternalAgent" neexistuje.
* Pro ukládání a čtení vygenerovaných metadat se využívají ontologie `SaveMetadata`, `GetMetadata`, `GetAllMetadata` a `GetMultipleBestAgents`.

#### Agent_MetadataQueen

Tento agent generuje metadata pro jednotlivé datové množiny. Počítá statistické veličiny, které se ve výsledku používají k odhadu podobnosti dvou datasetů bez časově náročné manipulace a opětovného načítání.

Přijímá následující ontologie bez odpovědi:
* `NewDataset`  
Signál pro zaregistrování nového uživatelem uploadnutého datasetu do systému.
* `NewComputedData`  
Signál pro vygenerování metadat pro novou datovou množinu vypočtenou systémem. Výsledek se odešle v ontologii `Metadata` agentu `DataManager`, který je uloží do záznamů o daném datasetu.

<font color="red">TODO: nechtěli byste k následujícímu raději udělat obrázek?
- po vypočtení nové datové množiny výpočetním agentem nebo `DataProcessingem` agent `Planner` zavolá modul zajišťující přepravu dat. Ten zajistí uložení datové množiny do databáze za pomoci `DataManagera` na primárním containeru. Po úspěšné přepravě požádá agenta `MetadataQueen` o vygenerování metadat i k této datové množině. Data se totiž mohou u výpočtů obsahujících datové závislosti dostat na vstup dalšímu experimentu, kde se může nacházet i `Recommender` potřebující k běhu právě metadata.</font>

### Externí agenti

Specifikace ukládá umožnit přidávání nových "komponent" – agentů, kteří musí být specializací jednoho z typů definovaných systémem (výpočetní agent, doporučovač, hledač, ...). Uživatel musí dodat implementaci v podobě JADE agenta, který na požádání pošle svoji konfiguraci (mj. pro účely zobrazení ve webovém GUI).

Uživatel má možnost skrz webové rozhraní nahrát do systému JAR soubor obsahující zkompilovaný kód implementující funkčního agenta, dědícího od třídy `org.pikater.core.agents.PikaterAgent` nebo některé specializace, např. `org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing`.

Agenta lze ovšem ve webovém GUI použít až po jeho schválení některým z administrátorů aplikace. Jakmile k tomu dojde, jeho použití je stejné jako všech ostatních experimentových agentů, s jedinou výjimkou: JAR soubory musí jádro před během experimentu získat a začlenit do `classpath`. To zajišťuje automaticky platforma JADE, která při startu systému vyžaduje zadání cesty k JAR souborům na lokální disku jako parametr příkazové řádky. Příklad:

`-jade_core_management_AgentManagementService_agentspath <cesta>`

V našem případě je to:

`-jade_core_management_AgentManagementService_agentspath core/ext_agents`

Ukázkovou implementaci externího agenta lze nahlédnout ve třídě `org.pikater.external.ExternalWekaAgent`. Chová se jako výpočetní agent typu RBFNetwork.

**Vytvoření agenta**  

{{{{{{
Manager->+ManagerAgent: CreateAgent (type)
activate Manager
opt external agent JAR not present locally
ManagerAgent->+DataManager: GetExternalAgentJar (type)
DataManager->DataManager: loads JAR\nstores it locally
DataManager-->>-ManagerAgent: 
end
ManagerAgent->ManagerAgent: starts new agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

<font color="red">TODO: začlenit obrázek</font>

{{{{{{
Manager->+ManagerAgent: CreateAgent (type)
activate Manager
opt external agent JAR not present locally
ManagerAgent->+DataManager: GetExternalAgentJar (type)
DataManager->DataManager: loads JAR\nstores it locally
DataManager-->>-ManagerAgent: 
end
ManagerAgent->ManagerAgent: starts new agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

### Gateway agenti

Jádro si klade za cíl definovat komunikační rozhraní a poskytovat ho jako službu jiným komponentám. Slouží k tomu právě tito agenti.

Rozhraní je primárně koncipováno jako "předávání událostí a souvisejících informací". Původní snahou bylo poskytovat v jádře úplnou implementaci událostí pomocí RMI či RPC. Taková snaha měla ovšem velice nepříjemné dopady na vývoj a běhové vlastnosti systému a tak musí ostatní komponenty nejdříve připravit podmínky pro spuštění události (uložení dat do databáze) a teprve pak lze zavolat oznámení o události.

Veškeré rozhraní je definováno ve třídě `org.pikater.core.agents.gateway.WebToCoreEntryPoint` a předává události dále do systému prostřednictvím Gateway agentů. Aktuální implementace:

```java:/src/org/pikater/core/agents/gateway/WebToCoreEntryPoint.java```

## Popis ontologií

Všechny ontologie lze nalézt v balíčku `org.pikater.core.ontology`.

### AccountOntology

Tuto množinu ontologií využívají agenti, kteří využívají pro svůj chod informace z účtů uživatelů (ID, emaily a tak dále). K dispozici je dává agent `DataManager`.
    
### AgentInfoOntology

Jedná se o množinu ontologií používanou především pro vypublikování aktuálně podporované infrastruktury experimentů mimo systém.

Více informací lze nalézt v kapitole o agentu `AgentInfoManager` a doporučovačích.

### AgentManagementOntology

Přímo se týká vytváření, ukončování, zastavování a obnovování agentů.  
Adresátem těchto zpráv bývá agent `ManagerAgent`, který má v rámci svého containeru absolutní moc nad všemi ostatními agenty a jejich životním cyklu. Jeho služeb využívá agent `Initiator`, `AgentManager` a především `Planner`.

### BatchOntology

Zprostředkovává vytváření, načítání, přepravu a logování dávek.

**ComputationDescription**  
Podontologie navržená k reprezentaci libovolného množství vzájemně datově závislých úloh (instancí třídy `DataProcessing`). Jedná se tedy o strukturu logicky představující multigraf, kde vrcholy jsou "výpočty" a hrany "toky dat" ("chyb" nebo datasetů).

Obsahuje:
* Seznam globálních přepínačů (optionů), které slouží ke specifické konfiguraci dávky.
* Seznam instancí třídy `FileDataSaver`. 

<font color="red">Tady jsem skončil. Proboha lidi, nechte tady jenom důležitý přehledový informace a zbytek nechte na Javadocu... </font>  

Tyto elementy představují logické uložení vyprodukovaných dat v rámci jednoho Batche. Ontologie FileDataSaver obsahuje odkaz na hranu multigrafu, reprezentovanou pomocí DataSourceDescription. Hrana je napojená na další výpočetní element dědící od DataProcessingu. Protože obecný DataProsessing může obsahovat libovolné množství vstupních zdrojů a zároveň může produkovat data pro libovolné množství  DataProcessingů nebo jejich potomků musí každá hrana v multigrafu obsahovat  výstupní identifikátor říkající jaký tip dat má být předán z prvního DataProcessingu do druhého. Zároveň musí ComputationDescription obsahovat i výstupní identifikátor, který říká pro jaký účel se mají data použít. 

V systému se nejčastěji objevují agenti, kteří představují, tak zvaného výpočetního agenta, který v rámci datové transformace z trénovací, testovací a validační množiny dat je schopný, vytvořit natrénovaný model, poskytnout výsledky trénování a testování jako datovou množinu a udat míru chyby, které se model dopouští na daných datech. Tito agenti se v rámci Ontologie ComputationDescription jsou reprezentováni jako ComputationAgent. Agent, který slouží právě k efektivnímu procházení parametru předem zadaného rozsahu, se nazývá Searcher a je zadáván ComputationDescription jako třída Searcher. Searcher vždy spolupracuje s výpočetním agentem a prochází jeho parametry, které nabývají hodnoty Questionmark .

Pokud uživatel snažící se natrénovat model netuší jaká metoda strojového učení by byla pro konkrétní data optimální může využít některého z Recommender agentů, kteří na základě podobnosti dat a již spočítaných výsledků, dokážou poradit vhodnou metodu.  Takovýto agend bude v ComputationDescripion zadefinovaný jako Recommender. 
  
Speciálním potomkem DataProcessingu je ontologie CARecSearchComplex pracovně nazývaná jako Trojkrabička. Trojkrabička obsahuje tři vstupy a to Recommendera, Searchera a výpočetního agenta.
 Tato Ontologie je ve skutečnosti hack, který dává uživateli možnost nevybrat si konkrétního výpočetního agenta, ale místo toho vyplnit nedefinovaného a zapojit k němu Recommender.  V případě, že uživatel nevyplní všechny optiony výpočetního agenta musí být zapojený Searcher, který projde množinou přípustných hodnot pro daný Option a pokusí se přiblížit k optimální hodnotě parametrů pro daná data. Trojkrabička umožňuje vícero validních zapojení přičemž, chování Trojkrabičky je závislé na tom, jaké všechny Ontologie z množiny Searcher Recommender a výpočetní agent jsou do ní zapojeny. Nejjednodušší případ zapojení Trojkrabičky je takový, když je na ní připojen pouze samotný konkrétní výpočetní agent se všemi vyplněnými parametry. V tomto případě má Trojkrabička stejný význam, jako kdyby tam nebyla a výpočetní agent byl připojen mimo Trojkrabičku.

Speciálním případem výpočetního elementu dědící od Ontologie DataProcessing je Ontologie FileDataInput, která slouží jako reprezentant vstupního datasetu produkující data pro první řadu výpočetních elementů. 

Dalším speciálním výpočetním elementem  je evaluační metoda, která se zapojuje jako vstup pro výpočetního agenta. Evaluační metoda slouží k určení správné doby procesů učení modelu, aby se našel vhodný kompromis mezi nedostatečně vytrénovaným a přeučeným modelem.        
    
### DataOntology

DataOntology je množina ontologií používající se pro přepravu dat mezi agenty.  Díky možnosti spustit výpočet jednoho Batche distribuovaně na více strojích není možné, aby si jednotliví agenti předávali data pouze přes sdílený file systém, ale musela vzniknout infrastruktura pro přesuny dat a tato Ontologie. Tuto infrastrukturu z hlediska objemu dat bude nejvíce vytěžovat přeprava vstupních datasetů a vypočtených dat.  Jako soubory se mezi agenty na různých containerech přepravují JARka externích agentů, která nejsou součástí GitHub repozitáře.  Uživatelem těchto ontologií jsou tudíž agenti, kteří potřebují v systému přepravovat nějaká data. Agent ManagerAgent využívá přepravy JARek externích agentů, v případě že obdrží požadavek na vytvoření nového agenta jehož zdrojové kódy nemá na svém containeru k dispozici. Agent ARFFReader žádá o přepravu datasetu v případě, že data nejsou k dispozici na lokálním file systému . Agent Planner využívá ontologie pro řízení, ukládání vypočtených dat do databáze odstíněné centrálním DataManagerem.

### DurationOntology 

Následující množinu ontologií využívá primárně agent DutationAgent, který slouží k měření doby výpočtu.

### ExperimentOntology

Ontologie ExperimentOntology se využívá pro manipulaci s takzvanými experimenty, což jsou podproblémy jedné Batche. Tuto roli nejvíce využívá agent Manager, který za pomoci agenta DataManagera řeší řízení, rozpársování Batche na jednotlivé experimenty jejich vytvoření a zalogování a změny jejich statusů.    

### FileNameTranslationOntology

Pro potřebu překládání uživatelsky přívětivého takzvaného externího jména datasetu na systémem zpracovatelné interní jméno, se využívá právě tato skupina Ontologií. Interní jméno datového souboru je zpravidla md5 hash jeho obsahu. Tato konvence interního pojmenovávání souborů zjednodušuje řízení efektivní přepravy dat a přirozenou cestou zabraňuje tomu, aby se na filesystému objevovaly jedny data vícekrát. Dochází zde sice k překryvu jmen souborů, které mají logicky rozdílný význam, ovšem v problematice strojového učení jde touto cestou mnohonásobně zefektivnit využitelnost potřebných diskových kapacit. Překlad provádí agent DataManager, který má uloženou mapu párování interních a externích jmen souborů. Překlad se volá z agenta Managera při pársování zadaných Batchů, kde v Ontologii FileDataImput překládá logické jméno vstupního ARFF souboru. Překlad využívá také agent Duration pro získání interního jména datasetu nastaveného v konfiguraci,  na kterém se opakovaně spouští úloha lineární regrese představující pro systém jednu logickou jednotku systémového času.

### MailingOntology

Tuto množinu Ontologií využívají všichni agenti, kteří chtějí komunikovat s agentem Mailing za účelem nechání si poslat emailovou zprávu. Této možnosti využívá agent Manager při skončení výpočtu každého Batche za účelem informovat uživatele o dokončení výpočtu. V rámci celého systému Pikater se odesílání emailů využívá k informování uživatele i na dalších místech. Webové GUI emailem oznamuje třeba změnu hesla k uživatelskému účtu. V těchto případech se ale emaily posílají přímo z webové nástavby Pikateru za použití stejné knihovny, kterou využívá agent Mailing. Díky tomuto odstínění webové GUI z hlediska návrhu systému, získalo větší nezávislost na jádře a dostalo možnost provádět, některé pro jádro systému nepříliš podstatné operace samo.

### MetadataOntology

Výzkumnou motivací pro vznik systému Pikater bylo na základě předchozích výpočtů a podobností dat odhadovat optimální metodu strojového učení pro právě řešený experiment. Proces určení míry podobnosti dvou ARFFsouborů je časově i paměťově náročná operace, která může zároveň nezanedbatelně zatěžovat přenesenou infrastrukturu. Díky motivaci odstranit tyto problémy, došlo ke vzniku metadat , které by měly sloužit jako popis množiny dat. Algoritmus porovnávající ARFF soubory nevyužívá přímo fyzických dat, ale vystačí si pouze s vygenerovanými metadaty. Díky vhodné volbě struktury metadat jsou schopny Rekomendři velmi dobře odhadnout z předchozích výpočtů optimální metodu strojového učení. Cílem této Ontologie je zajistit řízení generování,  přepravu, ukládání a načítání datadat. Agent, který v systému zajišťuje vygenerování metadat ve spolupráci s ARFFReaderem je agent MetadataQueen. Tomuto agentovi chodí zprávy informujícího o přidání nového vstupního datasetu od Gateway agenta, nebo od uživateli definovanými GUIagenty. Příkazy pro vygenerování metadat k vypočteným datovým souborům posílá i agent Planner,  v koordinaci s datovými přenosy souborů. Vypočtená metadata se posílají k uložení agentovi DataManager, který je ukládá do databáze. Konzumentem pro každý dataset i vypočtená data vygenerovaných metadat jsou z pravidla pouze Recommendři, kteří provádějí na jejich základě porovnávání datových množin.

### ModelOntology

ModelOntology je množina Ontologií používající se pro práci s modely. Hlavní funkcionalitou je ukládání a načítání natrénovaných modelů. Ukládání modelu zajišťuje agent ManagerAgent ve spolupráci s DataManagerem.

### RecommendOntology

Tuto množinu Ontologií využívá agent Manager pro komunikaci s Recommendery. Účelem komunikace je předat popis experimentu a to primárně identifikovat vstupní data na základě kterých zvolený Recommender vybere nejlepšího agenta pro tento problém strojového učení. Následně doporučí Optiony,  se kterými by se měl agent spouštět za účelem minimalizovat chybu ErrorRate. Výsledky za použití těchto ontologií, pak Recommender vrací Managerovi.

### ResultOntology

Výpočetní agenti, za jednu sekvenci trénování, testování a validace, vyprodukují ontologickou strukturu pojmenovanou Results, která obsahuje výsledky důležité z pohledu výzkumu strojového učení, což paradoxně nejsou výsledná data, ale jedná se o seznam chyb, popisující úspěšnost učení modelu. Cílem této ontologie je zajistit  pro každou tuto sekvenci uložení ontologie Results za použití agenta DataManagera do databází. O ukládání výsledků se stará agent Manager.

### SearchOntology

Agent Manager tuto Ontologii používá pro komunikaci se Searcherem za účelem efektivního prohledávání, dávající důraz na vhodný poměr exploatace a explorace, stavového prostoru Optionu. Autoři jednotlivých Searcher agentů se snažili vytvořit co nejobecnější interface pro předávání parametrů pro procházení z agenta Managera do agenta Searchera. Za tímto účelem vznikla Ontologie Eval, na kterou se překládá Ontologie Option.

### TaskOntology

TaskOntology je Ontologie, která umožňuje pracovat jednotlivým agentům v rámci Pikateru s jedním Taskem, neboli s objektem reprezentující nejnižší úroveň logického problému. Agent Manager, který jako první v řadě z hlediska průtoku dat používá Ontologi Task, neřeší pouze problém řízení výpočtu, ale také transformace příchozích Batchů na jednotlivé experimenty, které transformuje na seznam konkrétních dále nedělitelných Tasků. Tyto Tasky pak přeposílá agentovi Plannerovi, který za podpory hlídání datových závislostí předává Tasky distribuovaným výpočetním agentům, případně DataProcessingům. Jak DataProcessingy, tak výpočetní agenti, přijímají zadání jednoho výpočtu jako Ontologii Task. 

#### Ontologie Task

Ontologie Task je obecná struktura, která je schopná pojmout jakýkoli pro Pikater dále nedělitelný problém určený pro libovolný typ agenta, obsahující neomezené množství vstupních souborů potřebných pro výpočet. Ukrývá informace o identifikaci Batche experimentu, ke kterému patří identifikátor uživatele, který Batche zadal  a spoustu dalších Optionů ovlivňující běh výpočtu. Pro plánování Tasku je zde informace o předpokládané délce běhu výpočtu, která má zajistit průchodnost systému pro krátce trvající výpočty. Po výpočtu Tasku se Ontologie Task předává zpátky až do agenta Managera. V tento okamžik jsou v Tasku vyplněné informace o vypočtených souborech, které výpočetní agent nebo DataProcessing vyprodukoval na file systém. Zároveň vypočtený Task obsahuje datum a čas začátku a konce běhu výpočtu.

Další agent využívající Ontologii Task je agent Duration, který využívá vlastního výpočetního agenta Duration servis odděděného od Wekovského výpočetního agenta LinearRegresion  pro počítání samplu lineární regrese. Zadávání tohoto samplu se ovšem posílá přímo agentovi DurationServis bez využití služeb agenta Plannera.

### TerminationOntology

Tuto Ontologii využívají všichni experimentoví agenti, což jsou agenti typu výpočetní agent Recommender a Sercher, případně DataProcessing, kteří jsou buď defaultně součástí systému a nebo si je uživatel může do systému sám přidat v podobě JARka. O zabíjení agentů se stará agent ManagerAgent. Při přidání nového agenta do systému nebo inicializaci systému společně s agentem AgentInfoManager budí a následně zabíjí experimentální agenty. Dochází k buzení a následnému šetrnému zabíjení experimentových agentů.  Aparát šetrného zabíjení agentů je v systému nezbytný, agenty nejde jednoduše pouze zabít, ale musí se také nechat odregistrovat z JADE Directory Facilitator. Nejjednodušší cestou je požádat příslušného agenta z pozice ManagerAgenta, aby sám sebe ukončil. Snaha o čistý způsob zabíjení agentů byla primární motivací pro vznik této ontologie.  
