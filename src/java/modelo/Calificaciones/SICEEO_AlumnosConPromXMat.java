package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import modelo.DAO.SICEEO_QueriesInformix;

/* 
    Creado el : 09-sep-2016, 10:15:55
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_AlumnosConPromXMat {
    private final SICEEO_QueriesInformix qryIfx;
    public SICEEO_AlumnosConPromXMat(SICEEO_QueriesInformix qryIfx){
        this.qryIfx =qryIfx;
    }
    
    public void getPaqueteDeMaterias (Map parameters, String cicescini, String modalidad, String cveplan, String grado) throws ClassNotFoundException, SQLException
    {
        ArrayList<Map> paqueteDeMaterias;
        int numMats, i, numObjsEnReporte=9;                                     //Ojo: Verificar el número en numObjsEnReporte, ya que es la cantidad de objetos estáticos en el reporte
        String subquery="";
        
        paqueteDeMaterias = qryIfx.getPaqueteDeMateriasDeCiclo (cicescini, modalidad, cveplan, grado);
        numMats = paqueteDeMaterias.size();
        
        for (i=0; i<numMats; i++)
        {   
            parameters.put("nombreMat"+(i+1), ""+paqueteDeMaterias.get(i).get("desmat"));
            
            subquery += "( SELECT promedio FROM alumnomaterias WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                        + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
            if ("CBA, LEX".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";
            subquery += " ) AS prommat"+(i+1);
            
            subquery += (i < numMats-1)?", ":" ";
        }
        
        //Si las materias no fueron el total especificados en numObjsEnReporte, entonces terminamos de rellenar
        if (i < numObjsEnReporte)
            for (;i<numObjsEnReporte; i++){
                parameters.put("nombreMat"+(i+1), "mat"+(i+1));
                subquery += ", -1 AS prommat"+(i+1);
            }
        
        parameters.put("sqryMaterias", subquery);
    }
    
    
}
