-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: ridehailing
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `indent`
--

DROP TABLE IF EXISTS `indent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `indent` (
  `id` bigint NOT NULL,
  `request_id` bigint NOT NULL,
  `driver_id` bigint NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `end_x` double DEFAULT NULL,
  `end_y` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `price` double DEFAULT '0',
  `priority` int NOT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  `version` int NOT NULL DEFAULT '1',
  `receive_time` timestamp NOT NULL,
  `number` varchar(45) DEFAULT NULL,
  `driver_deleted` int NOT NULL DEFAULT '0',
  `customer_deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `order_request_idx` (`request_id`),
  KEY `order_driver_idx` (`driver_id`),
  CONSTRAINT `order_driver` FOREIGN KEY (`driver_id`) REFERENCES `user` (`id`),
  CONSTRAINT `order_request` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indent`
--

LOCK TABLES `indent` WRITE;
/*!40000 ALTER TABLE `indent` DISABLE KEYS */;
INSERT INTO `indent` VALUES (1698874426060496898,1698874365519912962,1688734750117007361,'2023-09-05 01:44:49','2023-09-05 01:45:08',117.140527,36.667309,8.987303493438233,46.35429257637764,0,0,1,'2023-09-05 01:43:21','鲁H·UZ355',0,0),(1698874531673071617,1698874490984128514,1688734750117007361,'2023-09-05 01:45:25','2023-09-05 01:45:26',117.140527,36.667309,8.987303493438233,46.35429257637764,0,0,1,'2023-09-05 01:43:47','鲁H·UZ355',0,0);
/*!40000 ALTER TABLE `indent` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-05 15:39:03
