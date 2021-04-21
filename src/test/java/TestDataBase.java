
import Entities.XeMay;
import Model.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hesac
 */
public class TestDataBase {
    public static void timXeMayTheoBienSo(String bienSo) throws SQLException{
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        
        String q = "select * from XeMay join LoaiXe on XeMay.idLoaiXe = LoaiXe.id  where XeMay.bienso = '" + bienSo +"'";
        ResultSet rs = st.executeQuery(q);
        
        if(rs.next()){
            System.out.println(rs.getString("bienso")+" | "+ rs.getString("tenxe")+" | "+ rs.getInt("idChuSoHuu")+" | "+ rs.getString("tenloai"));
        }else{
            System.err.println("loiz");
        }
    }
    
    public static void main(String[] args) throws SQLException {
        timXeMayTheoBienSo("77H23123");
    }
}
