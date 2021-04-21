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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
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

    public void init() throws SQLException {
        themSuKienChoCacItem();
        loadDanhSachTrangThaiPhuTungTiepNhan();
        loadDanhSachLoaiXeComboBox();
        loadDanhSachDichVuBaoDuonginDialog();
        loadDanhSachLinhKieninDialog();

    }

    private void loadKhachHang(int id) {

    }

    private void loadDanhSachTrangThaiPhuTungTiepNhan() throws SQLException {
        ArrayList<PhuTung> arl = baoDuongModel.layDanhSachPhuTungCanKiemTra();
        DefaultTableModel dftb = (DefaultTableModel) baoduongPanel.getDanhSachtrangThaiPhuTungTiepNhan().getModel();
        dftb.setNumRows(0);
        arl.forEach(pt -> {
            dftb.addRow(new Object[]{pt.getTenPhuTung(), true, null, null, null, null});
        });
    }

    private void loadDanhSachLoaiXeComboBox() throws SQLException {
        ArrayList<LoaiXe> arl = baoDuongModel.layDanhSachLoaiXe();
        JComboBox<String> tb = baoduongPanel.getLoaiXeComboBox();
        JComboBox<String> tb1 = baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog();
        arl.forEach(loaiXe -> {
            tb.addItem(loaiXe.getTenLoai());
            tb1.addItem(loaiXe.getTenLoai());
        });
        tb.setSelectedIndex(1);
        tb1.setSelectedIndex(1);
    }

    private void loadDanhSachDichVuBaoDuonginDialog() throws SQLException {
        ArrayList<DichVuBaoDuong> arl = baoDuongModel.layDanhSachDichVuBaoDuong();
        JComboBox<String> cb = baoduongPanel.getLoaiXeComboBox();
        DefaultTableModel dftb = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
        dftb.setNumRows(0);
        arl.stream().filter(dichVuBaoDuong -> (cb.getSelectedItem().toString().equalsIgnoreCase(dichVuBaoDuong.getLoaiXe()))).forEachOrdered(dichVuBaoDuong -> {
            dftb.addRow(new Object[]{dichVuBaoDuong.getId(), dichVuBaoDuong.getTenDichVuBaoDuong(), dichVuBaoDuong.getPhi(), dichVuBaoDuong.getLoaiXe()});
        });
    }

    private void loadDanhSachLinhKieninDialog() throws SQLException {
        ArrayList<LinhKien> arl = baoDuongModel.layDanhSachLinhKien();
        DefaultTableModel dftb = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel();
        dftb.setNumRows(0);
        arl.forEach(lk -> {
            dftb.addRow(new Object[]{lk.getID(), lk.getTenLinhKien(), lk.getGia(), lk.getNhaCungCap(), lk.getNgayNhap(), lk.getSoLuong(), null});
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
                    if (xm == null || xm.getIdKhach() == 0) {
                        baoduongPanel.getTenKhachHangTF().setText("Khách vãng lai");
                        baoduongPanel.getSoDienThoaiTF().setText("");
                        baoduongPanel.getLoaiXeComboBox().setEnabled(true);
                        baoduongPanel.getSoDienThoaiTF().setEditable(true);
                        baoduongPanel.getTenKhachHangTF().setEditable(true);
                        baoduongPanel.getThemKhachHangMoiBT().setVisible(true);
                        baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().setText(baoduongPanel.getBienSoXeTF().getText().trim());
                        baoduongPanel.getTenKhachHangTF_ThemKhachHangMoiDailog().setText(baoduongPanel.getTenKhachHangTF().getText());
                        baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog().setSelectedItem(baoduongPanel.getLoaiXeComboBox().getSelectedItem().toString().trim());
                        baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().setText(baoduongPanel.getBienSoXeTF().getText());
                        baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().setText(baoduongPanel.getSoDienThoaiTF().getText().trim());

                    } else {
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

        baoduongPanel.getThemKhachHangMoiBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                baoduongPanel.getThemKhachHangMoiDailog().setVisible(true);
                baoduongPanel.getThemKhachHangMoiDailog().setModal(true);
            }
        });
        
        baoduongPanel.getLuuThongTinKhachHangMoiBT_ThemKhachHangMoiDailog().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                KhachHang kh = new KhachHang();
                kh.setHoTen(baoduongPanel.getTenKhachHangTF_ThemKhachHangMoiDailog().getText().trim());
                kh.setGioiTinh(baoduongPanel.getGioiTinhKhachHangComboBox_ThemKhachHangMoiDailog().getSelectedItem().toString());
                kh.setCMND(baoduongPanel.getChungMinhNhanDanTF_ThemKhachHangMoiDailog().getText().trim());
                kh.setSDT(baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().getText().trim());
                try {
                    baoDuongModel.themKhachHangMoi(kh);

                    kh = baoDuongModel.timKhachHangTheoTenVaSDT(kh.getHoTen(), kh.getSDT());

                   
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    XeMay xm = baoDuongModel.timXeMayTheoBienSo(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText());
                    if(xm == null){ 
                        baoDuongModel.themXeMayMoi(new XeMay(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText(), 
                                                            baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().getText(), 
                                                            kh.getID(), 
                                                            baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog().getSelectedItem().toString()));
                    } else if(xm.getIdKhach() == 0){
                        xm.setIdKhach(kh.getID());
                        baoDuongModel.capNhatthongTinXeMay(xm);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        baoduongPanel.getThemDichVuBaoDuongBT_ThemDichVuBaoDuongiDailog().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                DefaultTableModel dtm =  (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
                DefaultTableModel dtm1 =  (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
                Vector<Vector> lr = (Vector)dtm.getDataVector();
                for (Vector vt : lr) {
                    System.out.println(".mouseClicked()");
                    
                    if(vt.elementAt(4) != null && (boolean)vt.elementAt(4) == true){
                        dtm1.addRow(new Object[]{vt.elementAt(0),vt.elementAt(1),1,vt.elementAt(2),0,null});
                    }
                }
                baoduongPanel.getThemDichVuBaoDuongDailog().setVisible(false);
            }  
        });
    }
}
