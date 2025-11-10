-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: mein-mysql
-- Erstellungszeit: 09. Nov 2025 um 13:55
-- Server-Version: 9.4.0
-- PHP-Version: 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `Mitgliederverwaltung`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Artikel`
--

CREATE TABLE `Artikel` (
  `ArtikelID` int NOT NULL,
  `Name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Einzelpreis` decimal(6,2) DEFAULT NULL,
  `Kommentar` text COLLATE utf8mb4_unicode_ci,
  `KategorieID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Artikel`
--

INSERT INTO `Artikel` (`ArtikelID`, `Name`, `Einzelpreis`, `Kommentar`, `KategorieID`) VALUES
(1, 'Handtuch L', 9.99, NULL, 4),
(2, 'Handtuch XL', 14.99, NULL, 4),
(3, 'Bio-Protein-Shake', 4.50, 'Special-Harvest', 2),
(4, 'Protein-Shake', 3.50, 'Plastique Nutrition', 2),
(5, 'Kaffee schwarz', 4.50, NULL, 1),
(6, 'Kaffee Milch', 5.00, NULL, 1),
(7, 'Kaffee Hafermilch', 5.50, NULL, 1),
(8, 'Proteinpulver Vanille 500g', 15.99, 'Plastique Nutrition', 3),
(9, 'Proteinpulver Schoko 500g', 15.99, 'Plastique Nutrition', 3),
(10, 'Proteinpulver Banane 500g', 15.99, 'Plastique Nutrition', 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ArtikelBestellung`
--

CREATE TABLE `ArtikelBestellung` (
  `BestellungID` int NOT NULL,
  `ArtikelID` int NOT NULL,
  `Menge` int DEFAULT NULL,
  `Aufaddiert` decimal(6,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `ArtikelBestellung`
--

INSERT INTO `ArtikelBestellung` (`BestellungID`, `ArtikelID`, `Menge`, `Aufaddiert`) VALUES
(1, 2, 1, 14.99),
(1, 4, 1, 4.50),
(2, 4, 1, 4.50),
(3, 5, 1, 4.50),
(3, 6, 1, 5.00),
(4, 1, 1, 9.99),
(5, 3, 4, 18.00),
(5, 8, 1, 15.99),
(6, 5, 1, 4.50),
(6, 6, 1, 5.00);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Bestellung`
--

CREATE TABLE `Bestellung` (
  `BestellungID` int NOT NULL,
  `MitgliederID` int DEFAULT NULL,
  `Gesamtpreis` decimal(6,2) DEFAULT NULL,
  `Bestelldatum` timestamp NULL DEFAULT NULL,
  `ZahlungID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Bestellung`
--

INSERT INTO `Bestellung` (`BestellungID`, `MitgliederID`, `Gesamtpreis`, `Bestelldatum`, `ZahlungID`) VALUES
(1, 3, 19.49, '2025-10-27 12:30:12', 1),
(2, 4, 4.50, '2025-10-27 12:31:54', 1),
(3, 2, 9.50, '2025-10-27 12:43:24', 2),
(4, 1, 9.99, '2025-10-27 12:59:59', 2),
(5, 3, 33.99, '2025-11-06 15:14:02', 2),
(6, 1, 9.50, '2025-11-07 09:38:18', 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Intervall`
--

CREATE TABLE `Intervall` (
  `IntervallID` int NOT NULL,
  `Zahlungsintervall` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Intervall`
--

INSERT INTO `Intervall` (`IntervallID`, `Zahlungsintervall`, `Bezeichnung`) VALUES
(1, '1', '1. Tag des Monats'),
(2, '31', 'Letzter Tag des Monats'),
(3, '7', 'wöchentlich'),
(4, '14', 'zweiwöchentlich');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kategorie`
--

CREATE TABLE `Kategorie` (
  `KategorieID` int NOT NULL,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Kategorie`
--

INSERT INTO `Kategorie` (`KategorieID`, `Bezeichnung`) VALUES
(1, 'Getränke'),
(2, 'Shakes'),
(3, 'NEM'),
(4, 'Non food');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kurs`
--

CREATE TABLE `Kurs` (
  `KursID` int NOT NULL,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Kostenfrei` tinyint(1) DEFAULT NULL COMMENT 'Gibt an, ob der Kurs kostenlos ist (true/false)',
  `Aktiv` tinyint(1) DEFAULT '1' COMMENT 'Gibt an, ob der Kurs aktuell statt findet (true/false)',
  `Teilnehmerzahl` int DEFAULT NULL,
  `Preis` decimal(6,2) DEFAULT NULL,
  `AnzahlTermine` int DEFAULT NULL,
  `Kommentar` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Kurs`
--

INSERT INTO `Kurs` (`KursID`, `Bezeichnung`, `Kostenfrei`, `Aktiv`, `Teilnehmerzahl`, `Preis`, `AnzahlTermine`, `Kommentar`) VALUES
(1, 'Bauch, Beine, Po', 1, 1, 25, NULL, NULL, 'Hallenschuhe, freie Sportkleidung'),
(2, 'Yoga', 1, 1, 15, NULL, NULL, 'Barfüßig/Socken, lockere Kleidung, Handtuch'),
(3, 'AquaCycling', 0, 1, 9, 100.00, 11, 'AquaSchuhe, sichere Badekleidung'),
(4, 'Wirbelsäulengymnastik', 1, 0, 15, NULL, NULL, 'Lockere Kleidung, Handtuch');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kursteilnahme`
--

CREATE TABLE `Kursteilnahme` (
  `MitgliederID` int NOT NULL,
  `KursterminID` int NOT NULL,
  `Angemeldet` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied angemeldet ist oder war (true/false)',
  `Anmeldezeit` timestamp NULL DEFAULT NULL,
  `Abgemeldet` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied abgemeldet ist (true/false)',
  `Abmeldezeit` timestamp NULL DEFAULT NULL,
  `Aktiv` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied am Kurs teilnehmen kann (true/false)',
  `Kommentar` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Kursteilnahme`
--

INSERT INTO `Kursteilnahme` (`MitgliederID`, `KursterminID`, `Angemeldet`, `Anmeldezeit`, `Abgemeldet`, `Abmeldezeit`, `Aktiv`, `Kommentar`) VALUES
(2, 1, 1, '2025-10-25 01:32:39', 0, NULL, 1, NULL),
(2, 5, 1, '2025-10-25 01:33:15', 0, NULL, 1, NULL),
(3, 2, 1, '2025-10-26 14:34:22', 0, NULL, 1, NULL),
(3, 6, 1, '2025-10-26 14:36:45', 0, NULL, 1, NULL),
(4, 2, 1, '2025-10-26 14:37:02', 0, NULL, 1, NULL),
(4, 6, 1, '2025-10-26 14:39:21', 0, NULL, 1, NULL),
(5, 3, 1, '2025-10-18 00:05:23', 0, NULL, 1, NULL),
(5, 4, 1, '2025-10-26 00:05:12', 1, '2025-10-27 12:03:48', 0, 'Versehentlich angemeldet'),
(5, 7, 1, '2025-10-26 00:03:39', 0, NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kurstermin`
--

CREATE TABLE `Kurstermin` (
  `KursterminID` int NOT NULL,
  `KursID` int DEFAULT NULL,
  `Termin` datetime DEFAULT NULL,
  `TrainerID` int DEFAULT NULL,
  `Teilnehmerfrei` int DEFAULT NULL,
  `Anmeldbar` tinyint(1) DEFAULT '1' COMMENT 'Gibt an, ob der Kurs noch freie Plätze hat (true/false)',
  `Aktiv` tinyint(1) DEFAULT '1' COMMENT 'Gibt an, ob der Kurstermin stattfindet (true/false)',
  `Kommentar` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Kurstermin`
--

INSERT INTO `Kurstermin` (`KursterminID`, `KursID`, `Termin`, `TrainerID`, `Teilnehmerfrei`, `Anmeldbar`, `Aktiv`, `Kommentar`) VALUES
(1, 1, '2025-10-27 18:00:00', 1, 11, 1, 1, NULL),
(2, 2, '2025-10-28 18:30:00', 2, 3, 1, 1, NULL),
(3, 3, '2025-10-28 19:45:00', 2, 0, 0, 1, NULL),
(4, 4, '2025-10-29 10:00:00', 1, 2, 1, 1, NULL),
(5, 1, '2025-11-03 18:00:00', 1, 21, 1, 1, NULL),
(6, 2, '2025-11-04 18:30:00', 2, 13, 1, 1, NULL),
(7, 3, '2025-11-04 19:45:00', 2, 0, 0, 1, NULL),
(8, 4, '2025-11-05 10:00:00', 1, 6, 1, 1, NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Mitglieder`
--

CREATE TABLE `Mitglieder` (
  `MitgliederID` int NOT NULL,
  `Vorname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Nachname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Geburtsdatum` date DEFAULT NULL,
  `Aktiv` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied aktiv ist und trainieren darf (true/false)',
  `Straße` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Hausnr` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `OrtID` int DEFAULT NULL,
  `ZahlungsdatenID` int DEFAULT NULL,
  `Telefon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Mail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Mitglieder`
--

INSERT INTO `Mitglieder` (`MitgliederID`, `Vorname`, `Nachname`, `Geburtsdatum`, `Aktiv`, `Straße`, `Hausnr`, `OrtID`, `ZahlungsdatenID`, `Telefon`, `Mail`) VALUES
(1, 'Edgar', 'Hönness', '1957-03-13', 1, 'Benzstraße', '32', 1, 1, '04931038254382', 'e.hoenness@merzedes-bens.de'),
(2, 'Celicia', 'Störm', '1987-05-24', 1, 'Seeweg', '3', 2, 2, '0493240593543', 'celistorm@gmy.de'),
(3, 'Lisa-Marie', 'Störm', '2009-01-31', 1, 'Seeweg', '3', 2, 2, '0494863492135', 'princesslie@alo-mail.de'),
(4, 'Anne-Liese', 'Kreier', '2008-12-02', 1, 'Auenbachstraße', '109', 2, 3, '049573489128', 'lakreier@kreier-professionals.de'),
(5, 'Markus', 'Mayer', '1990-07-17', 1, 'Bachstraße', '4', 3, 4, '0498576432973674', 'mm@inkasso-stolz.de'),
(18, 'Holger', 'Braun', '1900-01-01', 0, 'Holgerstr.', '12a', 21, 16, '4353453534', 'ef34f4t@g544g4g.d4');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MitgliederVertrag`
--

CREATE TABLE `MitgliederVertrag` (
  `VertragNr` int NOT NULL,
  `MitgliederID` int NOT NULL,
  `VertragID` int NOT NULL,
  `Vertragsbeginn` date DEFAULT NULL,
  `Vertragsende` date DEFAULT NULL,
  `Verlängerung` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied bereits verlängert hat (true/false)',
  `Aktiv` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob der Vertrag aktiv ist (true/false)',
  `Gekündigt` tinyint(1) DEFAULT '0' COMMENT 'Gibt an, ob das Mitglied bereits gekündigt hat (true/false)',
  `Preisrabatt` decimal(6,2) DEFAULT NULL,
  `IntervallID` int DEFAULT NULL,
  `ZahlungID` int DEFAULT NULL,
  `Trainingsbeginn` date DEFAULT NULL,
  `Kommentar` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `MitgliederVertrag`
--

INSERT INTO `MitgliederVertrag` (`VertragNr`, `MitgliederID`, `VertragID`, `Vertragsbeginn`, `Vertragsende`, `Verlängerung`, `Aktiv`, `Gekündigt`, `Preisrabatt`, `IntervallID`, `ZahlungID`, `Trainingsbeginn`, `Kommentar`) VALUES
(1, 1, 2, '2024-10-12', '2026-10-13', 1, 1, 0, 2.50, 3, 2, '2024-09-12', 'Vereinsfunktionär, bekommt extra Rabatt von 2,50€'),
(2, 2, 1, '2024-12-31', '2026-01-01', 0, 1, 0, NULL, 4, 2, '2024-12-01', 'Mutter von Lisa-Marie Störm'),
(3, 3, 3, '2024-12-31', '2026-01-01', 0, 1, 0, 1.50, 4, 2, '2024-12-01', 'Tochter von Celicia Störm, hat Anne-Liese Kreier geworben, daher 1,50 € Rabatt'),
(4, 4, 3, '2025-02-15', '2026-02-16', 0, 1, 0, NULL, 4, 2, '2025-02-01', 'Geworben von Celicia Störm'),
(5, 5, 1, '2025-10-26', '2026-10-27', 0, 1, 0, NULL, 4, 1, '2025-10-06', NULL),
(8, 18, 1, '2025-12-01', '2026-11-30', 0, 1, 0, 1.50, 3, 4, '2025-11-07', 'Alter');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Ort`
--

CREATE TABLE `Ort` (
  `OrtID` int NOT NULL,
  `PLZ` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Ort` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Ort`
--

INSERT INTO `Ort` (`OrtID`, `PLZ`, `Ort`) VALUES
(1, '74583', 'Brumhoffen'),
(2, '74584', 'Cellstall'),
(3, '74585', 'Dombstadt'),
(4, '92832', 'Brumsdorf'),
(21, '67323', 'Holort');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Trainer`
--

CREATE TABLE `Trainer` (
  `TrainerID` int NOT NULL,
  `Vorname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Nachname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Kommentar` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Trainer`
--

INSERT INTO `Trainer` (`TrainerID`, `Vorname`, `Nachname`, `Kommentar`) VALUES
(1, 'Holger', 'Braun', NULL),
(2, 'Cecilia', 'Petters', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Vertrag`
--

CREATE TABLE `Vertrag` (
  `VertragID` int NOT NULL,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Laufzeit` int DEFAULT NULL,
  `Grundpreis` decimal(6,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Vertrag`
--

INSERT INTO `Vertrag` (`VertragID`, `Bezeichnung`, `Laufzeit`, `Grundpreis`) VALUES
(1, '12-Monat-Standard', 52, 9.50),
(2, '24-Monat-Standard', 104, 7.50),
(3, '12-Monat-Jugend', 52, 6.50),
(4, '24-Monat-Jugend', 104, 5.50);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Zahlung`
--

CREATE TABLE `Zahlung` (
  `ZahlungID` int NOT NULL,
  `Zahlungsart` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Zahlung`
--

INSERT INTO `Zahlung` (`ZahlungID`, `Zahlungsart`) VALUES
(1, 'Barzahlung'),
(2, 'Abbuchung'),
(3, 'SEPA-Lastschrift'),
(4, 'SEPA-Lastschrift');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Zahlungsdaten`
--

CREATE TABLE `Zahlungsdaten` (
  `ZahlungsdatenID` int NOT NULL,
  `Name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `IBAN` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `BIC` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `Zahlungsdaten`
--

INSERT INTO `Zahlungsdaten` (`ZahlungsdatenID`, `Name`, `IBAN`, `BIC`) VALUES
(1, 'Edgar Hönness', 'DE422020000045244568', 'SFOLDEC1110'),
(2, 'Celicia Störm', 'DE431046000375420923', 'HOBMDEF1028'),
(3, 'Anne-Liese Kreier', 'DE418468000061242957', 'IPOADEA3846'),
(4, 'Markus Mayer', 'DE420923932000658376', 'SFOLDEC1110'),
(16, 'Holger Braun', 'TEST', 'TEST');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `Artikel`
--
ALTER TABLE `Artikel`
  ADD PRIMARY KEY (`ArtikelID`),
  ADD KEY `fk_artikel_kategorie` (`KategorieID`);

--
-- Indizes für die Tabelle `ArtikelBestellung`
--
ALTER TABLE `ArtikelBestellung`
  ADD PRIMARY KEY (`BestellungID`,`ArtikelID`),
  ADD KEY `ArtikelID` (`ArtikelID`);

--
-- Indizes für die Tabelle `Bestellung`
--
ALTER TABLE `Bestellung`
  ADD PRIMARY KEY (`BestellungID`),
  ADD KEY `MitgliederID` (`MitgliederID`),
  ADD KEY `ZahlungID` (`ZahlungID`);

--
-- Indizes für die Tabelle `Intervall`
--
ALTER TABLE `Intervall`
  ADD PRIMARY KEY (`IntervallID`);

--
-- Indizes für die Tabelle `Kategorie`
--
ALTER TABLE `Kategorie`
  ADD PRIMARY KEY (`KategorieID`);

--
-- Indizes für die Tabelle `Kurs`
--
ALTER TABLE `Kurs`
  ADD PRIMARY KEY (`KursID`);

--
-- Indizes für die Tabelle `Kursteilnahme`
--
ALTER TABLE `Kursteilnahme`
  ADD PRIMARY KEY (`MitgliederID`,`KursterminID`),
  ADD KEY `KursterminID` (`KursterminID`);

--
-- Indizes für die Tabelle `Kurstermin`
--
ALTER TABLE `Kurstermin`
  ADD PRIMARY KEY (`KursterminID`),
  ADD KEY `KursID` (`KursID`),
  ADD KEY `TrainerID` (`TrainerID`);

--
-- Indizes für die Tabelle `Mitglieder`
--
ALTER TABLE `Mitglieder`
  ADD PRIMARY KEY (`MitgliederID`),
  ADD KEY `OrtID` (`OrtID`),
  ADD KEY `ZahlungsdatenID` (`ZahlungsdatenID`);

--
-- Indizes für die Tabelle `MitgliederVertrag`
--
ALTER TABLE `MitgliederVertrag`
  ADD PRIMARY KEY (`VertragNr`,`MitgliederID`,`VertragID`),
  ADD KEY `MitgliederID` (`MitgliederID`),
  ADD KEY `VertragID` (`VertragID`),
  ADD KEY `ZahlungID` (`ZahlungID`),
  ADD KEY `IntervallID` (`IntervallID`);

--
-- Indizes für die Tabelle `Ort`
--
ALTER TABLE `Ort`
  ADD PRIMARY KEY (`OrtID`);

--
-- Indizes für die Tabelle `Trainer`
--
ALTER TABLE `Trainer`
  ADD PRIMARY KEY (`TrainerID`);

--
-- Indizes für die Tabelle `Vertrag`
--
ALTER TABLE `Vertrag`
  ADD PRIMARY KEY (`VertragID`);

--
-- Indizes für die Tabelle `Zahlung`
--
ALTER TABLE `Zahlung`
  ADD PRIMARY KEY (`ZahlungID`);

--
-- Indizes für die Tabelle `Zahlungsdaten`
--
ALTER TABLE `Zahlungsdaten`
  ADD PRIMARY KEY (`ZahlungsdatenID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Artikel`
--
ALTER TABLE `Artikel`
  MODIFY `ArtikelID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT für Tabelle `Bestellung`
--
ALTER TABLE `Bestellung`
  MODIFY `BestellungID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT für Tabelle `Intervall`
--
ALTER TABLE `Intervall`
  MODIFY `IntervallID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `Kategorie`
--
ALTER TABLE `Kategorie`
  MODIFY `KategorieID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `Kurs`
--
ALTER TABLE `Kurs`
  MODIFY `KursID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `Kurstermin`
--
ALTER TABLE `Kurstermin`
  MODIFY `KursterminID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT für Tabelle `Mitglieder`
--
ALTER TABLE `Mitglieder`
  MODIFY `MitgliederID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT für Tabelle `MitgliederVertrag`
--
ALTER TABLE `MitgliederVertrag`
  MODIFY `VertragNr` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT für Tabelle `Ort`
--
ALTER TABLE `Ort`
  MODIFY `OrtID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT für Tabelle `Trainer`
--
ALTER TABLE `Trainer`
  MODIFY `TrainerID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT für Tabelle `Vertrag`
--
ALTER TABLE `Vertrag`
  MODIFY `VertragID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `Zahlung`
--
ALTER TABLE `Zahlung`
  MODIFY `ZahlungID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `Zahlungsdaten`
--
ALTER TABLE `Zahlungsdaten`
  MODIFY `ZahlungsdatenID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Artikel`
--
ALTER TABLE `Artikel`
  ADD CONSTRAINT `fk_artikel_kategorie` FOREIGN KEY (`KategorieID`) REFERENCES `Kategorie` (`KategorieID`) ON UPDATE CASCADE;

--
-- Constraints der Tabelle `ArtikelBestellung`
--
ALTER TABLE `ArtikelBestellung`
  ADD CONSTRAINT `artikelbestellung_ibfk_1` FOREIGN KEY (`BestellungID`) REFERENCES `Bestellung` (`BestellungID`) ON DELETE CASCADE,
  ADD CONSTRAINT `artikelbestellung_ibfk_2` FOREIGN KEY (`ArtikelID`) REFERENCES `Artikel` (`ArtikelID`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `Bestellung`
--
ALTER TABLE `Bestellung`
  ADD CONSTRAINT `bestellung_ibfk_1` FOREIGN KEY (`MitgliederID`) REFERENCES `Mitglieder` (`MitgliederID`) ON DELETE CASCADE,
  ADD CONSTRAINT `bestellung_ibfk_2` FOREIGN KEY (`ZahlungID`) REFERENCES `Zahlung` (`ZahlungID`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `Kursteilnahme`
--
ALTER TABLE `Kursteilnahme`
  ADD CONSTRAINT `kursteilnahme_ibfk_1` FOREIGN KEY (`MitgliederID`) REFERENCES `Mitglieder` (`MitgliederID`) ON DELETE CASCADE,
  ADD CONSTRAINT `kursteilnahme_ibfk_2` FOREIGN KEY (`KursterminID`) REFERENCES `Kurstermin` (`KursterminID`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `Kurstermin`
--
ALTER TABLE `Kurstermin`
  ADD CONSTRAINT `kurstermin_ibfk_1` FOREIGN KEY (`KursID`) REFERENCES `Kurs` (`KursID`) ON DELETE CASCADE,
  ADD CONSTRAINT `kurstermin_ibfk_2` FOREIGN KEY (`TrainerID`) REFERENCES `Trainer` (`TrainerID`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `Mitglieder`
--
ALTER TABLE `Mitglieder`
  ADD CONSTRAINT `mitglieder_ibfk_1` FOREIGN KEY (`OrtID`) REFERENCES `Ort` (`OrtID`) ON DELETE CASCADE,
  ADD CONSTRAINT `mitglieder_ibfk_2` FOREIGN KEY (`ZahlungsdatenID`) REFERENCES `Zahlungsdaten` (`ZahlungsdatenID`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `MitgliederVertrag`
--
ALTER TABLE `MitgliederVertrag`
  ADD CONSTRAINT `mitgliedervertrag_ibfk_1` FOREIGN KEY (`MitgliederID`) REFERENCES `Mitglieder` (`MitgliederID`) ON DELETE CASCADE,
  ADD CONSTRAINT `mitgliedervertrag_ibfk_2` FOREIGN KEY (`VertragID`) REFERENCES `Vertrag` (`VertragID`) ON DELETE CASCADE,
  ADD CONSTRAINT `mitgliedervertrag_ibfk_3` FOREIGN KEY (`ZahlungID`) REFERENCES `Zahlung` (`ZahlungID`) ON DELETE CASCADE,
  ADD CONSTRAINT `mitgliedervertrag_ibfk_4` FOREIGN KEY (`IntervallID`) REFERENCES `Intervall` (`IntervallID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
