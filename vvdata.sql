

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `vvdata`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `groups`
--

CREATE TABLE `groups` (
  `id` int(255) NOT NULL,
  `groupname` varchar(255) NOT NULL,
  `groupleaderid` int(11) DEFAULT NULL,
  `groupmembercount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `groups`
--

INSERT INTO `groups` (`id`, `groupname`, `groupleaderid`, `groupmembercount`) VALUES
(1, 'testgroup1', 1, 1),
(2, 'testgroup2', 1, 1),
(3, 'test34534', 1, 1),
(4, 'test32452', 1, 1),
(5, 'test32452', 1, 1),
(6, 'test32452', 1, 1);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `groupuser`
--

CREATE TABLE `groupuser` (
  `userid` int(11) DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `groupuser`
--

INSERT INTO `groupuser` (`userid`, `groupid`) VALUES
(5, 1),
(5, 4),
(1, 2),
(1, 1);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `project`
--

CREATE TABLE `project` (
  `id` int(11) NOT NULL,
  `projectname` varchar(255) NOT NULL,
  `projecttimecost` int(255) NOT NULL,
  `projectprice` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `roles`
--

INSERT INTO `roles` (`id`, `username`, `role`) VALUES
(1, 'test3', '3'),
(2, 'test', '5');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `skills`
--

CREATE TABLE `skills` (
  `id` int(111) NOT NULL,
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `skill_name` varchar(255) NOT NULL,
  `skill_level` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `skills`
--

INSERT INTO `skills` (`id`, `user_id`, `username`, `skill_name`, `skill_level`) VALUES
(1, 0, 'John Doe', 'test222', 0),
(10, 2, 'test35', 'test666 (Level: 6)', 1),
(15, 2, 'test37', 'test6', 1),
(16, 3, 'test36', 'test', 1),
(34, 1, 'test', 'testskill666', 2),
(35, 1, 'test', 'test66', 1),
(36, 1, 'test', 'test32', 1),
(37, 1, 'test', 'testskill666', 1),
(38, 1, 'test', 'test66', 1),
(39, 1, 'test', 'test32', 1),
(40, 5, 'test3', 'test', NULL);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `role` int(2) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `status`, `role`, `cost`) VALUES
(1, 'test', 'test', '1', 3, 3500),
(4, 'test8567', 'test', '1', 1, 2000),
(5, 'test3', 'test', 'Creator', 1, 2000),
(6, 'test6', 'test6', '1', 1, 2000);

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `groupuser`
--
ALTER TABLE `groupuser`
  ADD KEY `userid` (`userid`),
  ADD KEY `groupid` (`groupid`);

--
-- A tábla indexei `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT a táblához `project`
--
ALTER TABLE `project`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT a táblához `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT a táblához `skills`
--
ALTER TABLE `skills`
  MODIFY `id` int(111) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT a táblához `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `groupuser`
--
ALTER TABLE `groupuser`
  ADD CONSTRAINT `groupuser_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `groupuser_ibfk_2` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
