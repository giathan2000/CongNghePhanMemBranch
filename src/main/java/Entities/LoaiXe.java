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
public class LoaiXe {

    private int id;
    private String tenLoai;
    private String trangThai;

    public LoaiXe(int id, String tenLoai) {
        this.id = id;
        this.tenLoai = tenLoai;
    }

    public LoaiXe(int id, String tenLoai, String trangThai) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

}
