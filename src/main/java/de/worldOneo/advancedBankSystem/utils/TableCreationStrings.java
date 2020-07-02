package de.worldOneo.advancedBankSystem.utils;

public enum TableCreationStrings {
    TRANSACTIONS("CREATE TABLE IF NOT EXISTS `transactions` (" +
            "`id` TINYTEXT NOT NULL DEFAULT uuid()," +
            "`IDTo` TINYTEXT NOT NULL," +
            "`IDFrom` TINYTEXT NOT NULL," +
            "`Value` BIGINT(20) NOT NULL DEFAULT 0," +
            "`Time` BIGINT(20) NOT NULL DEFAULT 0," +
            "`Reason` TINYTEXT NOT NULL default ''," +
            "UNIQUE INDEX `id` (`id`) USING HASH" +
            ");", "transactions"),
    ACCOUNTS("CREATE TABLE IF NOT EXISTS `accounts`(" +
            "`id` TINYTEXT NOT NULL DEFAULT uuid()," +
            "`IDOwner` TINYTEXT NOT NULL," +
            "`Value` BIGINT(20) NOT NULL DEFAULT 0," +
            "`Name` TINYTEXT NOT NULL default ''," +
            "UNIQUE INDEX `id` (`id`) USING HASH" +
            ");", "accounts"),
    CUSTOMER("CREATE TABLE IF NOT EXISTS `customer`(" +
            "`id` TINYTEXT NOT NULL DEFAULT uuid()," +
            "`UUID` TINYTEXT NOT NULL DEFAULT uuid()," +
            "UNIQUE INDEX `id` (`id`) USING HASH," +
            "UNIQUE INDEX `UUID` (`UUID`) USING HASH" +
            ");", "customer");

    private final String SQLString;
    private final String tableName;

    TableCreationStrings(String SQLString, String tableName) {
        this.SQLString = SQLString;
        this.tableName = tableName;
    }

    public String getSQLString() {
        return SQLString;
    }

    public String getTableName() {
        return tableName;
    }
}
