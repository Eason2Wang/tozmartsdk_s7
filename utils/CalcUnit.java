package com.tozmart.tozisdk.utils;

/**
 * Created by wangyisong on 28/1/16.
 */
public class CalcUnit {

    public static final float FT2CM = 30.48f;
    public static final float INCH2CM = 2.54f;
    public static final float KG2LB = 2.205f;
    public static final float FT2INCH = 12f;

    public static float[] INCH_CAL
            = new float[]{0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f, 0.875f};
    public static String[] INCH_MEAS
            = new String[]{"(1/8)", "(1/4)", "(3/8)", "(1/2)", "(5/8)", "(3/4)", "(7/8)"};

    public static float ft2cm(String ftin) {
        String[] ft_in = ftin.split("_");
        int ft = Integer.parseInt(ft_in[0]);
        int in = Integer.parseInt(ft_in[1]);
        float cm = (float) Math.round(ft * FT2CM + in * INCH2CM);
        return cm;
    }

    public static float fts2cm(float ft) {
        float cm = (float) Math.round(ft * FT2CM);
        return cm;
    }

    public static float cm2fts(float cm) {
        float ft = Math.round(cm / (FT2CM / 10f)) / 10.f;
        return ft;
    }

    public static String ft2ftin(float fts) {
        int ft = (int)Math.floor(fts);
        int inch = Math.round(fts % 1 * FT2INCH);
        return ft + "'" + inch + "''";
    }

    public static String cm2ft(String cm) {
        float cm_f = Float.parseFloat(cm);
        float in_all = cm_f / INCH2CM;
        int ft = (int) Math.floor(in_all / FT2INCH);
        int in = Math.round(in_all % FT2INCH);
        return String.valueOf(ft) + "'" + String.valueOf(in) + "''";
    }

    public static String cm2ft(float cm) {
        float in_all = cm / INCH2CM;
        int ft = (int) Math.floor(in_all / FT2INCH);
        int in = Math.round(in_all % FT2INCH);
        return String.valueOf(ft) + "'" + String.valueOf(in) + "''";
    }

    public static String cm_ft(String cm) {
        float cm_f = Float.parseFloat(cm);
        float in_all = cm_f / INCH2CM;
        int ft = (int) Math.floor(in_all / FT2INCH);
        int in = Math.round(in_all % FT2INCH);
        return String.valueOf(ft) + "_" + String.valueOf(in);
    }

    public static String cm2inch(float cm) {
        float inch = cm / INCH2CM;
        float inchDeci = Math.round((inch - (int)inch) * 1000) / 1000.f;
        if (inchDeci == 0) {
            return (int)inch + "";
        }
        float minDis = Float.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < INCH_CAL.length; i++) {
            if (Math.abs(inchDeci - INCH_CAL[i]) < minDis) {
                minDis = Math.abs(inchDeci - INCH_CAL[i]);
                minIndex = i;
            }
        }

        return minIndex == -1 ? (int)inch + INCH_MEAS[0] : (int)inch + INCH_MEAS[minIndex];
    }
}
