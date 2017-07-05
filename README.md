## osm17L_projekt2

**Przedmiot:** OSM

**Projekt:** 2

**Zadanie:** 12

**Temat:** System udostępniania wyników badań obrazowych pacjenta. Zadaniem
programu jest powiązanie danych pacjenta z wynikami wszystkich jego badań obrazowych
(przechowywanych w formacie DICOM) wykonanych w danej placówce. Program powinien
korzystać z relacyjnej bazy danych (np. Java DB w trybie embedded) i zapewniać prosty podgląd
danych obrazowych.


**Zespol:** Aleksandra Zając, Tomasz Kopacz

**Biblioteki:** derby.jar, ij.jar

**Uwagi dodatkowe:** brak

### Opis

Program umożliwia wyświetlanie badań każdego dodanego pacjenta. Możliwy jest podgląd zarówno danych pacjenta, obrazu graficznego konkretnego badania jak i informacje o badaniu uzyskane na podsatwie standardu DICOM. Wszystkie informacje przechowywane są w bazie danych. Istnieje możliwośc dodania wielu pacjentów do bazy, każdy z nich może posiadac wiele serii, a w każdej serii znajdują się obrazy badań. Ponadto możliwa jest edycja danych, usuwanie oraz zapis na dysku obrazów (w formacie dcm i jpg).

### Struktura kodu programu

Kod programu podzielony jest na 3 główne części, znajdujące się w osobnych pakietach:
* View - odpowiada za wygląd aplikacji
* Model - przechowuje klasę dostępu do bazy danych i reprezentacje obiektów: pacjenta, serii badań, badania
* Kontroler - jego zadaniem jest nadzorowanie działania programu na podstawie interakcji z użytkownikiem

#### Wygląd - pakiet View

W sklad pakietu wchodzą klasy:
* Application - gówne, podstawowe okno. Posiada pusty panel, w którym będą umieszczane podrzędne panele (panel startowy i panel widoku pacjenta). Ponadto zawiera menu, w którym można dodac lub wyszukac pacjenta, zamknąc aplikację, uzyskac informacje o programie
* StartPanel - panel początkowy, pojawiający się zaraz po uruchomieniu aplikacji. Posiada 2 przyciski: dodaj pacjenta i wyszukaj pacjenta
* PatientViewPanel - gowny panel użytkowy aplikacji. Sklada się z trzech części: Toolbar, umożliwiający dodanie serii lub badania, zapis badania da dysku, uzyskanie szczególowych informacji o badaniu; Drzewko, zawierające listę wszystkich serii i badan; panel wyświetlania obrazu.
* AddingPatientFrame, SearchingPatientFrame i inne - okna podrzędne, odpowiadające za dodanie pacjenta, wyszukanie go w bazie, wyswietlające informacje

#### Dostęp do bazy danych - pakiet Model

Glówną klasą pakietu jest DAO, odpowiadająca za komunikację z bazą danych. Klasa jest podzielona na 3 sekcje. Każda sekcja umożliwia podstawowe operacje CRUD dla każdego z obiektów: pacjent, seria, badanie. Do tych operacji zaliczają się: create, update, read (pobiera wiele wierszy z tabeli na podstawie podanych filtrów), get by id (pobiera jeden wiersz o podanym kluczu), delete, zmodyfikowane metody delete (do grupowego usuwania danych, np. wszystkie serie dla podanego pacjenta).

#### Mózg aplikacji - pakiet Controller

Kontroler jest klasą kierującą aplikacją na podstawie interakcji z użytkownikiem. Obsuguje takie zdarzenia jak: kliknięcie LPM lub PPM, najechanie myszką na przycisk, zmiana zaznaczenia galązki drzewka.

### Podzial zadan
**Aleksandra Zając** - View (Application, StartPanel, PatientViewPanel), Model (DAO, Series) </br>
**Tomasz Kopacz** - Controller, View (SearchingPatientFrame, AddingPatientFrame), Model (Patient, Test).
