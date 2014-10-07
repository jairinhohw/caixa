/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package caixa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jairinho
 */
public class DB {
    
    private Connection con = null;
    private final String host = "192.168.1.50";
    private final String banco = "caixa";
    private final String user = "jai";
    private final String senha = "jhw1414jhw";
    
    public Connection getConnection() throws SQLException{
        
        con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+banco,user,senha);
        
        return con;
    }
    
    public ResultSet readQuery(String query) throws SQLException{
        
        PreparedStatement stt = getConnection().prepareStatement(query);
        stt.execute();
        ResultSet res = stt.executeQuery();
        return res;
        
    }
    
    public boolean writeQuery(String query) throws SQLException{
        
        PreparedStatement stt = getConnection().prepareStatement(query);
        boolean res = stt.execute();
        return res;
        
    }
    
}
