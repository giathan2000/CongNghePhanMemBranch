/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author hesac
 */
public class DonBaoDuong {
    private int id;
    private String bienSo;
    private String ngayBatDau;
    private String ngayHoanThanh;
    private String trangThai;
    private int idNhanVienLapDon;
    private long tongTien;
    private String tenKhachHang;
    private String SDTKhachhang;

    public DonBaoDuong(int id, String bienSo, String ngayBatDau, String ngayHoanThanh, String trangThai, int idNhanVienLapDon, long tongTien) {
        this.id = id;
        this.bienSo = bienSo;
        this.ngayBatDau = ngayBatDau;
        this.ngayHoanThanh = ngayHoanThanh;
        this.trangThai = trangThai;
        this.idNhanVienLapDon = idNhanVienLapDon;
        this.tongTien = tongTien;
    }

    public DonBaoDuong(int id, String bienSo, String ngayBatDau, String ngayHoanThanh, String trangThai, int idNhanVienLapDon, long tongTien, String tenKhachHang, String SDTKhachhang) {
        this.id = id;
        this.bienSo = bienSo;
        this.ngayBatDau = ngayBatDau;
        this.ngayHoanThanh = ngayHoanThanh;
        this.trangThai = trangThai;
        this.idNhanVienLapDon = idNhanVienLapDon;
        this.tongTien = tongTien;
        this.tenKhachHang = tenKhachHang;
        this.SDTKhachhang = SDTKhachhang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSDTKhachhang() {
        return SDTKhachhang;
    }

    public void setSDTKhachhang(String SDTKhachhang) {
        this.SDTKhachhang = SDTKhachhang;
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(String ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getIdNhanVienLapDon() {
        return idNhanVienLapDon;
    }

    public void setIdNhanVienLapDon(int idNhanVienLapDon) {
        this.idNhanVienLapDon = idNhanVienLapDon;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }
    
}
