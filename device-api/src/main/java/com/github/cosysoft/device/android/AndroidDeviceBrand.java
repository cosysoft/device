package com.github.cosysoft.device.android;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltyao
 */
public final class AndroidDeviceBrand {

    private static final List<AndroidDeviceBrand> brands = new ArrayList<>();

    public static final AndroidDeviceBrand XIAOMI_MI_3W = createInstance(
            "xiaomi", "mi_3w");
    public static final AndroidDeviceBrand XIAOMI_MI_2 = createInstance(
            "xiaomi", "mi_2");
    public static final AndroidDeviceBrand XIAOMI_MI_3 = createInstance(
            "xiaomi", "mi_3");
    public static final AndroidDeviceBrand OPPO_X9007 = createInstance("oppo",
            "x9007");
    public static final AndroidDeviceBrand MEIZU_M355 = createInstance("meizu",
            "m355");
    public static final AndroidDeviceBrand HTC_M8ST = createInstance("htc",
            "htc_m8st");

    public static final AndroidDeviceBrand EMPTY = createInstance("", "");

    private String manufacture;
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    @Override
    public String toString() {
        return "AndroidDeviceBrand [manufacture=" + manufacture + ", model="
                + model + "]";
    }

    private AndroidDeviceBrand(String manufacture, String model) {
        this.manufacture = manufacture;
        this.model = model;
    }

    public boolean isXiaoMi() {
        return this.equals(AndroidDeviceBrand.XIAOMI_MI_2)
                || this.equals(AndroidDeviceBrand.XIAOMI_MI_3)
                || this.equals(AndroidDeviceBrand.XIAOMI_MI_3W);
    }

    private static AndroidDeviceBrand createInstance(String manufacture,
                                                     String model) {
        AndroidDeviceBrand brand = new AndroidDeviceBrand(manufacture, model);
        brands.add(brand);
        return brand;

    }

    public static AndroidDeviceBrand from(String manufacture, String model) {
        for (AndroidDeviceBrand brand : brands) {
            if (brand.getManufacture().equals(manufacture)
                    && brand.getModel().equals(model)) {
                return brand;
            }
        }
        return EMPTY;
    }
}
