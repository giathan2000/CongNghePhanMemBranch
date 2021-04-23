/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import org.apache.commons.lang.SerializationUtils;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import lib.Config;
import lib.Stringlib;

/**
 *
 * @author hesac
 */
public class DonBaoDuongController {

    DonBaoDuongPanel baoduongPanel;
    DonBaoDuongModel baoDuongModel;
    ArrayList<NhanVien> danhSachNhanVien;
    ArrayList<DichVuBaoDuong> danhSachDichVubaoDuong;

    public DonBaoDuongController(DonBaoDuongPanel donBaoDuongPanel, DonBaoDuongModel model) throws SQLException {
        this.baoduongPanel = donBaoDuongPanel;
        this.baoDuongModel = model;
        init();
    }

    public void init() throws SQLException {
        danhSachNhanVien = baoDuongModel.layDanhSachNhanVien();
        cauHinhCacItem();
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
                ((DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel()).setNumRows(0);
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
                    if (xm == null) {
                        baoDuongModel.themXeMayMoi(new XeMay(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText(),
                                baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().getText(),
                                kh.getID(),
                                baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog().getSelectedItem().toString()));
                    } else if (xm.getIdKhach() == 0) {
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

                DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
                DefaultTableModel dtm1 = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
                Vector<Vector> lr = (Vector) dtm.getDataVector();
                Vector<Vector> lr1 = (Vector) dtm1.getDataVector();
                for (Vector vt : lr) {
                    if (vt.elementAt(4) != null && (boolean) vt.elementAt(4) == true) {
                        boolean doit = false;
                        for (int i = 0; i < lr1.size(); i++) {
                            Vector vt1 = lr1.get(i);
                            if (vt.elementAt(1).toString().equalsIgnoreCase(vt1.elementAt(1).toString())) {
                                int data = Integer.parseInt(dtm1.getValueAt(i, 2).toString().trim()) + 1;
                                if (data > 100) {
                                    data = 100;
                                }
                                dtm1.setValueAt(data, i, 2);
                                doit = true;
                                break;
                            }
                        }
                        if (!doit) {
                            dtm1.addRow(new Object[]{vt.elementAt(0), vt.elementAt(1), 1, vt.elementAt(2), 0, null});
                        }

                    }
                }
                baoduongPanel.getThemDichVuBaoDuongDailog().setVisible(false);
            }
        });

        baoduongPanel.getXoaDichVuBaoDuongBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int[] lr = baoduongPanel.getDanhSachDichVuBaoDuongTB().getSelectedRows();
                DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();

                if (lr.length != 0) {
                    for (int i = lr.length; i > 0; i--) {
                        dtm.removeRow(lr[i - 1]);
                    }
                }
            }
        });

        baoduongPanel.getThemLinhKienThayTheBT_ThemLinhKienThayTheDailog().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel();
                DefaultTableModel dtm1 = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();
                Vector<Vector> lr = (Vector) dtm.getDataVector();
                Vector<Vector> lr1 = (Vector) dtm1.getDataVector();

                for (int j = 0; j < lr.size(); j++) {
                    Vector vt = lr.get(j);
                    if (vt.elementAt(6) != null && (boolean) vt.elementAt(6) == true) {
                        boolean doit = false;
                        int data = 0;
                        for (int i = 0; i < lr1.size(); i++) {
                            Vector vt1 = lr1.get(i);
                            if (vt.elementAt(0).toString().equalsIgnoreCase(vt1.elementAt(0).toString())) {
                                data = Integer.parseInt(dtm1.getValueAt(i, 2).toString().trim()) + 1;
                                if (data > 100) {
                                    data = 100;
                                }
                                dtm1.setValueAt(data, i, 2);
                                doit = true;
                                break;
                            }
                        }
                        if (!doit) {
                            dtm1.addRow(new Object[]{vt.elementAt(0), vt.elementAt(1), 1, vt.elementAt(2), null});
                        }
                    }
                }
                suaNhapSoLuong();
                baoduongPanel.getThemLinhKienThayTheDialog().setVisible(false);
                
            }
        });

        baoduongPanel.getXoaLinhKienThayTheBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int[] lr = baoduongPanel.getDanhSachLinhKienThayTheTB().getSelectedRows();
                DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();

                if (lr.length != 0) {
                    for (int i = lr.length; i > 0; i--) {
                        dtm.removeRow(lr[i - 1]);
                    }
                }
            }
        });

        baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel().addTableModelListener((TableModelEvent e) -> {
            capNhatNhatTinhTienTF();
        });

        baoduongPanel.getDanhSachLinhKienThayTheTB().getModel().addTableModelListener((TableModelEvent e) -> {
            capNhatNhatTinhTienTF();
        });

        baoduongPanel.getDanhSachDichVuBaoDuongTB().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                capNhatNhatTinhTienTF();
            }
        });

        baoduongPanel.getDanhSachLinhKienThayTheTB().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                capNhatNhatTinhTienTF();
            }
        });

        baoduongPanel.getSoLuongComboBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                capNhatNhatTinhTienTF();
            }
        });

        baoduongPanel.getTimKiemDichVuBaoDuongTF_ThemDichVuBaoDuongDailog().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    ArrayList<DichVuBaoDuong> ar = baoDuongModel.layDanhSachDichVuBaoDuong();
                    String key = baoduongPanel.getTimKiemDichVuBaoDuongTF_ThemDichVuBaoDuongDailog().getText().trim();
                    DefaultTableModel dm = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
                    if (!key.equalsIgnoreCase("")) {
                        dm.setNumRows(0);
                        for (DichVuBaoDuong dv : ar) {
                            if (Stringlib.isLikeString(key, dv.getTenDichVuBaoDuong()) >= 0.5
                                    && baoduongPanel.getLoaiXeComboBox().getSelectedItem().toString().equalsIgnoreCase(dv.getLoaiXe())) {
                                dm.addRow(new Object[]{dv.getId(), dv.getTenDichVuBaoDuong(), dv.getPhi(), dv.getLoaiXe()});
                            }
                        }
                    } else {
                        loadDanhSachDichVuBaoDuonginDialog();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        baoduongPanel.getTimKiemLinhKienThayTheTF_ThemLinhKienThayTheDailog().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    ArrayList<LinhKien> ar = baoDuongModel.layDanhSachLinhKien();
                    String key = baoduongPanel.getTimKiemLinhKienThayTheTF_ThemLinhKienThayTheDailog().getText().trim();
                    DefaultTableModel dm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel();
                    if (!key.equalsIgnoreCase("")) {
                        dm.setNumRows(0);
                        for (LinhKien lk : ar) {
                            if (Stringlib.isLikeString(key, lk.getTenLinhKien()) >= 0.5) {
                                dm.addRow(new Object[]{lk.getID(), lk.getTenLinhKien(), lk.getGia(), lk.getNhaCungCap(), lk.getNgayNhap(), lk.getSoLuong(), null});
                            }
                        }
                    } else {
                        loadDanhSachLinhKieninDialog();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        baoduongPanel.getLuuThongTinKhachHangMoiBT_ThemKhachHangMoiDailog().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                baoduongPanel.getChungMinhNhanDanTF_ThemKhachHangMoiDailog().setText("");
                baoduongPanel.getNgaySinhKhachHangTF_ThemKhachHangMoiDailog().setText("");
                baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().setText("");
                baoduongPanel.getThemKhachHangMoiDailog().setVisible(false);
            }

        });

        baoduongPanel.getSoLuongComboBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                suaNhapSoLuong();
            }
        });
        
        baoduongPanel.getSoLuongComboBox().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                suaNhapSoLuong();
            }
        });
        
        baoduongPanel.getLuuDonBaoDuongBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
            }
        });
    }

    private void capNhatNhatTinhTienTF() {
        DefaultTableModel dvm = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
        DefaultTableModel ptm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();

        Vector<Vector> dvData = dvm.getDataVector();
        Vector<Vector> ptData = ptm.getDataVector();

        long tongTienDichVu = 0l;
        long tongTienPhuTung = 0l;

        for (Vector dvr : dvData) {
            tongTienDichVu += Long.valueOf(dvr.get(2).toString()) * Long.valueOf(dvr.get(3).toString()) + Long.valueOf(dvr.get(4).toString());
        }

        for (Vector ptr : ptData) {
            tongTienPhuTung += (Long.valueOf(ptr.get(2).toString())) * (Long.valueOf(ptr.get(3).toString()));
        }
        baoduongPanel.getThanhTienDichVuBaoDuongTF().setText(Long.toString(tongTienDichVu));
        baoduongPanel.getThanhTienThayTheLinhKienTF().setText(Long.toString(tongTienPhuTung));
        baoduongPanel.getTongChiPhiTF().setText(Long.toString(tongTienDichVu + tongTienPhuTung));
        baoduongPanel.getThueVATTF().setText(Long.toString((long) ((tongTienDichVu + tongTienPhuTung) * Config.VAT)));
        baoduongPanel.getTongThanhToanTF().setText(Long.toString((long) (tongTienDichVu + tongTienPhuTung + (tongTienDichVu + tongTienPhuTung) * Config.VAT)));
    }

    private void cauHinhCacItem() throws SQLException {
        for (int i = 0; i < 100; i++) {
            baoduongPanel.getSoLuongComboBox().addItem(Integer.toString(i + 1));
        }
        baoduongPanel.getDanhSachDichVuBaoDuongTB().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(baoduongPanel.getSoLuongComboBox()));
        baoduongPanel.getDanhSachLinhKienThayTheTB().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(baoduongPanel.getSoLuongComboBox()));
        ArrayList<NhanVien> ar = baoDuongModel.layDanhSachNhanVien();
        for (NhanVien nv : ar) {
            baoduongPanel.getDanhSachNhanVienComboBox().addItem(nv.getHoTen() + " (" + nv.getID() + ")");
        }
        baoduongPanel.getDanhSachDichVuBaoDuongTB().getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(baoduongPanel.getDanhSachNhanVienComboBox()));
    }

    private void suaNhapSoLuong() {
        for (int i = 0; i < baoduongPanel.getDanhSachLinhKienThayTheTB().getModel().getRowCount(); i++) {
            for (int j = 0; j < baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel().getRowCount(); j++) {
                if (baoduongPanel.getDanhSachLinhKienThayTheTB().getModel().getValueAt(i, 0).toString().equalsIgnoreCase(
                        baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel().getValueAt(j, 0).toString())
                        && (Integer.parseInt(baoduongPanel.getDanhSachLinhKienThayTheTB().getModel().getValueAt(i, 2).toString())
                        > Integer.parseInt(baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel().getValueAt(j, 5).toString()))) {
                    baoduongPanel.getDanhSachLinhKienThayTheTB().getModel().setValueAt(baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel().getValueAt(j, 5), i, 2);
                }
            }
        }
    }
}
