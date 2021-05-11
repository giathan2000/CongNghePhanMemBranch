/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.DonBaoDuong.DonBaoDuongPanel;
import View.DonBaoDuong.DanhSachDonBaoDuongPanel;
import View.DonBaoDuong.DonBaoDuongContainerPanel;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.event.TableModelListener;
import lib.Stringlib;
import lib.Config;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author hesac
 */
public class DonBaoDuongController {

    DonBaoDuong donBaoDuongCurrent;
    DonBaoDuongPanel baoduongPanel;
    DonBaoDuongModel baoDuongModel;
    ArrayList<NhanVien> danhSachNhanVien;
    DonBaoDuongContainerPanel baoDuongContainerPanel;
    DanhSachDonBaoDuongPanel danhSachDonBaoDuongPanel;

    public DonBaoDuongController(DonBaoDuongContainerPanel baoDuongContainerPanel, DonBaoDuongModel model) throws SQLException, ParseException {
        this.baoDuongContainerPanel = baoDuongContainerPanel;
        this.baoduongPanel = baoDuongContainerPanel.getDonBaoDuongPanel();
        this.danhSachDonBaoDuongPanel = baoDuongContainerPanel.getDanhSachDonBaoDuongPanel();
        this.baoDuongModel = model;
        init();
    }

    public void init() throws SQLException, ParseException {
        loadDanhSachDonBaoDuongChuaHoanThanh(false);
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
        tb.removeAllItems();
        JComboBox<String> tb1 = baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog();
        tb1.removeAllItems();
        arl.forEach(loaiXe -> {
            tb.addItem(loaiXe.getTenLoai());
            System.err.println(loaiXe.getTenLoai());
            tb1.addItem(loaiXe.getTenLoai());
        });
        tb.setSelectedIndex(0);
        tb1.setSelectedIndex(0);
    }

    private void loadDanhSachDichVuBaoDuonginDialog() throws SQLException {
        ArrayList<DichVuBaoDuong> arl = baoDuongModel.layDanhSachDichVuBaoDuongMoiNhat();
        JComboBox<String> cb = baoduongPanel.getLoaiXeComboBox();
        DefaultTableModel dftb = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
        dftb.setNumRows(0);
        for (DichVuBaoDuong dichVuBaoDuong : arl) {
            if (cb.getSelectedItem().toString().trim().equalsIgnoreCase(dichVuBaoDuong.getLoaiXe())) {
                dftb.addRow(new Object[]{
                    dichVuBaoDuong.getId(),
                    dichVuBaoDuong.getTenDichVuBaoDuong(),
                    dichVuBaoDuong.getPhi(),
                    dichVuBaoDuong.getLoaiXe(),
                    false,
                    dichVuBaoDuong.getNgayCapNhat()});
            }
        }

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
        //Khi thay đổi loại xe thì sẽ load lại Danh sách dịch vụ bảo dưỡng cho phù hợp
        baoduongPanel.getLoaiXeComboBox().addItemListener((ItemEvent e) -> {
            try {
                loadDanhSachDichVuBaoDuonginDialog();
                ((DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel()).setNumRows(0);
            } catch (SQLException ex) {
                Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Khi gõ phím sẽ tự động tìm thông tin khách hàng dựa trên xe máy và tự động điền thông tin
        baoduongPanel.getBienSoXeTF().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    loadKhachHangTrongHoaDon(baoduongPanel.getBienSoXeTF().getText());
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
                if (Stringlib.kiemTraChuoiBienSoXe(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText())
                        && Stringlib.kiemTraSDT(baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().getText())) {
                    baoduongPanel.getBienSoKhongHopLe().setVisible(false);
                    baoduongPanel.getSDTKhongHopLE().setVisible(false);
                    KhachHang kh = new KhachHang();
                    kh.setHoTen(baoduongPanel.getTenKhachHangTF_ThemKhachHangMoiDailog().getText().trim());
                    kh.setGioiTinh(baoduongPanel.getGioiTinhKhachHangComboBox_ThemKhachHangMoiDailog().getSelectedItem().toString());
                    kh.setSDT(baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().getText().trim());
                    try {
                        baoDuongModel.themKhachHangMoi(kh);
                        kh = baoDuongModel.timKhachHangTheoTenVaSDT(kh.getHoTen(), kh.getSDT());
                        baoduongPanel.getTenKhachHangTF().setText(kh.getHoTen());
                        baoduongPanel.getSoDienThoaiTF().setText(kh.getSDT());

                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        XeMay xm = baoDuongModel.timXeMayTheoBienSo(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText().toLowerCase());
                        if (xm == null) {
                            baoDuongModel.themXeMayMoi(new XeMay(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText().toLowerCase(),
                                    baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().getText(),
                                    kh.getID(),
                                    baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog().getSelectedItem().toString()));
                        } else if (xm.getIdKhach() == 0) {
                            xm.setIdKhach(kh.getID());
                            baoDuongModel.capNhatthongTinXeMay(xm);
                        }
                        baoduongPanel.getBienSoXeTF().setText(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText());
                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().setText("");
                        baoduongPanel.getThemKhachHangMoiDailog().setVisible(false);
                        baoduongPanel.getThemKhachHangMoiBT().setVisible(false);
                    }
                } else {
                    if (!Stringlib.kiemTraChuoiBienSoXe(baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().getText())) {
                        baoduongPanel.getBienSoKhongHopLe().setVisible(true);
                    }
                    if (!Stringlib.kiemTraSDT(baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().getText())) {
                        baoduongPanel.getSDTKhongHopLE().setVisible(true);
                    }
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
                            dtm1.addRow(new Object[]{vt.elementAt(0), vt.elementAt(1), 1, vt.elementAt(2), 0, null, vt.elementAt(5)});
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
                            dtm1.addRow(new Object[]{vt.elementAt(0), vt.elementAt(1), 1, vt.elementAt(2), null, vt.elementAt(4)});
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
                    ArrayList<Entities.DichVuBaoDuong> ar = baoDuongModel.layDanhSachDichVuBaoDuongMoiNhat();
                    String key = baoduongPanel.getTimKiemDichVuBaoDuongTF_ThemDichVuBaoDuongDailog().getText().trim();
                    DefaultTableModel dm = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
                    if (!key.equalsIgnoreCase("")) {
                        dm.setNumRows(0);
                        for (Entities.DichVuBaoDuong dv : ar) {
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
                try {
                    luuDonBaoDuong(false);
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        baoduongPanel.getXuatDonBaoDuongBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    luuDonBaoDuong(true);
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        baoduongPanel.getHuyDonBaoDuongBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                baoduongPanel.getXacNhanHuyHoaDon().setVisible(true);
            }
        });

        baoduongPanel.getXacNhanCoHuyHoaDonBT().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (donBaoDuongCurrent != null) {
                        baoDuongModel.xoaDonBaoDuong(donBaoDuongCurrent.getId());
                    }
                    loadDanhSachDonBaoDuongChuaHoanThanh(true);
                    taiLaiGiaoDien();
                    baoduongPanel.getXacNhanHuyHoaDon().setVisible(false);
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        baoduongPanel.getXacNhanKhongHuyHoaDonBT().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baoduongPanel.getXacNhanHuyHoaDon().setVisible(false);
            }
        });

        baoduongPanel.getXuatHoaDonButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    taiLaiGiaoDien();
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        baoduongPanel.getThemDichVuBaoDuongBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel md = (DefaultTableModel) baoduongPanel.getDanhSachDichVubaoDuongTB_ThemDichVuBaoDuongiDailog().getModel();
                Vector<Vector> data = md.getDataVector();
                int i = 0;
                for (Vector c : data) {

                    md.setValueAt(false, i, 4);
                    i++;
                }
                baoduongPanel.getThemDichVuBaoDuongDailog().setVisible(true);
            }
        });

        baoduongPanel.getThemLinhKienThayTheBT().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel md = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB_ThemLinhKienThayTheDailog().getModel();
                Vector<Vector> data = md.getDataVector();
                int i = 0;
                for (Vector c : data) {

                    md.setValueAt(false, i, 6);
                    i++;
                }
                baoduongPanel.getThemLinhKienThayTheDialog().setVisible(true);
            }
        });

        danhSachDonBaoDuongPanel.getjButton_ChonDonBaoDuong().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow() > -1) {
                    voHieuHoaChucNang(true);
                    String id = danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getValueAt(danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow(), 0).toString();
                    try {
                        loadDonBaoDuong(id);
                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    danhSachDonBaoDuongPanel.getCardLayoutContainer().next(danhSachDonBaoDuongPanel.getContainer());
                }
            }
        });

        baoduongPanel.getjButton_QuayLayDanhSachDonBaoDuong().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadDanhSachDonBaoDuongChuaHoanThanh(danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().isSelected());
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
                baoduongPanel.getcardLayoutContainer().next(baoduongPanel.getContainer());
            }
        });

        danhSachDonBaoDuongPanel.getjButton_TaoHoaDonMoi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    taiLaiGiaoDien();
                    baoduongPanel.getBienSoXeTF().setEditable(true);
                    baoduongPanel.getcardLayoutContainer().next(baoduongPanel.getContainer());
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    if (danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().isSelected()) {
                        danhSachDonBaoDuongPanel.getjButton_XacNhanHoanThanhButton().setVisible(false);
                        danhSachDonBaoDuongPanel.getjButton_SuaDonBaoDuong().setVisible(false);
                        danhSachDonBaoDuongPanel.getjButton_XoaDonBaoDuong().setVisible(false);
                        danhSachDonBaoDuongPanel.getjButton_XemDonBaoDuong().setVisible(true);
                        loadDanhSachDonBaoDuongChuaHoanThanh(true);

                    } else {
                        danhSachDonBaoDuongPanel.getjButton_XacNhanHoanThanhButton().setVisible(true);
                        danhSachDonBaoDuongPanel.getjButton_SuaDonBaoDuong().setVisible(true);
                        danhSachDonBaoDuongPanel.getjButton_XoaDonBaoDuong().setVisible(true);
                        danhSachDonBaoDuongPanel.getjButton_XemDonBaoDuong().setVisible(false);
                        loadDanhSachDonBaoDuongChuaHoanThanh(false);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        danhSachDonBaoDuongPanel.getjButton_XacNhanHoanThanhButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getValueAt(danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow(), 0).toString());
                try {
                    baoDuongModel.capNhatTrangThaiDonBaoDuong(id, true);
                    loadDanhSachDonBaoDuongChuaHoanThanh(danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().isSelected());
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        danhSachDonBaoDuongPanel.getjButton_XemDonBaoDuong().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow() > -1) {
                    voHieuHoaChucNang(false);
                    String id = danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getValueAt(danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow(), 0).toString();
                    try {
                        loadDonBaoDuong(id);
                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    danhSachDonBaoDuongPanel.getCardLayoutContainer().next(danhSachDonBaoDuongPanel.getContainer());
                }

            }
        });

        danhSachDonBaoDuongPanel.getjButton_XoaDonBaoDuong().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int srow = danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow();
                if (srow > -1) {
                    danhSachDonBaoDuongPanel.getjDialog_XacNhanHuyDonBaoDuongTrongDanhSach().setVisible(true);
                }
            }
        });

        danhSachDonBaoDuongPanel.getjButton_CoHuyHoaDonTrongDanhSach().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int srow = danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow();
                if (srow > -1) {
                    int id = (int) danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getValueAt(srow, 0);
                    try {
                        baoDuongModel.xoaDonBaoDuong(id);
                        loadDanhSachDonBaoDuongChuaHoanThanh(danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().isSelected());
                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                danhSachDonBaoDuongPanel.getjDialog_XacNhanHuyDonBaoDuongTrongDanhSach().setVisible(false);

            }
        });

        danhSachDonBaoDuongPanel.getjButton_KhongHiuyHoaDonTRongDanhSach().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                danhSachDonBaoDuongPanel.getjDialog_XacNhanHuyDonBaoDuongTrongDanhSach().setVisible(false);
            }

        });

        danhSachDonBaoDuongPanel.getjButton_XuatHoaDon().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int srow = danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getSelectedRow();
                if (srow > -1) {
                    int id = (int) danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getValueAt(srow, 0);
                    try {
                        loadDonBaoDuong(id+"");
                        hienThiHoaDon();
                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    private void voHieuHoaChucNang(boolean kichHoat) {
        baoduongPanel.getThemKhachHangMoiBT().setVisible(kichHoat);
        baoduongPanel.getThemDichVuBaoDuongBT().setVisible(kichHoat);
        baoduongPanel.getThemLinhKienThayTheBT().setVisible(kichHoat);
        baoduongPanel.getHuyDonBaoDuongBT().setVisible(kichHoat);
        baoduongPanel.getLuuDonBaoDuongBT().setVisible(kichHoat);
        baoduongPanel.getXoaDichVuBaoDuongBT().setVisible(kichHoat);
        baoduongPanel.getXoaLinhKienThayTheBT().setVisible(kichHoat);
        baoduongPanel.getTrangThaiDonBaoDuongCheckBox().setVisible(kichHoat);
        baoduongPanel.getjLabel18().setVisible(kichHoat);
        baoduongPanel.getDanhSachtrangThaiPhuTungTiepNhan().setEnabled(kichHoat);
        baoduongPanel.getTrangThaiDonBaoDuongCheckBox().setEnabled(kichHoat);
        baoduongPanel.getBienSoXeTF().setEditable(kichHoat);
        baoduongPanel.getXuatDonBaoDuongBT().setVisible(kichHoat);

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

    private void cauHinhCacItem() throws SQLException, ParseException {
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
        baoduongPanel.getNgayThangNamTF().setText(Stringlib.dinhDangNgayHienThitu_yyyyMMdd_Thanh_ddMMyyyy(LocalDate.now().toString()));
        danhSachDonBaoDuongPanel.getjCheckBox_CheDoHienThiDanhSachHoaDon().setSelected(false);
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

    private void luuThongTinXe() throws SQLException {
        String bienSo = baoduongPanel.getBienSoXeTF().getText().toLowerCase();
        String loaiXe = baoduongPanel.getLoaiXeComboBox().getSelectedItem().toString();
        if (baoDuongModel.timXeMayTheoBienSo(bienSo) == null) {
            baoDuongModel.themXeMayMoi(bienSo, loaiXe);
        }
    }

    private void luuThongTinDichVuBaoDuong(int idDonBaoDuong) throws SQLException {
        DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
        Vector<Vector> dataDichVuBaoDuong = dtm.getDataVector();
        for (Vector data : dataDichVuBaoDuong) {
            String idNV = "1";
            if (data.get(5) != null) {
                for (String str : data.get(5).toString().split("\\(")) {
                    idNV = str.substring(0, str.length() - 1);
                }
            }

            baoDuongModel.themChiTietDonBaoDuong(
                    Integer.parseInt(data.get(0).toString()),
                    idDonBaoDuong,
                    Integer.parseInt(data.get(2).toString()),
                    Long.parseLong(data.get(4).toString()),
                    Integer.parseInt(idNV),
                    data.get(6).toString());
        }
    }

    private void luuThongTinThayTheLinhKien(int idDonBaoDuong) throws SQLException {
        DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();
        Vector<Vector> dataLinhKien = dtm.getDataVector();
        for (Vector data : dataLinhKien) {
            int idLinhKien = Integer.parseInt(data.get(0).toString());
            String ngayNhap = data.get(5).toString();
            String ghiChu = data.get(4) == null ? "" : data.get(4).toString();
            int soLuong = Integer.parseInt(data.get(2).toString());

            baoDuongModel.themChiTietThayTheLinhKien(idDonBaoDuong, idLinhKien, ngayNhap, ghiChu, soLuong);
            baoDuongModel.capNhatSoLuongLinhKien(idLinhKien, soLuong);
        }
    }

    private void luuThongTinTrangThaiNhanXe(int idDonBaoDuong) throws SQLException {
        DefaultTableModel dtm = (DefaultTableModel) baoduongPanel.getDanhSachtrangThaiPhuTungTiepNhan().getModel();
        Vector<Vector> dataLinhKien = dtm.getDataVector();
        for (Vector data : dataLinhKien) {
            for (int i = 1; i < data.size(); i++) {
                Object isCheck = data.get(i);
                if (isCheck != null && (boolean) isCheck) {
                    baoDuongModel.themChiTietTrangThaiKhiTiepNhanXe(idDonBaoDuong, baoDuongModel.timIDPhuTungKiemTra(data.get(0).toString()), i);
                }
            }
        }
    }

    public boolean kiemTraThongTinTruocKhiLuu() {
        if (!Stringlib.kiemTraChuoiBienSoXe(baoduongPanel.getBienSoXeTF().getText())) {
            baoduongPanel.getBienSoKhongHopLeMainPanel().setVisible(true);
            return true;
        }
        if (baoduongPanel.getBienSoXeTF().getText().equalsIgnoreCase("")) {
            baoduongPanel.getBienSoKhongHopLeMainPanel().setVisible(true);
            return true;
        }
        if (baoduongPanel.getDanhSachDichVuBaoDuongTB().getRowCount() == 0 && baoduongPanel.getDanhSachLinhKienThayTheTB().getRowCount() == 0) {
            return true;
        }

        return false;
    }

    private int layIDNhanVienLapDon() {
        return 1;
    }

    private void hienThiHoaDon() throws ParseException {
        baoduongPanel.getXuatHoaDonTextArea().setText("");
        baoduongPanel.getXuatHoaDonTextArea().append(Config.TenCuaHang + "\n\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Ngày xuất hóa đơn: " + Stringlib.dinhDangNgayHienThitu_yyyyMMdd_Thanh_ddMMyyyy(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString()) + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Mã hóa đơn : " + donBaoDuongCurrent.getId() + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("------------------------------------------------------" + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Tên khách hàng : " + baoduongPanel.getTenKhachHangTF().getText() + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Biển số xe : " + baoduongPanel.getBienSoXeTF().getText() + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("\n");

        baoduongPanel.getXuatHoaDonTextArea().append("Dịch vụ bảo dưỡng : " + "\n");
        DefaultTableModel dichVuTM = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
        Vector<Vector> dichVuData = dichVuTM.getDataVector();
        if (dichVuData != null) {
            for (Vector row : dichVuData) {
                String ten = Stringlib.suaTenDichVuKhiXuatHoaDon(row.get(1).toString());
                baoduongPanel.getXuatHoaDonTextArea().append(
                        "Mã: " + Integer.parseInt(row.get(0).toString()) + " "
                        + ten + ", "
                        + "SL:" + Integer.parseInt(row.get(2).toString()) + ", "
                        + "G/Đ:" + Stringlib.dinhDangTienHienThi((long) Integer.parseInt(row.get(3).toString())) + ", "
                        + "Phụ phí:" + Stringlib.dinhDangTienHienThi((long) Integer.parseInt(row.get(4).toString())) + ", "
                        + "\n");
            }
        }
        baoduongPanel.getXuatHoaDonTextArea().append("> Tổng : " + baoduongPanel.getThanhTienDichVuBaoDuongTF().getText().trim() + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("\n");

        baoduongPanel.getXuatHoaDonTextArea().append("Linh kiện thay thế : " + "\n");
        DefaultTableModel lichKienTM = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();
        Vector<Vector> linhKienData = dichVuTM.getDataVector();
        if (linhKienData != null) {
            for (Vector row : linhKienData) {
                String ten = Stringlib.suaTenDichVuKhiXuatHoaDon(row.get(1).toString());
                baoduongPanel.getXuatHoaDonTextArea().append(
                        "mã: " + Integer.parseInt(row.get(0).toString()) + " "
                        + ten + ", "
                        + "SL:" + Integer.parseInt(row.get(2).toString()) + ", "
                        + "G/Đ:" + Stringlib.dinhDangTienHienThi((long) Integer.parseInt(row.get(3).toString())) + ", "
                        + "\n");
            }
        }
        baoduongPanel.getXuatHoaDonTextArea().append("> Tổng : " + Stringlib.dinhDangTienHienThi(baoduongPanel.getThanhTienThayTheLinhKienTF().getText().trim()) + "\n");

        baoduongPanel.getXuatHoaDonTextArea().append("------------------------------------------------------" + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Tổng chí phí : " + Stringlib.dinhDangTienHienThi(baoduongPanel.getTongChiPhiTF().getText()) + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Thuế VAT : " + Stringlib.dinhDangTienHienThi((long) (Long.parseLong(baoduongPanel.getTongChiPhiTF().getText()) * Config.VAT)) + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Thành tiền : " + Stringlib.dinhDangTienHienThi(((Long.parseLong(baoduongPanel.getTongChiPhiTF().getText()))
                + (long) (Long.parseLong(baoduongPanel.getTongChiPhiTF().getText()) * Config.VAT))) + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("------------------------------------------------------" + "\n");
        baoduongPanel.getXuatHoaDonTextArea().append("Cảm ơn quý khách" + "\n");
        baoduongPanel.getXuatHoaDon().setVisible(true);
    }

    public void taiLaiGiaoDien() throws SQLException, ParseException {
        donBaoDuongCurrent = null;
        baoduongPanel.getBienSoXeTF().setText("");
        baoduongPanel.getThanhTienDichVuBaoDuongTF().setText("0");
        baoduongPanel.getThanhTienThayTheLinhKienTF().setText("0");
        baoduongPanel.getBienSoXeMayTF_ThemKhachHangMoiDailog().setText("");
        baoduongPanel.getTenXeMayTF_ThemKhachHangMoiDailog().setText("");
        baoduongPanel.getTenKhachHangTF_ThemKhachHangMoiDailog().setText("");
        baoduongPanel.getSoDienThoaiTF_ThemKhachHangMoiDialog().setText("");
        baoduongPanel.getNgayThangNamTF().setText(Stringlib.dinhDangNgayHienThitu_yyyyMMdd_Thanh_ddMMyyyy(LocalDate.now().toString()));
        baoduongPanel.getTenKhachHangTF().setText("");
        baoduongPanel.getSoDienThoaiTF().setText("");
        baoduongPanel.getTongChiPhiTF().setText("0");
        baoduongPanel.getTongThanhToanTF().setText("0");
        baoduongPanel.getThueVATTF().setText("0");
        baoduongPanel.getTrangThaiDonBaoDuongCheckBox().setSelected(false);
        ((DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel()).setNumRows(0);
        ((DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel()).setNumRows(0);
        baoduongPanel.getLoaiXeComboBox().setSelectedIndex(0);
        baoduongPanel.getLoaiXeComboBox_ThemKhachHangMoiDailog().setSelectedIndex(0);
        baoduongPanel.getSDTKhongHopLE().setVisible(false);
        baoduongPanel.getBienSoKhongHopLe().setVisible(false);
        baoduongPanel.getBienSoKhongHopLeMainPanel().setVisible(false);
        baoduongPanel.getLoaiXeComboBox().setEnabled(true);
        baoduongPanel.getThemKhachHangMoiBT().setVisible(true);
        baoduongPanel.getXuatHoaDon().setVisible(false);
        loadDanhSachDichVuBaoDuonginDialog();
        loadDanhSachLinhKieninDialog();
        loadDanhSachTrangThaiPhuTungTiepNhan();
    }

    public void loadDonBaoDuong(String iđonBaoDuong) throws SQLException, ParseException {
        donBaoDuongCurrent = baoDuongModel.timDonBaoDuongTheoID(iđonBaoDuong);
        loadXeMay(donBaoDuongCurrent.getBienSo());
        loadDanhSachDichVuBaoDuongTrongHoaDon(donBaoDuongCurrent.getId());
        loadDanhSachLinhKienTrongHoaDon(donBaoDuongCurrent.getId());
        loadKhachHangTrongHoaDon(donBaoDuongCurrent.getBienSo());
        loadDanhSachTrangThaiPhuTungTiepNhanTrongHoaDon(donBaoDuongCurrent.getId());
        loadThongTin(donBaoDuongCurrent);
        capNhatNhatTinhTienTF();
    }

    private void loadXeMay(String bienSo) throws SQLException {
        XeMay xm = baoDuongModel.timXeMayTheoBienSo(bienSo);
        if (xm == null) {
            System.err.println("Loi tim xe may");
        } else {
            baoduongPanel.getBienSoXeTF().setText(xm.getBienSo());
            baoduongPanel.getLoaiXeComboBox().setSelectedItem(xm.getLoaiXe());
            baoduongPanel.getLoaiXeComboBox().setEnabled(false);
            baoduongPanel.getBienSoXeTF().setEditable(false);
        }
    }

    private void loadDanhSachDichVuBaoDuongTrongHoaDon(int id) throws SQLException {
        DefaultTableModel dm = (DefaultTableModel) baoduongPanel.getDanhSachDichVuBaoDuongTB().getModel();
        dm.setNumRows(0);
        ArrayList<DichVuBaoDuong> ds = baoDuongModel.layDanhSachDichVuBaoDuongTrongHoaDon(id);

        for (DichVuBaoDuong d : ds) {
            dm.addRow(new Object[]{d.getId(),
                d.getTenDichVuBaoDuong(),
                d.getSoLuongTrongHoaDon(),
                d.getPhi(),
                d.getPhuPhi(),
                d.getNv().getHoTen() + " (" + d.getNv().getID() + ")",
                d.getNgayCapNhat()});
        }
    }

    private void loadDanhSachLinhKienTrongHoaDon(int id) throws SQLException {
        DefaultTableModel dm = (DefaultTableModel) baoduongPanel.getDanhSachLinhKienThayTheTB().getModel();
        dm.setNumRows(0);
        ArrayList<LinhKien> ds = baoDuongModel.layDanhSachLinhKienTrongHoaDon(id);
        for (LinhKien d : ds) {
            dm.addRow(new Object[]{d.getID(),
                d.getTenLinhKien(),
                String.valueOf(d.getSoLuongTrongDonBaoDuong()),
                d.getGia(),
                d.getGhiChuTrongDonBaoDuong(),
                d.getNgayNhap()});
        }
    }

    private void loadKhachHangTrongHoaDon(String bienSo) throws SQLException {
        try {
            XeMay xm = baoDuongModel.timXeMayTheoBienSo(bienSo);
            if (xm == null || xm.getIdKhach() == 0) {
                baoduongPanel.getTenKhachHangTF().setText("Khách vãng lai");
                baoduongPanel.getSoDienThoaiTF().setText("");
                baoduongPanel.getLoaiXeComboBox().setEnabled(true);
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

    private void loadDanhSachTrangThaiPhuTungTiepNhanTrongHoaDon(int id) throws SQLException {
        ArrayList<PhuTung> arl = baoDuongModel.layDanhSachPhuTungTrongHoaDon(id);
        DefaultTableModel dftb = (DefaultTableModel) baoduongPanel.getDanhSachtrangThaiPhuTungTiepNhan().getModel();
        dftb.setNumRows(0);
        arl.forEach(pt -> {
            boolean[] A = new boolean[]{true, false, false, false, false};
            for (String c : pt.getTrangThai()) {
                A[Integer.parseInt(c) - 1] = true;
            }
            dftb.addRow(new Object[]{pt.getTenPhuTung(), A[0], A[1], A[2], A[3], A[4]});
        });
    }

    private void loadThongTin(DonBaoDuong d) throws ParseException {
        baoduongPanel.getNgayThangNamTF().setText(Stringlib.dinhDangNgayHienThitu_yyyyMMdd_Thanh_ddMMyyyy(d.getNgayBatDau()));
        baoduongPanel.getTongThanhToanTF().setText(String.valueOf(d.getTongTien()));
    }

    private void loadDanhSachDonBaoDuongChuaHoanThanh(boolean mode) throws SQLException {
        ArrayList<DonBaoDuong> arl = baoDuongModel.layDanhSachDonBaoDuong(mode);
        DefaultTableModel dm = (DefaultTableModel) danhSachDonBaoDuongPanel.getjTable_DanhSachDonBaoDuong().getModel();
        dm.setNumRows(0);
        for (DonBaoDuong d : arl) {
            String t = "Đang sửa";
            if (d.getTrangThai().equalsIgnoreCase("1")) {
                t = "Đã sửa xong";
            }
            dm.addRow(new Object[]{d.getId(), d.getBienSo(), d.getTenKhachHang(), d.getSDTKhachhang(), Stringlib.dinhDangTienHienThi(d.getTongTien()), t});
        }
    }

    private DonBaoDuong taoHoaDonTam() {
        String ngayHoanThanh = "";
        String trangThai = "0";
        if (baoduongPanel.getTrangThaiDonBaoDuongCheckBox().isSelected()) {
            ngayHoanThanh = LocalDate.now().toString();
            trangThai = "1";
        }

        int idNhanVienLapDon = layIDNhanVienLapDon();
        long tongTien = Long.parseLong(baoduongPanel.getTongThanhToanTF().getText().toString());

        return new DonBaoDuong(
                0,
                baoduongPanel.getBienSoXeTF().getText().trim().toLowerCase(),
                baoduongPanel.getNgayThangNamTF().getText().trim(),
                ngayHoanThanh,
                trangThai,
                idNhanVienLapDon,
                tongTien);
    }

    private void luuDonBaoDuong(boolean hienThiHoaDon) throws SQLException, ParseException {
        if (kiemTraThongTinTruocKhiLuu()) {
            baoduongPanel.getThongBaoDialog().setVisible(true);
        } else {
            if (donBaoDuongCurrent == null || donBaoDuongCurrent.getId() == 0) {

                donBaoDuongCurrent = taoHoaDonTam();

                try {
                    luuThongTinXe();
                    donBaoDuongCurrent = baoDuongModel.themDonBaoDuong(donBaoDuongCurrent);
                } catch (SQLException ex) {
                    Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (donBaoDuongCurrent == null || donBaoDuongCurrent.getId() == 0) {
                    System.err.println("Lỗi Nhap Don bao dưỡng");
                } else {
                    try {
                        luuThongTinXe();
                        luuThongTinDichVuBaoDuong(donBaoDuongCurrent.getId());
                        luuThongTinThayTheLinhKien(donBaoDuongCurrent.getId());
                        luuThongTinTrangThaiNhanXe(donBaoDuongCurrent.getId());

                    } catch (SQLException ex) {
                        Logger.getLogger(DonBaoDuongController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                capNhatDonBaoDuong();
            }

            if (hienThiHoaDon) {
                hienThiHoaDon();
            }
        }
    }

    private void xoaThongTinVeChiTietDonBaoDuong(int id) throws SQLException {
        baoDuongModel.xoaToanBoChiTietDonBaoDuongTheoHoaDon(id);
        baoDuongModel.xoaToanBoChiTietThayTheLinhKienTheoHoaDon(id);
        baoDuongModel.xoaToanBoChiTietTrangThaiPhuTungKiemTraTheoHoaDon(id);
    }

    private void capNhatDonBaoDuong() throws SQLException {
        xoaThongTinVeChiTietDonBaoDuong(donBaoDuongCurrent.getId());
        luuThongTinDichVuBaoDuong(donBaoDuongCurrent.getId());
        luuThongTinThayTheLinhKien(donBaoDuongCurrent.getId());
        luuThongTinTrangThaiNhanXe(donBaoDuongCurrent.getId());
        DonBaoDuong temp = taoHoaDonTam();
        temp.setId(donBaoDuongCurrent.getId());
        baoDuongModel.capNhatDonBaoDuong(temp);
    }
}
