/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author hesac
 */
public class Stringlib {
    public static float isLikeString(String key, String content){
        float cf = 0;
        content = content.toLowerCase().replaceAll("\\s\\s+", " ").trim();
        key = key.toLowerCase().replaceAll("\\s\\s+", " ").trim();
        float icr = (float) (1.0/((float)key.split("[ ]").length));
        for (String st : key.split(" ")) {
            if( content.indexOf(st)>=0){
                cf+=icr;
            }
        }
        return cf;
    }
}
