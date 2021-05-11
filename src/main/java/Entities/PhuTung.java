/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;

/**
 *
 * @author hesac
 */
public class PhuTung {
    private int id;
    private String tenPhuTung;
    ArrayList<String> trangThai;

    public PhuTung() {
    }



    public PhuTung(int id, String tenPhuTung) {
        this.id = id;
        this.tenPhuTung = tenPhuTung;
        this.trangThai = new ArrayList<>();
    }

    public ArrayList<String> getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(ArrayList<String> trangThai) {
        this.trangThai = trangThai;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenPhuTung() {
        return tenPhuTung;
    }

    public void setTenPhuTung(String tenPhuTung) {
        this.tenPhuTung = tenPhuTung;
    }
    
}
