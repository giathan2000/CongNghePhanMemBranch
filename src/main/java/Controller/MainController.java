/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.DonBaoDuong.DonBaoDuongContainerPanel;
import View.*;
import Model.*;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author hesac
 */
public class MainController {

    MainModel model;
    MainView view;
    DonBaoDuongController donBaoDuongController;
    DichVuBaoDuongController dichVuBaoDuongController;
    DonBaoDuongContainerPanel baoDuongContainerPanel;

    public MainController() {
    }

    public MainController(MainModel model, MainView view) throws SQLException, ParseException {
        this.model = model;
        this.view = view;
        view.setVisible(true);
        init();
    }

    private void init() throws SQLException, ParseException {
        //donBaoDuongController = new DonBaoDuongController((DonBaoDuongContainerPanel) view.getDonBaoDuongPanel(), model.getDonBaoDuongModel());
        dichVuBaoDuongController = new DichVuBaoDuongController((DichVubaoDuongPanel) view.getdichVuBaoDuongPanel(), model.getDonBaoDuongModel());
    }
}
