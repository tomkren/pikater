Provedené změny
=====

Distribuce dat
-----
##### Požadavky
****
• Soubory s datovými množinami.

• Kód implementující jednotlivé metody strojového učení

• Natrénované a uložené modely (agenti).


• v jednotném formátu, kterému rozumí všichni výpočetní agenti,

• dostatečně efektivně na to, aby byla umožněna práce i s velkými množinami,

• co nejméně, ve smyslu opakovaného přenosu daných dat na jeden výpočetní
uzel.
****

TODO

##### Načítání dat
{{{{{{

ComputingAgent->ARFFReader: GetData
opt data not cached
  ARFFReader->DataManager:
  DataManager->DataManager: gets data from DB,\nstores them locally
  DataManager->ARFFReader:
end
ARFFReader->ARFFReader: loads data
ARFFReader->ComputingAgent: (data - O2A)
}}}}}}


Externí agenti
----
##### Požadavky
****
* Přidávání nových komponent -„krabiček“. „Krabička” musí být specializací
jednoho z typů definovaných systémem (Search, výpočetní agent,
doporučovač) a uživatel musí dodat implementaci v podobě JADE agenta,
který umí na požádání poslat svoji konfiguraci(mj. pro účely zobrazení v GUI).
Přidání úplně nového typu krabičky umožněno nebude. Implementace bude
podléhat kontrole a schválení administrátorem systému, systém neposkytuje
ochranu proti potenciálně škodlivé implementaci uživatelských agentů.

****

TODO

Mailing
----
##### Požadavky
****
* Možnost upozornění na dokončení výpočtu prostřednictvím e-mailu. Budou
v něm zároveň stručné výsledky experimentu – výsledky metod na daných
datech.

****

Do systému byl zaveden nový agent `org.pikater.core.agents.system.Agent_Mailing`, který zprostředkovává službu posílání e-mailů ostatním agentům jádra pomocí akce `SendEmail`.

##### Odesílání notifikace o dokončeném výpočtu
Agent\nPlanner->Agent\nDataManager: updateExperimentStatus
opt new experiment status = FINISHED
Agent\nDataManager->Agent\nMailing: SendEmail
  Agent\nMailing->Agent\nMailing: sends e-mail notification\nvia local SMTP server
  Agent\nMailing-->Agent\nDataManager: 
end
Agent\nDataManager-->Agent\nPlanner:

Komunikace s lokálním SMTP serverem potřebná k odeslání e-mailu je pro potřeby agenta `Agent_Mailing` a webového serveru zprostředkována rozhraním `org.pikater.shared.utilities.mailing.Mailing`, resp. metodou `org.pikater.shared.utilities.mailing.Mailing.sendEmail(to, subj, body)`.

Modely
----
##### Požadavky
****
8. U každého trénování metody bude možné nastavit, zda se má trvale uložit
celý natrénovaný model. V případě použití prohledávacích algoritmů bude
zároveň možnost trvale uložit pouze nejlepší natrénovaný model.
Trvale uložené agenty bude možné použít v dalších výpočtech bez jejich
nového trénování.

****

TODO

##### Vytvoření agenta s natrénovaným modelem
{{{{{{
Planner->ManagerAgent: LoadAgent (ID)
ManagerAgent->DataManager: GetModel (ID)
DataManager->ManagerAgent: (agent)
ManagerAgent->Planner: 
}}}}}}

##### Vytvoření agenta
{{{{{{
Planner->ManagerAgent: CreateAgent (type)
ManagerAgent->Planner: (agent name)
}}}}}}


DataRegistry
-----

##### Požadavky
****
* Šetrnost k využití síťové infrastruktury – omezení zbytečných přesunů dat
mezi jednotlivými stroji, příp. agenty v rámci jednoho stroje (když už má
agent data načtena, je lepší použít ho znovu na stejných datech, než data
načítat znova v jiném agentovi).

****

TODO

Zbytek
---

* ukončování agentů ?