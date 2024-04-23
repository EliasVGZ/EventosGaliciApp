package controladores;

import java.util.List;

import modelos.Conciertos;

public interface IConciertosBD {


    Conciertos elemento(String nombre); //devuelve concierto por su id
    List<Conciertos> lista();//Devuelve lista con todos los conciertos

}
