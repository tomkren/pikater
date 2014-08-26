Provedené změny
=====

Plánovač
-----
2. Šetrnost k využití síťové infrastruktury – omezení zbytečných přesunů dat
mezi jednotlivými stroji, příp. agenty v rámci jednoho stroje (když už má
agent data načtena, je lepší použít ho znovu na stejných datech, než data
načítat znova v jiném agentovi).


Distribuce dat
-----

• Soubory s datovými množinami.

• Kód implementující jednotlivé metody strojového učení

• Natrénované a uložené modely (agenti).


• v jednotném formátu, kterému rozumí všichni výpočetní agenti,

• dostatečně efektivně na to, aby byla umožněna práce i s velkými množinami,

• co nejméně, ve smyslu opakovaného přenosu daných dat na jeden výpočetní
uzel.

Externí agenti
----
9. Přidávání nových komponent -„krabiček“. „Krabička” musí být specializací
jednoho z typů definovaných systémem (Search, výpočetní agent,
doporučovač) a uživatel musí dodat implementaci v podobě JADE agenta,
který umí na požádání poslat svoji konfiguraci(mj. pro účely zobrazení v GUI).
Přidání úplně nového typu krabičky umožněno nebude. Implementace bude
podléhat kontrole a schválení administrátorem systému, systém neposkytuje
ochranu proti potenciálně škodlivé implementaci uživatelských agentů.


Mailing
----
7. Možnost upozornění na dokončení výpočtu prostřednictvím e-mailu. Budou
v něm zároveň stručné výsledky experimentu – výsledky metod na daných
datech.


Modely
----

8. U každého trénování metody bude možné nastavit, zda se má trvale uložit
celý natrénovaný model. V případě použití prohledávacích algoritmů bude
zároveň možnost trvale uložit pouze nejlepší natrénovaný model.
Trvale uložené agenty bude možné použít v dalších výpočtech bez jejich
nového trénování.

{{{{{{
title Vytvoření agenta s natrénovaným modelem

Planner->ManagerAgent: LoadAgent (ID)
ManagerAgent->DataManager: GetModel (ID)
DataManager->ManagerAgent: (agent)
ManagerAgent->Planner: 
}}}}}}

Zbytek
---

* ukončování agentů ?