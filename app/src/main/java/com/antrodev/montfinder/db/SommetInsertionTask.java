package com.antrodev.montfinder.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Leonardo on 12/01/2016.
 */
public class SommetInsertionTask extends AsyncTask<SommetDatabaseHandler,String,String> {
    public final static String MESSAGE_TYPE = "DBstatus";
    final static String KEY_STATUS = "status_ins";
    private Context context;
    private SommetDatabaseHandler db;

    public SommetInsertionTask(Context context){
        super();
        this.context=context;
    }

    @Override
    protected String doInBackground(SommetDatabaseHandler... params) {
        db=params[0];
        String[] queries=new String[]{
                "INSERT INTO sommets VALUES (1116621810, '6.84486789904718', '47.8224908948974', 'Ballon d''Alsace', 1247);",
                "INSERT INTO sommets VALUES (1661447505, '6.89760629903985', '47.8895027948947', 'Col de Bussang', 731);",
                "INSERT INTO sommets VALUES (1662823209, '6.91711559903713', '47.9227979948934', 'Col d''Oderen', 884);",
                "INSERT INTO sommets VALUES (1708009463, '6.92187929903647', '47.9388803948927', 'Les Winterges', 1049);",
                "INSERT INTO sommets VALUES (1762485578, '6.92214789903643', '47.7727801948995', 'Le Baerenkopf', 1074);",
                "INSERT INTO sommets VALUES (262504048, '6.92533359903599', '47.9594413948919', 'Le Grand Ventron', 1204);",
                "INSERT INTO sommets VALUES (1708009464, '6.92569139903594', '47.9502369948923', 'Petit Ventron', 1155);",
                "INSERT INTO sommets VALUES (670516109, '6.93129899903515', '47.9267717948932', 'Felsachkopf', 1108);",
                "INSERT INTO sommets VALUES (670516525, '6.97825919902862', '47.9526210948921', 'Griebkopf', 979);",
                "INSERT INTO sommets VALUES (415760096, '6.98064249902829', '47.9938686948905', 'Batteriekopf', 1311);",
                "INSERT INTO sommets VALUES (415760104, '6.98099649902824', '48.0007430948902', 'Rothenbachkopf', 1316);",
                "INSERT INTO sommets VALUES (1422805899, '6.98247549902803', '47.9290832948931', 'Gommkopf', 842);",
                "INSERT INTO sommets VALUES (415760121, '6.982689099028', '48.0097701948898', 'Rainkopf', 1305);",
                "INSERT INTO sommets VALUES (415760066, '6.98971059902703', '47.9751878948912', 'Le Schweisel', 1271);",
                "INSERT INTO sommets VALUES (1738882536, '7.00102799902545', '48.0213749948894', 'Kastelberg', 1350);",
                "INSERT INTO sommets VALUES (893079333, '7.00894299902435', '47.8203699948975', 'Rossberg', 1191);",
                "INSERT INTO sommets VALUES (670516121, '7.0157285990234', '47.9267147948932', 'Trehkopf', 1266);",
                "INSERT INTO sommets VALUES (190684419, '7.01628449902332', '48.0376222948887', 'Hohneck', 1363);",
                "INSERT INTO sommets VALUES (1659537303, '7.02254209902245', '48.0637777948877', 'Col de la Schlucht', 1139);",
                "INSERT INTO sommets VALUES (670516130, '7.0243840990222', '47.9262036948932', 'Jungfrauenkopf', 1267);",
                "INSERT INTO sommets VALUES (2515145576, '7.02697749902184', '47.9475971948924', 'Breitfirst', 1280);",
                "INSERT INTO sommets VALUES (634567985, '7.03879699902019', '47.9641508948917', 'Nonselkopf', 1257);",
                "INSERT INTO sommets VALUES (634567965, '7.0394256990201', '47.956195994892', 'Lauchenkopf', 1314);",
                "INSERT INTO sommets VALUES (938282869, '6.7736388990571', '47.7672319948997', 'Planche des Belles Filles', 1148);",
                "INSERT INTO sommets VALUES (303919134, '6.01820239916226', '46.525902594954', 'Roche au Dade', 918);",
                "INSERT INTO sommets VALUES (306394529, '6.09955459915094', '46.4256491949587', 'La Dôle', 1678);"
        };

        for(int i=0;i<queries.length;i++){
            db.addSommet(queries[i]);
        }

        return "Fini";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SommetDatabaseHandler.initialized=true;
        db.savePreferences();

        Intent i=new Intent(MESSAGE_TYPE);
        i.putExtra(KEY_STATUS, true);
        context.sendBroadcast(i);
    }
}
