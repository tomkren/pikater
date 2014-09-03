<!-- --- title: Technická dokumentace část druhá: Dokumentace jádra systému -->

[[_TOC_]]

# PIKATER (Pikantní agentní teritorium)

Pikater je multiagentní systém vyvíjený týmem Mgr. Romana Nerudy, CSc. na Matematicko-fyzikální fakultě Univerzity Karlovy a je úzce spojen s Ústavem informatiky Akademie věd České Republiky.  Systém byl vyvíjen za účelem vytvořit výpočetní platformu pro akademický výzkum v oblasti strojového učení.

Systém prošel mnoha podobami, využíval se jako výpočetní model pro výzkum v oblasti umělé inteligence na Edinburské univerzitě, rovněž také jako výukový program pro studenty středních škol poskytující úvod do problematiky umělé inteligence. Zároveň slouží jako univerzální prostředí pro ověření správnosti nových nápadů členům katedry. 

## Výzkumná motivace pro vznik systému

Procesy strojového učení patří mezi výpočetně velmi náročné úlohy způsobu jak učit model rozhodovat obecné problémy je mnoho a jde těžko predikovat, který způsob strojového učení by pro daný typ problému optimální. Problém volby optimální metody strojového učení je v systému řešen analýzou vstupních dat a na základě podobnosti datasetů je z již vypočtených výsledků v systému vybírána metoda strojového učení, která byla v minulých pokusech nejúspěšnější. Metody strojového učení dávají dispozici spoustu parametrů, kterými jde ovlivnit proces učení, testování i validace. Pikater se snaží vhodnými způsoby procházení stavového prostoru parametrů ušetřit strojový čas učení, zároveň zvolit vhodný poměr explorace a exploatace pro zajištění konvergence procesů učení co možná nejblíže k lokálnímu optimu. 

## Pikater jádro

Jedná se o nejstarší část systému, která je nezávislá na všech ostatních nadstavbách. Jádro je schopné spolupráce s webovým GUI a dodržuje standardy pro možnost propojení s jinými platformami multiagentních případně expertních systémů. Zároveň může být jádro využíváno pro učení modelů na superpočítačích. Pro kompilaci jádra právě z tohoto důvodu nejsou potřeba žádné externí knihovny. 

## Technologie

* JADE

Systém Pikater je postaven nad multiagentní platformou JADE vyvíjený italským Telecomem. Jedná se o vhodnou platformu pro modulární členění pro jednotlivé metody strojového učení. Middleware dává autorům nových metod šanci na  jednoduché uložení naučených modelů, využívá se zde JADE uspávání a probouzení agentů. JADE komunikační ontologie posílané jako zprávy mezi agenty dávají jednoduchý interface umožňující jednoduché přidávání dalších metod učení.

* JADE gateway

Brána do systému, umožňující poslat agentovi zprávu z prostředí mimo JADE.

* WEKA

Modularita agentního systému umožňuje jednoduše využít ve výpočetních agentech libovolnou knihovnu. Ve většině případů využívají výpočetní agenti přeprogramovaných metod z knihovny WEKA. 

* PostgreSQL

Databáze Postgre se používá společně s JPA frameworkem pro ukládání JAVA objektů pomocí objektově relačního mapování. Soubory jsou ukládány jako LargeObjekty.

* Xstream

Knihovna pro serializaci objektu do XML.

## Nadstavba jádra Pikateru

Původní systém uměl efektivně procházet stavový prostor parametrů jednotlivých algoritmů a uměl pomocí Doporučovačů na základě podobnosti vstupních dat vybrat vhodnou metodu strojového učení. Program uměl zpracovávat jednotlivé úlohy, které se nazývaly experimenty představující jedno trénování a testování, případně ukládání modelu. Úloha mohla být spouštěna v rámci jednoho experimentu postupně na několika stupních datasetech a s různými parametry zadanými buď to uživatelem nebo vygenerovanými Searcherem. Uživatel si mohl vybrat konkrétní metodu strojového učení, evaluační metodu a nastavit spoustu parametrů ovlivňujících řízení běhu výpočtu. V případě, že metoda zdrojového učení nebyla uživatelem zadána musel být vybrán Recommender.
 
Nadstavba jádra Pikateru kromě staré funkcionality dovoluje spojovat zadané experimenty do Batchů. Motivací pro tuto funkci bylo umět zpracovat problémy, kde vstup jednoho experimentu je závislý na výstupu předešlého experimentu. Pro reprezentaci tohoto zapojení byla v jádře systému vytvořena ontologie ComputationDescription představuje pro agenta řídící výčty v systému jeden Batch.

Problém s nedostatečným výkonem stroje, na kterém běželo jádro systému, byl v nadstavbě vyřešen pomocí nové funkce distribuovaného počítání a zároveň efektivního plánování. Plánování zohledňuje šetrné využití síťové infrastruktury pro přesun velkých dat. Jádro systému slouží jako poskytovatel informací pro webové GUI jehož primárním účelem je schopnost zadávat  a vizualizovat Batche ve formě multigrafu reprezentující několik na sobě datově závislých experimentů. Vrcholu multigrafu ve webovém GUI reprezentující určitou transformaci vstupních dat říkáme "krabička". Pro agregaci a zpřístupnění vstupních data webové stavebnice byla v jádře vybudována celá infrastruktura, která zajišťuje co možná nejpřívětivější zadefinování krabiček a efektivní export těchto dat webovému rozhraní. Jádro systému také podporuje posílání emailů oznamující konec výpočtu.

Z hlediska návrhu systému prošel systém kompletní přestavbou. Z podoby připomínající skript, který se spouštěl z příkazové řádky, odkazující se konfiguráky a data umístěná na filesystému má dnes Pikater podobu interaktivní aplikace, která podporuje jak konzolový tak grafický webový režim. Z hlediska struktury kódu byly vytvořena jedna package pikater/core pro jádro systému, ve které se jednotliví agenti dělí na základě jejich funkce na systémové a experimentové agenty. Systémoví agenti se starají o běh systému a zajišťují infrastrukturu právě těm experimentovým.

Všechna data jsou ukládána do nově napojené objektově-relační databáze, kterou jádro systému sdílí společně s webovým rozhraním.

Došlo k přeuspořádání rolí mezi agenty v rámci systému, kde se někteří agenti jako agent Manager a agent OptionManager spojili do jednoho agenta, zároveň došlo za vhodného použití polymorfizmu k rozdělení jednoho výpočetního agenta obsahující všechny algoritmy strojového učení na několik samostatných výpočetních agentů. Vedlejším efektem tohoto přeuspořádání, mající za cíl zpřehlednit zdrojový kód bylo téměř perfektně bijektivní zobrazení experimentových agentů na zadefinované Krabičky.
 
Z hlediska datových struktur byla vytvořena obecná struktura nazývaná "Univerzální formát" do které je možné zkonvertovat libovolná instance ComputationDescription ale zároveň díky obecnosti, může tento formát využívat i webové GUI pro ukládání v grafické Stavebnici zadaných Batchů, které obsahují pro jádro nepovinné informace.

Kompletní přestavbou prošla většina ontologií. Jednalo se většinou buď o zobecňování stávajících ontologií nebo o přidávání nové funkcionality. V systému se celkově objevilo mnoho nových situací na které je potřeba zareagovat a tím pádem přibylo i ontologických zpráv vyzívající k činnosti nějakého agenta. Pro zpřehlednění komunikačních protokolů, byly ontologie rozděleny podle použití do separátních jmenných prostorů a zároveň byly decentralizovány do navzájem nesouvisejících podmnožin odpovídající agentním rolím.

Z hlediska nadstavby Pikateru je důležitý vznik ontologie AgentInfo a výměna ontologie Option, která využívala k reprezentaci informací řetězce na obecnou, typovanou, rozšiřitelnou, validovatelnou, do níž je zároveň možné ukládat informace o omezeních na hodnoty pro každý typ. Ontologie AgentInfo je datová struktura reprezentující jednu krabičku a slouží jako vstupní informace pro grafickou Stavebnici.

### Schéma architektury

[[architektura-final.png]]

## Popis agentů

### Agent Initiator

Jedná se o systémového agenta, který spouští všechny ostatní systémové agenty, na základě konfiguračního souboru Configuration.xml. Jedná se tedy o agenta, který zajistí start celého systému a probudí všechny agenty zadané v přiloženém XMLku. Agent je naprogramován dostatečně obecně, aby mohl spouštět Pikater, jak na primárním containeru, kde běží Management všech výpočtů a celého systému, tak na Slave containerech, které by měly být převážně vytěžovány výpočetními agenty. Způsob spouštění systému je stejný ve všech případech, jediné v čem se liší je parametr a jméno konfiguráku, v případě Slave containeru se přidává ještě druhý parametr, označující číslo Slave containeru. Pro spuštění agenta Initiatora z příkazové řádky je potřeba pomocí JAVA spustit JADE Middleware a buď do primárního nebo Slaveového containeru umístit inicializačního agenta s parametrem cesty ke konfiguračnímu souboru a v případě Slave containeru s druhým parametrem udávající číslo Slave containeru. Konfigurační soubor obsahuje jméno třídy v četně celé package spouštěného agenta, jeho jméno a seznam parametrů. Pomocí druhého parametru se zajišťuje pouze jednoznačnost pojmenování agentů v rámci všech containérů a ulehčuje se tím práce uživateli, kterému pro spuštění několika Slave containérů stačí pouze jeden konfigurák a druhý parametr označující číslo containeru si agent Iniciator přilepí jako sufix ke všem jménům v konfiguračním souboru. Nejpohodlněji se dá systém ovšem zapnout pomocí Bashových skriptů odděleně zajišťujících bildování jádra systému, spouštění hlavního containeru a skriptu pro spouštění slave containeru s parametrem čísla Slave containeru. Tyto skripty volají pouze ANT na souboru buildCore.xml. Z kompilace jsou záměrně vyloučeny package potřebné pouze pro běh webové nástavby, protože pro ni jsou potřeba knihovny dostupné přes Apache Ivy. Důvodem proč se i pro kompilaci jádra nepoužívá Ivy je přání zadavatelů, aby jádro systému Pikater šlo zkompilovat přímo ze zdrojových kódů bez nutnosti mít dostupné připojení k internetu, které z bezpečnostních důvodů často nebývá na super počítačích k dispozici.



### Agent ManagerAgent
   
Tento agent se nachází na každém containeru právě jednou. Jak na master containeru tak na Slave containerech má funkci vládce, který při přijmutí Ontologie CreateAgent vytváří požadovaného agenta specifikovaného ve zprávě. Dále nabízí možnost zabití agenta podle jména pomocí Ontologie KillAgent. Ontologie CreateAgent obsahuje tři fieldy:  String type reprezentující plné jméno třídy agenta,  String name udávající jméno agenta, Ontologii Argumens sloužící pro přepravu argumentů pro nového agenta. Ontologie KillAgent obsahuje pouze jeden field a to String name reprezentující jednoznačné jméno agenta v rámci systému. Agent přijímá Ontologie LoadAgent pro oživení již natrénovaného modelu. Pro uložení již natrénovaného agenta se používá ontologie SaveAgent. Agenti se ukládají i oživují ze souborů umístěných na filesystému. Přepravu souborů z filesystému do databáze nebo na jiný stroj řeší modul pro přepravu dat.

Agent jako vládce containeru poskytuje na vyžádání pomocí Ontologie GetComputerInfo informaci o počítači, na kterém běží. Vrací Ontologii ComputerInfo, kde nejžádanější informací o počítači bývá počet jader procesoru. Předpokládá se, že na jednom počítači poběží pouze jeden container. Na agenta ManageraAgenta je možné zaslat zprávu s Ontologií Ping a ověřit jestli je container v rámci JADE dostupný. 

#### Vytvoření agenta
{{{{{{

Manager->+ManagerAgent: CreateAgent (type)
activate Manager
ManagerAgent->ManagerAgent: starts new agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

#### Vytvoření agenta s natrénovaným modelem
{{{{{{

Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

### Agent Manager

Agent Manager je nejdůležitějším agentem celého Pikateru, přijímá, transformuje, přeposílá a řídí zadané výpočty v rámci celého Pikateru. Agent přijímá Ontologii NewBatch, která obsahuje jednoznačný identifikátor Batche a ID uživatele, který tento Batch zadal. Agent Manager pomocí Ontologie LoadBatch načte z agenta DataManager zadanou Batch obsahující ComputationDescription. DataManagerem vrácená ontologie Batch obsahuje fieldy jako String name, popisující jméno Batche, String note, představující poznámku k Batche nebo také prioritu. Pro příchozí ComputationDescription se nainicializuje třída Parser, na které se zavolá funkce parseRoots a jako parametr se parseru zadá ComputationDiskription batchID a userID. Parser postupně prochází všechny výpočetní elementy Ontologie ComputationDescription, ve které se nachází field  rootElements, což je seznam všech instancí Ontologie FileDataSave. Parser postupně prochází celý orientovaný a cyklický multigraf, začíná s průchodem od FileDataSaverů, které jsou uloženy ve fieldu rootElements. Multigraf je vybudován pomocí javových referencí a využitím porovnávání JAVA referencí, by šly zcela jistě porovnávat na rovnost i vrcholy multigrafu. Tato možnost se ovšem nevyužívá, každý výpočetní element dědící od DataProcessingu obsahuje jednoznačné ID v rámci Batche. ID se nagenerují procházením multigrafu do hloubky při vzniku Batche. Reference se nevyužívají, protože JADE Middleware při posílání Ontologií z jednoho agenta druhému agentovi, JAVA reference nezachovává.
     
#### Pársování elementů

Při pársování logického multigrafu obsahující jak reprezentaci experimentů, tak ontologie, které mají pouze logický význam pomíjející. Problémy distribuovanosti výpočtu, problémy datových závislostí, skrývající problém vhodného pořadí spouštění tasku a přepravu dat na potřebná místa. V průběhu pársování si Manager vytváří závislostní graf reprezentovaný třídou ComputationGraf. Vrcholy závislostního grafu, reprezentované pomocí tříd ComputationNode, DataProcessingComputationNode, RecommenderComputationNode, SearchComputaionNode a ModelComputationNode. V rámci grafu vytváří mezi Nody vzájemně sdílenou strukturu front, velmi blízce připomínající problém a návrhový vzor Producent Konzument. Do systému front tvořených potomky třídy AbstractComputionBuffer se přidávají informace už v průběhu pársování Ontologie ComputationDescription jako například přeložené logické jméno vstupního datasetu z Ontologie FileDataInput na interní jméno neboli hash, Optiony upravující způsob běhu Searche agenta a další Optiony, přičemž jeden z nejdůležitějších Optionů je typ agenta. Touto cestou se postaví závislostní graf včetně sdílených front, nainicializuje se potřebnými informacemi.

Všechny fronty se  cyklicky prochází zda-li jsou k dispozici všechny data pro zpracování dalšího kroku výpočtu. V průběhu pársování Batche se poprvé v systému zjistí kolik logických experimentů v sobě aktuální ComputationDescription obsahuje, tudíž v parseru dochází k lobování existence nových experimentů. V okamžiku dokončení experimentu se za použití návrhového vzoru Observeru zalogovává opět použitím zprávy DataManagerovi změna statusu experimentu na Finished. 

Pro změnu statusu Batche se používá opět odesílání Ontologie DataManagerovi, v tomto případě Ontologie UpdateBatchStatus. Pro změnu statusu experimentu se využívá Ontologie UpdateExperimentStatus. Experimentům se na rozdíl od Batchů Managerovi pouze nemění statusy, ale experimenty se pomocí Ontologie SaveExperiment za pomoci DataManagera v systému i vytváří.

Pro zpracování jednotlivých vrcholů závislostního grafu se využívají takzvané strategie. Strategie jsou třídy, které na základě příchozích dat upravují informace uložené ve struktuře front. Mezi nejintuitivnější strategii patří CAStartComputationStrategy, která řídí strukturu front výpočetního agenta a pokud jsou k dispozici všechna data a informace, vyprodukuje z těchto informací jeden task. Mezi další strategie patří RecommenderStartComputation, která zajišťuje komunikaci s Recommendrem, a následně za použití strategie výpočetního agenta vygeneruje opět jeden Task. Strategie SearchStartComputationStrategy zajišťuje komunikaci se Searcherem, který Managerovi vrací parametry, na kterých se postupně spouští strategie výpočetního agenta. Nejobecnější strategie je strategie DataProcessecingStrategy, která pouze čeká na všechna potřebná data pro vytvoření jedné Ontologie Task. Strategiemi vyprodukované Tasky se předávají ke zpracování agentovi Plannerovi.

V okamžiku, kdy se všechny fronty vyprázdní a Manager obdrží od plánovače výsledky všech Tasků příslušících  aktuálnímu Batche, statusy všech experimentů jsou již dokončeny, výpočet ComputationDescription končí a jako poslední krok se změní status Batche na Finished. 

### Agent Planner

Agent plánovač slouží k efektivnímu rozplánovávání příchozích instancí Ontologie Task, které v rámci Pikateru představující jeden problém pro jednoho výpočetního agenta nebo DataProcessing. Důvodem proč vznikl tento agent z hlediska architektury celého systému je snaha maximálně odstínit problematiku spouštění tasku od problémů pársování Batchů a řízení výpočtů, o které se stará agent Manager. Díky funkci distribuovatelnosti výpočtů, čímž se myslí paralelní spouštění problémů reprezentovaných Ontologií Task, na několika containerech umístěných na různých strojích, kde v každém containeru zároveň může běžet několik výpočtů Ontologie Task, přináší problém přepravy dat. V případě, že by výpočty běžely pouze na jednom stroji, mohl by se pro přepravu dat mezi agenty využívat sdílený file systém. Přeprava dat mezi containery je vyřešená pomocí utility využívající na každém containeru umístěný DataManager přepravující velká data mimo JADE za použití síťových socketů. 
Agent Planner přijímá Ontologii ExecuteTask, která pro agenta představuje zadání jednoho Tasku, neboli úlohy pro jednoho agenta. Přijaté Tasky se řadí v agentovy do datové struktury WaitingTaskQueues, představující skupinu prioritních front pro Tasky čekající na zpracování. Po každém doručení nového Tasku se zároveň volá funkce přeplánování. Inverzní Ontologií je ontologie KillTasks. Tato Ontologie obsahuje identifikaci Batche, kterou by si administrátor nebo případně uživatel Batche přál před jejím dopočítáním násilně ukončit. Plánovač přijímá tuto Ontologii a na základě příchozího IDéčka ukončovaného Batche, vyhodí ze struktury front všechny tásky odpovídající zabíjenému Batchy. Tasky, které odpovídají zabíjenému Batchy a už byly spuštěny se nechají dopočítat. 
Další Ontologií, kterou plánovač přijímá je Ontologie GetSystemLoad. Jedná se o žádost o vrácení aktuální vytíženosti celého systému. Ontologie SystemLoad, obsahuje aktuální využití všech procesorových jader počítačů, na kterých se nachází Slave container. Tyto informace agent získává z třídy CPUCoresStructure, kterou využívá k rozvrhování úloh. 

#### Proces plánování      

Proces plánování se spouští pomocí funkce Plan při  detekovaných změnách, které by mohly změnit efektivnějším směrem rozvržení zadaných úloh na dostupné zdroje. Typické situace jsou příchod nového Tasku ke zpracování, nebo příchod již spočteného Tasku. Jako první krok plánování je zjištění, zda-li je k dispozici aspoň jeden Task k naplánování. V případě že ano, vybere se Task s nejvyšší prioritou, čímž se ale nemyslí celková priorita Tasku tvořená uživatelem nastavenou prioritou Batche, zkombinovanou s prioritou uživatele, která závisí na bonitě uživatele z pohledu administrátora, jde o prioritu navíc ještě zohledňující průchodnost systému pro krátce trvající Batche, predikovaná za předpokládané doby výpočtu. Jako první se tedy vybírají Tasky s nejvyšší prioritou, které zároveň nebudou trvat dlouho. V následujícím kroku se vyhledávají nové dostupné, v plánovači ještě nezaregistrované, Slave servery  a následně se přidávají do struktury CPUCoreStructure nová volná procesorová jádra. Informaci o tom, kolik jader je k dispozici na stroji, nám prozradí agent ManagerAgent, kterého dohledáme v JADE Directory Facilitator. Pomocí Ontologie Ping poslané všem zaregistrovaným agentům AgentManager jsme schopni ověřit zda je agent pořád dostupný a má připravený container pro počítání. V případě, že nějaký container umřel, musí být všechny výpočty na něm počítané znovu vráceny do struktury front. Pro vybraný Task se ze třídy DataRegistry vybere pro každý potřebný soubor ke  spočítání  Tasků seznam všech strojů na kterých se soubor nachází. Na základě těchto informací se zjistí, jestli všechny data pro spuštění tásku jsou k dispozici, v případě že ano, plánovač se pokusí vybrat ze třídy CPUCoreStructure volné procesorové jádro na stroji, kde jsou aspoň nějaká data potřebná pro výpočet Tasků. Nastane-li situace, že data potřebná pro spočítání Tasků nejsou k dispozici na žádném aktuálně dostupném stroji, plánovač se pokusí znovu spustit Tasky, které již dříve tyto data spočítaly. Vybraný Task je vrácen zpátky do struktury front a znovu se volá funkce Plan. 

Pokud jsou všechna data k dispozici a pokud pro výpočet existuje volné jádro požádá plánovač ManagerAgenta na stroji, kde se nachází vybrané CPU jádro, aby vytvořil nového agenta potřebného typu a vrátil plánovači nazpátek jeho jméno. Plánovač žádá o vytvoření pouze výpočetního agenta nebo DataProcessing agenta. Plánovač vytvořenému agentovi pošle vybraný Task a označí si, že aktuální výpočetní jádro je obsazené. Po dokončení výpočtu Tasku, se plánovači vrátí Ontologie Task obohacená o informace o agentem spočtených souborech. Plánovač si informace o nových souborech uloží do třídy DataRegistry a vrátí vypočtený Task zpátky agentovi Managerovi. O výpočetní agenty vytvořené na SlaveContainerech ani o DataProcessingy se agent plánovač nestará.

Agenti po dokončení výpočtu jednoho Tasku a vrácení výsledku sami zemřou. 


### Agent AgentInfoManager

Tento agent slouží ke sbírání informací o experimentových agentech. Tyto informace využívá jak grafické webové GUI, tak třeba agenti Recommendeři. Pro grafickou stavebnici slouží právě tyto třídy, nazývající se AgentInfo jako vstupní množina dat. Mezi definovanými grafickými krabičkami ve webovém GUI a mezi naprogramovanými experimentovými agenty existuje téměř perfektní bijektivní zobrazení, tudíž by se dalo říct, až na výjimky, že jeden agent produkuje vždy jedno AgentInfo, které odpovídá jedné grafické krabičce. 

Z webového GUI přichází přes agenta Gateway Ontologie GetAgentInfo obsahující ID uživatele, reprezentující požadavek na vrácení všech krabiček, které se mají specifikovanému uživateli zobrazit ve webové stavebnici. Agent InfoManager vrátí v tomto případě seznam instancí AgentInfo Ontologií obsahující informace o všech defaultních agentech, uložených na GitHubu a informace o všech, aktuálním uživatelem, do systému nahraných externích agentech. Dále agent přijímá Ontologii NewAgent, která poskytuje agentovi informaci o tom, že do systému byl přidán nový externí agent. Z příchozí Ontologie si agent zjistí jméno třídy nového agenta včetně package a za použití Ontologie CreateAgent požádá agenta ManagerAgent o vytvoření agenta. Agent InfoManager se může tohoto oživeného agenta pomocí Ontologie GetAgentInfo zeptat na jeho popis, který hned po obdržení za pomoci DataManagera a Ontologie SaveAgentInfo ukládá do databáze. Záhy je agent pomocí agenta

ManageraAgent a Ontologie KillAgent zabit. Tímto způsobem si AgentInfoManager ukládá do databáze informace o agentech. Při každém zapnutí agenta se ovšem ověří, zda jsou v databázi uloženy AgentInfa všech agentů v systému. Agent při svém startu porovná jestli ke všem externím agentům, jejichž jména získá pomocí Ontologie GetExternalAgentNames z agenta DataManagera a ke všem defaultním agentům, jejichž jména získá průchodem package zdrojových kódů, existuje již uložené AgentInfo. V případě, že ne, provádí se sekvence buzení agenta a vyptávání se na AgentInfo a následné zabíjení. 

### Agent KlaraGUIAgent

Tento agent je pojmenovaný po magistře Kláře Peškové autorce původní verze Pikateru. Důvodem vzniku tohoto agenta je motivace vytvořit samostatné na webové nástavbě nezávislé jádro systému. S tímto agentem, by samostatné jádro mělo mít plnou funkcionalitu potřebnou pro práci s programem za účelem výzkumu strojového učení. Po zapnutí agenta se spustí jednoduchý komunikační automat, umožňující v consolovém režimu zadat nový Batch, přidat nový dataset a vygenerovat pro něj metadata. Nový Batch se načítá, v podobě se serializované instance ComputationDescription, ze souboru uloženého na file systému. Pro serializaci se využívají přímo funkce ve třídě ComputationDescription pojmenované exportXml a importXml. Ty využívají knihovnu Xstream. Tento agent komunikuje s agentem GUIComunicator, který se snaží vytvořit přístupové rozhraní pro další uživateli zadefinované GUIAgenty. Myšlenkou pro vznik tohoto rozhraní byla představa, že si každý další uživatel bude tvořit vlastní formát pro serializaci třídy ComputationDescription, tak stejně jak to bylo v původní verzi Pikateru. 

### Agent Mailing

Do systému byl zaveden nový agent `org.pikater.core.agents.system.Agent_Mailing`, který zprostředkovává službu posílání e-mailů ostatním agentům jádra pomocí akce `SendEmail`.

Agent přijímá Ontologii SendEmail, která reprezentuje jednu emailovou zprávu. Tohoto agenta v jádře systému využívá pouze agent Manager při dopočtení Batche. 

Komunikace s lokálním SMTP serverem potřebná k odeslání e-mailu je pro potřeby agenta `Agent_Mailing` a webového serveru zprostředkována rozhraním `org.pikater.shared.utilities.mailing.Mailing`, resp. metodou `org.pikater.shared.utilities.mailing.Mailing.sendEmail(to, subj, body)`.

#### Odesílání notifikace o dokončeném výpočtu
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

### Agent ARFFReader         

Agent se využívá k načítání ARF souborů s file systému a transformaci načtených dat na Ontologii. Agent přijímá Ontologii GetData obsahující jméno souboru, na kterou odpovídá Ontologií DataInstances. Tohoto agenta využívají experimentovaní Výpočetní agenti a DataProcessingoví agenti, zároveň ho využívá systémový agent MetadataQueen.

#### Načítání dat
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
  
## Experimentoví Agenti:

Jsou to agenti, kteří tvoří základní stavební cihličky jednotlivých Batchů, nezabývající se technickými problémy, týkající se zajištění běhu Pikateru, ale právě implementací jednotlivých metod strojového učení.  Nacházejí se v samostatné package core/agents/experiment. Všichni agenti v této package dědí od agenta AbstractExperiment, který dědí od abstraktního agenta PikaterAgent.

AbstractExperiment implementuje JADE chování zajišťující reakci na žádosti o AgentInfo neboli posílání těchto zadefinovaných struktur. Agenti se dělí podle kategorií na výpočetní agenty, Recommendery, Searchery a DataProcessingy. Každá kategorie je spojena s definicí abstraktního agenta od kterého dědí už konkrétní typy agentů, představující implementaci jednotlivých algoritmů strojového učení. Celá tato package je tedy z hlediska GUI plná krabiček.

Pro zachování poměru, kde pro každou grafickou krabičku existuje právě jeden experimentový agent byla vytvořena package virtual, ve které se nacházejí agenti, kteří nemají žádnou jinou funkci, než na požádání poslat agentovi AgentInfoManagerovi definici svého AgentInfa. Touto cestou se zajišťuje doprava Krabiček speciálního významu do grafické stavebnice. Tyto krabičky se pak přetransformují na ontologii ComputationDescription, kterou následně pársuje agent Manager. Manager využívá těchto krabiček pouze k pochopení struktury výpočtu a samotné virtuální agenty nikdy ničím neúkoluje. Agenti slouží pouze jako prostředek dostat speciální krabičky do systému.

### Agenti DataProcessing

Agenti představující nejobecnější formu datové transformace. Motivací pro vznik těchto agentů byla potřeba preProcessingů, případně postProcessingů manipulaci s daty vzniklých strojovým učení. Existence DataProcessingů dává systému možnost učit modely postupně, případně vytvářet dva modely, kde jeden řeší lépe problém pro data jednoho typu a druhý pro data jiného typu.

Abstraktní agent DataProcessingu zajišťuje přepravu dat potřebných pro zpracování Tasku za použití ontologii DataInstances a agenta ARFFReadra. Dále poskytuje prostředky pro ukládání vypočtených datových množin do ARFF formátu. V zadání Batche se pro reprezentaci v ComputationDescription využívá Ontologie DataProcessing.

Jako příklad DataProcessingu uvedeme agenta WeatherSplitter, který dostane na vstupu dva vstupní datasety mající strukturu stejnou jako Wekovský vzorový dataset weather.arff. Agent rozdělí tyto dva vstupy pojmenované "firstInput" a "secondInput" na tři soubory odpovídající výstupům "sunnyOutput" "overcastOutput" "rainyOutput". Agent nedělá vlastně nic jiného, než rozdělení množiny dat ze dvou souborů na základě hodnoty v jednom sloupečku na tři výstupní ARFF soubory.

### Agenti ComputingAgent

Výpočetní agenti jsou specifičtější formou DataProcessingu. Nejedná se zde o pouhé datové transformace, ale provádí se zde přímo proces strojového učení. Nejčastěji se využívá volání externí knihovny např. WEKA, ale nic nebrání vlastní implementaci jakéhokoli metody. Tyto agenty nazýváme výpočetní, protože provádějí podobně jako některé DataProcessingy časově nejnáročnější operace v systému. Z tohoto důvodu se právě tyto výpočty distribuují na Slave servery. Pro reprezentaci výpočetního agent v ComputationDescription se využívá ontologie ComputingAgent.

Agenti stejně jako DataProcessing přijímají ontologii Task, na které provádějí proces trénování, testování a validace. 

#### Natrénované modely ComputingAgentů

Dle požadavků na nový systém výpočetní agent v závislosti na zadání úlohy (`Task`) po jejím vyřešení serializuje svůj model a pošle jej jako součást výsledku výpočtu v conceptu `Task`.

Serializovanou podobu svého modelu lze získat pomocí metody předka všech výpočetních agentů `org.pikater.core.agents.experiment.computing.Agent_ComputingAgent.getAgentObject()`.

Agent Manager tento model přepošle v rámci výsledků k uložení DataManagerovi, který model uloží do databáze s vazbou na daný výsledek a experiment.

##### Uložení natrénovaného modelu
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

Dříve uložený model lze oživit prostřednictvím akce `org.pikater.core.ontology.subtrees.management.LoadAgent` agenta ManagerAgent.

##### Vytvoření agenta s natrénovaným modelem
{{{{{{

Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

### Agenti Searcherové

Tito agenti se využívají pro procházení množiny parametrů, v ontologii ComputationDescription se reprezentují pomocí ontologie Searcher. K efektivnímu procházení stavového prostoru se používá spousta přístupů. Od nejjednodušších agentů jako je RandomSearch využívající náhodný výběr hodnoty parametru, pres složitější jako ChooseXValues až ke složitým Searcherů využívající Genetických a Evolučních algoritmů. Složitější agenti obsahují více Optionů a nabízí širší možnosti jak upravovat způsob prohledávání. 

Všichni Searcherové dědí od abstraktního agenta Searcher, který přijímá ontologii GetParameters obsahující omezení buďto na rozsah nebo na množinu povolených hodnot. Agenti vrací ontologii ExecuteParameters. Searcherové navíc potřebují feedback k úspěšnosti vygenerovaným parametrů k čemuž  využívají ontologie Eval.  Míry chyb naučeného a otestovaného modelu, které přijdou agentovi Managerovi v ontologii Results jako seznam Errorů slouží ke spočtení hodnoty fitness funkce pro jeden model trénovaný na daných hodnotách.

### Agenti Recommendeři

Recommendeři neboli Doporučovače jsou agenti, kteří dokážou vybrat pro daná vstupní data vhodnou metodu strojivého učení. V rámci architektury Pikateru, jsou to agenti, kteří na základě hashe souboru trénovací množiny dat, vrátí jméno třídy agenta, který by měl vytvořit nejkvalitnější model. Doporučovače dokážou poradit nejen metodu, ale také nastavení parametrů na kterých se dá předpokládat vytvoření úspěšného modelu.

Recommendeři využívají pro odhadování správných parametrů i metod strojového učení primárně podobnost vstupních datových množin.

Pro porovnávání dat se nepoužívají přímo fyzické datasety, ale vygenerovaná metadata.  Metadata se využívají právě proto, aby Recommendeři nemuseli při každé obsluze žádosti o doporučení vhodné metody procházet několikagigové soubory, nebo při nejhorším odněkud přepravovat. Metadata byly navrženy tak, aby dokázaly pojmout informace o každém sloupci ARFF souboru a aby obsahovaly i statistickou povahu každého sloupce.

Agent je abstraktní agent Recommender  od kterého dědí ostatní naimplementovaní Recommendři. Zajišťuje přepravu všech dostupných metadat a mergovani Optionů. Jednotliví Recommendeři provádějí porovnávání metadat a na vybírají si jednu nebo více datových množin jako  nejbližší data,  k aktuálnímu vstupu.

Záměrně zde nepoužíváme termín datasetů, ale termín datových množin. Termínem Datasety se nejčastěji označují vstupní datasety. Neboli množina dat, která složí jako vstup do celého Batche,  zadaná případně naměřená uživatelem. V tomto případě Recommendeři zpracovávají i datové množiny které vznikly jako mezivýpočet v průběhu výpočtu Batche. I k těmto datovým množinám jsou po uložena  do databáze vygenerovány metadata.

Po tom, co Recommender najde datovou množinu, která je povahově nejblíže ke vstupním datům, za použití DataManagera projde výsledky všech předchozích výpočtů, které byly prováděny na těchto datech a získá představu o tom, jaká metoda strojového učení byla jak úspěšná. U výsledů Tasků posílané Datamanagerovi k uložení agentem Managerem je samozřejmě i informace o tom, který agent těchto výsledků dosáhl. Recommendeři si vyžádají a následně zpracovávají tyto informace a nakonec na žádost agenta Managera reprezentovanou Ontologii Recommend odpoví jménem třídy doporučovaného agenta.

Jako defaultní Doporučovač se v Pikateru používal BasicRecommender, který vrací agenta dosahující nejmenší chyby ErrorRate na nejpodobnější datové množině.

### Agent DataManager

Tvůrci původního návrhu Pikater jako multiagentního maximálně adaptibilního systém se z akademického pohledu snažili o architekturu, která odstiňuje ne jenom technologii databáze , ale i existenci databázový systém od Pikateru vyčleněním speciálního agenta pro zajišťující uchovávání dat. Agent komunikuje opět pouze pomocí ontologií a má jako jediný v jádře systému možnost přímo číst a měnit databázi.

Další informace související s ukládáním dat a funkcemi DataManagera viz kapitola Distribuce a reprezentace dat a dokument Databáze v pikateru.

Webové GUI využívá na rozdíl od jednotlivých agentů přímý přístup do databáze. Teoreticky by šlo i z webového GUI využívat  pro manipulaci s daty agenta DataManagera ve spolupráci s Gateway agentem. Tato varianta by ale nedovolovala použít konstruktů, které se používají pro perzistování změn dat v grafických pohledech přímo do databáze. Byl bz nutný opětovný překlad struktur reprezentující pohled na ontologie a zpět. Reakce na možné nepříchozí JADE zprávy by přinesly nutnost ošetřit spoustu takovýchto případů. Navíc by webové GUI by se stalo zcela závislé na jádře systému.

#### Univerzálni formát

Mezi jedno z nejdůležitějších využití DataManagera slouží uchovávání a načítání Batchů. Pro uložení jedné instance třídy ComputationDescription se využívá takzvaný "Univerzální formát". Motivací pro vznik tohoto formátu byla snaha vytvořit obecný a rozšiřitelný způsob jak ukládat Batche zadané jak z webového GUI tak z consolových GUIAgentů. Batche zadané z webového GUI obsahují na rozdíl od těch zadaných z konfiguráku informace navíc. Jsou to informace potřebné pro grafické zobrazení krabiček ve webové stavebnici. Jako příklad nepovinných informací slouží Xová a Yová pozice krabičky umístěná v poli stavebnice. Formát dává možnost obohatit stavebnici o další funkce a zároveň se snaží nenutit jádro Pikateru o těchto informacích vědět nebo jim dokonce rozumnět.

Návrh Univerzálního formátu vychází z nápadu obalit Ontologie ze struktury ComputationDescription a do každého wrapperu kromě výpočetního elementu i vložit nepovinou třídu obsahující informace pro GUI. Třída obalující celou strukturu je pojmenovaná UniversalComputationDescription. Třída obsahuje stejně navrženou stromovou strukturu instancí tříd UniversalElement, jako Ontologie ComputatioDescription. Třída UniversalElement zde slouží jako wrapper instance třídy UniversalOntology, což je třída představující datový model pro jednu instanci Ontologie Dataprocessing. Třída UniversalGui slouží pro reprezentaci informací pro webové GUI.

### Agent MetadataQueen

Tento agent má v rámci systému pouze jedinou úlohu, která spočívá ve vygenerování metadat pro jednotlivé datasety. Jedná se o vypočtení statistických veličin popisující pomáhající odhadnout podobnost dvou datasetů bez časové náročné manipulace a opětovného načítání.

Agent zpracovává dva typy zpráv:

* `NewDataset` - typicky obdrží od `Gateway` agenta za použití `WebToCoreEntryPoint` jako reakci na událost kdy uživatel nahrál do systému nový dataset přes webové rozhraní.

{{{{{{

PikaterGateway->+MetadataQueen: NewDataset
MetadataQueen->-DataManager: SaveMetadata

}}}}}}

* `NewComputedData` - po vypočtení nové datové množiny výpočetním agentem nebo `DataProcessingem` agent `Planner` zavolá modul zajišťující přepravu dat, který zajistí uložení datové množiny do databáze za pomoci `DataManagera` na primárním containeru. Po úspěšné přepravě požádá agenta `MetadataQueen` o vygenerování metadat i k této datové množině. Data se totiž můžou u výpočtů obsahující datové závislosti dostat na vstup dalšímu experimentu, kde se může nacházet i `Recommender` potřebující k běhu právě metadata.

{{{{{{

Planner->+MetadataQueen: NewComputedData
MetadataQueen->-DataManager: SaveMetadata

}}}}}}

Nově vypočítaná metadata, využívající pro reprezentaci Ontologii `Metadata` jsou poslány agentovi `DataManagerovi`, na uložení do záznamů o daném datasetu.

### Externí agenti

Požadavkem na rozšíření systému bylo umožnění přidávání nových komponent – agentů, kteří musí být specializací jednoho z typů definovaných systémem (searcher, výpočetní agent, doporučovač), kdy uživatel musí dodat implementaci v podobě JADE agenta, který umí na požádání poslat svoji konfiguraci(mj. pro účely zobrazení v GUI).

Uživatel má možnost skrz webové rozhraní do systému nahrát JAR soubor s přeloženým kódem implementujícím funkčního agenta.  Tento musí dědit od třídy `org.pikater.core.agents.PikaterAgent` nebo některé specializace, např. `org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing`.

Po schválení administrátorem je agent nabídnut v GUI a je prakticky rovnocenný s agenty které poskytuje systém.

Samotné spuštění agenta poskytuje jako službu agent `Agent_ManagerAgent` pomocí stejného rozhraní, jakým se dají vytvářet běžní systémoví agenti, tj. akce `org.pikater.core.ontology.subtrees.management.CreateAgent`.  Pokud tento agent zjistí, že nemá načtený kód potřebný pro spuštění agenta dané třídy, vyžádá si přenos JARka s kódem od agenta `Agent_DataManager`.

Načítání nových tříd v JAR souboru do běžící instance systému zajišťuje platforma JADE, pro kterou je při spouštění systému potřeba zadat na přikazové řádce jako parametr cestu, kde hledat nová JARka:

`-jade_core_management_AgentManagementService_agentspath <cesta>`

V našem případě je to:

`-jade_core_management_AgentManagementService_agentspath core/ext_agents`

Ukázková implementace externího agenta je v samostatném projektu `pikater-ext-agents`, třída `org.pikater.external.ExternalWekaAgent`, která se chová jako výpočetní agent typu RBFNetwork.

#### Vytvoření agenta
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

## Distribuce a reprezentace dat

Jedním z požadavků na nový systém bylo zefektivnění datových přenosů, které v systému probíhají.  Tato kapitola popisuje způsob reprezentace a distribuce jednotlivých typů dat, se kterými systém pracuje a kroky provedené pro optimalizaci těchto přenosů.

Podrobnosti k novému schématu databáze jsou v dokumentu Databaze v Pikateru.

### Datové množiny

Pro vnitřní reprezentaci uložených datasetů systém využívá formát ARFF, který je nativním formátem pro systémem využívanou knihovnu strojového učení WEKA.  ARFF soubor je v podstatě textový CSV soubor obohacený o metadata v podobě anotací.

Formát ARFF je podrobně zdokumentován na vlastních stránkách: http://weka.wikispaces.com/ARFF

Využití tohoto formátu bylo upřednostněno před zavedením vlastního nového formátu pro možnost přepoužití existujících parserů, kvalitní dokumentaci a přívětivost formátu pro lidské i strojové zpracování.  Nevýhodou formátu je neúspornost co do objemu dat, což by bylo možné řešit dodatečnou kompresí při ukládání nebo transportu.

Reprezentace datasetu v paměti je třída (JADE concept) `org.pikater.core.ontology.subtrees.dataInstance.DataInstances`.  Tento je obálkou pro data převoditelné z a do formátu zpracovatelného knihovnou WEKA `weka.core.Instances`.

V podobě `DataInstances` jsou datasety zprostředkovány výpočetním agentům prostřednictvím agenta `org.pikater.core.agents.system.Agent_ARFFReader`.  V původní implementaci systému Pikater se načtená data předávaly výpočetním agentům přímo ACL zprávami, což se ukázalo jako neefektivní – potenciálně velká data musely projít drahou serializací a deserializací i pokud se data předávají v rámci jednoho JADE kontejneru.

Tento problém byl pro lokální přenosy (mezi ARFFReaderem a výpočetními agenty) vyřešen využitím "object to agent" rozhraní, které platforma JADE poskytuje.  K přenosu dat pak dojde pouze pomocí odkazu do stejného adresního prostoru, bez zbytečné serializace nebo kopírování dat.

Pro zefektivnění přenosů mezi různými stroji pak byla zavedená keš dat.  Pokud potřebná data již někdy byla na daný stroj přenesena, agent je načte z keše a k dalším přenosům nedochází.  V případech, kdy je přenos datasetu potřebného k výpočtu nevyhnutelný se nyní data nepřenášejí pomocí ACL zprávy v načtené podobě, ale v serializované podobě z databáze.

Pro uložení dat používají výpočetní agenti připravenou metodu `org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing.saveArff()`.  `Agent_Planner` pak prostřednitcvím `Agent_DataManager` a jeho akce `org.pikater.core.ontology.subtrees.dataset.SaveDataset` zajistí uložení dat do databáze pro možnost vizualizace.

#### Načítání datasetů
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

### Kód implementující jednotlivé metody strojového učení

Přenos JAR souborů s novými metodami strojového učení (nebo jinými typy externích agentů) zajišťuje `Agent_DataManager` na vyžádání agenta `Agent_ManagerAgent`.  JAR soubory pak na daném stroji zůstanou v keši.

Protože jde typicky o malé soubory (do 5 MB), jejich přenos nepředstavuje riziko velkého vytížení infrastruktury.  Přenos probíhá přímým načtením ze vzdálené databáze do souboru.

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

### Natrénované a uložené modely (agenti).

Natrénované modely jsou uloženy v podobě serializované Javovské třídy daného agenta.  Samotnou serializaci modelu zajišťuje knihovna WEKA – její klasifikátory implementují rozhraní `Serializable`.

Serializovaní agenti mají typicky do 100 kB a pro potřeby jejich oživení agentem `Agent_ManagerAgent` jsou mu doručováni jako `byte[]` přímo v ACL zprávě.

{{{{{{

Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

## Popis ontologií:

### AccountOntology

Tuto množinu Ontologií využívají uživateli definovaní GUIagenti příklad Klára GUIagent, kteří pro poslání Batche ke zpracování, potřebují získat ze svého přihlašovacího jména ID svého uživatelského účtu. Na tyto požadavky zpravidla odpovídá agent DataManager, který je schopen tuto informaci vyhledat v databázi. Dále tuto množinu Ontologií využívá agent Mailing, který potřebuje znát k ID uživatele jeho emailovou adresu.

    
### AgentInfoOntology

Jedná se o množinu Ontologií využívající se pro provoz informační infrastruktury zajišťující vstupní data pro webové rozhraní. Středobodem této infrastruktury, tudíž nejčastějším uživatelem těchto antologií je agent AgentInfoManager, který za účelem sbírání informací o existujících krabičkách komunikuje s experimentovými agenty.  Nejdůležitější ontologií je AgentInfo antologie, která popisuje účel experimentového agenta z pohledu grafické krabičky. Těchto informací nevyužívá pouze webové GUI, ale také Recommendeři pro zjištění defaultních hodnot Optionů.   


### AgentManagementOntology

Následující množinu Ontologií využívají všichni agenti, kteří někdy potřebují nějakého agenta vytvořit, zabít, oživit či zmrazit. Adresátem těchto zpráv bývá agent ManagerAgent, který má na containeru, na kterém se nachází absolutistickou moc nad životy všech agentů.  Služeb agenta ManagerAgent využívá agent Initiator při spouštění systému AgentManager při spouštění Searcherů a Recommenderů. Nejfrekventovaněji tyto služby využívá agent  Planner pro vytváření výpočetních agentů, případně agentů představující Dataprocessing.    


### BatchOntology

Tuto roli si osvojili všichni agenti, kteří mají za úkol buď vytvářet, načítat, přepravovat nebo logovat do databáze zadání v jedné Batche. Součástí je pro systém nepostradatelná ontologie ComputationDescription, která slouží k reprezentaci jedné dávky.

### Ontologie ComputationDescription

Ontologie byla navržena tak, aby mohla reprezentovat libovolné množství vzájemně datově závislých DataProcessingu. Jedná se tedy o strukturu logicky představující multigraf, kde vrcholy jsou DataProcessingy a hrany představují tok dat, a to buďto, Errorů nebo datasetů.

DataProcessing představuje obecnou datovou transformaci dat z několika zdrojů na několik různých výstupů. Jedná se tedy z logického pohledu o nejobecnější formu práce s daty v systému zabývající se problémem strojového učení. Ontologie DataProcessing je z tohoto důvodu předkem všech ostatních výpočetních elementů z třídy ComputationDescription. 

#### Struktura

Třída ComputationDescription obsahuje seznam globálních Optionů, který je schopný pojmout za použití ontologie Option neomezeně specifickou konfiguraci. Dále třída obsahuje seznam FileDataSaverů potomků DataProcessingu. Tyto elementy představují logické uložení vyprodukovaných dat v rámci jednoho Batche. Ontologie FileDataSaver obsahuje odkaz na hranu multigrafu, reprezentovanou pomocí DataSourceDescription. Hrana je napojená na další výpočetní element dědící od DataProcessingu. Protože obecný DataProsessing může obsahovat libovolné množství vstupních zdrojů a zároveň může produkovat data pro libovolné množství  DataProcessingů nebo jejich potomků musí každá hrana v multigrafu obsahovat  výstupní identifikátor říkající jaký tip dat má být předán z prvního DataProcessingu do druhého. Zároveň musí ComputationDescription obsahovat i výstupní identifikátor, který říká pro jaký účel se mají data použít. 

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

### Ontologie Task

Ontologie Task je obecná struktura, která je schopná pojmout jakýkoli pro Pikater dále nedělitelný problém určený pro libovolný typ agenta, obsahující neomezené množství vstupních souborů potřebných pro výpočet. Ukrývá informace o identifikaci Batche experimentu, ke kterému patří identifikátor uživatele, který Batche zadal  a spoustu dalších Optionů ovlivňující běh výpočtu. Pro plánování Tasku je zde informace o předpokládané délce běhu výpočtu, která má zajistit průchodnost systému pro krátce trvající výpočty. Po výpočtu Tasku se Ontologie Task předává zpátky až do agenta Managera. V tento okamžik jsou v Tasku vyplněné informace o vypočtených souborech, které výpočetní agent nebo DataProcessing vyprodukoval na file systém. Zároveň vypočtený Task obsahuje datum a čas začátku a konce běhu výpočtu. 


Další agent využívající Ontologii Task je agent Duration, který využívá vlastního výpočetního agenta Duration servis odděděného od Wekovského výpočetního agenta LinearRegresion  pro počítání samplu lineární regrese. Zadávání tohoto samplu se ovšem posílá přímo agentovi DurationServis bez využití služeb agenta Plannera.        


### TerminationOntology

Tuto Ontologii využívají všichni experimentoví agenti, což jsou agenti typu výpočetní agent Recommender a Sercher, případně DataProcessing, kteří jsou buď defaultně součástí systému a nebo si je uživatel může do systému sám přidat v podobě JARka. O zabíjení agentů se stará agent ManagerAgent. Při přidání nového agenta do systému nebo inicializaci systému společně s agentem AgentInfoManager budí a následně zabíjí experimentální agenty. Dochází k buzení a následnému šetrnému zabíjení experimentových agentů.  Aparát šetrného zabíjení agentů je v systému nezbytný, agenty nejde jednoduše pouze zabít, ale musí se také nechat odregistrovat z JADE Directory Facilitator. Nejjednodušší cestou je požádat příslušného agenta z pozice ManagerAgenta, aby sám sebe ukončil. Snaha o čistý způsob zabíjení agentů byla primární motivací pro vznik této ontologie.  
