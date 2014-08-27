## Provedené změny

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

TODO

### Mailing

#### Požadavky
****
* Možnost upozornění na dokončení výpočtu prostřednictvím e-mailu. Budou
v něm zároveň stručné výsledky experimentu – výsledky metod na daných
datech.

****

#### Realizace

Do systému byl zaveden nový agent `org.pikater.core.agents.system.Agent_Mailing`, který zprostředkovává službu posílání e-mailů ostatním agentům jádra pomocí akce `SendEmail`.

Komunikace s lokálním SMTP serverem potřebná k odeslání e-mailu je pro potřeby agenta `Agent_Mailing` a webového serveru zprostředkována rozhraním `org.pikater.shared.utilities.mailing.Mailing`, resp. metodou `org.pikater.shared.utilities.mailing.Mailing.sendEmail(to, subj, body)`.

##### Odesílání notifikace o dokončeném výpočtu
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

TODO

##### Vytvoření agenta s natrénovaným modelem
{{{{{{

Manager->+ManagerAgent: LoadAgent (ID)
activate Manager
ManagerAgent->+DataManager: GetModel (ID)
DataManager-->>-ManagerAgent: (agent)
ManagerAgent->ManagerAgent: starts new agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}

##### Vytvoření agenta
{{{{{{

Manager->+ManagerAgent: CreateAgent (type)
activate Manager
opt external agent JAR not present locally
ManagerAgent->+DataManager: GetExternalAgentJar (type)
DataManager->DataManager: loads JAR\nstores it locally
DataManager-->>-ManagerAgent: 
end
ManagerAgent->ManagerAgent: starts saved agent
ManagerAgent-->>-Manager: (agent AID)
}}}}}}


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