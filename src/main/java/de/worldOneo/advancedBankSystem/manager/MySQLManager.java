package de.worldOneo.advancedBankSystem.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.worldOneo.advancedBankSystem.BankSystem;
import de.worldOneo.advancedBankSystem.utils.TableCreationStrings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MySQLManager {
    private static boolean enabled;
    private static HikariDataSource hikariDataSource;
    private static String dbName;
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final MySQLManager instance = new MySQLManager();

    private MySQLManager() {
        enabled = true;
        HikariConfig hikariConfig = new HikariConfig();

        Configuration config = BankSystem.getInstance().getConfig();
        dbName = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        String url = config.getString("mysql.url");
        String formatedConnectionURL = String.format("jdbc:mysql://%s/%s", url, dbName);

        try {

            BankSystem.getInstance().getLogger().info("Versuche eine verbindung zu " + formatedConnectionURL + " aufzubauen.");

            hikariConfig.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setJdbcUrl(formatedConnectionURL);
            hikariConfig.setMaximumPoolSize(5);
            hikariDataSource = new HikariDataSource(hikariConfig);
            connect();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "\nNo Connection to the database possible!\n Please configure your connection correct in the config!");
            enabled = false;
        }
    }

    private void connect() {
        try (Connection connection = hikariDataSource.getConnection()) {
            sellectDb(connection);
            Statement createTable = connection.createStatement();
            for (TableCreationStrings value : TableCreationStrings.values()) {
                createTable.executeUpdate(value.getSQLString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            enabled = false;
        }
    }

    public Future<Boolean> executeUpdate(String update) {
        Callable<Boolean> updateTask = () -> {
            try (Connection connection = hikariDataSource.getConnection()) {
                sellectDb(connection);
                Statement updateStatement = connection.createStatement();
                updateStatement.executeUpdate(update);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        };
        return executorService.submit(updateTask);
    }

    public boolean executeUpdateSync(String update) {
        try (Connection connection = hikariDataSource.getConnection()) {
            sellectDb(connection);
            Statement updateStatement = connection.createStatement();
            updateStatement.executeUpdate(update);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try (Connection connection = hikariDataSource.getConnection()) {
            if (connection == null) {
                return null;
            }
            sellectDb(connection);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private static void sellectDb(Connection connection) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        statement.executeUpdate("USE " + dbName + ";");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static MySQLManager getInstance() {
        return instance;
    }
}
