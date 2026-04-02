-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: master_tyres_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `categoria_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) NOT NULL,
  PRIMARY KEY (`categoria_id`),
  UNIQUE KEY `categoria_id_UNIQUE` (`categoria_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'SIN CATEGORIA'),(2,'SUV'),(3,'PICKUP'),(4,'HATCHBACK'),(5,'CONVERTIBLE'),(6,'CAMIONETA'),(7,'DEPORTIVO'),(8,'COUPE'),(9,'SEDÁN'),(10,'VAN'),(11,'MINIVAN');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `cliente_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre_cliente` varchar(50) NOT NULL,
  `apellido_cliente` varchar(30) DEFAULT NULL,
  `segundo_apellido` varchar(30) DEFAULT NULL,
  `hobbie` varchar(30) DEFAULT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `num_telefono` varchar(13) DEFAULT NULL,
  `estado` varchar(30) NOT NULL,
  `ciudad` varchar(60) DEFAULT NULL,
  `domicilio` varchar(200) DEFAULT NULL,
  `tipo_cliente` enum('EMPRESA','INDIVIDUAL') NOT NULL,
  `fecha_cumpleanios` date DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` enum('ACTIVE','INACTIVE','DELETE') NOT NULL DEFAULT 'ACTIVE',
  `genero` char(1) DEFAULT NULL,
  `correo` varchar(320) DEFAULT NULL,
  `nombre_empresa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`cliente_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_promocion`
--

DROP TABLE IF EXISTS `cliente_promocion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_promocion` (
  `cliente_promocion_id` int unsigned NOT NULL AUTO_INCREMENT,
  `promocion_id` int unsigned NOT NULL,
  `cliente_id` int unsigned NOT NULL,
  PRIMARY KEY (`cliente_promocion_id`),
  UNIQUE KEY `cliente_promocion_id` (`cliente_promocion_id`),
  KEY `fk_cliente_promocion_promocion` (`promocion_id`),
  KEY `fk_cliente_promocion_cliente` (`cliente_id`),
  CONSTRAINT `fk_cliente_promocion_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`cliente_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cliente_promocion_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`promocion_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_promocion`
--

LOCK TABLES `cliente_promocion` WRITE;
/*!40000 ALTER TABLE `cliente_promocion` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente_promocion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_categoria`
--

DROP TABLE IF EXISTS `detalle_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_categoria` (
  `detalle_categoria_id` int unsigned NOT NULL AUTO_INCREMENT,
  `marca_id` int unsigned NOT NULL,
  `modelo_id` int unsigned NOT NULL,
  `categoria_id` int unsigned NOT NULL,
  PRIMARY KEY (`detalle_categoria_id`),
  UNIQUE KEY `uq_marca_modelo_categoria` (`marca_id`,`modelo_id`,`categoria_id`),
  KEY `fk_detalle_modelo` (`modelo_id`),
  KEY `fk_detalle_categoria` (`categoria_id`),
  CONSTRAINT `fk_detalle_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_marca` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`marca_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_modelo` FOREIGN KEY (`modelo_id`) REFERENCES `modelo` (`modelo_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_categoria`
--

LOCK TABLES `detalle_categoria` WRITE;
/*!40000 ALTER TABLE `detalle_categoria` DISABLE KEYS */;
INSERT INTO `detalle_categoria` VALUES (1,2,2,10),(2,2,3,10),(3,2,4,10),(4,2,5,10),(5,2,6,2),(6,2,7,2),(7,2,8,2),(8,2,9,2),(9,3,10,10),(10,3,11,10),(11,3,12,10),(12,3,13,2),(13,3,14,2),(14,3,15,2),(15,3,16,2),(16,3,17,9),(17,3,18,9),(18,3,19,9),(19,4,20,2),(20,4,21,2),(21,4,22,2),(22,4,23,2),(23,4,24,10),(24,4,25,10),(25,4,26,10),(26,5,27,4),(27,5,28,4),(28,5,29,10),(29,5,30,10),(30,5,31,10),(31,5,32,2),(32,5,33,2),(33,5,34,2),(34,5,35,2),(35,5,36,2),(36,5,37,2),(37,5,38,3),(38,6,39,2),(39,6,40,2),(40,6,41,2),(41,6,42,2),(42,6,43,10),(43,6,44,10),(44,6,45,9),(45,7,46,10),(46,7,47,10),(47,7,48,10),(48,7,49,10),(49,7,50,10),(50,7,51,4),(51,7,52,4),(52,7,53,4),(53,7,54,10),(54,7,55,2),(55,7,56,2),(56,7,57,2),(57,7,58,2),(58,7,59,2),(59,7,60,2),(60,7,61,2),(61,7,62,2),(62,7,63,2),(63,7,64,3),(64,7,65,3),(65,7,66,3),(66,7,67,3),(67,7,68,3),(68,7,69,3),(69,7,70,9),(70,7,71,9),(71,7,72,4),(72,7,73,2),(73,7,74,2),(74,8,75,10),(75,8,76,10),(76,8,77,10),(77,8,78,10),(78,8,79,10),(79,8,80,10),(80,8,81,10),(81,8,82,8),(82,8,83,8),(83,8,84,8),(84,8,85,4),(85,8,86,9),(86,8,87,2),(87,8,88,2),(88,8,89,4),(89,8,90,9),(90,8,91,10),(91,8,92,10),(92,8,93,10),(93,8,94,10),(94,9,95,10),(95,9,96,10),(96,9,97,10),(97,9,98,9),(98,9,99,10),(99,9,100,10),(100,9,101,10),(101,9,102,10),(102,9,103,10),(103,9,104,2),(104,9,105,2),(105,9,106,2),(106,9,107,2),(107,9,108,4),(108,9,109,8),(109,9,110,8),(110,9,111,3),(111,9,112,3),(112,9,113,3),(113,9,114,3),(114,9,115,9),(115,9,116,9),(116,9,117,4),(117,9,118,4),(118,9,119,10),(119,9,120,10),(120,9,121,10),(121,9,122,2),(122,9,123,2),(123,10,124,4),(124,10,125,4),(125,10,126,2),(126,10,127,4),(127,10,128,4),(128,10,129,4),(129,10,130,4),(130,10,131,4),(131,10,132,10),(132,10,133,10),(133,10,134,10),(134,10,135,4),(135,10,136,3),(136,10,137,3),(137,10,138,4),(138,10,139,4),(139,11,140,4),(140,11,141,10),(141,11,142,10),(142,11,143,10),(143,11,144,10),(144,11,145,10),(145,11,146,10),(146,11,147,10),(147,11,148,9),(148,11,149,9),(149,11,150,2),(150,11,151,2),(151,11,152,2),(152,11,153,2),(153,11,154,2),(154,11,155,2),(155,11,156,2),(156,11,157,2),(157,11,158,2),(158,11,159,2),(159,11,160,2),(160,11,161,2),(161,11,162,2),(162,11,163,3),(163,11,164,3),(164,11,165,3),(165,11,166,3),(166,11,167,3),(167,11,168,3),(168,11,169,3),(169,11,170,3),(170,11,171,8),(171,11,172,8),(172,11,173,4),(173,11,174,4),(174,12,175,2),(175,12,176,2),(176,12,177,2),(177,12,178,2),(178,12,179,2),(179,12,180,3),(180,12,181,3),(181,12,182,3),(182,12,183,3),(183,12,184,3),(184,12,185,3),(185,12,186,2),(186,12,187,2),(187,12,188,2),(188,12,189,2),(189,12,190,2),(190,12,191,3),(191,13,192,10),(192,13,193,10),(193,13,194,10),(194,13,195,4),(195,13,196,4),(196,13,197,4),(197,13,198,2),(198,13,199,2),(199,13,200,2),(200,13,201,2),(201,13,202,2),(202,13,203,2),(203,13,204,2),(204,13,205,2),(205,13,206,8),(206,13,207,3),(207,13,208,9),(208,13,209,9),(209,13,210,4),(210,13,211,5),(211,13,212,10),(212,13,213,10),(213,14,214,10),(214,14,215,10),(215,14,216,10),(216,14,217,4),(217,14,218,4),(218,14,219,4),(219,14,220,2),(220,14,221,2),(221,14,222,2),(222,14,223,2),(223,14,224,2),(224,14,225,2),(225,15,226,3),(226,15,227,2),(227,15,228,2),(228,15,229,2),(229,15,230,2),(230,15,231,3),(231,15,232,3),(232,15,233,2),(233,15,234,2),(234,15,235,3),(235,15,236,3),(236,15,237,2),(237,16,238,2),(238,16,239,2),(239,16,240,2),(240,16,241,2),(241,16,242,2),(242,16,243,10),(243,16,244,10),(244,16,245,10),(245,16,246,3),(246,16,247,3),(247,16,248,10),(248,16,249,2),(249,16,250,2),(250,17,251,2),(251,17,252,2),(252,17,253,2),(253,17,254,2),(254,17,255,2),(255,17,256,3),(256,17,257,2),(257,17,258,2),(258,17,259,2),(259,17,260,2),(260,17,261,2),(261,18,262,9),(262,18,263,4),(263,18,264,9),(264,18,265,9),(265,18,266,9),(266,18,267,9),(267,18,268,7),(268,18,269,2),(269,18,270,2),(270,18,271,2),(271,18,272,2),(272,18,273,11),(273,18,274,11),(274,19,275,2),(275,19,276,2),(276,19,277,2),(277,19,278,2),(278,19,279,2),(279,19,280,9),(280,19,281,9),(281,19,282,9),(282,19,283,2),(283,19,284,2),(284,19,285,2),(285,19,286,9),(286,20,287,2),(287,20,288,2),(288,20,289,2),(289,20,290,2),(290,20,291,2),(291,20,292,2),(292,20,293,7),(293,20,294,9),(294,20,295,4),(295,20,296,9),(296,20,297,9),(297,20,298,7),(298,20,299,7),(299,20,300,7),(300,20,301,2),(301,20,302,4),(302,20,303,4),(303,20,304,2),(304,20,305,11),(305,20,306,2),(306,21,307,9),(307,21,308,9),(308,21,309,9),(309,21,310,9),(310,21,311,9),(311,21,312,2),(312,21,313,2),(313,21,314,2),(314,21,315,2),(315,21,316,3),(316,21,317,9),(317,21,318,2),(318,21,319,4),(319,21,320,7),(320,21,321,7),(321,21,322,10),(322,21,323,9),(323,21,324,4),(324,22,325,4),(325,22,326,2),(326,22,327,9),(327,22,328,9),(328,22,329,2),(329,22,330,10),(330,22,331,9),(331,22,332,4),(332,22,333,4),(333,22,334,9),(334,22,335,9),(335,22,336,7),(336,22,337,10),(337,22,338,4),(338,22,339,4),(339,22,340,4),(340,22,341,9),(341,22,342,9),(342,22,343,9),(343,23,344,3),(344,23,345,3),(345,23,346,3),(346,23,347,10),(347,23,348,10),(348,23,349,3),(349,23,350,3),(350,23,351,3),(351,23,352,3),(352,23,353,3),(353,24,354,3),(354,24,355,3),(355,24,356,3),(356,24,357,9),(357,24,358,9),(358,24,359,9),(359,24,360,4),(360,24,361,2),(361,24,362,2),(362,24,363,2),(363,24,364,2),(364,24,365,2),(365,24,366,2),(366,24,367,2),(367,24,368,11),(368,24,369,11),(369,24,370,10),(370,24,371,10),(371,24,372,3),(372,24,373,2),(373,24,374,4),(374,24,375,4),(375,24,376,9),(376,24,377,2),(377,24,378,7),(378,24,379,2),(379,24,380,3),(380,24,381,3),(381,25,382,9),(382,25,383,4),(383,25,384,7),(384,25,385,4),(385,25,386,9),(386,25,387,4),(387,25,388,9),(388,25,389,2),(389,25,390,2),(390,25,391,9),(391,25,392,4),(392,25,393,4),(393,25,394,5),(394,25,395,3),(395,25,396,3),(396,25,397,2),(397,25,398,2),(398,25,399,7);
/*!40000 ALTER TABLE `detalle_categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario_llantas`
--

DROP TABLE IF EXISTS `inventario_llantas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario_llantas` (
  `inventario_id` int unsigned NOT NULL AUTO_INCREMENT,
  `identificador_llanta` varchar(105) NOT NULL,
  `marca` varchar(30) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `medida` varchar(20) NOT NULL,
  `indice_carga` char(3) NOT NULL,
  `indice_velocidad` char(2) NOT NULL,
  `stock` int unsigned NOT NULL,
  `precio_compra` decimal(10,2) unsigned NOT NULL,
  `precio_venta` decimal(10,2) unsigned NOT NULL,
  `dot` varchar(13) DEFAULT NULL,
  `codigo_barras` varchar(13) DEFAULT NULL,
  `observaciones` text,
  `imagen` varchar(1024) DEFAULT NULL,
  `active` enum('ACTIVE','INACTIVE','DELETE','SIN_STOCK') NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`inventario_id`),
  UNIQUE KEY `inventario_id_UNIQUE` (`inventario_id`),
  UNIQUE KEY `codigo_barras_UNIQUE` (`codigo_barras`),
  UNIQUE KEY `dot_UNIQUE` (`dot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario_llantas`
--

LOCK TABLES `inventario_llantas` WRITE;
/*!40000 ALTER TABLE `inventario_llantas` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventario_llantas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marcas`
--

DROP TABLE IF EXISTS `marcas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marcas` (
  `marca_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) NOT NULL,
  PRIMARY KEY (`marca_id`),
  UNIQUE KEY `marca_id_UNIQUE` (`marca_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marcas`
--

LOCK TABLES `marcas` WRITE;
/*!40000 ALTER TABLE `marcas` DISABLE KEYS */;
INSERT INTO `marcas` VALUES (1,'SIN MARCA'),(2,'AUDI'),(3,'BMW'),(4,'BUICK'),(5,'BYD'),(6,'CADILLAC'),(7,'CHEVROLET'),(8,'CHRYSLER'),(9,'DODGE'),(10,'FIAT'),(11,'FORD'),(12,'GMC'),(13,'HONDA'),(14,'HYUNDAI'),(15,'ISUZU'),(16,'JAC'),(17,'JEEP'),(18,'KIA'),(19,'LINCOLN'),(20,'MAZDA'),(21,'NISSAN'),(22,'PEUGEOT'),(23,'RAM'),(24,'TOYOTA'),(25,'VOLKSWAGEN');
/*!40000 ALTER TABLE `marcas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modelo`
--

DROP TABLE IF EXISTS `modelo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modelo` (
  `modelo_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `marca_id` int unsigned NOT NULL,
  PRIMARY KEY (`modelo_id`),
  UNIQUE KEY `modelo_id_UNIQUE` (`modelo_id`),
  KEY `fk_modelo_marca` (`marca_id`),
  CONSTRAINT `fk_modelo_marca` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`marca_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=400 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modelo`
--

LOCK TABLES `modelo` WRITE;
/*!40000 ALTER TABLE `modelo` DISABLE KEYS */;
INSERT INTO `modelo` VALUES (1,'SIN MODELO',1),(2,'A3',2),(3,'A4',2),(4,'A6',2),(5,'A8',2),(6,'Q3',2),(7,'Q5',2),(8,'Q7',2),(9,'Q8',2),(10,'SERIE 3',3),(11,'SERIE 5',3),(12,'SERIE 7',3),(13,'X1',3),(14,'X3',3),(15,'X5',3),(16,'X6',3),(17,'SERIE 2',3),(18,'SERIE 4',3),(19,'SERIE 8',3),(20,'ENCORE',4),(21,'ENCORE GX',4),(22,'ENVISION',4),(23,'ENCLAVE',4),(24,'VERANO',4),(25,'REGAL',4),(26,'LACROSSE',4),(27,'DOLPHIN',5),(28,'DOLPHIN MINI',5),(29,'SEAL',5),(30,'HAN EV',5),(31,'KING',5),(32,'SONG PLUS',5),(33,'SONG PRO',5),(34,'TANG EV',5),(35,'YUAN PLUS',5),(36,'YUAN PRO',5),(37,'SEALION 7',5),(38,'SHARK',5),(39,'XT4',6),(40,'XT5',6),(41,'XT6',6),(42,'ESCALADE',6),(43,'CT4',6),(44,'CT5',6),(45,'EL DORADO',6),(46,'AVEO',7),(47,'CAVALIER',7),(48,'MALIBU',7),(49,'ONIX',7),(50,'PRISMA',7),(51,'SPARK',7),(52,'BEAT',7),(53,'SONIC',7),(54,'CRUZE',7),(55,'TRACKER',7),(56,'TRAX',7),(57,'CAPTIVA',7),(58,'GROOVE',7),(59,'EQUINOX',7),(60,'BLAZER',7),(61,'TAHOE',7),(62,'SUBURBAN',7),(63,'TRAVERSE',7),(64,'S10',7),(65,'COLORADO',7),(66,'SILVERADO',7),(67,'MONTANA',7),(68,'TORNADO',7),(69,'CHEYENNE',7),(70,'CAMARO',7),(71,'CORVETTE',7),(72,'BOLT EV',7),(73,'BOLT EUV',7),(74,'ORLANDO',7),(75,'300',8),(76,'200',8),(77,'SEBRING',8),(78,'CONCORDE',8),(79,'LHS',8),(80,'CIRRUS',8),(81,'NEW YORKER',8),(82,'PACIFICA',8),(83,'VOYAGER',8),(84,'TOWN & COUNTRY',8),(85,'PT CRUISER',8),(86,'CROSSFIRE',8),(87,'ASPEN',8),(88,'AIRFLOW',8),(89,'YPSILON',8),(90,'LEBARON',8),(91,'IMPERIAL',8),(92,'FIFTH AVENUE',8),(93,'VALIANT',8),(94,'NEON',8),(95,'ATTITUDE',9),(96,'NEON',9),(97,'CHARGER',9),(98,'CHALLENGER',9),(99,'DART',9),(100,'AVENGER',9),(101,'STRATUS',9),(102,'INTREPID',9),(103,'MONACO',9),(104,'JOURNEY',9),(105,'DURANGO',9),(106,'HORNET',9),(107,'NITRO',9),(108,'CALIBER',9),(109,'GRAND CARAVAN',9),(110,'CARAVAN',9),(111,'RAM 1500',9),(112,'RAM 2500',9),(113,'RAM 3500',9),(114,'DAKOTA',9),(115,'VIPER',9),(116,'STEALTH',9),(117,'OMNI',9),(118,'SHADOW',9),(119,'SPIRIT',9),(120,'ARIES',9),(121,'DYNASTY',9),(122,'RAIDER',9),(123,'ASPEN',9),(124,'500',10),(125,'500L',10),(126,'500X',10),(127,'PANDA',10),(128,'PUNTO',10),(129,'UNO',10),(130,'ARGO',10),(131,'MOBI',10),(132,'TIPO',10),(133,'LINEA',10),(134,'SIENA',10),(135,'PALIO',10),(136,'STRADA',10),(137,'TORO',10),(138,'BRAVO',10),(139,'STILO',10),(140,'FIESTA',11),(141,'FOCUS',11),(142,'FUSION',11),(143,'TAURUS',11),(144,'MONDEO',11),(145,'ESCORT',11),(146,'IKON',11),(147,'ASPIRE',11),(148,'MUSTANG',11),(149,'GT',11),(150,'ECOSPORT',11),(151,'ESCAPE',11),(152,'EDGE',11),(153,'EXPLORER',11),(154,'EXPEDITION',11),(155,'BRONCO',11),(156,'BRONCO SPORT',11),(157,'TERRITORY',11),(158,'EVEREST',11),(159,'KUGA',11),(160,'PUMA',11),(161,'FLEX',11),(162,'EXCURSION',11),(163,'RANGER',11),(164,'F-150',11),(165,'F-250',11),(166,'F-350',11),(167,'F-450',11),(168,'MAVERICK',11),(169,'LOBO',11),(170,'COURIER',11),(171,'FREESTAR',11),(172,'GALAXY',11),(173,'C-MAX',11),(174,'B-MAX',11),(175,'TERRAIN',12),(176,'ACADIA',12),(177,'YUKON',12),(178,'YUKON XL',12),(179,'HUMMER EV SUV',12),(180,'SIERRA 1500',12),(181,'SIERRA 2500',12),(182,'SIERRA 3500',12),(183,'CANYON',12),(184,'SONOMA',12),(185,'SYCLONE',12),(186,'TYPHOON',12),(187,'JIMMY',12),(188,'ENVOY',12),(189,'ENVOY XL',12),(190,'ENVOY XUV',12),(191,'SPRINT',12),(192,'CIVIC',13),(193,'CITY',13),(194,'INSIGHT',13),(195,'FIT',13),(196,'JAZZ',13),(197,'BRIO',13),(198,'CR-V',13),(199,'HR-V',13),(200,'WR-V',13),(201,'PILOT',13),(202,'PASSPORT',13),(203,'ELEMENT',13),(204,'ZR-V',13),(205,'BR-V',13),(206,'ODYSSEY',13),(207,'RIDGELINE',13),(208,'NSX',13),(209,'PRELUDE',13),(210,'CR-Z',13),(211,'S2000',13),(212,'INTEGRA',13),(213,'ACCORD',13),(214,'ACCENT',14),(215,'ELANTRA',14),(216,'SONATA',14),(217,'GRAND I10',14),(218,'I10',14),(219,'HB20',14),(220,'CRETA',14),(221,'VENUE',14),(222,'TUCSON',14),(223,'SANTA FE',14),(224,'PALISADE',14),(225,'KONA',14),(226,'D-MAX',15),(227,'MU-X',15),(228,'RODEO',15),(229,'N-SERIES',15),(230,'TROOPER',15),(231,'HOMBRE',15),(232,'PANTHER',15),(233,'BIGHORN',15),(234,'AMIGO',15),(235,'GEMINI',15),(236,'WFR',15),(237,'MU-7',15),(238,'JAC S2',16),(239,'JAC S3',16),(240,'JAC S4',16),(241,'JAC S5',16),(242,'JAC S7',16),(243,'JAC J4',16),(244,'JAC J5',16),(245,'JAC J6',16),(246,'JAC T6',16),(247,'JAC T8',16),(248,'JAC SUNRAY',16),(249,'JAC SEI 2',16),(250,'JAC SEI 3',16),(251,'WRANGLER',17),(252,'CHEROKEE',17),(253,'GRAND CHEROKEE',17),(254,'COMPASS',17),(255,'RENEGADE',17),(256,'GLADIATOR',17),(257,'WAGONEER',17),(258,'GRAND WAGONEER',17),(259,'LIBERTY',17),(260,'COMMANDER',17),(261,'PATRIOT',17),(262,'RIO',18),(263,'PICANTO',18),(264,'FORTE',18),(265,'CERATO',18),(266,'OPTIMA',18),(267,'K5',18),(268,'SOUL',18),(269,'SPORTAGE',18),(270,'SORENTO',18),(271,'TELLURIDE',18),(272,'NIRO',18),(273,'CARENS',18),(274,'RONDO',18),(275,'NAVIGATOR',19),(276,'NAVIGATOR L',19),(277,'AVIATOR',19),(278,'CORSAIR',19),(279,'NAUTILUS',19),(280,'MKZ',19),(281,'CONTINENTAL',19),(282,'TOWN CAR',19),(283,'MKT',19),(284,'MKC',19),(285,'MKX',19),(286,'ZEPHYR',19),(287,'CX-3',20),(288,'CX-30',20),(289,'CX-5',20),(290,'CX-50',20),(291,'CX-7',20),(292,'CX-9',20),(293,'MX-5 MIATA',20),(294,'626',20),(295,'2',20),(296,'3',20),(297,'6',20),(298,'RX-7',20),(299,'RX-8',20),(300,'BT-50',20),(301,'TRIBUTE',20),(302,'323',20),(303,'121',20),(304,'CX-4',20),(305,'5',20),(306,'CX-8',20),(307,'TSURU',21),(308,'VERSA',21),(309,'SENTRA',21),(310,'ALTIMA',21),(311,'MARCH',21),(312,'KICKS',21),(313,'NP300',21),(314,'X-TRAIL',21),(315,'MURANO',21),(316,'FRONTIER',21),(317,'MAXIMA',21),(318,'JUKE',21),(319,'NOTE',21),(320,'370Z',21),(321,'GT-R',21),(322,'NV350',21),(323,'TIIDA',21),(324,'CUBE',21),(325,'208',22),(326,'2008',22),(327,'301',22),(328,'3008',22),(329,'5008',22),(330,'PARTNER',22),(331,'407',22),(332,'307',22),(333,'308',22),(334,'406',22),(335,'508',22),(336,'RCZ',22),(337,'EXPERT',22),(338,'107',22),(339,'206',22),(340,'207',22),(341,'306',22),(342,'605',22),(343,'BIPPER',22),(344,'1500',23),(345,'2500',23),(346,'3500',23),(347,'PROMASTER',23),(348,'PROMASTER CITY',23),(349,'700',23),(350,'REBEL',23),(351,'1500 CLASSIC',23),(352,'POWER WAGON',23),(353,'DAKOTA',23),(354,'HILUX',24),(355,'TACOMA',24),(356,'TUNDRA',24),(357,'COROLLA',24),(358,'YARIS SEDAN',24),(359,'CAMRY',24),(360,'PRIUS',24),(361,'RAV4',24),(362,'HIGHLANDER',24),(363,'4RUNNER',24),(364,'LAND CRUISER',24),(365,'FORTUNER',24),(366,'C-HR',24),(367,'SEQUOIA',24),(368,'AVANZA',24),(369,'SIENNA',24),(370,'HIACE',24),(371,'PROACE',24),(372,'TACOMA TRD',24),(373,'COROLLA CROSS',24),(374,'PRIUS C',24),(375,'PRIUS PRIME',24),(376,'YARIS IA',24),(377,'LAND CRUISER PRADO',24),(378,'86 / GT86',24),(379,'FJ CRUISER',24),(380,'HILUX REVO',24),(381,'TACOMA LIMITED',24),(382,'JETTA',25),(383,'GOLF',25),(384,'GOLF GTI',25),(385,'GOLF VARIANT',25),(386,'VENTO',25),(387,'POLO',25),(388,'VIRTUS',25),(389,'TIGUAN',25),(390,'TIGUAN ALLSPACE',25),(391,'PASSAT',25),(392,'BEETLE',25),(393,'ESCARABAJO',25),(394,'BEETLE',25),(395,'SAVEIRO',25),(396,'AMAROK',25),(397,'ATLAS',25),(398,'TERAMONT',25),(399,'VENTO GLI',25);
/*!40000 ALTER TABLE `modelo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nota`
--

DROP TABLE IF EXISTS `nota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nota` (
  `nota_id` int unsigned NOT NULL AUTO_INCREMENT,
  `cliente_id` int unsigned NOT NULL,
  `vehiculo_id` int unsigned NOT NULL,
  `inventario_id` int unsigned DEFAULT NULL,
  `num_nota` varchar(8) NOT NULL,
  `num_factura` varchar(32) DEFAULT NULL,
  `total` decimal(11,2) NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` enum('ACTIVE','INACTIVE','DELETE') NOT NULL DEFAULT 'ACTIVE',
  `fecha_vencimiento` date DEFAULT NULL,
  `adeudo` decimal(11,2) unsigned DEFAULT NULL,
  `saldo_favor` decimal(11,2) unsigned DEFAULT NULL,
  `status_nota` enum('PAGADO','POR_PAGAR','VENCIDO','A_FAVOR') NOT NULL,
  PRIMARY KEY (`nota_id`),
  UNIQUE KEY `nota_id_UNIQUE` (`nota_id`),
  UNIQUE KEY `num_nota` (`num_nota`),
  UNIQUE KEY `num_factura` (`num_factura`),
  KEY `cliente_id` (`cliente_id`),
  KEY `vehiculo_id` (`vehiculo_id`),
  KEY `fk_nota_inventario` (`inventario_id`),
  CONSTRAINT `fk_nota_inventario` FOREIGN KEY (`inventario_id`) REFERENCES `inventario_llantas` (`inventario_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `nota_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`cliente_id`),
  CONSTRAINT `nota_ibfk_2` FOREIGN KEY (`vehiculo_id`) REFERENCES `vehiculo` (`vehiculo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nota`
--

LOCK TABLES `nota` WRITE;
/*!40000 ALTER TABLE `nota` DISABLE KEYS */;
/*!40000 ALTER TABLE `nota` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nota_cliente_detalles`
--

DROP TABLE IF EXISTS `nota_cliente_detalles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nota_cliente_detalles` (
  `nota_cliente_det_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nota_id` int unsigned NOT NULL,
  `nombre_cliente` varchar(150) NOT NULL,
  `direccion1` varchar(100) DEFAULT NULL,
  `direccion2` varchar(100) DEFAULT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `correo` varchar(320) DEFAULT NULL,
  `marca` varchar(40) NOT NULL,
  `modelo` varchar(30) NOT NULL,
  `categoria` varchar(40) NOT NULL,
  `anio` int unsigned NOT NULL,
  `kilometros` int unsigned DEFAULT NULL,
  `placas` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`nota_cliente_det_id`),
  UNIQUE KEY `nota_cliente_det_id` (`nota_cliente_det_id`),
  KEY `fk_nota_cliente_detalles` (`nota_id`),
  CONSTRAINT `fk_nota_cliente_detalles` FOREIGN KEY (`nota_id`) REFERENCES `nota` (`nota_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nota_cliente_detalles`
--

LOCK TABLES `nota_cliente_detalles` WRITE;
/*!40000 ALTER TABLE `nota_cliente_detalles` DISABLE KEYS */;
/*!40000 ALTER TABLE `nota_cliente_detalles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nota_detalle`
--

DROP TABLE IF EXISTS `nota_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nota_detalle` (
  `nota_detalle_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nota_id` int unsigned NOT NULL,
  `observaciones` varchar(120) DEFAULT NULL,
  `observaciones2` varchar(120) DEFAULT NULL,
  `porcentaje_gas` int unsigned DEFAULT NULL,
  `rayones` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `golpes` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `tapones` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `tapetes` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `radio` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `gato` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `llave` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `llanta` enum('SI','NO','DESELECCIONADO') DEFAULT NULL,
  `alineacion` varchar(56) DEFAULT NULL,
  `alineacion_cantidad` int unsigned DEFAULT NULL,
  `alineacion_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `alineacion_total` decimal(11,2) unsigned DEFAULT NULL,
  `balanceo` varchar(56) DEFAULT NULL,
  `balanceo_cantidad` int unsigned DEFAULT NULL,
  `balanceo_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `balanceo_total` int unsigned DEFAULT NULL,
  `amor_delanteros` varchar(90) DEFAULT NULL,
  `amor_del_cantidad` int unsigned DEFAULT NULL,
  `amor_del_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `amor_del_total` decimal(11,2) unsigned DEFAULT NULL,
  `amor_traseros` varchar(90) DEFAULT NULL,
  `amor_tras_cantidad` int unsigned DEFAULT NULL,
  `amor_tras_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `amor_tras_total` decimal(11,2) unsigned DEFAULT NULL,
  `suspension` varchar(90) DEFAULT NULL,
  `suspension2` varchar(90) DEFAULT NULL,
  `suspension_cantidad2` int unsigned DEFAULT NULL,
  `suspension_unitario2` decimal(11,2) unsigned DEFAULT NULL,
  `suspension_total2` decimal(11,2) unsigned DEFAULT NULL,
  `suspension_cantidad` int unsigned DEFAULT NULL,
  `suspension_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `suspension_total` decimal(11,2) unsigned DEFAULT NULL,
  `mecanica` varchar(90) DEFAULT NULL,
  `mecanica_cantidad2` int unsigned DEFAULT NULL,
  `mecanica_unitario2` decimal(11,2) unsigned DEFAULT NULL,
  `mecanica_total2` decimal(11,2) unsigned DEFAULT NULL,
  `mecanica_cantidad` int unsigned DEFAULT NULL,
  `mecanica_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `mecanica_total` decimal(11,2) unsigned DEFAULT NULL,
  `mecanica2` varchar(90) DEFAULT NULL,
  `frenos` varchar(90) DEFAULT NULL,
  `frenos_cantidad2` int unsigned DEFAULT NULL,
  `frenos_unitario2` decimal(11,2) unsigned DEFAULT NULL,
  `frenos_total2` decimal(11,2) unsigned DEFAULT NULL,
  `frenos_cantidad` int unsigned DEFAULT NULL,
  `frenos_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `frenos_total` decimal(11,2) unsigned DEFAULT NULL,
  `frenos2` varchar(90) DEFAULT NULL,
  `otros` varchar(90) DEFAULT NULL,
  `otros_cantidad2` decimal(11,2) unsigned DEFAULT NULL,
  `otros_unitario2` decimal(11,2) unsigned DEFAULT NULL,
  `otros_total2` decimal(11,2) unsigned DEFAULT NULL,
  `otros_cantidad` int unsigned DEFAULT NULL,
  `otros_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `otros_total` decimal(11,2) unsigned DEFAULT NULL,
  `otros2` varchar(90) DEFAULT NULL,
  `subtotal_mecanica` decimal(11,2) unsigned DEFAULT NULL,
  `subtotal_frenos` decimal(11,2) unsigned DEFAULT NULL,
  `subtotal_otros` decimal(11,2) unsigned DEFAULT NULL,
  `llanta_campo` varchar(90) DEFAULT NULL,
  `llanta_cantidad` int unsigned DEFAULT NULL,
  `llanta_unitario` decimal(11,2) unsigned DEFAULT NULL,
  `llanta_total` decimal(11,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`nota_detalle_id`),
  UNIQUE KEY `nota_detalle_id_UNIQUE` (`nota_detalle_id`),
  KEY `nota_id` (`nota_id`),
  CONSTRAINT `nota_detalle_ibfk_1` FOREIGN KEY (`nota_id`) REFERENCES `nota` (`nota_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nota_detalle`
--

LOCK TABLES `nota_detalle` WRITE;
/*!40000 ALTER TABLE `nota_detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `nota_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promociones`
--

DROP TABLE IF EXISTS `promociones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promociones` (
  `promocion_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre_promocion` varchar(50) NOT NULL,
  `descripcion` text NOT NULL,
  `tipo_descuento` enum('PORCENTAJE','OTRO') NOT NULL,
  `porcentaje` int DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `active` enum('ACTIVE','INACTIVE','DELETE') NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `img` varchar(1024) DEFAULT NULL,
  `tipo_promocion` enum('VEHICULO','CLIENTE') NOT NULL,
  PRIMARY KEY (`promocion_id`),
  UNIQUE KEY `promocion_id_UNIQUE` (`promocion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promociones`
--

LOCK TABLES `promociones` WRITE;
/*!40000 ALTER TABLE `promociones` DISABLE KEYS */;
/*!40000 ALTER TABLE `promociones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `respaldo`
--

DROP TABLE IF EXISTS `respaldo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `respaldo` (
  `respaldo_id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre_archivo` varchar(100) NOT NULL,
  `ruta_archivo` varchar(300) NOT NULL,
  `tamanio_bytes` bigint unsigned DEFAULT NULL,
  `estado` enum('COMPLETO','FALLIDO','EN_PROCESO') NOT NULL DEFAULT 'EN_PROCESO',
  `codigo_error` text,
  `tipo_respaldo` enum('LOCAL','CLOUD') NOT NULL DEFAULT 'LOCAL',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`respaldo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `respaldo`
--

LOCK TABLES `respaldo` WRITE;
/*!40000 ALTER TABLE `respaldo` DISABLE KEYS */;
/*!40000 ALTER TABLE `respaldo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `usuario_id` int unsigned NOT NULL AUTO_INCREMENT,
  `auth_id` varchar(50) DEFAULT NULL,
  `username` varchar(40) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') DEFAULT NULL,
  `nombre` varchar(40) NOT NULL,
  `apellidos` varchar(40) NOT NULL,
  `correo` varchar(320) NOT NULL,
  `foto_perfil` varchar(1024) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` enum('ACTIVE','INACTIVE','DELETE') NOT NULL DEFAULT 'ACTIVE',
  `status_licencia` enum('LIFETIME','SUSPENDED','ACTIVE') NOT NULL DEFAULT 'ACTIVE',
  `next_check` datetime DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `usuario_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo` (
  `vehiculo_id` int unsigned NOT NULL AUTO_INCREMENT,
  `cliente_id` int unsigned NOT NULL,
  `marca_id` int unsigned NOT NULL,
  `modelo_id` int unsigned NOT NULL,
  `categoria_id` int unsigned NOT NULL,
  `anio` int unsigned NOT NULL,
  `kilometros` int unsigned DEFAULT NULL,
  `color` varchar(20) DEFAULT NULL,
  `placas` varchar(15) DEFAULT NULL,
  `numero_serie` varchar(17) DEFAULT NULL,
  `observaciones` text,
  `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ultimo_servicio` date DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` enum('ACTIVE','INACTIVE','DELETE') NOT NULL DEFAULT 'ACTIVE',
  `contador_mensaje` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`vehiculo_id`),
  UNIQUE KEY `vehiculo_id_UNIQUE` (`vehiculo_id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `marca_id` (`marca_id`),
  KEY `modelo_id` (`modelo_id`),
  KEY `categoria_id` (`categoria_id`),
  CONSTRAINT `vehiculo_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`cliente_id`),
  CONSTRAINT `vehiculo_ibfk_2` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`marca_id`),
  CONSTRAINT `vehiculo_ibfk_3` FOREIGN KEY (`modelo_id`) REFERENCES `modelo` (`modelo_id`),
  CONSTRAINT `vehiculo_ibfk_4` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculo`
--

LOCK TABLES `vehiculo` WRITE;
/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;
/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculo_promocion`
--

DROP TABLE IF EXISTS `vehiculo_promocion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo_promocion` (
  `vehiculo_promocion_id` int unsigned NOT NULL AUTO_INCREMENT,
  `marca_id` int unsigned NOT NULL,
  `modelo_id` int unsigned NOT NULL,
  `anio` int unsigned NOT NULL,
  `promocion_id` int unsigned NOT NULL,
  PRIMARY KEY (`vehiculo_promocion_id`),
  UNIQUE KEY `vehiculo_promocion_id_UNIQUE` (`vehiculo_promocion_id`),
  KEY `marca_id` (`marca_id`),
  KEY `modelo_id` (`modelo_id`),
  KEY `promocion_id` (`promocion_id`),
  CONSTRAINT `fk_vehiculo_promocion_marcas` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`marca_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_vehiculo_promocion_modelos` FOREIGN KEY (`modelo_id`) REFERENCES `modelo` (`modelo_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_vehiculo_promocion_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`promocion_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculo_promocion`
--

LOCK TABLES `vehiculo_promocion` WRITE;
/*!40000 ALTER TABLE `vehiculo_promocion` DISABLE KEYS */;
/*!40000 ALTER TABLE `vehiculo_promocion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-21 12:52:51
