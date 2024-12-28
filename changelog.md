API changes documented on the [wiki](https://github.com/QuickScythe/QUIPT/wiki/SQL)
# Using SQL
QUIPT provides a simple API for interacting with SQL databases. The API is designed to be easy to use and flexible.

## Connecting to a Database
To connect to a database, you can use the `SqlDatabase` class. The `SqlDatabase` class provides methods for connecting to a database and executing queries. Below is an example of how to connect to a database.

```java
public void connectToDatabase() {
    SqlDatabase database = new SqlDatabase(SqlUtils.SQLDriver.MYSQL, "example.com", 3306, "database", "username", "password");
    SqlUtils.createDatabase("example", database);
}
```

## Executing Queries
To execute queries on the database, you can use the `Database` class. The `Database` class provides methods for executing queries, updating data, and retrieving data. Below is an example of how to execute a query.

```java
public void executeQuery() {
    SqlDatabase database = SqlUtils.getDatabase("example");
    database.query("SELECT * FROM table", resultSet -> {
        try {
            while (resultSet.next()) {
                String column1 = resultSet.getString("column1");
                int column2 = resultSet.getInt("column2");
                System.out.println(column1 + " " + column2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}
```

