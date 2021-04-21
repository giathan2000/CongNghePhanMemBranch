/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;
import java.util.ArrayList;
import Entities.*;

/**
 *
 * @author hesac
 */
public class DonBaoDuongModel {

    public ArrayList<PhuTung> layDanhSachPhuTungCanKiemTra() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<PhuTung> arl = new ArrayList<>();
        String q = "select * from PhuTungCanKiemTra";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
            PhuTung pt = new PhuTung(rs.getInt("id"), rs.getString("TenPhuTung"));
            arl.add(pt);
        }

        con.close();

        return arl;
    }

    public ArrayList<DichVuBaoDuong> layDanhSachDichVuBaoDuong() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<DichVuBaoDuong> arl = new ArrayList<>();
        String q = "select * from DichVuBaoDuong join LoaiXe on DichVuBaoDuong.idLoaiXe = LoaiXe.id";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
            DichVuBaoDuong pt = new DichVuBaoDuong(rs.getInt(1), rs.getString("ten"), rs.getLong("phi"), rs.getString("tenloai"));
            arl.add(pt);
        }

        con.close();
        return arl;
    }

    public ArrayList<LoaiXe> layDanhSachLoaiXe() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<LoaiXe> arl = new ArrayList<>();
        String q = "select * from LoaiXe";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
            LoaiXe pt = new LoaiXe(rs.getInt("id"), rs.getString("tenloai"));
            arl.add(pt);
        }

        con.close();
        return arl;
    }

    public ArrayList<LinhKien> layDanhSachLinhKien() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<LinhKien> arl = new ArrayList<>();
        String q = "select * from LinhKien join NhaCungCap on LinhKien.nhacungcap = NhaCungCap.id";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
            LinhKien pt = new LinhKien(rs.getInt(1), rs.getString("tenlinhkien"), rs.getInt("soluong"), rs.getLong("gia"), rs.getString("tenNhaCungCap"), rs.getString("ngaynhap"));
            arl.add(pt);
        }

        con.close();
        return arl;
    }

    public XeMay timXeMayTheoBienSo(String bienSo) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        String q = "select * from XeMay join LoaiXe on XeMay.idLoaiXe = LoaiXe.id  where XeMay.bienso = '" + bienSo + "'";
        ResultSet rs = st.executeQuery(q);

        if (rs.next()) {
            return new XeMay(rs.getString("bienso"), rs.getString("tenxe"), rs.getInt("idChuSoHuu"), rs.getString("tenloai"));
        } else {
            return null;
        }
    }

    public KhachHang timKhachHangTheoID(int ID) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from KhachHang where KhachHang.id = '" + Integer.toString(ID) + "'";
        ResultSet rs = st.executeQuery(q);

        if (rs.next()) {
            return new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        } else {
            return null;
        }
    }

    public KhachHang timKhachHangTheoTenVaSDT(String hoTen, String SDT) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from KhachHang where KhachHang.ten = N'" + hoTen + "' AND " + "KhachHang.sdt = '" + SDT + "'";
        System.out.println(q);
        ResultSet rs = st.executeQuery(q);

        if (rs.next()) {
            return new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        } else {
            return null;
        }
    }

    public boolean themKhachHangMoi(KhachHang kh) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO KhachHang (ten,cmnd,sdt,gioitinh) "
                + "VALUES (N'" + kh.getHoTen() + "', '" + kh.getCMND() + "', '" + kh.getSDT() + "', '" + kh.getGioiTinh() + "'" + ")";
        return st.execute(q);
    }

    public short layIdLoaiXe(String tenLoai) throws SQLException {
        ArrayList<LoaiXe> ar = layDanhSachLoaiXe();
        for (LoaiXe l : ar) {
            if (l.getTenLoai().equalsIgnoreCase(tenLoai.trim())) {
                return (short) l.getId();
            }
        }
        return -1;
    }

    public void themXeMayMoi(XeMay xm) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO XeMay (bienso,idChuSoHuu,idLoaiXe,tenxe) "
                + "VALUES (N'" + xm.getBienSo() + "', '" + xm.getIdKhach() + "', '" + layIdLoaiXe(xm.getLoaiXe()) + "', '" + xm.getTenXe() + "'" + ")";
        st.execute(q);
    }

    public void capNhatthongTinXeMay(XeMay xm) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "UPDATE XeMay"
                + "SET bienso = '"+xm.getBienSo()+"', idChuSoHuu = "+xm.getIdKhach()+", idLoaiXe = "+layIdLoaiXe(xm.getLoaiXe())+", tenxe = "+xm.getTenXe()+""
                + "WHERE bienso = "+ xm.getBienSo();
        st.execute(q);
    }
}
