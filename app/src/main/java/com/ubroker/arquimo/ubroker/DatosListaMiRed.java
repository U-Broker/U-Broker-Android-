package com.ubroker.arquimo.ubroker;

/**
 * Created by andresrodriguez on 03/08/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelos.Referencias;

public class DatosListaMiRed {
    public static List<Referencias> getData() {
       List<Referencias> expandableListDetail = new ArrayList<Referencias>();


        //TEST
        List<Referencias> referencias_referencia1 = new ArrayList<>();
        referencias_referencia1.add(
                new Referencias(
                        "",
                        "Marco Antonio",
                        "Sombrerero",
                        null,
                        ""
                )
        );
        referencias_referencia1.add(
                new Referencias(
                        "",
                        "Jorge",
                        "Gonzalez Solorzano",
                        null,
                        ""
                )
        );

        Referencias referencia1 = new Referencias(
                "12K3BK1J23KJB31",
                "Rogelio",
                "Sanchéz Pallares",
                referencias_referencia1,
                "Oportunidades: 1 | Cantidad Red: 2"
        );


        List<Referencias> referencias_referencia2 = new ArrayList<>();
        referencias_referencia2.add(
                new Referencias(
                        "",
                        "Adrián Augusto",
                        "Moreno Cortes",
                        null,
                        ""
                )
        );
        referencias_referencia2.add(
                new Referencias(
                        "",
                        "Roberto",
                        "Gonzalez Solorzano",
                        null,
                        ""
                )
        );

        Referencias referencia2 = new Referencias(
                "ak1k2h312kj1k2j",
                "Mario Adán",
                "López Martínez",
                referencias_referencia2,
                "Oportunidades: 0 | Cantidad Red: 2"
        );

        List<Referencias> referencias_referencia3 = new ArrayList<>();
        referencias_referencia3.add(
                new Referencias(
                        "",
                        "Eduardo",
                        "Martínez",
                        null,
                        ""
                )
        );
        referencias_referencia3.add(
                new Referencias(
                        "",
                        "Guillermo",
                        "Mascote Mejía",
                        null,
                        ""
                )
        );

        Referencias referencia3 = new Referencias(
                "ak1k2h312kj1k2j",
                "Irving Román",
                "Soto Toríz",
                referencias_referencia3,
                "Oportunidades: 4 | Cantidad Red: 2"
        );


        /*List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia"); */

        expandableListDetail.add(referencia1);
        expandableListDetail.add(referencia2);
        expandableListDetail.add(referencia3);
        return expandableListDetail;
    }

}


//(referencia1.getNombre() + " " + referencia1.getApellido()
//(referencia2.getNombre() + " " + referencia2.getApellido()
//(referencia3.getNombre() + " " + referencia3.getApellido()