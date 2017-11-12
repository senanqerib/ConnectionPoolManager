/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionpoolmanager;

/**
 *
 * @author Acer
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionPoolManager {

     public static void main(String args[]) throws SQLException, IOException
     {
         //ConnectionPoolManager CPM = new ConnectionPoolManager();
        Connection conn = ConnectionPoolManager.getConnection();
        String my_query = "SELECT NOW() FROM DUAL"; 
        PreparedStatement stmp = conn.prepareStatement(my_query);
        ResultSet rs = stmp.executeQuery();
        while (rs.next())
        {
         System.out.println("date: "+ rs.getString(1));
        }
     }
    
    private static final ConcurrentHashMap<String, BasicDataSource> dataSources = new ConcurrentHashMap();

    private ConnectionPoolManager() {
        //
    }

    public static Connection getConnection() throws SQLException, IOException {

            Properties    props = new Properties();
      
    
    props.load(new FileInputStream("conf" + File.separator + "config.properties"));
    
         String driver = props.getProperty("driver").trim();
         String url = props.getProperty("url").trim();
         String username = props.getProperty("username").trim();
         String password = props.getProperty("password").trim();
        
        BasicDataSource dataSource;

        if (dataSources.containsKey(url)) {
            dataSource = dataSources.get(url);
        } else {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSources.put(url, dataSource);
        }

        return dataSource.getConnection();

    }

}