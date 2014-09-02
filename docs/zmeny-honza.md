## Provedené změny (část programátorské dokumentace)

### Distribuce a reprezentace dat
#### Požadavky
****
Typy dat:

• Soubory s datovými množinami.

• Kód implementující jednotlivé metody strojového učení

• Natrénované a uložené modely (agenti).

Podmínky distribuce:

• v jednotném formátu, kterému rozumí všichni výpočetní agenti,

• dostatečně efektivně na to, aby byla umožněna práce i s velkými množinami,

• co nejméně, ve smyslu opakovaného přenosu daných dat na jeden výpočetní
uzel.
****

#### Realizace

##### Datové množiny

Pro vnitřní reprezentaci uložených datasetů systém využívá formát ARFF, který je nativním formátem pro systémem využívanou knihovnu strojového učení WEKA.  ARFF soubor je v podstatě textový CSV soubor obohacený o metadata v podobě anotací.

Formát ARFF je podrobně zdokumentován na vlastních stránkách: http://weka.wikispaces.com/ARFF

Využití tohoto formátu bylo upřednostněno před zavedením vlastního nového formátu pro možnost přepoužití existujících parserů, kvalitní dokumentaci a přívětivost formátu pro lidské i strojové zpracování.  Nevýhodou formátu je neúspornost co do objemu dat, což by bylo možné řešit dodatečnou kompresí při ukládání nebo transportu.

Reprezentace datasetu v paměti je třída (JADE concept) `org.pikater.core.ontology.subtrees.dataInstance.DataInstances`.  Tento je obálkou pro data převoditelné z a do formátu zpracovatelného knihovnou WEKA `weka.core.Instances`.

V podobě `DataInstances` jsou datasety zprostředkovány výpočetním agentům prostřednictvím agenta `org.pikater.core.agents.system.Agent_ARFFReader`.  V původní implementaci systému Pikater se načtená data předávaly výpočetním agentům přímo ACL zprávami, což se ukázalo jako neefektivní – potenciálně velká data musely projít drahou serializací a deserializací i pokud se data předávají v rámci jednoho JADE kontejneru.

Tento problém byl pro lokální přenosy (mezi ARFFReaderem a výpočetními agenty) vyřešen využitím "object to agent" rozhraní, které platforma JADE poskytuje.  K přenosu dat pak dojde pouze pomocí odkazu do stejného adresního prostoru, bez zbytečné serializace nebo kopírování dat.

Pro zefektivnění přenosů mezi různými stroji pak byla zavedená keš dat.  Pokud potřebná data již někdy byla na daný stroj přenesena, agent je načte z keše a k dalším přenosům nedochází.  V případech, kdy je přenos datasetu potřebného k výpočtu nevyhnutelný se nyní data nepřenášejí pomocí ACL zprávy v načtené podobě, ale v serializované podobě z databáze.

Pro uložení dat používají výpočetní agenti připravenou metodu `org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing.saveArff()`.  `Agent_Planner` pak prostřednitcvím `Agent_DataManager` a jeho akce `org.pikater.core.ontology.subtrees.dataset.SaveDataset` zajistí uložení dat do databáze pro možnost vizualizace.

###### Načítání dat
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

##### Kód implementující jednotlivé metody strojového učení

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

##### Natrénované a uložené modely (agenti).

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

### Externí agenti

#### Požadavky
****
* Přidávání nových komponent -„krabiček“. „Krabička” musí být specializací
jednoho z typů definovaných systémem (Search, výpočetní agent,
doporučovač) a uživatel musí dodat implementaci v podobě JADE agenta,
který umí na požádání poslat svoji konfiguraci(mj. pro účely zobrazení v GUI).
Přidání úplně nového typu krabičky umožněno nebude. Implementace bude
podléhat kontrole a schválení administrátorem systému, systém neposkytuje
ochranu proti potenciálně škodlivé implementaci uživatelských agentů.

****

#### Realizace

Uživatel má možnost skrz webové rozhraní do systému nahrát JAR soubor s přeloženým kódem implementujícím funkčního agenta.  Tento musí dědit od třídy `org.pikater.core.agents.PikaterAgent` nebo některé specializace, např. `org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing`.

Po schválení administrátorem je agent nabídnut v GUI a je prakticky rovnocenný s agenty které poskytuje systém.

Samotné spuštění agenta poskytuje jako službu agent `Agent_ManagerAgent` pomocí stejného rozhraní, jakým se dají vytvářet běžní systémoví agenti, tj. akce `org.pikater.core.ontology.subtrees.management.CreateAgent`.  Pokud tento agent zjistí, že nemá načtený kód potřebný pro spuštění agenta dané třídy, vyžádá si přenos JARka s kódem od agenta `Agent_DataManager`.

Načítání nových tříd v JAR souboru do běžící instance systému zajišťuje platforma JADE, pro kterou je při spouštění systému potřeba zadat na přikazové řádce jako parametr cestu, kde hledat nová JARka:

`-jade_core_management_AgentManagementService_agentspath <cesta>`

V našem případě je to:

`-jade_core_management_AgentManagementService_agentspath core/ext_agents`

Ukázková implementace externího agenta je v samostatném projektu `pikater-ext-agents`, třída `org.pikater.external.ExternalWekaAgent`, která se chová jako výpočetní agent typu RBFNetwork.

##### Vytvoření agenta
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

### Modely

#### Požadavky
****
8. U každého trénování metody bude možné nastavit, zda se má trvale uložit
celý natrénovaný model. V případě použití prohledávacích algoritmů bude
zároveň možnost trvale uložit pouze nejlepší natrénovaný model.
Trvale uložené agenty bude možné použít v dalších výpočtech bez jejich
nového trénování.

****

#### Realizace

Výpočetní agent v závislosti na zadání úlohy (`Task`) po jejím vyřešení serializuje svůj model a pošle jej jako součást výsledku výpočtu v conceptu `Task`.

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

<!--
### DataRegistry

#### Požadavky
****
* Šetrnost k využití síťové infrastruktury – omezení zbytečných přesunů dat
mezi jednotlivými stroji, příp. agenty v rámci jednoho stroje (když už má
agent data načtena, je lepší použít ho znovu na stejných datech, než data
načítat znova v jiném agentovi).

****

#### Realizace

TODO

### Zbytek

* ukončování agentů ?
-->
