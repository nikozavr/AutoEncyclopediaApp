package ru.nikozavr.auto;

import ru.nikozavr.auto.model.Generation;
import ru.nikozavr.auto.model.Marque;
import ru.nikozavr.auto.model.Model;

/**
 * Created by Никита on 06.11.2014.
 */
public class CurrentAuto {
    private static CurrentAuto mInstance= null;

    private static Marque _marque = null;
    private static Model _model = null;
    private static Generation _generetion = null;

    private static String marq_info = "marq_info";

    public void CurrentAuto(){

    }

    public static void setMarque(Marque marque){
        if(mInstance == null)
            mInstance = new CurrentAuto();
        _marque = marque;
    }

    public static void setModel(Model model){
        if(mInstance == null)
            mInstance = new CurrentAuto();
        _model = model;
    }

    public static void setGeneration(Generation generation){
        if(mInstance == null)
            mInstance = new CurrentAuto();
        _generetion = generation;
    }

    public static synchronized Marque getMarque(){
        return mInstance._marque;
    }

    public static synchronized Model getModel(){
        return mInstance._model;
    }

    public static synchronized Generation getGeneration(){
        return mInstance._generetion;
    }

    public static synchronized String getMarqInfoID() {
        return new String(marq_info + _marque.getID());
    }

    public static synchronized String getFragNameModelInfo() {
        return null;
    }

}
