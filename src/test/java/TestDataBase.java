import lib.*;
import Entities.KhachHang;
import Entities.XeMay;
import Model.*;
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
    
    public  void inChuoi(KhachHang kh){
        String q = "INSERT INTO KhachHang (ten,cmnd,sdt,gioitinh,sinhnhat) " +
                    "VALUE (N'"+kh.getHoTen()+"', '"+kh.getCMND()+"', '"+kh.getSDT()+"', '"+kh.getGioiTinh()+"', '"+"'"+")";
        System.out.println(q);
    }
    
    public static void testTimkiemkahchhang() throws SQLException{
        DonBaoDuongModel b =new DonBaoDuongModel();
        KhachHang kh =b.timKhachHangTheoTenVaSDT("minh", "02342342");
        System.out.println(kh.getCMND());
        System.out.println(kh.getGioiTinh());
        System.out.println(kh.getHoTen());
        System.out.println(kh.getID());
        System.out.println(kh.getSDT());
    }    
    public static void main(String[] args) throws SQLException {
        System.out.println(Stringlib.isLikeString("min minh       s  aaa", "Binhf minh moở sáng là số min"));
    }
}
