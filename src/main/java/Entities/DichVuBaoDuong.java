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
public class DichVuBaoDuong {

    private int id;
    private String tenDichVuBaoDuong;
    private long phi;
    private String loaiXe;
    private String trangThai;
    private int SoLuongTrongHoaDon;
    private long phuPhi;
    private NhanVien nv;
    private String ngayCapNhat;

    public DichVuBaoDuong(int id, String tenDichVuBaoDuong, long phi, String loaiXe, String trangThai ,String ngayCapNhat) {
        this.id = id;
        this.tenDichVuBaoDuong = tenDichVuBaoDuong;
        this.phi = phi;
        this.loaiXe = loaiXe;
        this.trangThai = trangThai;
        this.ngayCapNhat = ngayCapNhat;
    }

    public DichVuBaoDuong(int id, String tenDichVuBaoDuong, long phi, String loaiXe) {
        this.id = id;
        this.tenDichVuBaoDuong = tenDichVuBaoDuong;
        this.phi = phi;
        this.loaiXe = loaiXe;

    }

    public DichVuBaoDuong(int id, String tenDichVuBaoDuong, long phi, String loaiXe, String trangThai, int SoLuongTrongHoaDon, long phuPhi, NhanVien nv) {
        this.id = id;
        this.tenDichVuBaoDuong = tenDichVuBaoDuong;
        this.phi = phi;
        this.loaiXe = loaiXe;
        this.trangThai = trangThai;
        this.SoLuongTrongHoaDon = SoLuongTrongHoaDon;
        this.phuPhi = phuPhi;
        this.nv = nv;
    }

    public String getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(String ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getSoLuongTrongHoaDon() {
        return SoLuongTrongHoaDon;
    }

    public void setSoLuongTrongHoaDon(int SoLuongTrongHoaDon) {
        this.SoLuongTrongHoaDon = SoLuongTrongHoaDon;
    }

    public long getPhuPhi() {
        return phuPhi;
    }

    public void setPhuPhi(long phuPhi) {
        this.phuPhi = phuPhi;
    }

    public NhanVien getNv() {
        return nv;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenDichVuBaoDuong() {
        return tenDichVuBaoDuong;
    }

    public void setTenDichVuBaoDuong(String tenDichVuBaoDuong) {
        this.tenDichVuBaoDuong = tenDichVuBaoDuong;
    }

    public long getPhi() {
        return phi;
    }

    public void setPhi(long phi) {
        this.phi = phi;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

}
