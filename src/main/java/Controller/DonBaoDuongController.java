/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.*;
import Model.*;
import Entities.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
/**
 *
 * @author hesac
 */
public class DonBaoDuongController {
    DonBaoDuongPanel baoduongPanel;
    DonBaoDuongModel baoDuongModel;
    
    public DonBaoDuongController(DonBaoDuongPanel donBaoDuongPanel, DonBaoDuongModel model) throws SQLException {
        this.baoduongPanel = donBaoDuongPanel;
        this.baoDuongModel = model;
        init();
    }
    
    public void init() throws SQLException{
        themSuKienChoCacItem();
        loadDanhSachTrangThaiPhuTungTiepNhan();
        loadDanhSachLoaiXeComboBox();
        loadDanhSachDichVuBaoDuonginDialog();
        loadDanhSachLinhKieninDialog();
      
    }
    
    private void loadKhachHang(int id){
        
    }
    
    private void loadDanhSachTrangThaiPhuTungTiepNhan() throws SQLException{
        ArrayList<PhuTung> arl =  baoDuongModel.layDanhSachPhuTungCanKiemTra();
        DefaultTableModel dftb = (DefaultTableModel)baoduongPanel.getDanhSachtrangThaiPhuTungTiepNhan().getModel();
        dftb.setNumRows(0);
        arl.forEach(pt -> {
            dftb.addRow(new Object[]{pt.getTenPhuTung(),null,null,null,null,null});
        });
    }
    
    private void loadDanhSachLoaiXeComboBox() throws SQLException{
        ArrayList<LoaiXe> arl = baoDuongModel.layDanhSachLoaiXe();
        JComboBox<String> tb = baoduongPanel.getLoaiXeComboBox();
        arl.forEach(loaiXe -> {
            tb.addItem(loaiXe.getTenLoai());
        });
        tb.setSelectedIndex(1);
    }
    
    private void loadDanhSachDichVuBaoDuonginDialog() throws SQLException{
        ArrayList<DichVuBaoDuong> arl = baoDuongModel.layDanhSachDichVuBaoDuong();
        JComboBox<String> cb = baoduongPanel.getLoaiXeComboBox();
        DefaultTableModel dftb = (DefaultTableModel)baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
        dftb.setNumRows(0);
        arl.stream().filter(dichVuBaoDuong -> ( cb.getSelectedItem().toString().equalsIgnoreCase(dichVuBaoDuong.getLoaiXe()))).forEachOrdered(dichVuBaoDuong -> {
            dftb.addRow(new Object[] {dichVuBaoDuong.getId(),dichVuBaoDuong.getTenDichVuBaoDuong(),dichVuBaoDuong.getPhi(),dichVuBaoDuong.getLoaiXe()});
        });
    }

    private void loadDanhSachLinhKieninDialog() throws SQLException{
        ArrayList<LinhKien> arl = baoDuongModel.layDanhSachLinhKien();
        DefaultTableModel dftb = (DefaultTableModel)baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel();
        dftb.setNumRows(0);
        arl.forEach(lk -> {
            dftb.addRow(new Object[]{lk.getID(),lk.getTenLinhKien(),lk.getGia(),lk.getNhaCungCap(),lk.getNgayNhap(),lk.getSoLuong(),null});
        });
    }
    
    private void themSuKienChoCacItem() {
        baoduongPanel.getLoaiXeComboBox().addItemListener((ItemEvent e) -> {
            try {
                loadDanhSachDichVuBaoDuonginDialog();
            } catch (SQLException ex) {
                Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        baoduongPanel.getBienSoXeTF().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    XeMay xm = baoDuongModel.timXeMayTheoBienSo(baoduongPanel.getBienSoXeTF().getText().trim());
                    if(xm == null || xm.getIdKhach() == 0){
                        baoduongPanel.getTenKhachHangTF().setText("Khách vãng lai");
                        baoduongPanel.getSoDienThoaiTF().setText("");
                        baoduongPanel.getLoaiXeComboBox().setEnabled(true);
                        baoduongPanel.getSoDienThoaiTF().setEditable(true);
                        baoduongPanel.getTenKhachHangTF().setEditable(true);
                        baoduongPanel.getThemKhachHangMoiBT().setVisible(true);
                        
                    }else {
                        KhachHang kh = baoDuongModel.timKhachHangTheoID(xm.getIdKhach());
                        baoduongPanel.getTenKhachHangTF().setText(kh.getHoTen());
                        baoduongPanel.getTenKhachHangTF().setEditable(false);
                        baoduongPanel.getSoDienThoaiTF().setText(kh.getSDT());
                        baoduongPanel.getSoDienThoaiTF().setEditable(false);
                        baoduongPanel.getLoaiXeComboBox().setSelectedItem(xm.getLoaiXe());
                        baoduongPanel.getLoaiXeComboBox().setEnabled(false);
                        baoduongPanel.getThemKhachHangMoiBT().setVisible(false);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
