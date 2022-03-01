package es.android.coches;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import es.android.coches.databinding.FragmentConocimientosBinding;
import es.android.coches.entidad.Pregunta;
import es.android.coches.servicio.implementacion.ServicioPreguntasXMLImpl;
import es.android.coches.servicio.interfaz.ServicioPreguntas;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConocimientosFragment extends Fragment {

    private FragmentConocimientosBinding binding;

    List<Pregunta> preguntas;
    int respuestaCorrecta;
    int respuestaCorrectaXml;
    int puntos;
    String cadena="";
    ServicioPreguntas servicio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicio = new ServicioPreguntasXMLImpl(getContext());
        try {
            preguntas = new ArrayList<>(servicio.generarPreguntas("coches.xml"));
            Collections.shuffle(preguntas);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if(!fileExists(getContext(), "XmlPuntuacion.xml")){


                try {
                    salvarFichero("XmlPuntuacion.xml","<puntuacion><maxima>"+respuestaCorrectaXml+"</maxima><ultima>"+puntos+"</ultima></puntuacion>");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConocimientosBinding.inflate(inflater, container, false);

        try {
            presentarPregunta();
            leerPuntuacionXml();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.botonRespuesta.setOnClickListener(v -> {
            int seleccionado = binding.radioGroup.getCheckedRadioButtonId();
            CharSequence mensaje = seleccionado == respuestaCorrecta ? "¡Acertaste!" : "Fallaste";

            if(seleccionado==respuestaCorrecta){

                mensaje = "¡Acertaste!";
                puntos++;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(respuestaCorrectaXml<puntos) {
                    respuestaCorrectaXml = puntos;
                    cadena = "¡Has batido tu récord de aciertos! Has alcanzado " + respuestaCorrectaXml+" puntos";
                }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }else{
                mensaje = "Fallaste";
            }

            Snackbar.make(v, mensaje, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Siguiente", v1 -> {
                        try {
                            presentarPregunta();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
            v.setEnabled(false);
        });

        return binding.getRoot();
    }

    private void presentarPregunta() throws IOException, JSONException {
        if (preguntas.size() > 0) {
            binding.botonRespuesta.setEnabled(true);

            int pregunta = new Random().nextInt(preguntas.size());

            Pregunta preguntaActual = preguntas.remove(pregunta);
            preguntaActual.setRespuetas(servicio.generarRespuestasPosibles(preguntaActual.getRespuestaCorrecta(), binding.radioGroup.getChildCount()));

            InputStream bandera = null;
            try {
                bandera = getContext().getAssets().open(preguntaActual.getFoto());
                binding.bandera.setImageBitmap(BitmapFactory.decodeStream(bandera));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // anadir
            binding.radioGroup.clearCheck();
            for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
                RadioButton radio = (RadioButton) binding.radioGroup.getChildAt(i);
                // comentar
                // radio.setChecked(false);
                CharSequence respuesta = preguntaActual.getRespuetas().get(i);
                if (respuesta.equals(preguntaActual.getRespuestaCorrecta()))
                    respuestaCorrecta = radio.getId();

                radio.setText(respuesta);
            }
        } else {
            binding.bandera.setVisibility(View.GONE);
            binding.radioGroup.setVisibility(View.GONE);
            binding.botonRespuesta.setVisibility(View.GONE);
            binding.textView.setText("¡Fin!\nTu Puntuación: "+puntos);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            salvarFichero("XmlPuntuacion.xml","<puntuacion><maxima>"+respuestaCorrectaXml+"</maxima><ultima>"+puntos+"</ultima></puntuacion>");
            if(cadena.equals("")) cadena = "Has conseguido \n"+cadena;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            System.out.println(puntos);
        }
    }

    private void salvarFichero(String fichero, String texto) {
        FileOutputStream fos;
        try {
            fos = getContext().openFileOutput(fichero, Context.MODE_PRIVATE);
            fos.write(texto.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void leerPuntuacionXml() throws Exception {
        Document doc = leerXMLfichero("XmlPuntuacion.xml");
        Element documentElement = doc.getDocumentElement();
        NodeList puntuaciones = documentElement.getChildNodes();
        for(int i=0; i<puntuaciones.getLength(); i++) {
            if(puntuaciones.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element puntuacion = (Element) puntuaciones.item(i);
                //String nombre = pais.getAttribute("nombre");
                String puntuacionMaxima = puntuacion.getElementsByTagName("maxima").item(0).getTextContent();
                respuestaCorrectaXml= Integer.parseInt(puntuacionMaxima);
            }
        }
    }

    private Document leerXMLfichero(String fichero) throws Exception {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = factory.newDocumentBuilder();
        InputStream is = getContext().openFileInput(fichero);
        Document doc = constructor.parse(is);
        doc.getDocumentElement().normalize();
        return doc;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}