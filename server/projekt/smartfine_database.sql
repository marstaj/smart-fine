
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Datab�ze: `sf8`
--
CREATE DATABASE `smartfine` DEFAULT CHARACTER SET cp1250 COLLATE cp1250_general_ci;
USE `smartfine`;

-- --------------------------------------------------------

--
-- Struktura tabulky `associations`
--

CREATE TABLE `associations` (
  `DEVICE` int(10) unsigned NOT NULL,
  `POLICEMAN` int(10) unsigned NOT NULL,
  PRIMARY KEY (`DEVICE`,`POLICEMAN`),
  KEY `FKASSOCPOLICEMAN` (`POLICEMAN`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka asociac� mezi policisty a mobiln�mi za��zen�mi.';

-- --------------------------------------------------------

--
-- Struktura tabulky `geolocation`
--

CREATE TABLE `geolocation` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TIME` datetime NOT NULL,
  `LONGTITUDE` double NOT NULL,
  `LATITUDE` double NOT NULL,
  `BADGENUMBER` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKGEOLOCATIONBADGENUMBER` (`BADGENUMBER`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250;

-- --------------------------------------------------------

--
-- Struktura tabulky `mobiledevices`
--

CREATE TABLE `mobiledevices` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IMEI` char(15) NOT NULL COMMENT '��slo IMEI mobiln�ho za��zen�.',
  `NAME` varchar(20) NOT NULL COMMENT 'N�zev mobiln�ho za��zen�',
  `DESCRIPTION` varchar(255) NOT NULL DEFAULT '' COMMENT 'Popis mobiln�ho za��zen�',
  `OFFICE` int(10) unsigned NOT NULL COMMENT 'Slu�ebna, kde je za��zen� registrovan�.',
  `BLOCKED` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Ur�uje, zda nen� za��zen� blokov�no.',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `IMEI` (`IMEI`),
  KEY `FKMOBILEDEVICESOFFICE` (`OFFICE`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka mobiln�ch za��zen�.';

-- --------------------------------------------------------

--
-- Struktura tabulky `offices`
--

CREATE TABLE `offices` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CHIEF` int(10) unsigned NOT NULL COMMENT 'Vedouc� slu�ebny.',
  PRIMARY KEY (`ID`),
  KEY `FKOFFICECHIEF` (`CHIEF`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka policejn�ch slu�eben.';

-- --------------------------------------------------------

--
-- Struktura tabulky `policemen`
--

CREATE TABLE `policemen` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` varchar(20) NOT NULL COMMENT 'Jm�no.',
  `LASTNAME` varchar(20) NOT NULL COMMENT 'P��jmen�.',
  `BADGENUMBER` int(11) NOT NULL COMMENT 'Slu�ebn� ��slo.',
  `PIN` int(11) NOT NULL COMMENT 'PIN.',
  `PERMISSIONS` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Opr�vn�n�.',
  `OFFICE` int(10) unsigned DEFAULT NULL COMMENT 'Policejn� slu�ebna.',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BADGENUMBER` (`BADGENUMBER`),
  KEY `FKPOLICEMENOFFICE` (`OFFICE`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka policist�.';

-- --------------------------------------------------------

--
-- Struktura tabulky `smsparking`
--

CREATE TABLE `smsparking` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `VRP` varchar(10) NOT NULL COMMENT 'SPZ',
  `SINCE` datetime NOT NULL COMMENT 'Od kdy lze parkovat.',
  `UNTIL` datetime NOT NULL COMMENT 'Do kdy lze parkovat.',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka pro z�znam placen�ho parkov�n� pomoc� SMS.';

-- --------------------------------------------------------

--
-- Struktura tabulky `spcblacklist`
--

CREATE TABLE `spcblacklist` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SPCNUMBER` varchar(10) NOT NULL COMMENT '��slo PPK.',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SPCNUMBER` (`SPCNUMBER`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Black list p�enosn�ch parkovac�ch karet.';

-- --------------------------------------------------------

--
-- Struktura tabulky `ticketphotos`
--

CREATE TABLE `ticketphotos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PHOTO` mediumblob NOT NULL,
  `TICKET` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKPHOTOSTICKET` (`TICKET`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka fotografi� k parkovac�m l�stk�m.';

-- --------------------------------------------------------

--
-- Struktura tabulky `tickets`
--

CREATE TABLE `tickets` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UPLOADEDBY` int(11) NOT NULL,
  `BADGENUMBER` int(11) NOT NULL,
  `VRP` varchar(10) NOT NULL,
  `VRPCOLOR` varchar(15) NOT NULL,
  `IVRC` varchar(3) NOT NULL,
  `DATE` datetime NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `STREET` varchar(255) NOT NULL,
  `HOUSENUMBER` int(10) unsigned NOT NULL,
  `LOCATION` varchar(255) NOT NULL,
  `VEHICLETYPE` varchar(50) NOT NULL,
  `VEHICLEBRAND` varchar(50) NOT NULL,
  `MOVEABLEDZ` tinyint(1) NOT NULL,
  `TOW` tinyint(1) NOT NULL,
  `LAW_COLLECTION` smallint(5) unsigned NOT NULL,
  `LAW_LAWNUMBER` smallint(5) unsigned NOT NULL,
  `LAW_LETTER` varchar(2) NOT NULL,
  `LAW_PARAGRAPH` smallint(5) unsigned NOT NULL,
  `LAW_RULEOFLAW` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKTICKETSUPLOADEDBY` (`UPLOADEDBY`),
  KEY `FKTICKETSBADGENUMBER` (`BADGENUMBER`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COMMENT='Tabulka s parkovac�mi l�stky.';


--
-- Omezen� pro tabulku `associations`
--
ALTER TABLE `associations`
  ADD CONSTRAINT `FKASSOCDEVV` FOREIGN KEY (`DEVICE`) REFERENCES `mobiledevices` (`ID`),
  ADD CONSTRAINT `FKASSOCPOLICEMAN` FOREIGN KEY (`POLICEMAN`) REFERENCES `policemen` (`ID`);

--
-- Omezen� pro tabulku `geolocation`
--
ALTER TABLE `geolocation`
  ADD CONSTRAINT `FKGEOLOCATIONBADGENUMBER` FOREIGN KEY (`BADGENUMBER`) REFERENCES `policemen` (`BADGENUMBER`);

--
-- Omezen� pro tabulku `mobiledevices`
--
ALTER TABLE `mobiledevices`
  ADD CONSTRAINT `FKMOBILEDEVICESOFFICE` FOREIGN KEY (`OFFICE`) REFERENCES `offices` (`ID`);

--
-- Omezen� pro tabulku `offices`
--
ALTER TABLE `offices`
  ADD CONSTRAINT `FKOFFICECHIEF` FOREIGN KEY (`CHIEF`) REFERENCES `policemen` (`ID`);

--
-- Omezen� pro tabulku `policemen`
--
ALTER TABLE `policemen`
  ADD CONSTRAINT `FKPOLICEMENOFFICE` FOREIGN KEY (`OFFICE`) REFERENCES `offices` (`ID`);

--
-- Omezen� pro tabulku `ticketphotos`
--
ALTER TABLE `ticketphotos`
  ADD CONSTRAINT `FKPHOTOSTICKET` FOREIGN KEY (`TICKET`) REFERENCES `tickets` (`ID`);

--
-- Omezen� pro tabulku `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `FKTICKETSBADGENUMBER` FOREIGN KEY (`BADGENUMBER`) REFERENCES `policemen` (`BADGENUMBER`),
  ADD CONSTRAINT `FKTICKETSUPLOADEDBY` FOREIGN KEY (`UPLOADEDBY`) REFERENCES `policemen` (`BADGENUMBER`);

