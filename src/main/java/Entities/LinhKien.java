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
public class LinhKien {
    private int ID;
    private String tenLinhKien;
    private int soLuong;
    private long gia;
    private String nhaCungCap;
    private String ngayNhap;

    public LinhKien(int ID, String tenLinhKien, int soLuong, long gia, String nhaCungCap, String ngayNhap) {
        this.ID = ID;
        this.tenLinhKien = tenLinhKien;
        this.soLuong = soLuong;
        this.gia = gia;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = ngayNhap;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenLinhKien() {
        return tenLinhKien;
    }

    public void setTenLinhKien(String tenLinhKien) {
        this.tenLinhKien = tenLinhKien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    
    
}
