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

    public ArrayList<DichVuBaoDuong> layDanhSachDichVuBaoDuongMoiNhat() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<DichVuBaoDuong> arl = new ArrayList<>();
        String q = "with temporaryTable(id, NgayCapNhat) as "
                + "(SELECT id,  max(NgayCapNhat)"
                + " from DichVuBaoDuong"
                + " group by id)"
                + " select DichVuBaoDuong.* from DichVuBaoDuong ,temporaryTable"
                + " where DichVuBaoDuong.id = temporaryTable.id and DichVuBaoDuong.NgayCapNhat = temporaryTable.NgayCapNhat";
        ResultSet rs = st.executeQuery(q);
        ArrayList<LoaiXe> dsLoaiXe = layDanhSachLoaiXe();
        while (rs.next()) {
            String str = null;
            if (rs.getString(5) == null || rs.getString(5).toString().equalsIgnoreCase("0")) {
                str = "Sử dụng";
            } else {
                str = "Không sử dụng";
            }
            String t = "Xe Máy";
            for (LoaiXe loaiXe : dsLoaiXe) {
                if (rs.getInt("idLoaiXe") == loaiXe.getId()) {
                    t = loaiXe.getTenLoai();
                }
            }
            DichVuBaoDuong pt = new DichVuBaoDuong(
                    rs.getInt("id"),
                    rs.getString("Ten"),
                    rs.getLong("Phi"),
                    t,
                    str,
                    rs.getString("NgayCapNhat"));
            //System.out.println("Model.DonBaoDuongModel.layDanhSachDichVuBaoDuongMoiNhat()");
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
            String str = null;
            if (rs.getString(3) == null || rs.getString(5).toString().equalsIgnoreCase("0")) {
                str = "Sử dụng";
            } else {
                str = "Không sử dụng";
            }
            LoaiXe pt = new LoaiXe(rs.getInt("id"), rs.getString("tenloai"), str);
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
            return new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
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
            return new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
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

    public void themXeMayMoi(String bienSo, String loaiXe) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO XeMay (bienso,idLoaiXe) "
                + "VALUES ('" + bienSo + "', " + layIdLoaiXe(loaiXe) + "" + ")";
        st.execute(q);
    }

    public void capNhatthongTinXeMay(XeMay xm) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "UPDATE XeMay"
                + "SET bienso = '" + xm.getBienSo() + "', idChuSoHuu = " + xm.getIdKhach() + ", idLoaiXe = " + layIdLoaiXe(xm.getLoaiXe()) + ", tenxe = " + xm.getTenXe() + ""
                + "WHERE bienso = " + xm.getBienSo();
        st.execute(q);
    }

    public ArrayList<NhanVien> layDanhSachNhanVien() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<NhanVien> arl = new ArrayList<>();
        String q = "select * from NhanVien ";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
            NhanVien pt = new NhanVien(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            arl.add(pt);
        }
        con.close();
        return arl;
    }

//    private int getSCOPEIDENTITY(){
//        Connection con = DatabaseConnection.getConnection();
//        Statement st = con.createStatement();
//        String q = "";
//        st.execute(q);
//    }
    public DonBaoDuong themDonBaoDuong(DonBaoDuong dbd) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO DonBaoDuong (bienso,NgayBatDau,NgayHoanThanh,trangthai,tongtien,idNhanVienLapDon) "
                + "VALUES ('" + dbd.getBienSo() + "', '" + dbd.getNgayBatDau() + "', '" + dbd.getNgayHoanThanh()
                + "', '" + dbd.getTrangThai() + "', " + dbd.getTongTien() + ", " + dbd.getIdNhanVienLapDon() + "" + ");"
                + "SELECT SCOPE_IDENTITY() AS [SCOPE_IDENTITY]; ";
        ResultSet rs = st.executeQuery(q);
        if (rs.next()) {
            dbd.setId(rs.getInt(1));
        }
        con.close();
        return dbd;
    }

    public DonBaoDuong timDonBaoDuongTheoID(String id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from DonBaoDuong where DonBaoDuong.id = " + id;
        ResultSet rs = st.executeQuery(q);

        if (rs.next()) {
            return new DonBaoDuong(rs.getInt("id"),
                    rs.getString("BienSo"),
                    rs.getString("NgayBatDau"),
                    rs.getString("NgayHoanThanh"),
                    rs.getString("TrangThai"),
                    rs.getInt("idNhanVienLapDon"),
                    rs.getInt("TongTien"));
        }
        con.close();
        return null;
    }

    public ArrayList<DonBaoDuong> layDanhSachDonBaoDuong(boolean trangThai) throws SQLException {
        int t = trangThai ? 1 : 0;
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from "
                + "DonBaoDuong left join XeMay on DonBaoDuong.BienSo = XeMay.BienSo left join  KhachHang on XeMay.idChuSoHuu = KhachHang.id "
                + "where DonBaoDuong.TrangThai = " + t;
        ResultSet rs = st.executeQuery(q);

        ArrayList<DonBaoDuong> arl = new ArrayList<>();
        while (rs.next()) {
            String tenKhach = "Khách vãng lai";
            String sdtKhach = "";
            if (!(rs.getString(13) == null || rs.getString(13).equalsIgnoreCase(""))) {
                tenKhach = rs.getString(13);
                sdtKhach = rs.getString(14);
            }
            arl.add(new DonBaoDuong(rs.getInt("id"),
                    rs.getString("BienSo"),
                    rs.getString("NgayBatDau"),
                    rs.getString("NgayHoanThanh"),
                    rs.getString("TrangThai"),
                    rs.getInt("idNhanVienLapDon"),
                    rs.getInt("TongTien"),
                    tenKhach,
                    sdtKhach));
        }
        con.close();
        return arl;
    }

    public void themChiTietDonBaoDuong(int idDichVu, int idDonBaoDuong, int soLuong, long phuPhi, int idNhanVienPhuTrach, String NgayCapNhatDichVuBaoDuong) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO ChiTietDonBaoDuong(idDichVuBaoDuong,idDonBaoDuong,idNhanVienPhuTrach,phuphi,soluong,NgayCapNhatDichVuBaoDuong) "
                + "VALUES (" + idDichVu + ", " + idDonBaoDuong + ", " + idNhanVienPhuTrach
                + ", " + phuPhi + ", " + soLuong + ",'" + NgayCapNhatDichVuBaoDuong + "');";
        st.execute(q);
        con.close();

    }

    public void xoaToanBoChiTietDonBaoDuongTheoHoaDon(int id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "DELETE FROM ChiTietDonBaoDuong"
                + " WHERE ChiTietDonBaoDuong.idDonBaoDuong = " + id;
        st.execute(q);
        con.close();
    }

    public void xoaToanBoChiTietThayTheLinhKienTheoHoaDon(int id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        ArrayList<LinhKien> ar = layDanhSachLinhKienTrongHoaDon(id);
        for (LinhKien lk : ar) {
            int soLuongMoi = lk.getSoLuongTrongDonBaoDuong() + lk.getSoLuong();
            String q = "UPDATE LinhKien"
                    + " SET soluong = " + soLuongMoi
                    + " WHERE LinhKien.id = " + lk.getID() + " AND LinhKien.NgayNhap = '" + lk.getNgayNhap() + "'";
            st.execute(q);
        }

        String q = "DELETE FROM ChiTietThayTheLinhKien"
                + " WHERE ChiTietThayTheLinhKien.idDonBaoDuong = " + id;
        st.execute(q);
        con.close();
    }

    public void xoaToanBoChiTietTrangThaiPhuTungKiemTraTheoHoaDon(int id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "DELETE FROM ChiTietTrangThaiKhiTiepNhanXe"
                + " WHERE ChiTietTrangThaiKhiTiepNhanXe.idDonBaoDuong = " + id;
        st.execute(q);
        con.close();
    }

    public void themChiTietThayTheLinhKien(int idDonBaoDuong, int idLinkKien, String ngaynhaplinhkien, String ghichu, int soLuong) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO ChiTietThayTheLinhKien(idDonBaoDuong,idLinkKien,ngaynhaplinhkien,soLuong,ghichu) "
                + "VALUES (" + idDonBaoDuong + ", " + idLinkKien + ", '" + ngaynhaplinhkien + "', " + soLuong
                + ", N'" + ghichu + "');";
        st.execute(q);
        con.close();
    }

    public void themChiTietTrangThaiKhiTiepNhanXe(int idDonBaoDuong, int idPhuTungCanKiemTra, int idTrangThaiPhuTung) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "INSERT INTO ChiTietTrangThaiKhiTiepNhanXe(idDonBaoDuong,idPhuTungCanKiemTra,idTrangThaiPhuTung) "
                + "VALUES (" + idDonBaoDuong + ", " + idPhuTungCanKiemTra + ", " + idTrangThaiPhuTung + ");";
        st.execute(q);
        con.close();
    }

    public void capNhatSoLuongLinhKien(int idLinhKien, int soLuongBanDi) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        ArrayList<LinhKien> ar = layDanhSachLinhKien();
        for (LinhKien lk : ar) {
            if (lk.getID() == idLinhKien) {
                int soLuongMoi = lk.getSoLuong() >= soLuongBanDi ? lk.getSoLuong() - soLuongBanDi : 0;
                String q = "UPDATE LinhKien"
                        + " SET soluong = " + soLuongMoi
                        + " WHERE id = " + idLinhKien;
                st.execute(q);
                break;
            }
        }
        con.close();
    }

    public int timIDPhuTungKiemTra(String str) throws SQLException {
        ArrayList<PhuTung> ar = layDanhSachPhuTungCanKiemTra();
        for (PhuTung pt : ar) {
            if (pt.getTenPhuTung().equalsIgnoreCase(str.trim())) {
                return pt.getId();
            }
        }
        return 0;
    }

    public void themDichVuBaoDuong(DichVuBaoDuong dv) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String t = "1";
        if (dv.getTrangThai().equalsIgnoreCase("Sử dụng")) {
            t = "0";
        }
        String q = "INSERT INTO DichVuBaoDuong(Ten,Phi,idLoaiXe,TrangThai,NgayCapNhat)"
                + "VALUES (N'" + dv.getTenDichVuBaoDuong() + "', " + dv.getPhi() + "," + layIdLoaiXe(dv.getLoaiXe()) + "," + t + ",'" + dv.getNgayCapNhat() + "')";
        st.execute(q);
        con.close();
    }

    public void capNhatDichVuBaoDuong(DichVuBaoDuong dv) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String t = "1";
        if (dv.getTrangThai().equalsIgnoreCase("Sử dụng")) {
            t = "0";
        }
        String q = " SET IDENTITY_INSERT DichVuBaoDuong ON ;"
                + "INSERT INTO DichVuBaoDuong(id,Ten,Phi,idLoaiXe,TrangThai)"
                + "VALUES (" + dv.getId() + ",N'" + dv.getTenDichVuBaoDuong() + "', " + dv.getPhi() + "," + layIdLoaiXe(dv.getLoaiXe()) + "," + t + ")";
        st.execute(q);
        con.close();
    }

    public void themLoaiXe(LoaiXe lx) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String t = "1";
        if (lx.getTrangThai().equalsIgnoreCase("Sử dụng")) {
            t = "0";
        }
        String q = "INSERT INTO LoaiXe(Ten,TrangThai)"
                + "VALUES (N'" + lx.getTenLoai() + "', " + t + ")";
        st.execute(q);
        con.close();
    }

    public void capNhatLoaiXe(LoaiXe lx) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String t = "1";
        if (lx.getTrangThai().equalsIgnoreCase("Sử dụng")) {
            t = "0";
        }
        String q = "UPDATE LoaiXe"
                + " SET Ten = N'" + lx.getTenLoai() + "'"
                + " , TrangThai = " + t
                + " WHERE id = " + lx.getId();
        con.close();
    }

    public NhanVien timNhanVienTheoID(int id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from NhanVien where NhanVien.id = " + id;
        ResultSet rs = st.executeQuery(q);
        if (rs.next()) {
            return new NhanVien(rs.getInt("id"), rs.getString("Ten"), rs.getString("CMND"), rs.getString("GioiTinh"), rs.getString("SDT"));
        } else {
            return null;
        }
    }

    public ArrayList<DichVuBaoDuong> layDanhSachDichVuBaoDuongTrongHoaDon(int idDonBaoDuong) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from ChiTietDonBaoDuong where ChiTietDonBaoDuong.idDonBaoDuong = " + idDonBaoDuong;
        ResultSet rs = st.executeQuery(q);

        ArrayList<DichVuBaoDuong> dsDichVu = layDanhSachDichVuBaoDuongMoiNhat();

        ArrayList<DichVuBaoDuong> dsDichVuKetQua = new ArrayList<>();
        while (rs.next()) {
            for (DichVuBaoDuong dv : dsDichVu) {
                if (rs.getInt("idDichVuBaoDuong") == dv.getId()) {
                    dv.setNv(timNhanVienTheoID(rs.getInt("idNhanVienPhuTrach")));
                    dv.setPhuPhi(rs.getLong("PhuPhi"));
                    dv.setSoLuongTrongHoaDon(rs.getInt("SoLuong"));
                    dsDichVuKetQua.add(dv);
                    System.out.println("Model.DonBaoDuongModel.layDanhSachDichVuBaoDuongTrongHoaDon()");
                    break;
                }
            }
        }
        return dsDichVuKetQua;
    }

    public ArrayList<LinhKien> layDanhSachLinhKienTrongHoaDon(int idDonBaoDuong) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from ChiTietThayTheLinhKien where ChiTietThayTheLinhKien.idDonBaoDuong = " + idDonBaoDuong;
        ResultSet rs = st.executeQuery(q);

        ArrayList<LinhKien> dsLinhKien = layDanhSachLinhKien();

        ArrayList<LinhKien> dsLinhKienKetQua = new ArrayList<>();
        while (rs.next()) {
            for (LinhKien lk : dsLinhKien) {
                if (rs.getInt("idLinkKien") == lk.getID() && rs.getString("NgayNhapLinhKien").equalsIgnoreCase(lk.getNgayNhap())) {
                    lk.setSoLuongTrongDonBaoDuong(rs.getInt("SoLuong"));
                    dsLinhKienKetQua.add(lk);
                    break;
                }
            }
        }
        return dsLinhKienKetQua;
    }

    public ArrayList<PhuTung> layDanhSachPhuTungTrongHoaDon(int idDonBaoDuong) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "select * from ChiTietTrangThaiKhiTiepNhanXe where ChiTietTrangThaiKhiTiepNhanXe.idDonBaoDuong = " + idDonBaoDuong;
        ResultSet rs = st.executeQuery(q);

        ArrayList<PhuTung> dsPhuTung = layDanhSachPhuTungCanKiemTra();

        ArrayList<PhuTung> dsPhuTungKetQua = new ArrayList<>();
        while (rs.next()) {
            for (PhuTung pt : dsPhuTung) {
                if (rs.getInt("idPhuTungCanKiemTra") == pt.getId()) {
                    pt.getTrangThai().add(rs.getString("idTrangThaiPhuTung"));
                    dsPhuTungKetQua.add(pt);
                }
            }
        }
        return dsPhuTungKetQua;
    }

    public void capNhatDonBaoDuong(DonBaoDuong d) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        if (d != null && d.getId() != 0) {
            String q = "UPDATE DonBaoDuong set BienSo = '" + d.getBienSo() + "' ,"
                    + "idNhanVienLapDon = " + d.getIdNhanVienLapDon() + ", "
                    + "NgayHoanThanh = '" + d.getNgayHoanThanh() + "', "
                    + "TongTien= " + d.getTongTien() + ", "
                    + "TrangThai = " + d.getTrangThai() + " "
                    + "where DonBaoDuong.id = " + d.getId();
            st.execute(q);
        }
        con.close();
    }

    public void xoaDonBaoDuong(int id) throws SQLException {
        xoaToanBoChiTietDonBaoDuongTheoHoaDon(id);
        xoaToanBoChiTietThayTheLinhKienTheoHoaDon(id);
        xoaToanBoChiTietTrangThaiPhuTungKiemTraTheoHoaDon(id);
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String q = "DELETE FROM DonBaoDuong"
                + " WHERE DonBaoDuong.id = " + id;
        st.execute(q);
        con.close();
    }

    public void capNhatTrangThaiDonBaoDuong(int id, boolean duocHoanThanh) throws SQLException {
        int t = duocHoanThanh ? 1 : 0;
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();

        String q = "UPDATE DonBaoDuong set TrangThai = " + t + " WHERE DonBaoDuong.id = " + id;
        st.execute(q);

        con.close();
    }
}
