package com.example.mesibajk;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfigUtilJava extends OrmLiteConfigUtil {
    public static void main(String[] args) throws SQLException, IOException {
        Class<?>[] classes = new Class[]{Bike.class, Ride.class};
        writeConfigFile(new File("C:/Users/Nejc/AndroidStudioProjects/MesiBajk/app/src/main/res/raw/ormlite_config.txt"), classes);
    }
}