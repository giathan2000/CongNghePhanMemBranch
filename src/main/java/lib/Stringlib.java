/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hesac
 */
public class Stringlib {

    public static float isLikeString(String key, String content) {
        float cf = 0;
        content = content.toLowerCase().replaceAll("\\s\\s+", " ").trim();
        key = key.toLowerCase().replaceAll("\\s\\s+", " ").trim();
        float icr = (float) (1.0 / ((float) key.split("[ ]").length));
        for (String st : key.split(" ")) {
            if (content.indexOf(st) >= 0) {
                cf += icr;
            }
        }
        return cf;
    }

    public static boolean kiemTraChuoiBienSoXe(String st) {
        return st.matches("^[0-9]{2}[a-zA-Z]{1}[0-9]{4,7}$");
    }
    
    public static boolean kiemTraSDT(String st) {
        return st.matches("^[0-9]{6,11}$");
    }

    public static String suaTenDichVuKhiXuatHoaDon(String ten) {
        int max = 30;
        if (ten.length() <= max) {
            return ten;
        } else {
            return ten.substring(0, max - 3) + "...";
        }
    }

    public static String dinhDangTienHienThi(String st) {
        if (st.length() <= 3) {
            return st;
        }
        String rs = "";
        for (int i = 0; i < st.length(); i++) {
            if (i % 3 == 0 && i != 0) {
                rs += '.';
            }
            rs += st.charAt(st.length() - i - 1);
        }

        StringBuilder str = new StringBuilder(rs);
        return str.reverse().toString();
    }
    
    public static String dinhDangTienHienThi(Long n){
        String st = Long.toString(n);
        return dinhDangTienHienThi(st);
    }
    
    public static String dinhDangNgayHienThitu_yyyyMMdd_Thanh_ddMMyyyy(String date) {
        Date d;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException ex) {
            return date;
        }
        return new SimpleDateFormat("dd/MM/yyyy").format(d);
    }
    
    public static String dinhDangNgayHienThitu_ddMMyyyy_Thanh_yyyyMMdd(String date){
        Date d;
        try {
            d = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException ex) {
            return date;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
        
    }
}
