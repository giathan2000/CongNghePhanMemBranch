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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import java.time.LocalDate;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import lib.Config;
import lib.Stringlib;

/**
 *
 * @author hesac
 */
public class DichVuBaoDuongController {

    DichVubaoDuongPanel dichVuPanel;
    DonBaoDuongModel baoDuongModel;
    ArrayList<NhanVien> danhSachNhanVien;
    ArrayList<DichVuBaoDuong> danhSachDichVubaoDuong;

    public DichVuBaoDuongController(DichVubaoDuongPanel dichVuPanel, DonBaoDuongModel model) throws SQLException {
        this.dichVuPanel = dichVuPanel;
        this.baoDuongModel = model;
        init();
    }

    private void init() throws SQLException {
        loadDanhSachDichVuBaoDuong();
        themSuKienChoItem();
        loadDanhSachLoaiXeComboBoxvaTable();
    }

    private void loadDanhSachDichVuBaoDuong() throws SQLException {
        ArrayList<DichVuBaoDuong> arl = baoDuongModel.layDanhSachDichVuBaoDuongMoiNhat();
        DefaultTableModel dftb = (DefaultTableModel) dichVuPanel.getjTable_DanhSachDichVuBaoDuong().getModel();
        dftb.setNumRows(0);
        for (DichVuBaoDuong dichVuBaoDuong : arl) {
            dftb.addRow(new Object[]{
                dichVuBaoDuong.getId(),
                dichVuBaoDuong.getTenDichVuBaoDuong(),
                dichVuBaoDuong.getPhi(),
                dichVuBaoDuong.getLoaiXe(),
                dichVuBaoDuong.getTrangThai(),
                dichVuBaoDuong.getNgayCapNhat()});
        }
    }

    private void loadDanhSachLoaiXeComboBoxvaTable() throws SQLException {

        ArrayList<LoaiXe> arl = baoDuongModel.layDanhSachLoaiXe();
        DefaultTableModel dftb = (DefaultTableModel) dichVuPanel.getjTable_DanhSachLoaiXe().getModel();
        dftb.setNumRows(0);
        JComboBox<String> tb = dichVuPanel.getjComboBox_LoaiXe();
        arl.forEach(loaiXe -> {
            dftb.addRow(new Object[]{loaiXe.getId(), loaiXe.getTenLoai(), loaiXe.getTrangThai()});
            tb.addItem(loaiXe.getTenLoai());
        });
        tb.setSelectedIndex(0);
    }

    private void themSuKienChoItem() {

        dichVuPanel.getjTable_DanhSachDichVuBaoDuong().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = dichVuPanel.getjTable_DanhSachDichVuBaoDuong().getSelectedRow();
                if (row > -1) {

                    DefaultTableModel md = ((DefaultTableModel) dichVuPanel.getjTable_DanhSachDichVuBaoDuong().getModel());
                    dichVuPanel.getjTextField_MaDichVuBaoDuong().setText(md.getValueAt(row, 0).toString());
                    dichVuPanel.getjTextField_TenDichVuBaoDuong().setText(md.getValueAt(row, 1).toString());
                    dichVuPanel.getjTextField_PhiDichVuBaoDuong().setText(md.getValueAt(row, 2).toString());
                    dichVuPanel.getjComboBox_LoaiXe().setSelectedItem(md.getValueAt(row, 3).toString());
                    if (md.getValueAt(row, 4).toString().equalsIgnoreCase("Sử dụng")) {
                        dichVuPanel.getjCheckBox_TrangThaiSuDungDichVuBaoDuong().setSelected(true);
                    } else {
                        dichVuPanel.getjCheckBox_TrangThaiSuDungDichVuBaoDuong().setSelected(false);
                    }
                }
            }
        });
                        

        dichVuPanel.getjTable_DanhSachLoaiXe().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = dichVuPanel.getjTable_DanhSachLoaiXe().getSelectedRow();
                if (row > -1) {

                    DefaultTableModel md = ((DefaultTableModel) dichVuPanel.getjTable_DanhSachLoaiXe().getModel());
                    dichVuPanel.getjTextField_MaLoaiXe().setText(md.getValueAt(row, 0).toString());
                    dichVuPanel.getjTextField_LoaiXe().setText(md.getValueAt(row, 1).toString());
                    if (md.getValueAt(row, 2).toString().equalsIgnoreCase("Sử dụng")) {
                        dichVuPanel.getjCheckBox_TrangThaiSuDungLaoiXe().setSelected(true);
                    } else {
                        dichVuPanel.getjCheckBox_TrangThaiSuDungLaoiXe().setSelected(false);
                    }
                }
            }
        });

        dichVuPanel.getjTextField_TimKiemDichVuBaoDuong().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    ArrayList<DichVuBaoDuong> ar = baoDuongModel.layDanhSachDichVuBaoDuongMoiNhat();
                    String key = dichVuPanel.getjTextField_TimKiemDichVuBaoDuong().getText().trim();
                    DefaultTableModel dm = (DefaultTableModel) dichVuPanel.getjTable_DanhSachDichVuBaoDuong().getModel();
                    if (!key.equalsIgnoreCase("")) {
                        dm.setNumRows(0);
                        for (DichVuBaoDuong dv : ar) {
                            if (Stringlib.isLikeString(key, dv.getTenDichVuBaoDuong()) >= 0.51) {
                                dm.addRow(new Object[]{dv.getId(), dv.getTenDichVuBaoDuong(), dv.getPhi(), dv.getLoaiXe(), dv.getTrangThai()});
                            }
                        }
                    } else {
                        loadDanhSachDichVuBaoDuong();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        dichVuPanel.getjButton_LuuDichVuBaoDuong().addActionListener((e) -> {
            String st = "Không sử dụng";
            if (dichVuPanel.getjCheckBox_TrangThaiSuDungDichVuBaoDuong().isSelected()) {
                st = "Sử dụng";
            }
            DichVuBaoDuong dv = new DichVuBaoDuong(
                    Integer.parseInt(dichVuPanel.getjTextField_MaDichVuBaoDuong().getText()),
                    dichVuPanel.getjTextField_TenDichVuBaoDuong().getText(),
                    Long.parseLong(dichVuPanel.getjTextField_PhiDichVuBaoDuong().getText().trim()),
                    dichVuPanel.getjComboBox_LoaiXe().getSelectedItem().toString(),
                    st,
                    dichVuPanel.getjTextField_NgayCapNhat().getText());
            if (dichVuPanel.getjTextField_MaDichVuBaoDuong().getText().equalsIgnoreCase("")) {

                try {
                    baoDuongModel.themDichVuBaoDuong(dv);
                } catch (SQLException ex) {
                    Logger.getLogger(DichVuBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    baoDuongModel.capNhatDichVuBaoDuong(dv);
                } catch (SQLException ex) {
                    Logger.getLogger(DichVuBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                loadDanhSachDichVuBaoDuong();
            } catch (SQLException ex) {
                Logger.getLogger(DichVuBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dichVuPanel.getjButton_ThemLoaiXemoi().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dichVuPanel.getjTextField_MaDichVuBaoDuong().setText("");
                dichVuPanel.getjTextField_TenDichVuBaoDuong().setText("");
                dichVuPanel.getjTextField_PhiDichVuBaoDuong().setText("");
                dichVuPanel.getjComboBox_LoaiXe().setSelectedIndex(0);
                dichVuPanel.getjCheckBox_TrangThaiSuDungDichVuBaoDuong().setSelected(true);
            }
        });

        dichVuPanel.getjButton_ThemLoaiXemoi().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dichVuPanel.getjTextField_MaLoaiXe().setText("");
                dichVuPanel.getjTextField_LoaiXe().setText("");
                dichVuPanel.getjCheckBox_TrangThaiSuDungLaoiXe().setSelected(true);
            }
        });

        dichVuPanel.getjButton_LuuLoaiXe().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String st = "Không sử dụng";
                if (dichVuPanel.getjCheckBox_TrangThaiSuDungDichVuBaoDuong().isSelected()) {
                    st = "Sử dụng";
                }
                LoaiXe lx = new LoaiXe(
                        Integer.parseInt(dichVuPanel.getjTextField_MaLoaiXe().getText()),
                        dichVuPanel.getjTextField_LoaiXe().getText(),
                        st);
                if (dichVuPanel.getjTextField_MaLoaiXe().getText().equalsIgnoreCase("")) {
                    try {
                        baoDuongModel.themLoaiXe(lx);
                    } catch (SQLException ex) {
                        Logger.getLogger(DichVuBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        baoDuongModel.capNhatLoaiXe(lx);
                    } catch (SQLException ex) {
                        Logger.getLogger(DichVuBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
