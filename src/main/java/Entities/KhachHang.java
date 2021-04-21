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
public class KhachHang {
    private int ID;
    private String hoTen;
    private String CMND;
    private String SDT;
    private String gioiTinh;
    private String ngaySinh;

    public KhachHang(int ID, String hoTen, String CMND, String SDT, String gioiTinh, String ngaySinh) {
        this.ID = ID;
        this.hoTen = hoTen;
        this.CMND = CMND;
        this.SDT = SDT;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    
}
